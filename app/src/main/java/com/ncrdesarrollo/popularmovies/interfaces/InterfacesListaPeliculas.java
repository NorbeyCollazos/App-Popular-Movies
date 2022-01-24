package com.ncrdesarrollo.popularmovies.interfaces;

import com.ncrdesarrollo.popularmovies.models.pojo.MovieModel;

import java.util.List;

public interface InterfacesListaPeliculas {

    interface View{
        void findElement(View view);
        void listaPeliculas(List<MovieModel> list);
        void mostrarMensajeError(String mensaje);
        void showProgress();
        void hideProgress();
    }

    interface Presenter{
        void realizarConsulta();
        void mostrarListaPeliculas(List<MovieModel> list);
        void mensajeError(String mensaje);
        void showProgress();
        void hideProgress();
    }

    interface Model{
        List<MovieModel> consultaLista();
    }
}
