package com.example.Y3840030.Y3840030mobiledevassignment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetLocation extends AppCompatActivity {

    private LocationManager l_mgr;
    private int LOCATION_PERMISSION_CODE = 1;
    public String Location_Name;
    private double LONGITUDE;
    private double LATITUDE;
    private String location;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBar);


        l_mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Button buttonRequest = findViewById(R.id.button2);
        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(GetLocation.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(GetLocation.this, "You've already got location permissions on", Toast.LENGTH_SHORT).show();

                    ProgressBar p = findViewById(R.id.progressBar);
                    p.setVisibility(View.VISIBLE);

                    getGPS();
                } else {
                    requestLocationPermission();
                }
            }
        });
    }

    public void getGPS() {
        l_mgr = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        TextView txt = findViewById(R.id.textView);
        txt.setVisibility(View.VISIBLE);
        try {
            l_mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    ((TextView) findViewById(R.id.textView)).setText(
                            "YOUR POSITION:\n" +
                                    "LAT: " + String.valueOf(location.getLatitude()) + "\n" +
                                    "LNG: " + String.valueOf(location.getLongitude()) + "\n" +
                                    "ACC: " + String.valueOf(location.getAccuracy()) + "m\n"
                    );

                    ProgressBar p = findViewById(R.id.progressBar);
                    p.setVisibility(View.INVISIBLE);

                    Button b = findViewById(R.id.button3);
                    b.setVisibility(View.VISIBLE);

                    TextView t = findViewById(R.id.textView3);
                    t.setVisibility(View.VISIBLE);

                    Button bu = findViewById(R.id.button2);
                    bu.setVisibility(View.INVISIBLE);

                    TextView tv = findViewById(R.id.textView4);
                    tv.setVisibility(View.INVISIBLE);

                    LATITUDE = location.getLatitude();
                    LONGITUDE = location.getLongitude();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } catch (SecurityException e) {
            //user rejected request for permission
            ((TextView) findViewById(R.id.textView)).setText(e.getLocalizedMessage());
        }
    }


    private void requestLocationPermission() {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            new AlertDialog.Builder(this)
            .setTitle("Permission needed")
            .setMessage("This permission is needed because because")
            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

            }
            })
            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            }
            })
            .create().show();
            } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            }
            }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if(requestCode == LOCATION_PERMISSION_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
            Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
            }
            }

    public void getLocation(View v) {

                new downloadLocation().execute("https://api.opencagedata.com/geocode/v1/json?q=" + LATITUDE +"%2C%20" + LONGITUDE +"&key=e8ce16d53ca14f2da911602059a5dbae&language=en&pretty=1");
                }

    public void buttonPressed(View view) {
                Intent i = new Intent(this, ShowNews.class);
                i.putExtra("key", location);
                startActivity(i);
                }

private class downloadLocation extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
        URL url;

        try {
            url = new URL(urls[0]);
        } catch (MalformedURLException e) {
            return "Could not connect to the internet, please try again";
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
            return "Oops, an error has occurred";
        }
    }

    @Override
    protected void onPostExecute(String jsonLocationString) {
        if (jsonLocationString.length() == 0) {
            setLocation("ERROR couldn't get location");
            return;
        }

        try {
            JSONObject json = new JSONObject(jsonLocationString);
            JSONArray jsonArray = json.getJSONArray("results");

            JSONObject obj2 = jsonArray.getJSONObject(0);
            JSONObject address_components = obj2.getJSONObject("components");

            //System.out.println(jsonArray);
            location = address_components.getString("county");

            System.out.println(location);
            Location_Name = location;
        } catch (JSONException e) {
            location = e.getLocalizedMessage();
        }
        setLocation(location);
    }


    public void setLocation(String st) {
        TextView tv = findViewById(R.id.textView3);
        tv.setText(st);

        Button b = findViewById(R.id.button);
        b.setVisibility(View.VISIBLE);
    }


}
}
