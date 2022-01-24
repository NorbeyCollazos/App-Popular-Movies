package com.ncrdesarrollo.popularmovies.interfaces;

import com.ncrdesarrollo.popularmovies.models.pojo.MovieModel;

import java.util.List;

public interface InterfacesDetallePelicula {

    interface View{
        void datosPelicula(String image, String title, String overview);
        void peliculasSimilares(List<MovieModel> peliculasSimilares);
        void showProgress();
        void hideProgress();
        void mensajeError(String mensaje);
    }

    interface Presenter{
        void consultarDatosPelicula();
        void consultarListaPeliculasSimilares();
        void showProgress();
        void hideProgress();
        void mensajeError(String mensaje);
    }

    interface Model{
        MovieModel datosPelicula(String id);
        List<MovieModel> peliculasSimilares(String id);
    }
}
