package movies.anandsingh.net.movies;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
  import com.github.ivbaranov.mfb.MaterialFavoriteButton;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by delaroy on 5/18/17.
 */
public class DetailActivity extends AppCompatActivity {
    TextView nameOfMovie, overviewT, releaseDate,genreT;
    ImageView imageView;
    private Context mContext = DetailActivity.this;
    ArrayList<Integer>favList=new ArrayList<Integer>();
    private RecyclerView recyclerView,recyclerView2;
     private MovieDetails favorite;
    ImageView backArrow  ;

    MovieDetails movie;
    String thumbnail, movieName, overview, rating, dateOfRelease,sessionID,userID;
    int movie_id;
    JSONArray GenresRetrieved;
    ArrayList<CastInfo>castInfoArrayList=new ArrayList<CastInfo>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initCollapsingToolbar();
        backArrow = (ImageView) findViewById(R.id.ivBackArrow2);
        imageView = (ImageView) findViewById(R.id.image_movie_detail_poster);
        nameOfMovie = (TextView) findViewById(R.id.text_movie_original_title);
        overviewT = (TextView) findViewById(R.id.text_movie_overview);
         releaseDate = (TextView) findViewById(R.id.text_movie_release_date);

        genreT=(TextView) findViewById(R.id.genre_text);
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("movies")){
            movie = (MovieDetails) intentThatStartedThisActivity.getSerializableExtra("movies");
            sessionID=intentThatStartedThisActivity.getStringExtra("sessionID");
            userID=intentThatStartedThisActivity.getStringExtra("uID");

            thumbnail = movie.getPoster_path();
            movieName = movie.getOriginal_title();
            overview= movie.getOverview();
            dateOfRelease = movie.getRelease_date();
            GenresRetrieved=movie.getGenres();
            movie_id=movie.getID();

            recyclerView = (RecyclerView) findViewById(R.id.cast);
            recyclerView2 = (RecyclerView) findViewById(R.id.SimilarM  );
            new GetSimilarMovies().execute("https://api.themoviedb.org/3/movie/"+movie_id+"/similar?api_key=2c39d91079d98b3f6a66bae9c030f22d&language=en-US&page=1");
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                recyclerView2.setLayoutManager(new GridLayoutManager(this, 2));

            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
                recyclerView2.setLayoutManager(new GridLayoutManager(this, 4));

            }

         new GetGenres().execute("https://api.themoviedb.org/3/genre/movie/list?api_key=2c39d91079d98b3f6a66bae9c030f22d&language=en-US");
            new getFavMovies().execute("https://api.themoviedb.org/3/account/"+userID+"/favorite/movies?api_key=2c39d91079d98b3f6a66bae9c030f22d&language=en-US&sort_by=created_at.asc&page=1&session_id="+sessionID);



            String poster = "https://image.tmdb.org/t/p/w500" + thumbnail;

            Glide.with(this)
                    .load(poster)
                    .placeholder(R.drawable.load)
                    .into(imageView);

            nameOfMovie.setText(movieName);
            overviewT.setText(overview);
             releaseDate.setText(dateOfRelease);

        }else{
            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show();
        }

        MaterialFavoriteButton materialFavoriteButtonNice =
                (MaterialFavoriteButton) findViewById(R.id.favorite_button);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        materialFavoriteButtonNice.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener(){
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite){
                        if (favorite){
                            Log.d("clicked","cc");
                            SharedPreferences.Editor editor = getSharedPreferences("movies.anandsingh.net.movies.DetailActivity", MODE_PRIVATE).edit();
                            editor.putBoolean("Favorite Added", true);
                            editor.commit();

                                addFavMovie(""+movie_id,sessionID);

                            Snackbar.make(buttonView, "Added to Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        }else{
                            removeFavMovie(""+movie_id,sessionID);



                            SharedPreferences.Editor editor = getSharedPreferences("movies.anandsingh.net.movies.DetailActivity", MODE_PRIVATE).edit();
                            editor.putBoolean("Favorite Removed", true);
                            editor.commit();
                            Snackbar.make(buttonView, "Removed from Favorite",
                                   Snackbar.LENGTH_SHORT).show();
                        }

                    }
                }
        );

       // initViews();

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



     private void addFavMovie(String mediaID,String sessionID) {

        final String temptMediaId=mediaID;
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest sr=new StringRequest(Request.Method.POST, "https://api.themoviedb.org/3/account/"+userID+"/favorite?api_key=2c39d91079d98b3f6a66bae9c030f22d&session_id="+sessionID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response inside fav",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);


                    if(jsonObject.getInt("success")==1)
                    {
                        //addFav.setImageResource(R.drawable.star_green);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }
        })

        {

             protected Map<String,String> getParams(){
                Map<String, String> param = new HashMap<String, String>();

                param.put("media_type", "movie");
                param.put("media_id",temptMediaId);
                param.put("favorite","true");




                return param;
            }


        };
        queue.add(sr);
    }
    private void removeFavMovie(String mediaID,String sessionID) {

        final String temptMediaId=mediaID;
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest sr=new StringRequest(Request.Method.POST, "https://api.themoviedb.org/3/account/"+userID+"/favorite?api_key=2c39d91079d98b3f6a66bae9c030f22d&session_id="+sessionID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response inside fav",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);


                    if(jsonObject.getInt("success")==1)
                    {
                        //addFav.setImageResource(R.drawable.star_green);

                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }
        })

        {
             public Map<String, String> getHeaders()  {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("content-type", "application/json;charset=utf-8");
                return headers;
            }
             protected Map<String,String> getParams(){
                Map<String, String> param = new HashMap<String, String>();

                param.put("media_type", "movie");
                param.put("media_id",temptMediaId);
                param.put("favorite","false");




                return param;
            }

        };
        queue.add(sr);
    }






    class GetGenres extends AsyncTask<String, Void, String> {
        //This method will run on UIThread and it will execute before doInBackground
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        //This method will run on background thread and after completion it will return result to onPostExecute
        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
//As we are passing just one parameter to AsyncTask, so used param[0] to get value at 0th position that is URL
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//Getting inputstream from connection, that is response which we got from server
                InputStream inputStream = urlConnection.getInputStream();
//Reading the response
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String s = bufferedReader.readLine();
                bufferedReader.close();
//Returning the response message to onPostExecute method
                return s;
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage(), e);
            }
            return null;
        }
        //This method runs on UIThread and it will execute when doInBackground is completed
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject = null;
            String genresResults="";
            try {

//Parent JSON Object. Json object start at { and end at }
                jsonObject = new JSONObject(s);


                 JSONArray jsonArray = jsonObject.getJSONArray("genres");

//Reading JSON object inside Json array
                for (int i =0; i<GenresRetrieved.length();i++)
                {
                    for (int j =0; j<jsonArray.length();j++){
                        if(GenresRetrieved.getInt(i)==jsonArray.getJSONObject(j).getInt("id")){
                            genresResults+=jsonArray.getJSONObject(j).getString("name")+"/";
                        }

                    }


                }
                genreT.setText(genresResults);
                new GetCastInfo1().execute("https://api.themoviedb.org/3/movie/"+movie_id+"?api_key=2c39d91079d98b3f6a66bae9c030f22d&append_to_response=credits");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class GetCastInfo1 extends AsyncTask<String, Void, String> {
         @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("result0","jjj");

        }
         @Override
        protected String doInBackground(String... params) {
            URL url = null;
            Log.d("result1","jvvvvv");

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
            Log.d("result2","nnnn");

            try {

                 jsonObject = new JSONObject(s);
                JSONObject Credit=jsonObject.getJSONObject("credits");


                JSONArray jsonArray = Credit.getJSONArray("cast");

                 for (int i =0; i<jsonArray.length();i++)
                {
                    CastInfo tempCastInfo=new CastInfo();
                    tempCastInfo.setcName(jsonArray.getJSONObject(i).getString("name"));
                    tempCastInfo.setcPoster(jsonArray.getJSONObject(i).getString("profile_path"));
                    tempCastInfo.setcID(jsonArray.getJSONObject(i).getInt("id"));

                    Log.d("result11","here"+tempCastInfo);
                    castInfoArrayList.add(tempCastInfo);
                }

                CastAdapter movieArrayAdapter = new CastAdapter(DetailActivity.this,castInfoArrayList,sessionID,userID);

                 recyclerView.setAdapter(movieArrayAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    class getFavMovies extends AsyncTask<String, Void, String> {
        //This method will run on UIThread and it will execute before doInBackground
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("result0","jjj");

        }
        //This method will run on background thread and after completion it will return result to onPostExecute
        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            Log.d("result1","jvvvvv");

            try {
//As we are passing just one parameter to AsyncTask, so used param[0] to get value at 0th position that is URL
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//Getting inputstream from connection, that is response which we got from server
                InputStream inputStream = urlConnection.getInputStream();
//Reading the response
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String s = bufferedReader.readLine();
                bufferedReader.close();
//Returning the response message to onPostExecute method
                return s;
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage(), e);
            }
            return null;
        }
        //This method runs on UIThread and it will execute when doInBackground is completed
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject = null;
            Log.d("result2","nnnn");

            try {

//Parent JSON Object. Json object start at { and end at }
                jsonObject = new JSONObject(s);
                JSONArray favArray=jsonObject.getJSONArray("results");





//Reading JSON object inside Json array
                for (int i =0; i<favArray.length();i++)
                {
                    Log.d("inside","inside111");
                    favList.add(favArray.getJSONObject(i).getInt("id"));
                }
                MaterialFavoriteButton materialFavoriteButtonNice =
                        (MaterialFavoriteButton) findViewById(R.id.favorite_button);

                for (int j =0; j<favList.size();j++){
                   if(movie_id==favList.get(j)){
                       Log.d("inside","inside22");

                       SharedPreferences.Editor editor = getSharedPreferences("movies.anandsingh.net.movies.DetailActivity", MODE_PRIVATE).edit();
                       editor.putBoolean("Favorite Added", true);
                       editor.commit();
                       return;
                       //saveFavorite();
                       // new GetUID().execute("https://api.themoviedb.org/3/account?api_key=2c39d91079d98b3f6a66bae9c030f22d&session_id="+sessionID);
                       //boolean idExists= getAccountID(sessionID);



                   }

                }




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class GetSimilarMovies extends AsyncTask<String, Void, String> {
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

                 JSONArray jsonArray = jsonObject1.getJSONArray("results");

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
                    Log.d("moviwee","here111"+movieDetails.getPoster_path());
                    movieList.add(movieDetails);

                }

                SimilarAdapter movieArrayAdapter = new SimilarAdapter(DetailActivity.this,movieList,sessionID,userID);
                recyclerView2.setAdapter(movieArrayAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }




}
