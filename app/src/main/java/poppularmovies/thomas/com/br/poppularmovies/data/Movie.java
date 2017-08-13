package poppularmovies.thomas.com.br.poppularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Thomas on 29/07/2017.
 */

public class Movie implements Parcelable {
    private int ID;
    private String title;
    private String poster;
    private String synopsis;
    private float rating;
    private String release_date;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster(PosterSize size) {
        String sURL = "http://image.tmdb.org/t/p/"+size+"/";
        return sURL + poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public enum PosterSize {
        w185,
        w500
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ID);
        dest.writeString(this.title);
        dest.writeString(this.poster);
        dest.writeString(this.synopsis);
        dest.writeFloat(this.rating);
        dest.writeString(this.release_date);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.ID = in.readInt();
        this.title = in.readString();
        this.poster = in.readString();
        this.synopsis = in.readString();
        this.rating = in.readFloat();
        this.release_date = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
