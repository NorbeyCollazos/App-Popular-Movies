package com.ncrdesarrollo.popularmovies.presenter;

import android.content.Context;
import android.os.AsyncTask;

import com.ncrdesarrollo.popularmovies.interfaces.InterfacesDetallePelicula;
import com.ncrdesarrollo.popularmovies.models.ModelDetallePelicula;
import com.ncrdesarrollo.popularmovies.models.pojo.MovieModel;

import java.util.List;

public class PresenterDetallePelicula implements InterfacesDetallePelicula.Presenter {

    Context context;
    InterfacesDetallePelicula.View view;
    InterfacesDetallePelicula.Model model;
    String idPelicula;

    public PresenterDetallePelicula(Context context, InterfacesDetallePelicula.View view, String idPelicula) {
        this.context = context;
        this.view = view;
        this.model = new ModelDetallePelicula(context, this);
        this.idPelicula = idPelicula;
    }

    @Override
    public void consultarDatosPelicula() {

        new consultaDatosPelicula().execute();
    }

    @Override
    public void consultarListaPeliculasSimilares() {

        new consultalistaPeliculasSimilares().execute();
    }

    @Override
    public void showProgress() {
        view.showProgress();
    }

    @Override
    public void hideProgress() {
        view.hideProgress();
    }

    @Override
    public void mensajeError(String mensaje) {
        view.mensajeError(mensaje);
    }

    private class consultaDatosPelicula extends AsyncTask<MovieModel, Void, MovieModel>{

        @Override
        protected MovieModel doInBackground(MovieModel... movieModels) {

            MovieModel movieModel = model.datosPelicula(idPelicula);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return movieModel;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(MovieModel movieModel) {
            super.onPostExecute(movieModel);
            view.datosPelicula(movieModel.getPoster_path(), movieModel.getTitle(), movieModel.getOverview());
        }
    }

    private class consultalistaPeliculasSimilares extends AsyncTask<List<MovieModel>, Void, List<MovieModel>>{

        @Override
        protected List<MovieModel> doInBackground(List<MovieModel>... lists) {

            List<MovieModel> list = model.peliculasSimilares(idPelicula);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return list;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected void onPostExecute(List<MovieModel> movieModels) {
            super.onPostExecute(movieModels);
            hideProgress();
            view.peliculasSimilares(movieModels);
        }
    }


}
