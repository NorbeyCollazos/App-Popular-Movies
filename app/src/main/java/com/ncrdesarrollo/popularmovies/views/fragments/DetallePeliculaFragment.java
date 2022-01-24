package com.ncrdesarrollo.popularmovies.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ncrdesarrollo.popularmovies.R;
import com.ncrdesarrollo.popularmovies.adapters.MovieAdapter;
import com.ncrdesarrollo.popularmovies.interfaces.InterfacesDetallePelicula;
import com.ncrdesarrollo.popularmovies.models.pojo.MovieModel;
import com.ncrdesarrollo.popularmovies.presenter.PresenterDetallePelicula;

import java.util.List;

public class DetallePeliculaFragment extends Fragment implements InterfacesDetallePelicula.View {

    TextView tv_title;
    TextView tv_overview;
    ImageView imageView;
    String idPelicula;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    InterfacesDetallePelicula.Presenter presenter;


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

        tv_title = view.findViewById(R.id.tv_title);
        tv_overview = view.findViewById(R.id.tv_overview);
        imageView = view.findViewById(R.id.img_portada);

        recyclerView = view.findViewById(R.id.recycler);
        progressBar = view.findViewById(R.id.progress_circular);

        presenter = new PresenterDetallePelicula(getContext(), this, idPelicula);
        presenter.consultarDatosPelicula();
        presenter.consultarListaPeliculasSimilares();


    }

    @Override
    public void datosPelicula(String image, String title, String overview) {
        tv_title.setText(title);
        tv_overview.setText(overview);
        Glide.with(getActivity())
                .load(image)
                .into(imageView);
    }

    @Override
    public void peliculasSimilares(final List<MovieModel> peliculasSimilares) {
        MovieAdapter movieAdapter = new MovieAdapter(getContext(), peliculasSimilares);
        //recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(movieAdapter);

        movieAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = peliculasSimilares.get(recyclerView.getChildAdapterPosition(view)).getId();
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                Navigation.findNavController(view).navigate(R.id.detallePeliculaFragment, bundle);
            }
        });
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void mensajeError(String mensaje) {
        Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
    }



}