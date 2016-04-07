package com.example.jake.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    private String BASE_URL;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Get url to load images
        BASE_URL = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // If this was activity was called with startedActivity
        Intent sentIntent = getActivity().getIntent();
        if(sentIntent != null)
        {
            // check to see if we were sent data
            final Movie movieData = sentIntent.getParcelableExtra(Intent.EXTRA_INTENT);
            if(movieData != null && BASE_URL != null)
            {
                // get views
                View titleHeader = rootView.findViewById(R.id.title_header);
                ImageView detail_Image = (ImageView)rootView.findViewById(R.id.detail_image_View);
                TextView movie_title = (TextView) rootView.findViewById(R.id.text_movie_title);
                TextView movie_year = (TextView) rootView.findViewById(R.id.text_year);
                TextView movie_rating = (TextView) rootView.findViewById(R.id.text_rating);
                TextView movie_synopsis = (TextView) rootView.findViewById(R.id.text_synopsis);

                // set views with movie data
                movie_title.setText(movieData.getTitle());
                movie_year.setText(movieData.getReleaseDate());
                movie_rating.setText(String.format("%.1f", movieData.getRating()) +"/10");
                movie_synopsis.setText(movieData.getSynopsis());

                PicassoTransformation transformation = new PicassoTransformation(detail_Image);
                String url = BASE_URL + movieData.getUrl();
                Picasso.with(getContext())
                        .load(url)
                        .error(R.drawable.ic_info_black_24dp)
                        .transform(transformation)
                        .into(detail_Image);

            }
        }

        return rootView;
    }
}
