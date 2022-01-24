package com.ncrdesarrollo.popularmovies.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ncrdesarrollo.popularmovies.R;
import com.ncrdesarrollo.popularmovies.adapters.MovieAdapter;
import com.ncrdesarrollo.popularmovies.interfaces.InterfacesListaPeliculas;
import com.ncrdesarrollo.popularmovies.models.pojo.MovieModel;
import com.ncrdesarrollo.popularmovies.presenter.PresenterListaPeliculas;
import java.util.List;

public class ListaPeliculaFragment extends Fragment implements InterfacesListaPeliculas.View {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    InterfacesListaPeliculas.Presenter presenter;

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

        recyclerView = view.findViewById(R.id.myRecicler);
        progressBar = view.findViewById(R.id.progress_circular);
        presenter = new PresenterListaPeliculas(getContext(), this);
        presenter.realizarConsulta();

    }


    @Override
    public void findElement(InterfacesListaPeliculas.View view) {

    }

    @Override
    public void listaPeliculas(final List<MovieModel> list) {
        MovieAdapter movieAdapter = new MovieAdapter(getContext(), list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setAdapter(movieAdapter);

        movieAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = list.get(recyclerView.getChildAdapterPosition(view)).getId();
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                Navigation.findNavController(view).navigate(R.id.detallePeliculaFragment, bundle);
            }
        });
    }

    @Override
    public void mostrarMensajeError(String mensaje) {
        Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }


}