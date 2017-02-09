package com.francalanci.michael.moviestars;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private final String HOST_IMAGE = BuildConfig.THEMOVIEDB_IMAGE_HOST;
    private final String size = "w185/";
    private final String poster_path = "poster_path";
    private final String id_path = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setInfoView();
    }

    private void setInfoView() {
        Intent intent = getIntent();
        for (String extra: MainActivity.extraDetails) {
            int identifier = getResources().getIdentifier(extra, id_path, DetailsActivity.this.getPackageName());
            if(!extra.equals(poster_path)) {
                ((TextView) findViewById(identifier)).setText(intent.getStringExtra(extra));
            } else {
                ImageView poster = (ImageView) findViewById(identifier);
                String url = HOST_IMAGE+size+intent.getStringExtra(extra);
                Picasso.with(this).load(url).into(poster);
            }
        }
    }

}
