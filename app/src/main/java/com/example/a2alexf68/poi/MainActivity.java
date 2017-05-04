package com.example.a2alexf68.poi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This line sets the user agent, a requirement to download OSM maps
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main);
        mv = (MapView) findViewById(R.id.map1);

        ItemizedIconOverlay<OverlayItem> items;
        ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;

        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(14);
        mv.getController().setCenter(new GeoPoint(50.9319, -1.4011));

        /*
        Button submitButton = (Button) findViewById(R.id.locationButton);
        submitButton.setOnClickListener(this);
        */

        markerGestureListener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            public boolean onItemLongPress(int i, OverlayItem item) {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }

            public boolean onItemSingleTapUp(int i, OverlayItem item) {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);
        OverlayItem milano = new OverlayItem("Milano", "City in north Italy", new GeoPoint(45.4641, 9.1928));
        // OverlayItem blackdown = new OverlayItem("Blackdown", "highest point in West Sussex", new GeoPoint(51.0581, -0.6897));
        items.addItem(milano);
        //items.addItem(blackdown);
        mv.getOverlays().add(items);


        BufferedReader reader = null;
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/poi.txt";
            reader = new BufferedReader(new FileReader(path));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.print(line);
                String[] components = line.split(",");
                if (components.length == 5) {
                    OverlayItem List = new OverlayItem(components[0], components[2], new GeoPoint(Double.parseDouble(components[4]), Double.parseDouble(components[3])));
                    if (components[1].equals("pub")) {
                        List.setMarker(getResources().getDrawable(R.drawable.pub));
                    } else if (components[1].equals("restaurant")) {
                        List.setMarker(getResources().getDrawable(R.drawable.restaurant));
                    }
                    items.addItem(List);

                }


            }

            reader.close();
        } catch (IOException e) {
            new AlertDialog.Builder(this).setMessage("ERROR: " + e).show();
        }

    }

    @Override
    public void onClick(View view) {
        EditText latitudeEditText = (EditText) findViewById(R.id.latitudeEditText);
        String latitudeAsString = latitudeEditText.getText().toString();

        EditText longitudeEditText = (EditText) findViewById(R.id.longitudeEditText);
        String longitudeAsString = longitudeEditText.getText().toString();

        if (latitudeAsString.isEmpty() || longitudeAsString.isEmpty()) {
            mv.getController().setCenter(new GeoPoint(41.1, 12.1));
            mv.getController().setZoom(10);
        } else {
            double latitude = Double.parseDouble(latitudeAsString);
            double longitude = Double.parseDouble(longitudeAsString);

            mv.getController().setCenter(new GeoPoint(longitude, latitude));
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);//id to resolve !!!!
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_location) {//id to resolve !!!!
            Intent intent = new Intent(this, AddLocation.class);
            startActivityForResult(intent, 0);
            return true;
        }
        else if(item.getItemId() == R.id.set_location) {//id to resolve !!!!
            Intent intent = new Intent(this, SetLocation.class);
            startActivityForResult(intent, 1);
            return true;
        }
        return false;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                Bundle extras = intent.getExtras();
                boolean tileMap = extras.getBoolean("com.example.mapnik");
                if (tileMap == true) {
                    mv.setTileSource(TileSourceFactory.MAPNIK);
                }
            }
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle extras = intent.getExtras();
                double latitude = extras.getDouble("com.example.coordinate");
                double longitude = extras.getDouble("com.example.coordinate");
                //double latitude = Double.parseDouble();
                //double longitude = Double.parseDouble();
                mv.getController().setCenter(new GeoPoint(longitude, latitude));
            }
        }
    }
}
