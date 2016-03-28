package com.example.jake.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */

public class MovieGridFragment extends Fragment {

    // Class property so async task can update
    MovieAdapter moviesAdapter;
    GridView gridView;

    private String API_KEY = "";
    private String TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated?api_key=";
    private String POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key=";
    private String w185 = "w185";
    private String w342 = "w342";
    private String w780 = "w780";
    private String URL_BASE = "http://image.tmdb.org/t/p/";

    public MovieGridFragment() {
    }

    private void GetMovieData()
    {
        FetchMovieDataTask newFetchTask = new FetchMovieDataTask();
        String sortSetting = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(
                    getString(R.string.pref_sort_shared),
                    getString(R.string.pref_sort_value_default)
                );

        String MovieList_URL = "";
        if(sortSetting.equals(getString(R.string.pref_sort_value_default)))
        {
            MovieList_URL = POPULAR+API_KEY;
        }
        else
        {
            MovieList_URL = TOP_RATED+API_KEY;
        }

        newFetchTask.execute(MovieList_URL);
    }

    @Override
    public void onStart(){
        super.onStart();
        GetMovieData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Let activity select image size that needs to be fetched
        URL_BASE = URL_BASE + w342;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Setup the grid view
        ArrayList<Movie> moviesList = new ArrayList<Movie>();
        moviesAdapter = new MovieAdapter(
                // The current context
                getActivity(),
                // The layout for the grid item
                R.layout.grid_item_movie,
                // The data to use for the grid view
                moviesList,
                // The url base to use when the adapter gets the image
                URL_BASE
        );

        // Get a reference to the grid view
        gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(moviesAdapter);

        // set on item click listener to launch movie detail activity
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the movie data that was selected
                Movie selectedMovie = moviesAdapter.getItem(position);

                // create intent with data and open detail activity
                Intent movieDetailIntent = new Intent(getContext(), MovieDetail.class);
                movieDetailIntent.putExtra(Intent.EXTRA_PROCESS_TEXT, URL_BASE);
                movieDetailIntent.putExtra(Intent.EXTRA_INTENT, selectedMovie);
                startActivity(movieDetailIntent);
            }
        });

        // Start task to fetch data
        GetMovieData();

        return rootView;
    }

    // Async Task to fetch data
    public class FetchMovieDataTask extends AsyncTask<String, Void, Movie[]> {

        private final String LOG_TAG = FetchMovieDataTask.class.getSimpleName();

        protected Movie[] doInBackground(String... urls) {
            // This will be the data recieved from the movie db api.
            Movie[] movieJSONData = null;
            HttpURLConnection urlConnection = null;

            try
            {
                URL dataUrl = new URL(urls[0]);

                urlConnection = (HttpURLConnection) dataUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer(); // Thread safe, and string builder is not

                if(inputStream != null)
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = null;
                    while((line = reader.readLine()) != null)
                    {
                        stringBuffer.append(line + "\n");
                    }
                    reader.close();

                    movieJSONData = JSONMovieResultParser.getMoviesFromJSON(stringBuffer.toString());
                }

            }
            catch(Exception ex)
            {
                Log.e("LOG_TAG", "Error ", ex);
            }
            finally {
                if(urlConnection != null)
                {
                    urlConnection.disconnect();
                }
            }

            return movieJSONData;
        }

        protected void onPostExecute(Movie[] result)
        {
            if(result != null)
            {
                moviesAdapter.clear();
                moviesAdapter.addAll(result);

                // also reset grid view to start, otherwise users could start at end of data.
                gridView.smoothScrollToPosition(0);
            }

        }

    }

}