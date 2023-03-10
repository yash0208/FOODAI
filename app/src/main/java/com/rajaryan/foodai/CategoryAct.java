package com.rajaryan.foodai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class CategoryAct extends AppCompatActivity {
    RecyclerView rec;
    TextView name1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        rec=findViewById(R.id.rec);
        name1=findViewById(R.id.name);
        Intent i=getIntent();
        String name=i.getStringExtra("cat");
        name1.setText(name);
        com.google.firebase.firestore.Query query4 = FirebaseFirestore.getInstance()
                .collection("Objects").whereGreaterThanOrEqualTo("RecipeCategory",name)
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
                holder.setProductName(model.getName(),model.getAuthorName(),link,model.getTotalTime(),model.getRecipeServings(),model.getRecipeId());
            }

            @Override
            public ChatHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.card1, group, false);

                return new ChatHolder(view);
            }
        };
        rec.setLayoutManager(new LinearLayoutManager(CategoryAct.this,LinearLayoutManager.VERTICAL,false));
        rec.setAdapter(adapter3);
        adapter3.startListening();
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
                    Intent i=new Intent(CategoryAct.this,Recipe.class);
                    i.putExtra("Id",id);
                    startActivity(i);

                }
            });
            if(TextUtils.equals(link,"character(0")){
                Picasso.get()
                        .load(R.drawable.card_default_back).fit().centerCrop().into(image);
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