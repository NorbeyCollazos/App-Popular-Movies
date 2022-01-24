package com.ncrdesarrollo.popularmovies.models;

import android.content.Context;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ncrdesarrollo.popularmovies.BuildConfig;
import com.ncrdesarrollo.popularmovies.interfaces.InterfacesListaPeliculas;
import com.ncrdesarrollo.popularmovies.models.pojo.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModelListaPeliculas implements InterfacesListaPeliculas.Model {

    Context context;
    InterfacesListaPeliculas.Presenter presenter;

    List<MovieModel> list;
    private static final String API_TMDT = BuildConfig.CLAVE_API_TMDT;
    private static String JSON_URL = "https://api.themoviedb.org/3/movie/popular?api_key="+API_TMDT;
    RequestQueue requestQueue;


    public ModelListaPeliculas(Context context, InterfacesListaPeliculas.Presenter presenter) {
        this.context = context;
        this.presenter = presenter;
        requestQueue = Volley.newRequestQueue(context);
        list = new ArrayList<>();

    }



    @Override
    public List<MovieModel> consultaLista()  {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                JSON_URL,
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
                            presenter.mensajeError("Error en el servidor");
                        }
                        if (error instanceof NoConnectionError){
                            presenter.mensajeError("No hay conexión a internet");
                        }

                        if (error instanceof TimeoutError) {
                            presenter.mensajeError("Se tardó la respuesta");
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);

        return list;
    }
}
