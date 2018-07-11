package movies.anandsingh.net.movies;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Fadwasa on 10/07/2018 AD.
 */

public class CastDetails extends AppCompatActivity{
    TextView nameOfCast, biography, birthDate,otherNames;
    ImageView imageView;
    String poster,sessionID,userID;
    RecyclerView recyclerView2 ;
    int castID;
    private Context mContext = CastDetails.this;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cast_detail);
        recyclerView2 = (RecyclerView) findViewById(R.id.cast);
        imageView = (ImageView) findViewById(R.id.image_movie_detail_poster);
        nameOfCast = (TextView) findViewById(R.id.text_movie_original_title);
        biography = (TextView) findViewById(R.id.text_movie_overview);
        birthDate = (TextView) findViewById(R.id.text_movie_release_date);
        otherNames=(TextView) findViewById(R.id.genre_text);
        CastInfo castInfo;
        Intent intent = getIntent();
        if (intent.hasExtra("cast")){
            castInfo = (CastInfo) intent.getSerializableExtra("cast");
            castID=castInfo.getcID();
        }
        sessionID=intent.getStringExtra("sessionid");
        userID=intent.getStringExtra("uid");

        new getCastDetails().execute("https://api.themoviedb.org/3/person/"+castID+"?api_key=2c39d91079d98b3f6a66bae9c030f22d&language=en-US");
        new getMoviesActed().execute("https://api.themoviedb.org/3/person/"+castID+"/movie_credits?api_key=2c39d91079d98b3f6a66bae9c030f22d&language=en-US");
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
             recyclerView2.setLayoutManager(new GridLayoutManager(this, 2));

        } else {
             recyclerView2.setLayoutManager(new GridLayoutManager(this, 4));

        }


    }
    public Activity getActivity(){
        Context context = this;
        while (context instanceof ContextWrapper){
            if (context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;

    }

    class getCastDetails extends AsyncTask<String, Void, String> {
         @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
         @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
                 url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                 InputStream inputStream = urlConnection.getInputStream();
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String s = bufferedReader.readLine();
                bufferedReader.close();
                 return s;
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage(), e);
            }
            return null;
        }
         @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject = null;
            try {

                 jsonObject = new JSONObject(s);
                poster = "https://image.tmdb.org/t/p/w500" +jsonObject.getString("profile_path");
                if(jsonObject.getString("name")!=null)
                nameOfCast.setText(jsonObject.getString("name"));
                else
                    nameOfCast.setText(jsonObject.getString("Name not found"));
                if(jsonObject.getString("biography")!=null)
                biography.setText(jsonObject.getString("biography"));
                else
                    biography.setText(jsonObject.getString("No biography found"));
                if(jsonObject.getString("birthday")!=null)

                birthDate.setText(jsonObject.getString("birthday"));
                else
                    birthDate.setText(jsonObject.getString("No birth date found"));

                if(jsonObject.getString("also_known_as")!=null)

                    otherNames.setText(jsonObject.getString("also_known_as"));
                else
                    otherNames.setText(jsonObject.getString(""));
                Glide.with(mContext)
                        .load(poster)
                        .placeholder(R.drawable.load)
                        .into(imageView);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    class getMoviesActed extends AsyncTask<String, Void, String> {
         @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("doInBackground","onPreExecute");

        }
         @Override
        protected String doInBackground(String... params) {
            URL url = null;
            Log.d("doInBackground","doInBackground");

            try {
                 url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                 InputStream inputStream = urlConnection.getInputStream();
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String s = bufferedReader.readLine();
                bufferedReader.close();
                 return s;
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage(), e);
            }
            return null;
        }
         @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject = null;
            try {
                Log.d("moviwee","here11");


                 JSONObject jsonObject1 = new JSONObject(s);

                ArrayList<MovieDetails> movieList = new ArrayList<>();

                 JSONArray jsonArray = jsonObject1.getJSONArray("cast");

                 for (int i =0; i<jsonArray.length();i++)
                {

                     JSONObject object = jsonArray.getJSONObject(i);
                    MovieDetails movieDetails = new MovieDetails();
                    movieDetails.setGenres(object.getJSONArray("genre_ids"));
                    movieDetails.setOriginal_title(object.getString("original_title"));
                    movieDetails.setVote_average(object.getDouble("vote_average"));
                    movieDetails.setOverview(object.getString("overview"));
                    movieDetails.setRelease_date(object.getString("release_date"));
                    movieDetails.setPoster_path(object.getString("poster_path"));
                    movieDetails.setID(object.getInt("id"));
                     movieList.add(movieDetails);

                }

                MoviesAdapter movieArrayAdapter = new MoviesAdapter(CastDetails.this,movieList,sessionID,userID);

                recyclerView2.setAdapter(movieArrayAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}
