package movies.anandsingh.net.movies;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import java.util.Locale;

/**
 * Created by razan on 2/2/18.
 */

public class SearchActivity extends AppCompatActivity   {

    private EditText mSearchParam;

    private String text,sessionID,uID ;
    final android.os.Handler handler = new android.os.Handler();
    private Runnable runnable;
    private ArrayList<MovieDetails> mUserList;
    private ListView mListView;
    private Context mContext= SearchActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSearchParam = (EditText) findViewById(R.id.search);
        mUserList=new ArrayList<MovieDetails>();
        Intent intent = getIntent();
        sessionID=intent.getStringExtra("sessionID");
        uID=intent.getStringExtra("uID");
        mListView = (ListView) findViewById(R.id.listView);


        initTextListener();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Moving to MovieDetailsActivity from MainActivity. Sending the MovieDetails object from one activity to another activity
            MovieDetails clickedDataItem = mUserList.get(position);
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra("movies", clickedDataItem );
            intent.putExtra("sessionID", sessionID );
            intent.putExtra("uID", uID );
            startActivity(intent);


        }});


    }



    private void initTextListener(){
        mSearchParam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(runnable);

            }

            @Override
            public void afterTextChanged(Editable s) {

                text = mSearchParam.getText().toString().toLowerCase(Locale.getDefault());
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        searchForMatch(text); //do some work with s.toString()
                    }
                };
                handler.postDelayed(runnable, 500);
            }
        });
    }

    private void searchForMatch(String keyword){
        mUserList.clear();
        String[] selectionArgs = new String[1];
        selectionArgs[0] = keyword;
         if(keyword.length() >0){
             new searchMovie().execute("https://api.themoviedb.org/3/search/movie?api_key=2c39d91079d98b3f6a66bae9c030f22d&language=en-US&page=1&include_adult=false&query="+keyword);






        }
    }





    class searchMovie extends AsyncTask<String, Void, String>  {
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
            try {

//Parent JSON Object. Json object start at { and end at }
                jsonObject = new JSONObject(s);

                ArrayList<MovieDetails> movieList = new ArrayList<>();

//JSON Array of parent JSON object. Json array starts from [ and end at ]
                JSONArray jsonArray = jsonObject.getJSONArray("results");

//Reading JSON object inside Json array
                for (int i =0; i<jsonArray.length();i++)
                {

//Reading JSON object at 'i'th position of JSON Array
                    JSONObject object = jsonArray.getJSONObject(i);
                    MovieDetails movieDetails = new MovieDetails();
                    movieDetails.setGenres(object.getJSONArray("genre_ids"));
                    movieDetails.setOriginal_title(object.getString("original_title"));
                    movieDetails.setVote_average(object.getDouble("vote_average"));
                    movieDetails.setOverview(object.getString("overview"));
                    movieDetails.setRelease_date(object.getString("release_date"));
                    movieDetails.setPoster_path(object.getString("poster_path"));
                    movieDetails.setID(object.getInt("id"));

                    mUserList.add(movieDetails);

                }

                 MovieArrayAdapter mAdapter = new MovieArrayAdapter(SearchActivity.this,R.layout.movie_list, mUserList);


                mListView.setAdapter(mAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }

}
