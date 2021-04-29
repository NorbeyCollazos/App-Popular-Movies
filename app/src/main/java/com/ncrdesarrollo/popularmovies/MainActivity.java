package com.ncrdesarrollo.popularmovies;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ncrdesarrollo.popularmovies.adapters.MovieAdapter;
import com.ncrdesarrollo.popularmovies.interfaces.MovieListInterface;
import com.ncrdesarrollo.popularmovies.models.MovieModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieListInterface {

    private static String JSON_URL = "https://api.themoviedb.org/3/movie/popular?api_key=ea0b061bc609cf004a8c467e95c24ce1";
    private static String JSON_URL_SEARCH = "https://api.themoviedb.org/3/search/movie?api_key=ea0b061bc609cf004a8c467e95c24ce1&query=";
    List<MovieModel> movieModelList;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieModelList = new ArrayList<>();
        recyclerView = findViewById(R.id.myRecicler);
        progressBar = findViewById(R.id.progress_circular);
        requestQueue = Volley.newRequestQueue(this);
        jsonObjectRequest();

    }


    private void jsonObjectRequest(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                JSON_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressBar();
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            int size = jsonArray.length();
                            for (int i = 0; i < size; i++){
                                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                MovieModel model = new MovieModel();
                                model.setId(jsonObject.getString("id"));
                                model.setTitle(jsonObject.getString("title"));
                                model.setPoster_path(jsonObject.getString("poster_path"));
                                model.setOverview(jsonObject.getString("overview"));
                                movieModelList.add(model);
                                PutDataIntoRecycler(movieModelList);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof ServerError){
                            Toast.makeText(MainActivity.this, "Error en el servidor", Toast.LENGTH_SHORT).show();
                        }
                        if (error instanceof NoConnectionError){
                            Toast.makeText(MainActivity.this, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);

    }

    private void jsonObjectRequestSearch(String query){
        movieModelList.clear();
        if (query.isEmpty()){
            jsonObjectRequest();
        }else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    JSON_URL_SEARCH + query,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideProgressBar();
                            try {
                                JSONArray jsonArray = response.getJSONArray("results");
                                int size = jsonArray.length();
                                for (int i = 0; i < size; i++) {
                                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                    MovieModel model = new MovieModel();
                                    model.setId(jsonObject.getString("id"));
                                    model.setTitle(jsonObject.getString("title"));
                                    model.setPoster_path(jsonObject.getString("poster_path"));
                                    model.setOverview(jsonObject.getString("overview"));
                                    movieModelList.add(model);
                                    PutDataIntoRecycler(movieModelList);


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof ServerError) {
                                Toast.makeText(MainActivity.this, "Error en el servidor", Toast.LENGTH_SHORT).show();
                            }
                            if (error instanceof NoConnectionError) {
                                Toast.makeText(MainActivity.this, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );

            requestQueue.add(jsonObjectRequest);
        }

    }

    private void PutDataIntoRecycler(List<MovieModel> movieModelList) {

        MovieAdapter movieAdapter = new MovieAdapter(this, movieModelList);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(movieAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                jsonObjectRequestSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                jsonObjectRequestSearch(newText);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

}