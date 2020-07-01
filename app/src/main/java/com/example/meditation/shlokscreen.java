package com.example.meditation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class shlokscreen extends AppCompatActivity {
    private ImageButton back;
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;
    public TextView chaptername;

    // URL to get contacts JSON
    public String url = "https://bhagavadgita.io/api/v1/chapters/";
    public String url2 = "/verses?access_token=";
    final int random = new Random().nextInt(19);
    String chapter=String.valueOf(random);
    ArrayList<HashMap<String, String>> shlokList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shlokscreen);
        chaptername = findViewById(R.id.textView);
        shlokList = new ArrayList<>();

        final String chapterdet = "Chapter: "+chapter;
        chaptername.setText(chapterdet);
        lv = findViewById(R.id.list);
        new GetShlok().execute();

        back = findViewById(R.id.imageButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(shlokscreen.this, mainscreen.class);
                startActivity(i);
            }
        });
    }
    private class GetShlok extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(shlokscreen.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
//            String token = sh.makeServiceCall(url2);
//            String authtoken = sh.makeServiceCall()
            String authtoken = "2X91vob9JZJEHBBwkVQICMxMjQHkoL";

            url = url + chapter + url2 + authtoken;
            String jsonStr = sh.makeServiceCall(url);


            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray shlokarray = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < shlokarray.length(); i++) {
                        JSONObject c = shlokarray.getJSONObject(i);

                        String id = c.getString("verse_number");
                        String shlok = c.getString("text");
                        String trans = c.getString("transliteration");
                        String meaning = c.getString("meaning");
                        HashMap<String, String> shlokarray2 = new HashMap<>();

                        shlokarray2.put("id", id);
                        shlokarray2.put("shlok", shlok);
                        shlokarray2.put("trans", trans);
                        shlokarray2.put("meaning", meaning);

                        // adding contact to contact list
                        shlokList.add(shlokarray2);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    shlokscreen.this, shlokList,
                    R.layout.list_item, new String[]{"shlok", "trans",
                    "meaning"}, new int[]{R.id.shlok,
                    R.id.trans, R.id.meaning});

            lv.setAdapter(adapter);
        }

    }
}
