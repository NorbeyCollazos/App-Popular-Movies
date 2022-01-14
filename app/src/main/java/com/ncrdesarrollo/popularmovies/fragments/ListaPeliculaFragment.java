package com.ncrdesarrollo.popularmovies.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import com.ncrdesarrollo.popularmovies.R;
import com.ncrdesarrollo.popularmovies.adapters.MovieAdapter;
import com.ncrdesarrollo.popularmovies.models.ConsultasApiRest;
import com.ncrdesarrollo.popularmovies.models.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaPeliculaFragment extends Fragment {

    List<MovieModel> movieModelList;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ConsultasApiRest consultasApiRest;

    public ListaPeliculaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_pelicula, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieModelList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.myRecicler);
        progressBar = view.findViewById(R.id.progress_circular);
        consultasApiRest = new ConsultasApiRest(getContext());
        new ConsultaPeliculas().execute();

    }

    private class ConsultaPeliculas extends AsyncTask<List<MovieModel>, Void, List<MovieModel>>{

        @Override
        protected List<MovieModel> doInBackground(List<MovieModel>... lists) {
            movieModelList = consultasApiRest.consultarListaPeliculas();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return movieModelList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<MovieModel> movieModels) {
            super.onPostExecute(movieModels);
            PutDataIntoRecycler(movieModels);
            progressBar.setVisibility(View.GONE);
        }
    }


    private void PutDataIntoRecycler(final List<MovieModel> movieModelList) {

        MovieAdapter movieAdapter = new MovieAdapter(getContext(), movieModelList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
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




}