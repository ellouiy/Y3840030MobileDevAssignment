package com.example.Y3840030.Y3840030mobiledevassignment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShowNews extends AppCompatActivity {

    private String API_KEY = "820dc531-f4bc-4ae6-8a66-e1e8013b47d0";
    public String location;
    static int count = 0;
    public String url_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            location = extras.getString("key");
        }

        count = 0;

    }

    public void goBack(View view) {
        //This method takes the user back to the first screen of the app.
        Intent i = new Intent(this, com.example.Y3840030.Y3840030mobiledevassignment.MainActivity.class);
        startActivity(i);
    }

    public void getNews(View view) {

        String splitLocation = location.replace(" ", "_");

        System.out.println("I GOT HERE " + splitLocation);

        TextView txt = findViewById(R.id.infoText);
        Button btn = findViewById(R.id.urlText);

        btn.setVisibility(View.VISIBLE);
        txt.setVisibility(View.INVISIBLE);

        new ShowNews.downloadNews().execute("https://eventregistry.org/api/v1/article/getArticles?query=" +
                "%7B%22%24query%22%3A%7B%22conceptUri%22%3A%22http%3A%2F%2Fen.wikipedia.org%2Fwiki%2F" +
                splitLocation +
                "%22%7D%7D&dataType=news&resultType=articles&articlesSortBy=date&articlesCount=100" +
                "&articleBodyLen=-1&" +
                "apiKey=" +
                API_KEY);
    }

    public void setNews(News new_news) {
        TextView body = findViewById(R.id.textView2);
        TextView title = findViewById(R.id.textView7);
        TextView date = findViewById(R.id.textView9);
        Button url = findViewById(R.id.urlText);


        if (new_news != null) {

            if (count > new_news.getArticles().getResults().size() - 1) {
                title.setText("whoa there no more news");
                body.setText("lol bye");

                count = 0;
            }
            else {
                title.setText(new_news.getArticles().getResults().get(ShowNews.count).getTitle());
                body.setText(new_news.getArticles().getResults().get(ShowNews.count).getBody());
                date.setText("Article Date : " + new_news.getArticles().getResults().get(ShowNews.count).getDate());

                url.setVisibility(View.VISIBLE);

                url_ = new_news.getArticles().getResults().get(ShowNews.count).getUrl();
            }

        } else {
            title.setText("YIKES");
            date.setText("No date allowed");
            body.setText("No news found :(");
        }

    }


    public void gotoWebsite(View view) {
        //https://programming.guide/java/open-url-in-androids-web-browser.html

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url_)));
    }

    private class downloadNews extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            URL url;

            try {
                url = new URL(urls[0]);
            } catch (MalformedURLException e) {
                return "";
            }

            StringBuilder sb = new StringBuilder();

            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = bf.readLine()) != null) {
                    sb.append(line + "\n");
                }
                bf.close();
                connection.getInputStream().close();

                return (sb.toString());
            } catch (IOException e) {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            News news = null;
            try {
                news = mapper.readValue(result, News.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Button fill = findViewById(R.id.button5);
            fill.setVisibility(View.INVISIBLE);

            TextView summary = findViewById(R.id.textView2);
            TextView title = findViewById(R.id.textView7);
            TextView date = findViewById(R.id.textView9);
            TextView url = findViewById(R.id.urlText);

            summary.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            date.setVisibility(View.VISIBLE);
            url.setVisibility(View.VISIBLE);

            setNews(news);

            count++;

        }
    }


}
