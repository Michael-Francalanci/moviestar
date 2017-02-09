package com.francalanci.michael.moviestars;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ImageAdapter imageAdapter;
    private GridView gridview;
    int order = 0; //0 top rated, 1 popular
    private TheMovieAPI apiClient;
    private boolean first = true;
    private JSONArray arrayJSON;
    private ArrayList<JSONObject> list;
    private ProgressBar progress;

    public static final String[] extraDetails = {"original_title", "overview", "vote_average", "poster_path",
            "release_date"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progress = (ProgressBar) findViewById(R.id.progress);
        apiClient = new TheMovieAPI();
        gridview = (GridView) findViewById(R.id.grid_movies);
        gridview.setVisibility(View.INVISIBLE);
        list = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, list);
        gridview.setAdapter(imageAdapter);
        setGridViewOnItemClick();
        refreshGridView(0);
    }

    private void setGridViewOnItemClick() {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                try {
                    for (String extra : extraDetails) {
                        intent.putExtra(extra, list.get(i).getString(extra));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.order_popular) {
            refreshGridView(1);
            return true;
        }

        if (id == R.id.order_rated) {
            refreshGridView(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshGridView(int orderNew) {
        if (first || order != orderNew) {
            if (orderNew == 0) {
                getTopRated(apiClient.getTopRated());
            } else {
                getTopRated(apiClient.getPopular());
            }
        }
    }

    private void getTopRated(Call call) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response == null || !response.isSuccessful()) {
                    showError();
                } else {
                    list.clear();
                    String responseStr = response.body().string();
                    try {

                        JSONObject mJsonObject = new JSONObject(responseStr);
                        arrayJSON = mJsonObject.getJSONArray("results");
                        for (int i = 0; i < arrayJSON.length(); i++) {
                            list.add(arrayJSON.getJSONObject(i));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageAdapter.notifyDataSetChanged();
                                progress.setVisibility(View.INVISIBLE);
                                gridview.setVisibility(View.VISIBLE);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showError() {
        Toast.makeText(this, getString(R.string.error_call_api), Toast.LENGTH_LONG).show();
    }
}
