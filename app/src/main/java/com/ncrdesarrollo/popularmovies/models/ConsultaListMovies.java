package com.ncrdesarrollo.popularmovies.models;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ncrdesarrollo.popularmovies.MainActivity;
import com.ncrdesarrollo.popularmovies.adapters.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConsultaListMovies {
    Context context;
    private static String JSON_URL = "https://api.themoviedb.org/3/movie/popular?api_key=ea0b061bc609cf004a8c467e95c24ce1";
    List<MovieModel> movieModelList;
    RequestQueue requestQueue;

    public ConsultaListMovies(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        movieModelList = new ArrayList<>();

    }

    public void jsonObjectRequest(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                JSON_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //hideProgressBar();
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
                            Toast.makeText(context, "Error en el servidor", Toast.LENGTH_SHORT).show();
                        }
                        if (error instanceof NoConnectionError){
                            Toast.makeText(context, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
        MovieAdapter movieAdapter = new MovieAdapter(context, movieModelList);


    }
}
