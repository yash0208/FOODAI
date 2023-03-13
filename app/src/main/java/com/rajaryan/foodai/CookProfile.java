package com.rajaryan.foodai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class CookProfile extends AppCompatActivity {
    String name,id;
    TextView pt,rt,nm;
    RecyclerView rec;
    int count;
    float rating = 0;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_profile);
        Intent i=getIntent();
        name=i.getStringExtra("Name");
        pt=findViewById(R.id.pt);
        id=i.getStringExtra("Id");
        Log.e("Id",id);
        com.google.firebase.firestore.Query query4 = FirebaseFirestore.getInstance()
                .collection("Objects").whereEqualTo("AuthorId",id);
        FirestoreRecyclerOptions<RecipeData> options4 = new FirestoreRecyclerOptions.Builder<RecipeData>()
                .setQuery(query4, RecipeData.class)
                .build();
        rec=findViewById(R.id.rec);
        rt=findViewById(R.id.rate);
        nm=findViewById(R.id.nm);
        nm.setText(name);
        rec.setLayoutManager(new GridLayoutManager(this,2));
        FirestoreRecyclerAdapter<RecipeData, ChatHolder> adapter3 = new FirestoreRecyclerAdapter<RecipeData, ChatHolder>(options4) {
            int inc;

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
                inc++;
                count++;
                pt.setText(String.valueOf(inc));

                if(model.getAggregatedRating().equals("NA")){
                    rating=rating+5;
                }else {
                    rating=rating+Float.valueOf(model.getAggregatedRating());
                }
                float finalr=rating/count;
                Log.e("Final", String.valueOf(finalr));
                rt.setText(String.valueOf(finalr));
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
        rec.setAdapter(adapter3);
        adapter3.startListening();

    }

    public void back(View view) {
        onBackPressed();
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
                    Intent i=new Intent(getApplicationContext(),Recipe.class);
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
}