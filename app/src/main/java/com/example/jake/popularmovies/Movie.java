package com.example.jake.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jake on 3/15/16.
 */
public class Movie implements Parcelable {

    // Movie details
    private int id;
    private String title;
    private String urlPath;
    private String synopsis;
    private double rating;
    private String releaseDate;

    public Movie(int pId, String pTitle, String pUrl, String pSynopsis, double pRating, String pReleaseDate)
    {
        id = pId;
        title = pTitle;
        urlPath = pUrl;
        synopsis = pSynopsis;
        rating = pRating;
        releaseDate = pReleaseDate;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getUrl() {
        return urlPath;
    }
    public String getSynopsis() { return synopsis; }
    public double getRating() { return rating; }
    public String getReleaseDate() { return releaseDate; }

    //
    // Stuff for the parcelable interface
    //

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>(){
        public Movie createFromParcel(Parcel in)
        {
            return new Movie(in);
        }

        public Movie[] newArray(int size)
        {
            return new Movie[size];
        }
    };

    public int describeContents()
    {
        // No children that use parcelable, so set to 0
        return 0;
    }

    public void writeToParcel(Parcel out, int flags)
    {
        out.writeInt(id);
        out.writeDouble(rating);
        out.writeString(title);
        out.writeString(urlPath);
        out.writeString(synopsis);
        out.writeString(releaseDate);
    }

    private Movie(Parcel in)
    {
        id = in.readInt();
        rating = in.readDouble();
        title = in.readString();
        urlPath = in.readString();
        synopsis = in.readString();
        releaseDate = in.readString();
    }
}
