package com.rajaryan.foodai;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Video#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Video extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rec;
    EditText search;
    String query;
    ImageButton search_btn;

    public Video() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Video.
     */
    // TODO: Rename and change types and number of parameters
    public static Video newInstance(String param1, String param2) {
        Video fragment = new Video();
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
        View view=inflater.inflate(R.layout.fragment_video, container, false);
        rec=view.findViewById(R.id.rec);
        search=view.findViewById(R.id.search);
        search_btn=view.findViewById(R.id.search_button);
        query="Top Indian Food";
        OkHttpClient client = new OkHttpClient();
        String status="s";
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://www.googleapis.com/youtube/v3/search?q="+query+"recipe&key=AIzaSyBsWJfo8m7CXSoxmXKkuCdDgY5AVeeSCqc")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                String myresponse = response.body().string();

                try {
                    JSONObject json = new JSONObject(myresponse);
                    JSONArray res= json.getJSONArray("items");
                    String[] new_items = new String[res.length()];
                    for (int i = 0; i < res.length(); i++) {
                        try {
                            JSONObject songObject = res.getJSONObject(i);
                            JSONObject id=songObject.getJSONObject("id");
                            new_items[i]=id.getString("videoId").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("E12","not");
                        }
                    }
                    CustomAdapter adapter= new CustomAdapter(new_items);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rec.setAdapter(adapter);
                            rec.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("res", myresponse);
            }
        });
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(search.getText().toString())){
                    Toast.makeText(getActivity(),"Please Enter Search Query",Toast.LENGTH_LONG).show();
                }
                else {

                    OkHttpClient client = new OkHttpClient();
                    String status="s";
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url("https://www.googleapis.com/youtube/v3/search?q="+search.getText().toString()+"recipe&key=AIzaSyBsWJfo8m7CXSoxmXKkuCdDgY5AVeeSCqc")
                            .get()
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                            String myresponse = response.body().string();

                            try {
                                JSONObject json = new JSONObject(myresponse);
                                JSONArray res= json.getJSONArray("items");
                                String[] new_items = new String[res.length()];
                                for (int i = 0; i < res.length(); i++) {
                                    try {
                                        JSONObject songObject = res.getJSONObject(i);
                                        JSONObject id=songObject.getJSONObject("id");
                                        new_items[i]=id.getString("videoId").toString();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e("E12","not");
                                    }
                                }
                                CustomAdapter adapter= new CustomAdapter(new_items);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        rec.setAdapter(adapter);
                                        rec.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                                    }
                                });


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.e("res", myresponse);
                        }
                    });
                }


            }
        });

        return view;
    }
    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        private String[] localDataSet;


        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView textView;
            private final TextView textView2;
            private final ImageView iView;

            public ViewHolder(View view) {
                super(view);

                // Define click listener for the ViewHolder's View
                textView = (TextView) view.findViewById(R.id.name);
                textView2= view.findViewById(R.id.channel);
                iView=view.findViewById(R.id.img);

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
            localDataSet = dataSet;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.youtube, viewGroup, false);

            return new CustomAdapter.ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(CustomAdapter.ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            OkHttpClient client = new OkHttpClient();
            String status="s";
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("https://www.googleapis.com/youtube/v3/videos?part=snippet&id="+localDataSet[position]+"&key=AIzaSyBsWJfo8m7CXSoxmXKkuCdDgY5AVeeSCqc")
                    .get()
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                    String myresponse = response.body().string();

                    try {
                        JSONObject json = new JSONObject(myresponse);
                        JSONArray res= json.getJSONArray("items");

                        String[] new_items = new String[res.length()];
                        for (int i = 0; i < res.length(); i++) {
                            try {
                                JSONObject songObject = res.getJSONObject(i);
                                JSONObject id=songObject.getJSONObject("snippet");
                                String name=id.getString("title").toString();
                                String chnl=id.getString("channelTitle").toString();
                                JSONObject jsonObject=id.getJSONObject("thumbnails").getJSONObject("maxres");
                                String img=jsonObject.getString("url");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewHolder.textView.setText(name);
                                        viewHolder.textView2.setText(chnl);
                                        Picasso.get().load(img).fit().centerCrop().into(viewHolder.iView);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("E12","not");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("res", myresponse);
                }
            });


        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return localDataSet.length;
        }
    }

}