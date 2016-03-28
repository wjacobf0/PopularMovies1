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
    private View rootView = null;
    private View titleHeader = null;
    private ImageView detail_Image = null;
    private TextView movie_title = null;
    private TextView movie_year = null;
    private TextView movie_rating = null;
    private TextView movie_synopsis = null;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Get url to load images
        BASE_URL = getActivity().getIntent().getStringExtra(Intent.EXTRA_PROCESS_TEXT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // If this was activity was called with startedActivity
        Intent sentIntent = getActivity().getIntent();
        if(sentIntent != null)
        {
            // check to see if we were sent data
            final Movie movieData = sentIntent.getParcelableExtra(Intent.EXTRA_INTENT);
            if(movieData != null && BASE_URL != null)
            {
                // get views
                titleHeader = rootView.findViewById(R.id.title_header);
                detail_Image = (ImageView)rootView.findViewById(R.id.detail_image_View);
                movie_title = (TextView) rootView.findViewById(R.id.text_movie_title);
                movie_year = (TextView) rootView.findViewById(R.id.text_year);
                movie_rating = (TextView) rootView.findViewById(R.id.text_rating);
                movie_synopsis = (TextView) rootView.findViewById(R.id.text_synopsis);

                // set views with movie data
                movie_title.setText(movieData.getTitle());
                movie_year.setText(movieData.getReleaseDate());
                movie_rating.setText(String.format("%.1f", movieData.getRating()) +"/10");
                movie_synopsis.setText(movieData.getSynopsis());

                // Setup listener to get layout width, and set Picasso image size
                detail_Image.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (detail_Image != null) {
                            // remove listener
                            detail_Image.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                            // get height and width
                            int width = rootView.getWidth();
                            int height = rootView.getHeight();

                            // set title header size
                            titleHeader.setMinimumHeight(height / 5);

                            // set image size
                            String url = BASE_URL + movieData.getUrl();
                            Picasso.with(getContext())
                                    .load(url)
                                    .resize(width / 3, 0)// set height to 0 so picasso will scale the image
                                    .into(detail_Image);
                        }
                    }
                });

            }
        }

        return rootView;
    }
}
