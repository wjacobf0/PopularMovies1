package com.example.jake.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */

public class MovieGridFragment extends Fragment {

    // Class property so async task can update
    private MovieAdapter moviesAdapter;
    private GridView gridView;
    private final String INSTANCE_STATE_DATA_KEY = "MOVIE_DATA";
    private final String INSTANCE_STATE_POS_KEY = "GRID_POSITION";
    private final String INSTANCE_STATE_SORT_KEY = "MOVIE_SORT";
    private String SortSetting = "";

    private final String API_KEY = "";
    private final String TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated?api_key=";
    private final String POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key=";
    private final String w342 = "w342";
    private String URL_BASE = "http://image.tmdb.org/t/p/";

    public MovieGridFragment() {
    }

    private void GetMovieData(String pref)
    {
        FetchMovieDataTask newFetchTask = new FetchMovieDataTask();

        String MovieList_URL = "";
        if(pref.equals(getString(R.string.pref_sort_value_default)))
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

        String currentSetting = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(
                        getString(R.string.pref_sort_shared),
                        getString(R.string.pref_sort_value_default)
                );

        if(!SortSetting.equals(currentSetting)) {
            SortSetting = currentSetting;
            // Start task to fetch data
            GetMovieData(currentSetting);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Let activity select image size that needs to be fetched
        URL_BASE = URL_BASE + w342;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        ArrayList<Movie> currentData = moviesAdapter.data;
        outState.putParcelableArrayList(INSTANCE_STATE_DATA_KEY,currentData);
        outState.putInt(INSTANCE_STATE_POS_KEY, gridView.getFirstVisiblePosition());
        outState.putString(INSTANCE_STATE_SORT_KEY, SortSetting);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<Movie> moviesList;
        int position = -1;
        if(savedInstanceState != null)
        {
            moviesList = savedInstanceState.getParcelableArrayList(INSTANCE_STATE_DATA_KEY);
            position = savedInstanceState.getInt(INSTANCE_STATE_POS_KEY);
            SortSetting = savedInstanceState.getString(INSTANCE_STATE_SORT_KEY);
        }
        else
        {
            moviesList = new ArrayList<>();
        }

        // Setup the grid view
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
                movieDetailIntent.putExtra(Intent.EXTRA_TEXT, URL_BASE);
                movieDetailIntent.putExtra(Intent.EXTRA_INTENT, selectedMovie);
                startActivity(movieDetailIntent);
            }
        });

        if(position >= 0)
            gridView.smoothScrollToPosition(position); // restore view position

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
                    String line;
                    while((line = reader.readLine()) != null)
                    {
                        stringBuffer.append(line).append("\n");
                    }
                    reader.close();

                    movieJSONData = JSONMovieResultParser.getMoviesFromJSON(stringBuffer.toString());
                }

            }
            catch(Exception ex)
            {
                Log.e(LOG_TAG, "Error ", ex);
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
                try{
                    moviesAdapter.clear();
                    moviesAdapter.addAll(result);
                    // also reset grid view to start, otherwise users could start at end of data.
                    gridView.smoothScrollToPosition(0);
                }
                catch (Exception ex)
                {
                    Log.e(LOG_TAG, "Error ", ex);
                }
            }

        }

    }

}