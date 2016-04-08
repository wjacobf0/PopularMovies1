package com.example.jake.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;


public class MovieAdapter extends ArrayAdapter<Movie> {

    Context context;
    int layoutId;
    ArrayList<Movie> data;
    String URL_BASE;

    public MovieAdapter(Context context, int layoutId, ArrayList<Movie> data, String pURL_BASE)
    {
        super(context, layoutId, data);

        this.context = context;
        this.layoutId = layoutId;
        this.data = data;
        URL_BASE = pURL_BASE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        GridView parentGrid = (GridView)parent;
        int numColumns = parentGrid.getNumColumns();
        int viewWidth = parentGrid.getMeasuredWidth();
        int width = viewWidth/numColumns;

        if(convertView == null) // create the view
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutId, parent, false);
        }

        ImageView moviePoster= (ImageView)convertView;
        moviePoster.setAdjustViewBounds(true);

        // Get the url
        String url = URL_BASE + data.get(position).getUrl();

        // Get picasso transformation
        PicassoTransformation transformation = new PicassoTransformation(moviePoster);

        // Put the data in the view
        Picasso.with(context)
                .load(url)
                .error(R.drawable.ic_info_black_24dp)
                .transform(transformation)
                .into( moviePoster);

        return convertView;
    }
}