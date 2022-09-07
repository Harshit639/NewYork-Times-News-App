package com.example.nytimesnewsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViews;
    adaptter asd;
    ArrayList<liststructure> item;
    ArrayList<String> messages;

    SQLiteDatabase articleDB;
    LinearLayoutManager layoutManagera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getSupportActionBar().hide();

        item = new ArrayList<liststructure>();
        articleDB = this.openOrCreateDatabase("ARTICLES",MODE_PRIVATE,null);
        articleDB.execSQL("CREATE TABLE IF NOT EXISTS Articles (id INTEGER PRIMARY KEY , title VARCHAR , byline VARCHAR(500), date VARCHAR(500) ,url VARCHAR(3000),image VARCHAR(500))");

        DownloadTask task = new DownloadTask(); // calling the async task
        task.execute("https://api.nytimes.com/svc/mostpopular/v2/viewed/1.json?api-key=ARoGFAJJ6HlZgTAaUoYbEtS7ZxNrs7qn");
        recyclerViews = findViewById(R.id.recycler);
        layoutManagera = new LinearLayoutManager(this);
        layoutManagera.setOrientation(RecyclerView.VERTICAL);
        recyclerViews.setLayoutManager(layoutManagera);
        asd = new adaptter(item);
        recyclerViews.setAdapter(asd);
        asd.notifyDataSetChanged();

        updatelistView();


        Log.i("info" , String.valueOf(item));
    }

    // updating the database

    public void updatelistView(){
        Cursor c = articleDB.rawQuery("SELECT * FROM ARTICLES",null);
        int title= c.getColumnIndex("title");
        int byline = c.getColumnIndex("byline");
        int date = c.getColumnIndex("date");
        int url = c.getColumnIndex("url");
        int image = c.getColumnIndex("image");
        if(c.moveToFirst()){
            item.clear();

            do{
                item.add(new liststructure(c.getString(title),c.getString(byline),c.getString(date),c.getString(url),c.getString(image)));
            }while(c.moveToNext());

            asd.notifyDataSetChanged();


        }
    }

     // getting the data using the api and storing in a sql database

        public class DownloadTask  extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... urls) {
                URL url;
                HttpURLConnection connection;
                String result = "";
                try {
                    url = new URL(urls[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    InputStream in = connection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    int data = reader.read();
                    while (data != -1) {
                        char current = (char) data;
                        result += current;
                        data = reader.read();
                    }
                    return result;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String results = jsonObject.getString("results");
                    Log.i("results", results);
                    String link="";
                    String imageurl="";
                    articleDB.execSQL("DELETE FROM Articles");

                    JSONArray arr = new JSONArray(results);
                    for (int i=0; i < 20; i++) {
                        JSONObject jsonPart = arr.getJSONObject(i);
                        String title = jsonPart.getString("title");
                        String byline = jsonPart.getString("byline");
                        link = jsonPart.getString("url");
                        String date = jsonPart.getString("published_date");
                        String media =jsonPart.getString("media");
                        JSONArray mediadata = new JSONArray(media);
                        JSONObject kite = mediadata.getJSONObject(0);
                        String mediametadata = kite.getString("media-metadata");
                        JSONArray lastarray = new JSONArray(mediametadata);
                        JSONObject kvalue = lastarray.getJSONObject(0);
                         imageurl = kvalue.getString("url");


                        String sql = "INSERT INTO Articles(title,byline,date,url,image) VALUES(?, ?, ?, ?, ?)";
                        SQLiteStatement sqLiteStatement = articleDB.compileStatement(sql);
                        sqLiteStatement.bindString(1,title);
                        sqLiteStatement.bindString(2,byline);
                        sqLiteStatement.bindString(3,date);
                        sqLiteStatement.bindString(4,link);
                        sqLiteStatement.bindString(5,imageurl);
                        sqLiteStatement.execute();

                    }


                } catch (JSONException e) {
                    updatelistView();
                    e.printStackTrace();
                }

            }
        }









    }
