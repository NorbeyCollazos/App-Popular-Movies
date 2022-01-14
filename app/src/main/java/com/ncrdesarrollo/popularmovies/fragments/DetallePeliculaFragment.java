package com.ncrdesarrollo.popularmovies.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
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
import com.ncrdesarrollo.popularmovies.models.ConsultasApiRest;
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

    String idPelicula;

    List<MovieModel> movieModelList;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    ConsultasApiRest consultasApiRest;


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

        idPelicula = getArguments().getString("id");
        consultasApiRest = new ConsultasApiRest(getContext());

        tv_title = view.findViewById(R.id.tv_title);
        tv_overview = view.findViewById(R.id.tv_overview);
        imageView = view.findViewById(R.id.img_portada);

        movieModelList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler);
        progressBar = view.findViewById(R.id.progress_circular);

        new ConsultaPelicula().execute();
        new ConsultaPeliculasSimilares().execute();


    }

    private class ConsultaPelicula extends AsyncTask<MovieModel, Void, MovieModel>{

        @Override
        protected MovieModel doInBackground(MovieModel... movieModels) {
            MovieModel movieModel = consultasApiRest.consultarPelicula(idPelicula);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return movieModel;
        }

        @Override
        protected void onPostExecute(MovieModel movieModel) {
            super.onPostExecute(movieModel);
            tv_title.setText(movieModel.getTitle());
            tv_overview.setText(movieModel.getOverview());
            Glide.with(getActivity())
                    .load(movieModel.getPoster_path())
                    .into(imageView);
        }
    }

    private class ConsultaPeliculasSimilares extends AsyncTask<List<MovieModel>, Void, List<MovieModel>>{

        @Override
        protected List<MovieModel> doInBackground(List<MovieModel>... lists) {
            movieModelList = consultasApiRest.consultarPeliculasSimilares(idPelicula);
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

}