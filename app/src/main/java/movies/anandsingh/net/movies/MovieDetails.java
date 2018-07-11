package movies.anandsingh.net.movies;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by anandsingh on 28/12/16.
 */

public class MovieDetails  implements Serializable{

    private String original_title;
    
    private String overview;

    private String release_date;
    private int id;

    private double vote_average;
    private   String genres;
    private boolean liked;

    public int getID() {
        return id;
    }
    public void setID(int id) {
        this.id = id;
    }
    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    private String poster_path;

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }
    public   JSONArray getGenres() {
        try {
            return new JSONArray(genres);


        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
    public    void setGenres(JSONArray genres1) {
         genres = genres1.toString();
    }
    public   boolean getLiked() {

        return liked;
    }
    public  void setLiked(boolean liked1) {
        liked=liked1;
    }




}
