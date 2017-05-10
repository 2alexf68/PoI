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
        setContentView(R.layout.add_location);

        Button submitButton = (Button) findViewById(R.id.poi_button);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //assemble our bundle
        Bundle newpoiBundle = new Bundle();

        EditText nameEditText = (EditText)findViewById(R.id.poi_name);
        String name = nameEditText.getText().toString();

        EditText typeEditText = (EditText)findViewById(R.id.poi_type);
        String type = nameEditText.getText().toString();

        EditText descriptionEditText = (EditText)findViewById(R.id.poi_description);
        String description = nameEditText.getText().toString();

        newpoiBundle.putString("Name",name);
        newpoiBundle.putString("Type",type );
        newpoiBundle.putString("Description", description);

        //gives the box which contains information; send the bundle to the parent activity
        Intent intent = new Intent();
        intent.putExtras(newpoiBundle);
        //if it arrives at this part of the code send the message that everything is okay
        setResult(RESULT_OK, intent);
        finish();
    }
}