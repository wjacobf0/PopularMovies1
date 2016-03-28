package com.example.jake.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jake on 3/19/16.
 */
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

        // Get the url w185
        String url = URL_BASE + data.get(position).getUrl();

        // Put the data in the view
        ImageView gridImage =  (ImageView)convertView.findViewById(R.id.gridimageView);
        Picasso.with(context)
                .load(url)
                .resize(width, 0) // set height to 0 so picasso will scale the image
                .into(gridImage);

        return convertView;
    }
}