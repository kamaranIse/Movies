package com.example.twanist.project_movie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private ImageView backdrop_paths, poster_path, starImage, play;
    private TextView release_date, vote_ava, overview, author, content, type;
    private int Id;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lm;
    private RecyclerView getRecyclerView;
    private FloatingActionButton fab;
    private Intent intent;
    private boolean isSaved;
    private String t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(intent.getStringExtra("title"));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.play_view);
        getRecyclerView = (RecyclerView) findViewById(R.id.recycler_review);
        fab = (FloatingActionButton) findViewById(R.id.fab_app);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp, getBaseContext().getTheme()));
        lm = new LinearLayoutManager(this);
        getRecyclerView.setLayoutManager(lm);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        Id = intent.getIntExtra("id", 0);

        /********************************************/
        backdrop_paths = (ImageView) findViewById(R.id.backdrop_path);
        poster_path = (ImageView) findViewById(R.id.poster_path);
        starImage = (ImageView) findViewById(R.id.star);
        release_date = (TextView) findViewById(R.id.release_date);
        vote_ava = (TextView) findViewById(R.id.vote_average);
        overview = (TextView) findViewById(R.id.overvies_m);
        /******get data in intent put to string values******/

        String backdrop_path = intent.getStringExtra("Backdrop_path");
        String img = intent.getStringExtra("image");
        String release_d = intent.getStringExtra("release_date");
        double vote_a = intent.getDoubleExtra("vote_ava", 0.0);
        String overviews = intent.getStringExtra("overview");
        Picasso.with(this).load("https://image.tmdb.org/t/p/w640" + img).into(poster_path);
        Picasso.with(this).load("https://image.tmdb.org/t/p/original" + backdrop_path).into(backdrop_paths);
        release_date.setText(release_d);
        vote_ava.setText(String.valueOf(vote_a));
        overview.setText(overviews);


        final String reviews = "https://api.themoviedb.org/3/movie/" + Id + "/reviews?api_key=1159baf6180a418d3a19639f6471dc51";
        final String video = "https://api.themoviedb.org/3/movie/" + Id + "/videos?api_key=1159baf6180a418d3a19639f6471dc51";

        Cursor cursor = getContentResolver().query(MoviesContentProvider.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            t = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesContractEntry.TITLE));
            if (t.equals(intent.getStringExtra("title"))) {
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp, getBaseContext().getTheme()));
                isSaved = true;

            }
        }

        new TaskReviews().execute(reviews);
        new TaskVideo().execute(video);

    }


    public void addDataToContentProvider(View view) {
        if (isSaved == false) {

            ContentValues mc = new ContentValues();
            String backdrop_path = intent.getStringExtra("Backdrop_path");
            String img = intent.getStringExtra("image");
            String titles = intent.getStringExtra("title");
            String release_d = intent.getStringExtra("release_date");
            double vote_a = intent.getDoubleExtra("vote_ava", 0.0);
            String overviews = intent.getStringExtra("overview");
            mc.put(MoviesContract.MoviesContractEntry.POSTER_PATH, img);
            mc.put(MoviesContract.MoviesContractEntry.BACKDROP_PATH, "https://image.tmdb.org/t/p/original" + backdrop_path);
            mc.put(MoviesContract.MoviesContractEntry.TITLE, titles);
            mc.put(MoviesContract.MoviesContractEntry.RELEASE_DATE, release_d);
            mc.put(MoviesContract.MoviesContractEntry.VOTE_AVERAGE, String.valueOf(vote_a));
            mc.put(MoviesContract.MoviesContractEntry.OVERVIEW, overviews);
            Uri uri = getContentResolver().insert(MoviesContentProvider.CONTENT_URI, mc);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp, getBaseContext().getTheme()));
            isSaved = true;
            Toast.makeText(this, "favorate", Toast.LENGTH_LONG).show();
        } else {
            deteleFavorate();
        }


    }

    public void deteleFavorate() {

        if (isSaved == true) {
            Cursor cursor = getContentResolver().query(MoviesContentProvider.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String ts = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesContractEntry.TITLE));
                if (ts.equals(intent.getStringExtra("title"))) {
                    getContentResolver().delete(MoviesContentProvider.CONTENT_URI,
                            MoviesContract.MoviesContractEntry.TITLE + "=?", new String[]{ts});
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp, getBaseContext().getTheme()));
                    Toast.makeText(this, "un_favorate", Toast.LENGTH_LONG).show();
                    isSaved = false;

                }

            }

        }

    }


    private class TaskReviews extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
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
                List<ReviewMovies> list = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        int c = 1 + i;
                        JSONObject jo = jsonArray.getJSONObject(i);
                        ReviewMovies reviewMovies = new ReviewMovies(jo.getString("author"), jo.getString("content"));
                        list.add(reviewMovies);

                    }
                    adapter = new ReviewAdapter(list);
                    getRecyclerView.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


            super.onPostExecute(s);
        }
    }

    private class TaskVideo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
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
                List<PlayVideo> playVideoList = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        PlayVideo playVideo = new PlayVideo(jo.getString("name"), jo.getString("type"));
                        playVideoList.add(playVideo);

                    }
                    adapter = new VideoPlayAdapter(playVideoList, DetailsActivity.this);
                    recyclerView.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


            super.onPostExecute(s);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

}


