package com.example.a2alexf68.poi;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by 2alexf68 on 04/05/2017.
 */
public class Preference extends ListActivity {
    String[] data, details;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = new String[]{"Save online"};
        details = new String[]{"Select to save locations online"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        setListAdapter(adapter);
    }

    public class MyAdapter extends ArrayAdapter<String> {
        public MyAdapter() {
            // We have to use ExampleListActivity.this to refer to the outer class (the activity)
            super(Preference.this, android.R.layout.simple_list_item_1, data);
        }

        @Override
        //makes sure that you over write a super class; makes sure that it respects the following layout; it should look like this
        public View getView(int index, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                //inflate our poi entry layout
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.preference, parent, false);
            }

            //populate our poi entry with data
            TextView nameTextView = (TextView) view.findViewById(R.id.setting1);
            nameTextView.setText(data[index]);

            TextView descriptionTextView = (TextView) view.findViewById(R.id.setting1a);
            descriptionTextView.setText(details[index]);
            //return view
            return view;
        }
    }

    public void onListItemClick(ListView lv, View view, int index, long id) {
        Intent intent = new Intent();

        // handle list item selection
    }
}
