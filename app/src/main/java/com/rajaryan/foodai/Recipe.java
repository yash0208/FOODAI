package com.rajaryan.foodai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class Recipe extends AppCompatActivity {
    String id;
    TextView ca,ch,fat,pr,sf,so,su,views,reviews,description,calor;
    boolean rated;
    RatingBar ratingBar;
    ImageView pic;
    TextView cap,chp,fatp,prp,sfp,sop,sup;
     FirebaseFirestore db = FirebaseFirestore.getInstance();
     DocumentReference noteRef;
     TextToSpeech t1;
     String rates;
    String ins11,rate,count;
    LinearLayout ins,ing_lay,nut;
    Float ra,co;
    TextView ct,pt,sv,name,author;
     ImageButton spk;
     RecyclerView tags;
     String[] instruction,ing1,ing2,tag_s;
     String aut,aut_id;
     CustomAdapter customAdapter;
     RecyclerView lv,ing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        TabLayout tabLayout;
        tabLayout=findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Nutrition"));
        tabLayout.addTab(tabLayout.newTab().setText("Ingredients"));
        tabLayout.addTab(tabLayout.newTab().setText("Directions"));
        Intent i=getIntent();
        id=i.getStringExtra("Id");
        ca=findViewById(R.id.chquantity);
        fat=findViewById(R.id.fatquantity);
        pic=findViewById(R.id.picture);
        pr=findViewById(R.id.ptquantity);
        ch=findViewById(R.id.clquantity);
        sf=findViewById(R.id.sfquantity);
        so=findViewById(R.id.squantity);
        su=findViewById(R.id.suquantity);
        ing=findViewById(R.id.ing);
        ins=findViewById(R.id.instruction_layout);
        ing_lay=findViewById(R.id.ing_layout);
        nut=findViewById(R.id.nutritions1);
        spk=findViewById(R.id.spk);
        ct=findViewById(R.id.ct);
        pt=findViewById(R.id.pt);
        sv=findViewById(R.id.sv);
        name=findViewById(R.id.name);
        tags=findViewById(R.id.tags);
        views=findViewById(R.id.views);
        reviews=findViewById(R.id.reviews);
        description=findViewById(R.id.description);
        ratingBar=findViewById(R.id.ratingBar);
        calor=findViewById(R.id.calor);
        author=findViewById(R.id.author);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        noteRef=db.document("Objects/"+id);
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String title = documentSnapshot.getString("Name");
                            rate=documentSnapshot.getString("AggregatedRating");
                            count=documentSnapshot.getString("ReviewCount");

                            ca.setText(documentSnapshot.getString("CarbohydrateContent")+" g");
                            fat.setText(documentSnapshot.getString("FatContent")+" g");
                            pr.setText(documentSnapshot.getString("ProteinContent")+" g");
                            sf.setText(documentSnapshot.getString("SaturatedFatContent")+" g");
                            so.setText(documentSnapshot.getString("SodiumContent")+" mg");
                            su.setText(documentSnapshot.getString("SugarContent")+" g");

                            ch.setText(documentSnapshot.getString("CholesterolContent"));
                            name.setText(documentSnapshot.getString("Name"));
                            views.setText(documentSnapshot.getString("ReviewCount"));
                            reviews.setText(documentSnapshot.getString("AggregatedRating"));
                            calor.setText(documentSnapshot.getString("Calories"));
                            description.setText(documentSnapshot.getString("Description").replace("Food.com","Foodopedia"));
                            author.setText(documentSnapshot.getString("AuthorName"));
                            aut=documentSnapshot.getString("AuthorName");
                            aut_id=documentSnapshot.getString("AuthorId");
                            String image11=documentSnapshot.getString("Images").replace("c(","");
                            String image12=image11.replace(")","");
                            String[] strArray = image12.split(", ");
                            String link=strArray[0].replace("\"","");
                            Picasso.get().load(link).fit().centerCrop().into(pic);
                            ct.setText(documentSnapshot.getString("CookTime").replace("PT",""));
                            pt.setText(documentSnapshot.getString("PrepTime").replace("PT",""));
                            sv.setText(documentSnapshot.getString("RecipeServings"));
                            Log.e("Name",title);
                            //Map<String, Object> note = documentSnapshot.getData();
                            lv=findViewById(R.id.lv1);
                            String image=documentSnapshot.getString("RecipeInstructions").replace("c(","");
                            ins11=documentSnapshot.getString("RecipeInstructions").replace("c(","");
                            String image1=image.replace(")","");
                            instruction = image1.split(", ");
                            CustomAdapter customAdapter1=new CustomAdapter(instruction);
                            lv.setAdapter(customAdapter1);
                            lv.setHasFixedSize(false);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Recipe.this,LinearLayoutManager.VERTICAL,false) {
                                @Override
                                public boolean canScrollVertically() {
                                    return true;
                                }
                            };
                            lv.setLayoutManager(linearLayoutManager);
                            String image2=documentSnapshot.getString("RecipeIngredientParts").replace("c(","");
                            String image3=image2.replace(")","");
                            ing1 = image3.split(", ");
                            String image4=documentSnapshot.getString("RecipeIngredientQuantities").replace("c(","");
                            String image5=image4.replace(")","");
                            ing2 = image5.split(", ");
                            CustomAdapter2 customAdapter2=new CustomAdapter2(ing1,ing2);
                            ing.setAdapter(customAdapter2);
                            ing.setHasFixedSize(false);
                            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(Recipe.this,LinearLayoutManager.VERTICAL,false) {
                                @Override
                                public boolean canScrollVertically() {
                                    return true;
                                }
                            };
                            ing.setLayoutManager(linearLayoutManager1);
                            String image6=documentSnapshot.getString("Keywords").replace("c(","");
                            String image7=image6.replace(")","");
                            tag_s = image7.split(", ");
                            CustomAdapter3 customAdapter3=new CustomAdapter3(tag_s);
                            tags.setAdapter(customAdapter3);
                            tags.setLayoutManager(new LinearLayoutManager(Recipe.this, LinearLayoutManager.HORIZONTAL,false));
                        } else {
                            Toast.makeText(Recipe.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Recipe.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });
        author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(),CookProfile.class);
                i.putExtra("Id",aut_id);
                i.putExtra("Name",aut);
                startActivity(i);
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(rate=="NA"){
                    ra= Float.valueOf(0);
                    co= Float.valueOf(0);
                }
                else {
                    ra=Float.valueOf(rate);
                    co=Float.valueOf(count);
                }
                Float agc=ra*co;
                co++;
                Float ner= (agc+v)/co++;

                    Map<String, String> docData = new HashMap<>();
                    docData.put("User", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    docData.put("Rate",String.valueOf(v));
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = (DatabaseReference) database.getReference("Rating/"+id+"/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref.setValue(docData);
                    noteRef.update("ReviewCount",String.valueOf(co));
                    noteRef.update("AggregatedRating",String.valueOf(ner));



            }
        });
        spk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i;
                    String toSpeak = ins11;

                    Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    t1.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String s) {

                        }

                        @Override
                        public void onDone(String s) {

                        }

                        @Override
                        public void onError(String s) {

                        }
                    });

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String language = tab.getText().toString();

                if(language.equals("Directions")){
                    ins.setVisibility(View.VISIBLE);
                    ing_lay.setVisibility(View.GONE);
                    nut.setVisibility(View.GONE);
                }
                if(language.equals("Ingredients")){
                    ing_lay.setVisibility(View.VISIBLE);
                    ins.setVisibility(View.GONE);
                    nut.setVisibility(View.GONE);
                }
                if(language.equals("Nutrition")){
                    nut.setVisibility(View.VISIBLE);
                    ing_lay.setVisibility(View.GONE);
                    ins.setVisibility(View.GONE);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        Button mBottton = findViewById(R.id.review);
        mBottton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });
    }

    private void showBottomSheetDialog() {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.comment_bar);
            EditText editText=bottomSheetDialog.findViewById(R.id.comment);
            ImageView profile=bottomSheetDialog.findViewById(R.id.profile);
            Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).fit().into(profile);
            TextView post=bottomSheetDialog.findViewById(R.id.post);
            RecyclerView rec=bottomSheetDialog.findViewById(R.id.comments_rec);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            rec.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            Query query= FirebaseDatabase.getInstance().getReference().child("Comments").child(id);
            FirebaseRecyclerOptions<Comment> option1 =
                    new FirebaseRecyclerOptions.Builder<Comment>()
                            .setQuery(query,Comment.class)
                            .setLifecycleOwner(this)
                            .build();
            Adapter adapter2=new Adapter(option1);
            rec.setAdapter(adapter2);
            adapter2.startListening();
            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!TextUtils.isEmpty(editText.getText().toString())){
                        Map<String, String> docData = new HashMap<>();
                        docData.put("Comment", editText.getText().toString());
                        docData.put("User", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        docData.put("Pic",FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
                        docData.put("Name",FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference("Comments/"+id);
                        ref.push().setValue(docData);
                    }

                }
            });
            bottomSheetDialog.show();
    }
    public void back(View view) {
        onBackPressed();
    }
    public class Adapter extends FirebaseRecyclerAdapter<Comment, Adapter.viewholder> {
        String time1;
        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public Adapter(@androidx.annotation.NonNull FirebaseRecyclerOptions<Comment> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@androidx.annotation.NonNull viewholder viewholder, int i, @androidx.annotation.NonNull Comment recipeData) {
            Picasso.get().load(recipeData.getPic()).fit().into(viewholder.image);
            viewholder.name.setText(recipeData.getName());
            viewholder.cmt.setText(recipeData.getComment());
        }
        @androidx.annotation.NonNull
        @Override
        public viewholder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment, parent, false);
            return new viewholder(view);
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView name, cmt;
            ImageView image;
            public viewholder(@androidx.annotation.NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                cmt=itemView.findViewById(R.id.cmt);
                image = itemView.findViewById(R.id.profile);
            }
        }
    }
    public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.ViewHolder> {

        private String[] localDataSet2;
        private String[] localDataSet1;


        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView textView;
            private final TextView textView2;

            public ViewHolder(android.view.View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                textView = (TextView) view.findViewById(R.id.quantity);
                textView2=(TextView) view.findViewById(R.id.name);
            }

            public TextView getTextView() {
                return textView;
            }
            public TextView getTextView2() {
                return textView2;
            }
        }

        /**
         * Initialize the dataset of the Adapter
         *
         * @param dataSet String[] containing the data to populate views to be used
         * by RecyclerView
         */
        public CustomAdapter2(String[] dataSet,String[] data2) {
            localDataSet2 = ing1;
            localDataSet1=ing2;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.ingredians, viewGroup, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.getTextView().setText(localDataSet2[position].replace("\"",""));
            viewHolder.getTextView2().setText(localDataSet1[position].replace("\"",""));

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return localDataSet2.length;
        }
    }
    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        private String[] localDataSet;


        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView textView;

            public ViewHolder(android.view.View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                textView = (TextView) view.findViewById(R.id.quantity);
            }

            public TextView getTextView() {
                return textView;
            }
        }

        /**
         * Initialize the dataset of the Adapter
         *
         * @param dataSet String[] containing the data to populate views to be used
         * by RecyclerView
         */
        public CustomAdapter(String[] dataSet) {
            localDataSet = instruction;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_layout, viewGroup, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.getTextView().setText(localDataSet[position].replace("\"",""));
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return localDataSet.length;
        }
    }
    public class CustomAdapter3 extends RecyclerView.Adapter<CustomAdapter3.ViewHolder> {

        private String[] localDataSet;


        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView textView;

            public ViewHolder(android.view.View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                textView = (TextView) view.findViewById(R.id.tag);
            }

            public TextView getTextView() {
                return textView;
            }
        }

        /**
         * Initialize the dataset of the Adapter
         *
         * @param dataSet String[] containing the data to populate views to be used
         * by RecyclerView
         */
        public CustomAdapter3(String[] dataSet) {
            localDataSet = tag_s;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.tags_lay, viewGroup, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.getTextView().setText(localDataSet[position].replace("\"",""));
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return localDataSet.length;
        }
    }

}