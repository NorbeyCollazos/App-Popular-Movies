package com.ncrdesarrollo.popularmovies.presenter;

import android.content.Context;
import android.os.AsyncTask;

import com.ncrdesarrollo.popularmovies.interfaces.InterfacesListaPeliculas;
import com.ncrdesarrollo.popularmovies.models.ModelListaPeliculas;
import com.ncrdesarrollo.popularmovies.models.pojo.MovieModel;

import java.util.List;

public class PresenterListaPeliculas implements InterfacesListaPeliculas.Presenter {

    Context context;
    InterfacesListaPeliculas.View view;
    InterfacesListaPeliculas.Model model;

    public PresenterListaPeliculas(Context context, InterfacesListaPeliculas.View view) {
        this.context = context;
        this.view = view;
        this.model = new ModelListaPeliculas(context, this);
    }

    @Override
    public void realizarConsulta() {
        new realizarConsulta().execute();
    }

    @Override
    public void mostrarListaPeliculas(List<MovieModel> list) {
        view.listaPeliculas(list);
    }

    @Override
    public void mensajeError(String mensaje) {
        view.mostrarMensajeError(mensaje);
    }

    @Override
    public void showProgress() {
        view.showProgress();
    }

    @Override
    public void hideProgress() {
        view.hideProgress();
    }



    //metodo asincrono para realizar consulta de internet
    private class realizarConsulta extends AsyncTask<List<MovieModel>, Void, List<MovieModel>>{

        @Override
        protected List<MovieModel> doInBackground(List<MovieModel>... lists) {
            List<MovieModel> list = model.consultaLista();
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
            mostrarListaPeliculas(movieModels);
        }
    }
}
