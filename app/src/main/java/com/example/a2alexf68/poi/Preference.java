package com.example.a2alexf68.poi;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by 2alexf68 on 04/05/2017.
 */
public class Preference extends ListActivity  {
    String[] data;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        data = new String[] { "Save on device", "Save online" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        setListAdapter(adapter);
    }

    public void onListItemClick(ListView lv, View view, int index, long id)
    {
        // handle list item selection
    }
}
