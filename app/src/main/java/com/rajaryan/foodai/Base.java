package com.rajaryan.foodai;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Base#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Base extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView,rec_2;
    Adapter adapter;
    Query query2;
    TextView name;
    ImageView profile;
    AutoCompleteTextView search;
    Adapter2 adapter2;
    ImageButton search_button;
    DatabaseReference databaseReference;
    public Base() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Base.
     */
    // TODO: Rename and change types and number of parameters
    public static Base newInstance(String param1, String param2) {
        Base fragment = new Base();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_base, container, false);
        recyclerView=view.findViewById(R.id.rec_1);
        rec_2=view.findViewById(R.id.rec_2);
        databaseReference=FirebaseDatabase.getInstance().getReference("Recipe1");
        search=view.findViewById(R.id.search);
        name=view.findViewById(R.id.name);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),EditProfile.class);
                startActivity(i);
            }
        });
        profile=view.findViewById(R.id.profile);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if(account !=  null){
                String personName = account.getDisplayName();
            String Pic=account.getPhotoUrl().toString();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            name.setText("Hi, "+personName);
            Picasso.get()
                    .load(Pic).into(profile);
        }
        String[] arr = {"Weeknight", "Oven", "< 4 Hours","Fruit", "Vegetable", "< 15 Mins", "Easy"};
        int randIdx = ThreadLocalRandom.current().nextInt(arr.length);
        String randomElem = arr[randIdx];
        search_button=view.findViewById(R.id.search_button);
        com.google.firebase.firestore.Query query4 = FirebaseFirestore.getInstance()
                .collection("Objects").whereGreaterThanOrEqualTo("Keywords",randomElem).limit(4);
        FirestoreRecyclerOptions<RecipeData> options4 = new FirestoreRecyclerOptions.Builder<RecipeData>()
                .setQuery(query4, RecipeData.class)
                .build();
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(search.getText().toString())){

                }
                else{
                    com.google.firebase.firestore.Query query4 = FirebaseFirestore.getInstance()
                            .collection("Objects").whereGreaterThanOrEqualTo("Name",search.getText().toString())
                            .limit(5);
                    FirestoreRecyclerOptions<RecipeData> options4 = new FirestoreRecyclerOptions.Builder<RecipeData>()
                            .setQuery(query4, RecipeData.class)
                            .build();
                    FirestoreRecyclerAdapter<RecipeData, ChatHolder> adapter3 = new FirestoreRecyclerAdapter<RecipeData, ChatHolder>(options4) {
                        @Override
                        public void onBindViewHolder(ChatHolder holder, int position, RecipeData model) {
                            String image=model.getImages().replace("c(","");
                            String image1=image.replace(")","");
                            String[] strArray = image1.split(", ");
                            for (String s : strArray) {
                                Log.e("e",s);
                            }
                            String link=strArray[0].replace("\"","");
                            holder.setProductName(model.getName(),model.getRecipeCategory(),link,model.getTotalTime(),model.getRecipeServings(),model.getRecipeId());
                        }

                        @Override
                        public ChatHolder onCreateViewHolder(ViewGroup group, int i) {
                            // Create a new instance of the ViewHolder, in this case we are using a custom
                            // layout called R.layout.message for each item
                            View view = LayoutInflater.from(group.getContext())
                                    .inflate(R.layout.item, group, false);

                            return new ChatHolder(view);
                        }
                    };
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                    recyclerView.setAdapter(adapter3);
                    adapter3.startListening();
                }
            }
        });

        FirestoreRecyclerAdapter<RecipeData, ChatHolder> adapter3 = new FirestoreRecyclerAdapter<RecipeData, ChatHolder>(options4) {
            @Override
            public void onBindViewHolder(ChatHolder holder, int position, RecipeData model) {
                String image=model.getImages().replace("c(","");
                String image1=image.replace(")","");
                String[] strArray = image1.split(", ");
                for (String s : strArray) {
                    Log.e("e",s);
                }
                String link=strArray[0].replace("\"","");
                String recipeId=model.getRecipeId();
                holder.setProductName(model.getName(),model.getRecipeCategory(),link,model.getTotalTime(),model.getRecipeServings(),model.getRecipeId());
            }

            @Override
            public ChatHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item, group, false);

                return new ChatHolder(view);
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(adapter3);
        adapter3.startListening();
        rec_2=view.findViewById(R.id.rec_2);


        Query query3= FirebaseDatabase.getInstance().getReference().child("Categories");
        FirebaseRecyclerOptions<Category> option3 =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(query3,Category.class)
                        .setLifecycleOwner(getActivity())
                        .build();
        adapter2= new Adapter2(option3);
        rec_2.setLayoutManager(new LinearLayoutManagerWrapper(getContext(),LinearLayoutManager.HORIZONTAL,false));
        rec_2.setAdapter(adapter2);
        adapter2.startListening();
        return view;
    }
    private class ChatHolder extends RecyclerView.ViewHolder {
        private View view;

        ChatHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        void setProductName(String productName, String type, String link,String time, String serves, String id) {
            TextView textView = view.findViewById(R.id.name);
            TextView type1=view.findViewById(R.id.type);
            type1.setText(type);
            ImageView image=view.findViewById(R.id.image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(getActivity(),Recipe.class);
                    i.putExtra("Id",id);
                    startActivity(i);

                }
            });
            TextView time1=view.findViewById(R.id.time);
            TextView serve=view.findViewById(R.id.serve);
            serve.setText(serves);
            time=time.replace("PT","");
            time1.setText(time);
            if(TextUtils.equals(link,"character(0")){
                Picasso.get()
                        .load(R.drawable.card_default_back).into(image);
                textView.setText(productName);
            }
            else {
                Picasso.get()
                        .load(link).into(image);
            }
            textView.setText(productName);

        }
    }

    public class LinearLayoutManagerWrapper extends LinearLayoutManager {

        public LinearLayoutManagerWrapper(Context context) {
            super(context);
        }

        public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }
    public class Adapter extends FirebaseRecyclerAdapter<RecipeData, Adapter.viewholder> {
        String time1;
        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public Adapter(@NonNull FirebaseRecyclerOptions<RecipeData> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull viewholder viewholder, int i, @NonNull RecipeData recipeData) {
            viewholder.tittle.setText(recipeData.getName());
            viewholder.type.setText(recipeData.getRecipeCategory());
            String image=recipeData.getImages().replace("c(","");
            String image1=image.replace(")","");
            String[] strArray = image1.split(", ");
            for (String s : strArray) {
                Log.e("e",s);
            }

            String link=strArray[0].replace("\"","");
            Picasso.get()
                    .load(link).into(viewholder.imageView);

        }
        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item, parent, false);
            return new viewholder(view);
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tittle,type;
            ImageView imageView;
            RatingBar ratingBar;
            public viewholder(@NonNull View itemView) {
                super(itemView);
                type=itemView.findViewById(R.id.type);
                tittle = itemView.findViewById(R.id.name);
                imageView=itemView.findViewById(R.id.image);
            }
        }

    }
    public class Adapter2 extends FirebaseRecyclerAdapter<Category, Adapter2.viewholder> {
        String time1;
        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public Adapter2(@NonNull FirebaseRecyclerOptions<Category> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull viewholder viewholder, int i, @NonNull Category recipeData) {
            viewholder.type.setText(recipeData.getName());
            Picasso.get()
                    .load(recipeData.getImage()).fit().centerCrop().into(viewholder.imageView);
            viewholder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(getActivity(),CategoryAct.class);
                    i.putExtra("cat",recipeData.getName());
                    startActivity(i);
                }
            });
        }
        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.categories, parent, false);
            return new viewholder(view);
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView type;
            ImageView imageView;
            public viewholder(@NonNull View itemView) {
                super(itemView);
                type=itemView.findViewById(R.id.category);
                imageView=itemView.findViewById(R.id.image1);
            }
        }

    }
}