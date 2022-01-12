package com.ncrdesarrollo.popularmovies.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ncrdesarrollo.popularmovies.R;
import com.ncrdesarrollo.popularmovies.adapters.MovieAdapter;
import com.ncrdesarrollo.popularmovies.models.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetallePeliculaFragment extends Fragment {

    TextView tv_title;
    TextView tv_overview;
    ImageView imageView;

    String URL_DESCRIPTION = "";
    String URL_SIMILARES = "";
    RequestQueue requestQueue;

    List<MovieModel> movieModelList;
    RecyclerView recyclerView;
    ProgressBar progressBar;


    public DetallePeliculaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalle_pelicula, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String idPelicula = getArguments().getString("id");
        URL_DESCRIPTION = "https://api.themoviedb.org/3/movie/" + idPelicula + "?api_key=ea0b061bc609cf004a8c467e95c24ce1&language=es-ES";
        URL_SIMILARES = "https://api.themoviedb.org/3/movie/" + idPelicula + "/similar?api_key=ea0b061bc609cf004a8c467e95c24ce1&language=es-ES";


        tv_title = view.findViewById(R.id.tv_title);
        tv_overview = view.findViewById(R.id.tv_overview);
        imageView = view.findViewById(R.id.img_portada);
        requestQueue = Volley.newRequestQueue(getContext());
        consultarPelicula();

        movieModelList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler);
        progressBar = view.findViewById(R.id.progress_circular);
        consultarPeliculasSimilares();


    }

    private void consultarPelicula() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_DESCRIPTION,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //progressBar.setVisibility(View.GONE);
                        try {

                            tv_title.setText(response.getString("original_title"));
                            tv_overview.setText(response.getString("overview"));
                            Glide.with(getActivity())
                                    .load("https://image.tmdb.org/t/p/w500" + response.getString("backdrop_path"))
                                    .into(imageView);


                        } catch (JSONException e) {
                            Log.i("eeeeee", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof ServerError){
                            mensajeError("Error en el servidor",0);
                        }
                        if (error instanceof NoConnectionError){
                            mensajeError("No hay conexión a internet",0);
                        }

                        if (error instanceof TimeoutError) {
                            mensajeError("Se tardó la respuesta",0);
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);

    }

    private void consultarPeliculasSimilares(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_SIMILARES,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
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
                        progressBar.setVisibility(View.GONE);
                        if (error instanceof ServerError){
                            mensajeError("Error en el servidor",1);
                        }
                        if (error instanceof NoConnectionError){
                            mensajeError("No hay conexión a internet",1);
                        }

                        if (error instanceof TimeoutError) {
                            mensajeError("Se tardó la respuesta",1);
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);

    }

    private void PutDataIntoRecycler(final List<MovieModel> movieModelList) {

        MovieAdapter movieAdapter = new MovieAdapter(getContext(), movieModelList);
        //recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(movieAdapter);

        movieAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = movieModelList.get(recyclerView.getChildAdapterPosition(view)).getId();
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                Navigation.findNavController(view).navigate(R.id.detallePeliculaFragment, bundle);
            }
        });
    }

    private void mensajeError(String mensaje, final int llamado){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mensaje)
                .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        if (llamado==0){
                           consultarPelicula();
                        }else if (llamado==1){
                            consultarPeliculasSimilares();
                        }

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