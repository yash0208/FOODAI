package com.rajaryan.foodai;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fridge#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fridge extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String ing_list="c(";
    List<String> javaStringList = new ArrayList<>();
    com.google.firebase.firestore.Query query4;
    AutoCompleteTextView search;
    DatabaseReference ref;
    ImageButton search_button;
    String final_list;
    ListView list;
    RecyclerView rec,recyclerView;
    public Fridge() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fridge.
     */
    // TODO: Rename and change types and number of parameters
    public static Fridge newInstance(String param1, String param2) {
        Fridge fragment = new Fridge();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_fridge, container, false);
        search=view.findViewById(R.id.search);
        ref= FirebaseDatabase.getInstance().getReference("Ingredients");
        list=view.findViewById(R.id.list);
        populateSearch();
        search_button=view.findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(search.getText().toString())){
                    DatabaseReference re=FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Available");
                    Map<String, String> docData = new HashMap<>();
                    docData.put("Ingredient", search.getText().toString());
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();

                    re.push().setValue(docData);
                    search.setText("");
                }
            }
        });
        rec=view.findViewById(R.id.rec);
        Query query3= FirebaseDatabase.getInstance().getReference().child("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Available");
        FirebaseRecyclerOptions<Ingredient> option3 =
                new FirebaseRecyclerOptions.Builder<Ingredient>()
                        .setQuery(query3,Ingredient.class)
                        .setLifecycleOwner(getActivity())
                        .build();
        Adapter adapter2= new Adapter(option3);
        rec.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rec.setAdapter(adapter2);
        adapter2.startListening();

        recyclerView=view.findViewById(R.id.rec1);
        DatabaseReference re=FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Available_Items").child("Ingredient");
        re.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String list=snapshot.getValue().toString();
                    String image=list.replace("c(","");
                    String image1=image.replace(")","");
                    String[] strArray = image1.split(", ");
                    for (String s : strArray) {
                        Log.e("e",s);

                    }
                    String link=image1.replace("\"","");
                    query4 = FirebaseFirestore.getInstance()
                            .collection("Objects").whereLessThanOrEqualTo("RecipeIngredientParts",link)
                            .limit(4);
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
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                    recyclerView.setAdapter(adapter3);
                    adapter3.startListening();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
    private void populateSearch() {
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ArrayList<String> name=new ArrayList<>();
                    for (DataSnapshot ds:snapshot.getChildren()){
                        String n=ds.child("Food").getValue(String.class);
                        name.add(n);
                    }
                    ArrayAdapter adapter=new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,name);
                    search.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addListenerForSingleValueEvent(valueEventListener);
    }
    public class Adapter extends FirebaseRecyclerAdapter<Ingredient, Adapter.viewholder> {
        String time1;
        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public Adapter(@NonNull FirebaseRecyclerOptions<Ingredient> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull viewholder viewholder, int i, @NonNull Ingredient recipeData) {
            viewholder.tittle.setText(String.valueOf(recipeData.getIngredient()));
            String id=getRef(i).getKey();
            viewholder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference ref;
                    ref=FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Available").child(id);
                    ref.removeValue();
                }
            });
            ing_list=ing_list+"\""+recipeData.getIngredient()+"\""+",";
            Log.e("print",ing_list);
            int len=ing_list.length();
            StringBuffer string = new StringBuffer(ing_list);
            char ch = ')';
            string.setCharAt(len-1,ch);
            DatabaseReference re=FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Available_Items");
            Map<String, String> docData = new HashMap<>();
            docData.put("Ingredient", String.valueOf(string));
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            re.setValue(docData);
        }
        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ing_card, parent, false);
            return new viewholder(view);
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tittle;
            ImageView del;

            public viewholder(@NonNull View itemView) {
                super(itemView);
                tittle = itemView.findViewById(R.id.name11);
                del=itemView.findViewById(R.id.del);
            }
        }
    }

}