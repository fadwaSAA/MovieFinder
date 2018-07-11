package movies.anandsingh.net.movies;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

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
public class MainActivity extends AppCompatActivity  {

    private RecyclerView recyclerView;
    String sessionID,uID;
    ImageView searchIcon;
    Context mContext = MainActivity.this;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
          sessionID=intent.getStringExtra("sessionID");
          uID=intent.getStringExtra("uID");


        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

//Executing AsyncTask, passing api as parameter
        new CheckConnectionStatus().execute("https://api.themoviedb.org/3/movie/now_playing?api_key=2c39d91079d98b3f6a66bae9c030f22d&language=en-US&page=1");

        searchIcon=(ImageView)findViewById(R.id.search);
        searchIcon.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                     Intent intent = new Intent(mContext, SearchActivity.class);
                    intent.putExtra("sessionID",sessionID);
                    intent.putExtra("uID",uID);

                    mContext.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



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

     class CheckConnectionStatus extends AsyncTask<String, Void, String> {
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

                ArrayList<MovieDetails> movieList = new ArrayList<>();

                 JSONArray jsonArray = jsonObject.getJSONArray("results");

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

                 MoviesAdapter movieArrayAdapter = new MoviesAdapter(MainActivity.this,movieList,sessionID,uID);

                //Setting adapter to recyclerView

                recyclerView.setAdapter(movieArrayAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}