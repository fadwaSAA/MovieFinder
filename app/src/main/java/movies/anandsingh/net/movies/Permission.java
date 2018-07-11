package movies.anandsingh.net.movies;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Locale;

/**
 * Created by Fadwasa on 08/07/2018 AD.
 */

public class Permission extends AppCompatActivity {
    private static final String ARG_PARAM1 = "param1",ARG_PARAM2 = "param2";
    WebView webView;
    ImageView backArrow;
    private Context mContext = Permission.this;
    String sessionID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_web);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        backArrow = (ImageView) findViewById(R.id.ivBackArrow2);

        new  GetToken().execute("https://api.themoviedb.org/3/authentication/token/new?api_key=2c39d91079d98b3f6a66bae9c030f22d");

    }

    class GetToken extends AsyncTask<String, Void, String> {
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

            try {

                 jsonObject = new JSONObject(s);
                String token1=jsonObject.getString("request_token");
                Log.e("Error: ", "testinn1");
                 getPermission("https://www.themoviedb.org/authenticate/",token1);




             } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getPermission(String url,String token ){
        final String tempToken=token;
        webView.loadUrl(url+token);
        Log.e("Error: ", "onCreate");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetSessionId().execute("https://api.themoviedb.org/3/authentication/session/new?api_key=2c39d91079d98b3f6a66bae9c030f22d&request_token="+tempToken);

                //startActivity(new Intent(getActivity(), DetailActivity.class));


            }
        });


    }


    class GetSessionId extends AsyncTask<String, Void, String> {
         @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("result0","jjj");

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
            try {
                JSONObject jsonObject = new JSONObject(s);
                sessionID=jsonObject.getString("session_id");
                new GetUID().execute("https://api.themoviedb.org/3/account?api_key=2c39d91079d98b3f6a66bae9c030f22d&session_id="+sessionID);

            }
            catch (JSONException e){
                e.printStackTrace();
            }



        }
    }
    class GetUID extends AsyncTask<String, Void, String> {
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

            try {

                 jsonObject = new JSONObject(s);
                int uID=jsonObject.getInt("id");
                 Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("sessionID",sessionID);
                intent.putExtra("uID",""+uID);


                mContext.startActivity(intent);




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }






}
