package com.ncrdesarrollo.popularmovies.models;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ncrdesarrollo.popularmovies.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConsultasApiRest {

    Context context;
    List<MovieModel> list;
    private static final String API_TMDT = BuildConfig.CLAVE_API_TMDT;
    private static String JSON_URL = "https://api.themoviedb.org/3/movie/popular?api_key="+API_TMDT;
    RequestQueue requestQueue;

    public ConsultasApiRest(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        list = new ArrayList<>();
    }

    public List<MovieModel> consultarListaPeliculas(){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                JSON_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //progressBar.setVisibility(View.GONE);
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
                                list.add(model);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressBar.setVisibility(View.GONE);
                        if (error instanceof ServerError){
                            mensajeError("Error en el servidor");
                        }
                        if (error instanceof NoConnectionError){
                            mensajeError("No hay conexión a internet");
                        }

                        if (error instanceof TimeoutError) {
                            mensajeError("Se tardó la respuesta");
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);

        return list;
    }


    public MovieModel consultarPelicula(String idPelicula) {

        String URL_DESCRIPTION = "https://api.themoviedb.org/3/movie/" + idPelicula + "?api_key="+API_TMDT+"&language=es-ES";

        final MovieModel movie = new MovieModel();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_DESCRIPTION,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //progressBar.setVisibility(View.GONE);
                        try {
                            movie.setTitle(response.getString("original_title"));
                            movie.setOverview(response.getString("overview"));
                            movie.setPoster_path("https://image.tmdb.org/t/p/w500" + response.getString("backdrop_path"));

                        } catch (JSONException e) {
                            Log.i("eeeeee", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof ServerError){
                            mensajeError("Error en el servidor");
                        }
                        if (error instanceof NoConnectionError){
                            mensajeError("No hay conexión a internet");
                        }

                        if (error instanceof TimeoutError) {
                            mensajeError("Se tardó la respuesta");
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);

        return movie;

    }


    public List<MovieModel> consultarPeliculasSimilares(String idPelicula){

        String URL_SIMILARES = "https://api.themoviedb.org/3/movie/" + idPelicula + "/similar?api_key="+API_TMDT+"&language=es-ES";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_SIMILARES,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                                list.add(model);

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
                            mensajeError("Error en el servidor");
                        }
                        if (error instanceof NoConnectionError){
                            mensajeError("No hay conexión a internet");
                        }

                        if (error instanceof TimeoutError) {
                            mensajeError("Se tardó la respuesta");
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);

        return list;

    }





    private void mensajeError(String mensaje){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(mensaje)
                .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!


                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });

        builder.show();
    }
}
