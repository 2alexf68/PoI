package com.example.a2alexf68.poi;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by 2alexf68 on 04/05/2017.
 */
public class AddLocation extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location);//id to resolve?

        Button submitButton = (Button) findViewById(R.id.poi_button);//id to resolve?
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //retrieve our lat and long values from the edit boxes
        EditText nameEditText = (EditText) findViewById(R.id.poi_name);

        // to verify again !!!!

        //double.parsedouble() allows to transform a string into a double type
        EditText typeEditText = (EditText) findViewById(R.id.poi_type);
        // to verify again !!!!

        EditText descriptionEditText = (EditText) findViewById(R.id.poi_description);
        // to verify again !!!!

        //assemble our lat long bundle

       Bundle newpoiBundle = new Bundle();

        newpoiBundle.putString("com.example.latitude", name);
        newpoiBundle.putString("com.example.longitude", type);
        newpoiBundle.putString("com.example.longitude", description);


        //gives the box which contains information; send the bundle to the parent activity
        Intent intent = new Intent();
        intent.putExtras(newpoiBundle);
        //if it arrives at this part of the code send the message that everything is okay
        setResult(RESULT_OK, intent);
        finish();
    }
}
