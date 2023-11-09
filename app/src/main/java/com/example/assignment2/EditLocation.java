package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EditLocation extends AppCompatActivity {

    // here, i am instantiating all the variables i need to use in multiple methods.
    TextView textViewTitle;
    EditText editTextAddress, editTextLatitude, editTextLongitude, editTextState;

    Button buttonCreateLocation;

    String address, state;
    double latitude, longitude;
    int locationID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_location);

        // setting each view to its corresponding xml element
        textViewTitle = findViewById(R.id.textViewTitle);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLongitude = findViewById(R.id.editTextLongitude);
        editTextState = findViewById(R.id.editTextState);
        buttonCreateLocation = findViewById(R.id.buttonCreateLocation);

        // since this class re-uses the create location xml layout, change the title and button text each time
        textViewTitle.setText("Edit Location");
        buttonCreateLocation.setText("Save");

        // enter a try catch statement since the app will crash if these lines fail
        try {
            // grabbing location ID from intent to pull it from DB
            Intent moveintent = getIntent();
            locationID = moveintent.getIntExtra("clickedLocationID", -1);

            // create db helper for db functions
            DataBaseHelper dataBaseHelper = new DataBaseHelper(EditLocation.this);

            // generate location model by querying DB with location ID
            ArrayList<LocationModel> locationModel = dataBaseHelper.getOneByID(locationID);


            // pull values from location model to display in edit text boxes
            address = locationModel.get(0).getAddress();
            latitude = locationModel.get(0).getLatitude();
            longitude = locationModel.get(0).getLongitude();
            state = locationModel.get(0).getState();

            editTextAddress.setText(address);
            editTextLatitude.setText(Double.toString(latitude));
            editTextLongitude.setText(Double.toString(longitude));
            editTextState.setText(state);




        } catch (Exception e) {
            Toast.makeText(EditLocation.this, "New Note", Toast.LENGTH_SHORT).show();
        }

        // button listener for creating/saving location data
        buttonCreateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // enter try catch in case of failures
                try {
                    // put user inputted values back into location model
                    LocationModel locationModel = new LocationModel(locationID,
                            editTextAddress.getText().toString(),
                            Double.parseDouble(editTextLatitude.getText().toString()),
                            Double.parseDouble(editTextLongitude.getText().toString()),
                            editTextState.getText().toString());

                    // new database helper for db functions
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(EditLocation.this);
                    // update the entry with the new values using custom updateOne method
                    boolean success = dataBaseHelper.updateOne(locationModel);

                    // start an intent to switch back to main activity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                } catch (Exception e) {
                    Toast.makeText(EditLocation.this, "Error updating location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
