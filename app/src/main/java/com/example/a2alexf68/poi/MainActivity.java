package com.example.a2alexf68.poi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
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
        /*
        OverlayItem milano = new OverlayItem("Milano", "City in north Italy", new GeoPoint(45.4641, 9.1928));
        items.addItem(milano);

        OverlayItem uni = new OverlayItem("Solent", "University", new GeoPoint(50.9319, -1.4011));
        items.addItem(uni);
        */
        //adds the overlay item aka marker on the map
        mv.getOverlays().add(items);

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
        } else if (item.getItemId() == R.id.load_fromweb) {
            LoadFromWeb lfw = new LoadFromWeb();
            lfw.execute();
            return true;
        } else if (item.getItemId() == R.id.save_toweb) {
            SaveToWeb stw = new SaveToWeb();
            stw.execute();
            return true;
        } else if (item.getItemId() == R.id.save_location) {

            try {
                new AlertDialog.Builder(this).setMessage(dir_path).setPositiveButton("OK", null).show();

                FileWriter fw = new FileWriter(dir_path + "/poi.txt");
                PrintWriter pw = new PrintWriter(fw);

                for (int i = 0; i < items.size(); i++) {
                    OverlayItem marker = items.getItem(i);
                    pw.println(marker.getTitle() + "," + marker.getSnippet() + "," + marker.getPoint().getLatitude() + "," + marker.getPoint().getLongitude());
                }
                pw.close(); // close the file to ensure data is flushed to file
            } catch (IOException e) {
                new AlertDialog.Builder(this).setMessage(e.toString()).setPositiveButton("OK", null).show();
            }
            return true;
        } else if (item.getItemId() == R.id.load_location) {
            //-----------------------------------------------load locations
            BufferedReader reader = null;
            try {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/poi.txt";
                reader = new BufferedReader(new FileReader(path));
                String line = "";
                while ((line = reader.readLine()) != null) { //--------------------------------------its loading the first marker only
                    System.out.print(line);
                    String[] components = line.split(",");
                    if (components.length == 4) {
                        OverlayItem List = new OverlayItem(components[0], components[1], new GeoPoint(Double.parseDouble(components[3]), Double.parseDouble(components[2])));
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
                //new AlertDialog.Builder(this).setMessage("ERROR: " + e).show();
                new AlertDialog.Builder(this).setMessage(dir_path).setPositiveButton("OK", null).show();
            }
        }
        mv.invalidate();
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

                OverlayItem item = new OverlayItem(name, description, new GeoPoint(lat, lon));
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

    //LOAD from the web class to the main activity as over layed items, markers
    class LoadFromWeb extends AsyncTask<Void, Void, ItemizedIconOverlay<OverlayItem>> {
        public ItemizedIconOverlay<OverlayItem> doInBackground(Void... unused) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://www.free-map.org.uk/course/mad/ws/get.php?year=17&username=user002&format=csv");
                conn = (HttpURLConnection) url.openConnection();
                InputStream in = conn.getInputStream();
                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line = "";
                    while ((line = reader.readLine()) != null) { //--------------------------------------its loading the first marker only
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
                    return items;
                }
                return null;
            } catch (IOException e) {
                return null;
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
        }

        //initialize the data from the web
        public void onPostExecute(ItemizedIconOverlay<OverlayItem> itemsp) {
            mv.getOverlays().add(itemsp);
            mv.invalidate();
        }
    }

    //SAVE to the web class to the main activity as over layed items, markers
    class SaveToWeb extends AsyncTask<String, Void, String> {
        public String doInBackground(String... params) {
            HttpURLConnection conn = null;
            try {
                URL urlObj = new URL("http://www.free-map.org.uk/course/mad/ws/add.php");
                conn = (HttpURLConnection) urlObj.openConnection();

                String postData = "username=user039&name" + params[0] + "&type=" + params[1] + "&description=" + params[2] + "&lat=" + params[3] + "&lon=" + params[4] + "&year=17";

                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(postData.length());

                OutputStream out = null;
                out = conn.getOutputStream();
                out.write(postData.getBytes());
                if (conn.getResponseCode() == 200) {
                    InputStream in = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String all = "", line;
                    while ((line = br.readLine()) != null)
                        all += line;
                    return all;
                } else
                    return "HTTP ERROR: " + conn.getResponseCode();

            } catch (IOException e) {
                return e.toString();
            }
        }

        //execute to send the data from the web
        public void onPostExecute(String results) {
            //new AlertDialog.Builder(getActivity()).setMessage("Uploaded!" + results).setPositiveButton("OK", null).show();
        }
    }

    public void upload(String name, String type, String desc, double lat, double lon) {
        SaveToWeb save = new SaveToWeb();
        save.execute(name, type, desc, String.valueOf(lat), String.valueOf(lon));
    }
}
