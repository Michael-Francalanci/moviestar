package com.francalanci.michael.moviestars;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by michael on 06/02/17.
 */

public class ImageAdapter extends ArrayAdapter<JSONObject> {

    private final String poster = "poster_path";
    private final String id = "id";
    private final String HOST_IMAGE = BuildConfig.THEMOVIEDB_IMAGE_HOST;
    private final String size = "w342/";

    public ImageAdapter(Activity context, List<JSONObject> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject obj = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }
        ImageView iconView = (ImageView) convertView.findViewById(R.id.movie_image);
        try {
            String url = HOST_IMAGE+size+obj.getString(poster);
            Picasso.with(getContext()).load(url).into(iconView);
            convertView.setTag(obj.getString(id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
