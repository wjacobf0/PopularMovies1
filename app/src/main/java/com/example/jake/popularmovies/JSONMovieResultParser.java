package com.example.jake.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jake on 3/15/16.
 */
public class JSONMovieResultParser {
    public static Movie[] getMoviesFromJSON(String JSONMovieList)
            throws JSONException
    {
        List<Movie> movieNames = new LinkedList<Movie>();
        JSONObject movieListObject = new JSONObject(JSONMovieList);
        JSONArray movieList = movieListObject.getJSONArray("results");

        // temp items to store data while in loop.
        int id;
        String title;
        String urlPath;
        String synopsis;
        double rating;
        String releaseDate;


        int listLength = movieList.length();
        for(int i = 0; i < listLength; i++) {
            JSONObject movie = movieList.getJSONObject(i);
            id = movie.getInt("id");
            title = movie.getString("original_title");
            urlPath = movie.getString("poster_path");
            synopsis = movie.getString("overview");
            rating = movie.getDouble("vote_average");
            releaseDate = movie.getString("release_date").substring(0, 4);

            Movie movieItem = new Movie(id, title, urlPath, synopsis, rating, releaseDate);
            movieNames.add(movieItem);
        }
        return movieNames.toArray(new Movie[]{});
    }
}
