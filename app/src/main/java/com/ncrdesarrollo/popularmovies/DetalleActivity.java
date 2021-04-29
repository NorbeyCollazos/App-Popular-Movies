package com.ncrdesarrollo.popularmovies;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class DetalleActivity extends AppCompatActivity {

    TextView tv_title;
    TextView tv_overview;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String img = getIntent().getStringExtra("img");
        String title = getIntent().getStringExtra("title");
        String overview = getIntent().getStringExtra("overview");

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(title);
        tv_overview = findViewById(R.id.tv_overview);
        tv_overview.setText(overview);

        imageView = findViewById(R.id.img_portada);
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500"+img)
                .into(imageView);



    }
}