package com.example.twanist.project_movie;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private MyAdapter adapter;
    private static final String POPULER = "https://api.themoviedb.org/3/movie/popular?api_key=1159baf6180a418d3a19639f6471dc51";
    private static final String RELEASE_TOP = "https://api.themoviedb.org/3/movie/top_rated?api_key=1159baf6180a418d3a19639f6471dc51";
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle("Movies");
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new MyAdapter(null, MainActivity.this);
        recyclerView.setAdapter(adapter);
        new MyTask().execute(POPULER);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new MyTask().execute(POPULER);
    }

    public class MyTask extends AsyncTask<String, Void, String> {
        String urls;

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder sb = new StringBuilder();

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;


        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                try {

                    ArrayList<MovieData> arrayList = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(s);

                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        String post = jo.getString("poster_path");
                        String title = jo.getString("title");
                        String release_date = jo.getString("release_date");
                        String overview = jo.getString("overview");
                        String backdrop_path = jo.getString("backdrop_path");
                        int id = jo.getInt("id");
                        double vote_average = jo.getDouble("vote_average");
                        MovieData movieData = new MovieData(post, backdrop_path, title, release_date, vote_average, overview, id);

                        arrayList.add(movieData);

                    }

                    adapter.swapAdapter(arrayList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(s);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.populer) {
            new MyTask().execute(POPULER);
            toolbar.setTitle("Popular");
        } else if (id == R.id.release) {
            new MyTask().execute(RELEASE_TOP);
            toolbar.setTitle("Top Rated");

        } else if (id == R.id.favorate) {

              /*fetch data in database using content provider */
            Cursor cursor = getContentResolver().query(MoviesContentProvider.CONTENT_URI,
                    null, null, null, null);
            List<MovieData> list = new ArrayList<>();
            if(cursor !=null) {
                while (cursor.moveToNext()) {
                    String backdrop_path = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesContractEntry.BACKDROP_PATH));
                    String title = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesContractEntry.TITLE));
                    String poster_path = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesContractEntry.POSTER_PATH));
                    double vote_ava = cursor.getDouble(cursor.getColumnIndex(MoviesContract.MoviesContractEntry.VOTE_AVERAGE));
                    String release_date = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesContractEntry.RELEASE_DATE));
                    String overviews = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesContractEntry.OVERVIEW));
                    MovieData movieData = new MovieData(poster_path, poster_path, title, release_date, vote_ava, overviews, 0);
                    list.add(movieData);

                }
            }
            toolbar.setTitle("Favorite");
            adapter.swapAdapter(list);


        }

        return true;
    }
}