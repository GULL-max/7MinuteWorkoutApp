package com.gullafshan.workoutplans;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiDataActivity extends AppCompatActivity {

    private RecyclerView rvData;
    private ProgressBar progressBar;
    private ApiDataAdapter adapter;
    private List<ApiDataItem> dataList;

    private static final String PREFS = "api_prefs";
    private static final String KEY_OFFLINE_DATA = "offline_data";

    // Real API for testing
    private static final String API_URL = "https://jsonplaceholder.typicode.com/posts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_data);

        // Find views
        rvData = findViewById(R.id.rvData);
        progressBar = findViewById(R.id.progressBar);

        // Initialize data list and adapter
        dataList = new ArrayList<>();
        adapter = new ApiDataAdapter(dataList);

        // Set up RecyclerView
        rvData.setLayoutManager(new LinearLayoutManager(this));
        rvData.setAdapter(adapter);

        // Load offline data first
        loadOfflineData();

        // Fetch fresh data from API
        fetchApiData();
    }

    // Load offline data if available
    private void loadOfflineData() {
        SharedPreferences sp = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String json = sp.getString(KEY_OFFLINE_DATA, null);

        if (json != null) {
            try {
                JSONArray array = new JSONArray(json);
                dataList.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String title = obj.optString("title", "No Title");
                    String description = obj.optString("body", "No Description"); // fixed key
                    dataList.add(new ApiDataItem(title, description));
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Fetch data from API
    private void fetchApiData() {
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                API_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        dataList.clear();
                        try {
                            // Parse and add to list
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                String title = obj.optString("title", "No Title");
                                String description = obj.optString("body", "No Description"); // fixed key
                                dataList.add(new ApiDataItem(title, description));
                            }
                            adapter.notifyDataSetChanged();

                            // Save JSON string offline
                            saveOfflineData(response.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ApiDataActivity.this, "Parsing error", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ApiDataActivity.this, "Failed to fetch data. Showing offline data.", Toast.LENGTH_SHORT).show();

                        if (dataList.isEmpty()) {
                            dataList.addAll(getSampleData());
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
        );

        queue.add(jsonArrayRequest);
    }

    // Save JSON string for offline use
    private void saveOfflineData(String json) {
        SharedPreferences sp = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_OFFLINE_DATA, json).apply();
    }

    // Sample fallback data
    private List<ApiDataItem> getSampleData() {
        List<ApiDataItem> list = new ArrayList<>();
        list.add(new ApiDataItem("Push-ups", "Do 20 push-ups."));
        list.add(new ApiDataItem("Squats", "Do 15 squats."));
        list.add(new ApiDataItem("Plank", "Hold plank for 30 seconds."));
        return list;
    }
}


