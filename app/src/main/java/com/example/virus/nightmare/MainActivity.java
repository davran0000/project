package com.example.virus.nightmare;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.virus.nightmare.models.MovieModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private Button button;
    private ListView lvMovies ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvMovies = findViewById(R.id.lvMovies);




            }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();

        if(id == R.id.action_refresh){
            new JSONTask().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesData.txt");
        }
        return super.onOptionsItemSelected(item);
    }

    class JSONTask extends AsyncTask<String,String,List<MovieModel>>{

        @Override
        protected List<MovieModel> doInBackground(String... strings) {
            HttpURLConnection connection = null ;
            BufferedReader inputStream = null;
            List<MovieModel> movieModelList = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                inputStream = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = inputStream.readLine()) != null){
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray =parentObject.getJSONArray("movies");
                for (int i = 0; i <parentArray.length() ; i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    MovieModel movieModel = new MovieModel();
                    movieModel.setMovie(finalObject.getString("movie"));
                    movieModel.setYear(finalObject.getInt("year"));
                    movieModel.setRating(finalObject.getDouble("rating"));
                    movieModel.setDuration(finalObject.getString("duration"));
                    movieModel.setDirection(finalObject.getString("director"));
                    movieModel.setTagline(finalObject.getString("tagline"));
                    movieModel.setImage(finalObject.getString("image"));
                    movieModel.setStory(finalObject.getString("story"));
                    List<MovieModel.Cast> castList = new ArrayList<>();
                    for (int j = 0; j <finalObject.getJSONArray("cast").length() ; j++) {
                        MovieModel.Cast cast = new MovieModel.Cast();
                        cast.setName(finalObject.getJSONArray("cast").getJSONObject(j).getString("name"));
                        castList.add(cast);
                    }
                    movieModel.setCastList(castList);
                    movieModelList.add(movieModel);
                }
                return movieModelList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null)
                    connection.disconnect();
                try {
                    if(inputStream != null)
                        inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
    }

    @Override
        protected void onPostExecute(List<MovieModel> result) {
        super.onPostExecute(result);
        if(result != null) {
            MovieAdapter adapter = new MovieAdapter(getApplicationContext(), R.layout.row, result);
            lvMovies.setAdapter(adapter);
        }
    }
        }
    public class MovieAdapter extends ArrayAdapter<MovieModel>{
        private List<MovieModel> movieModelList ;
        int resource;
        private LayoutInflater inflater;


        public MovieAdapter( Context context, int resource, List<MovieModel> objects) {
            super(context, resource, objects);
            movieModelList =objects;
            this.resource = resource;
            inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position,  View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = inflater.inflate(resource, null);
            }
            ImageView movieIcon;
            TextView tvTitle;
            TextView tvTagline;
            TextView tvYear;
            TextView tvDuration;
            TextView tvDirection;
            RatingBar ratingBar;
            TextView tvCast;
            TextView tvStory;

            movieIcon = convertView.findViewById(R.id.movie_picture);
            tvTitle = convertView.findViewById(R.id.tv_title);
            tvTagline = convertView.findViewById(R.id.tv_tagline);
            tvYear = convertView.findViewById(R.id.tv_year);
            tvDuration = convertView.findViewById(R.id.tv_duration);
            tvDirection = convertView.findViewById(R.id.tv_direction);
            ratingBar = convertView.findViewById(R.id.rb_rating);
            tvCast = convertView.findViewById(R.id.tv_casts);
            tvStory =convertView.findViewById(R.id.tv_story);

            Picasso.get().load(movieModelList.get(position).getImage()).into(movieIcon);
            tvTitle.setText(movieModelList.get(position).getMovie());
            tvTagline.setText(movieModelList.get(position).getTagline());
            tvYear.setText("Year : "+movieModelList.get(position).getYear());
            tvDuration.setText("Duration: "+movieModelList.get(position).getDuration());
            tvDirection.setText("Direct: "+movieModelList.get(position).getDirection());
            StringBuffer sb = new StringBuffer();
            for (MovieModel.Cast cast:movieModelList.get(position).getCastList())
                sb.append(cast.getName()+", ");
            ratingBar.setRating((float)movieModelList.get(position).getRating()/2);
            tvCast.setText("Cast "+sb);
            tvStory.setText(movieModelList.get(position).getStory());

            return convertView;
        }
    }

}


