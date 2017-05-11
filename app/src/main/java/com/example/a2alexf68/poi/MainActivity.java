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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This line sets the user agent, a requirement to download OSM maps
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main);
        mv = (MapView) findViewById(R.id.map1);
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(14);
        mv.getController().setCenter(new GeoPoint(50.9319, -1.4011));

        ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;

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
        items.addItem(milano);

        OverlayItem uni = new OverlayItem("Solent", "University", new GeoPoint(50.9319, -1.4011));
        items.addItem(uni);

        mv.getOverlays().add(items);
        /*
        BufferedReader reader = null;
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/poi.txt";
            reader = new BufferedReader(new FileReader(path));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.print(line);
                String[] components = line.split(",");
                if (components.length == 5) {
                    OverlayItem List = new OverlayItem(components[0], components[1], components[2], new GeoPoint(Double.parseDouble(components[4]), Double.parseDouble(components[3])));
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
         */
    }

    //---------------------------------------------------------------------------options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //-------------------------------------------------------------------------I/O file save/load the file from local 
    public boolean onOptionsItemSelected(MenuItem item) {
        String dir_path = Environment.getExternalStorageDirectory().getAbsolutePath();

        if (item.getItemId() == R.id.add_location) {
            Intent intent = new Intent(this, AddLocation.class);
            startActivityForResult(intent, 0);
            return true;

        } else if (item.getItemId() == R.id.preference) {
            Intent intent = new Intent(this, Preference.class);
            startActivityForResult(intent, 2);
            return true;
        }
        if (item.getItemId() == R.id.save_location) {

            try {
                FileWriter fw = new FileWriter(dir_path + "/poi.txt");
                PrintWriter pw = new PrintWriter(fw);

                for (int i = 0; i < items.size(); i++) {
                    //pw.print(et.getText().toString());
                    // pw.flush();
                    // pw.close(); // close the file to ensure data is flushed to file
                }

            } catch (IOException e) {
                System.out.println("I/O Error: " + e);
            }
            return true;
        } else if (item.getItemId() == R.id.load_location) {
            try {
                FileReader fr = new FileReader(dir_path + "/poi.txt");
                BufferedReader br = new BufferedReader(fr);

                for (int i = 0; i < items.size(); i++) {
                    OverlayItem item = items.Item(i);

                    // et.setText(br.readLine());

                    String line = "";
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                    br.close();
                }
                return true;

            } catch (IOException e) {
                System.out.println("I/O Error: " + e);
            }
        }
        return false;
    }
    //--------------------------------------------------------extract bundle information
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                Bundle extras = intent.getExtras();

                String name = extras.getString("Name");
                String type = extras.getString("Type");
                String description = extras.getString("Description");

                double lat = mv.getMapCenter().getLatitude();
                double lon = mv.getMapCenter().getLongitude();

                OverlayItem item = new OverlayItem("" + name, "" + type, "" + description, new GeoPoint(lat, lon));
                items.addItem(item);
            }

            // Force the map to redraw
            mv.invalidate();
        }
        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                Bundle extras = intent.getExtras();
                boolean tileMap = extras.getBoolean("com.example.mapnik");
                if (tileMap == true) {
                    mv.setTileSource(TileSourceFactory.MAPNIK);
                }
            }
        }
    }
}

