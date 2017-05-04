package com.example.a2alexf68.poi;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PoiListActivity extends ListActivity {

    String[] details, names;//not declared yet

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_poi_list);
        // names = new String[] {"Cycling", "Regular"};
        // details = new String[]  {"Cycling path","Highway"};
        MyAdapter adapter = new MyAdapter();
        setListAdapter(adapter);
    }

    public void onListItemClick(ListView lv, View view, int index, long id) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        boolean cyclemap = false;

        if (index == 0)//0_firefox/1_apus/2_generic
        {
            cyclemap = true;
        }

        bundle.putBoolean("com.example.cyclemap", cyclemap);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    public class MyAdapter extends ArrayAdapter<String> {
        public MyAdapter() {
            // We have to use ExampleListActivity.this to refer to the outer class (the activity)
            super(PoiListActivity.this, android.R.layout.simple_list_item_1, names);
        }

        @Override
        //makes sure that you over write a super class; makes sure that it respects the following layout; it should look like this
        public View getView(int index, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                //inflate our poi entry layout
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.poi_list_activity, parent, false);
            }
            //populate our poi entry with data
            TextView nameTextView = (TextView) view.findViewById(R.id.poi_name);
            nameTextView.setText(names[index]);

            TextView descriptionTextView = (TextView) view.findViewById(R.id.poi_desc);
            descriptionTextView.setText(details[index]);
            //return view
            return view;
        }
    }
}

