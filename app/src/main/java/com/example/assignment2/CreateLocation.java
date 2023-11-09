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

public class CreateLocation extends AppCompatActivity {

    // instantiate any used variables
    EditText editTextAddress, editTextLatitude, editTextLongitude, editTextState;
    Button buttonCreateLocation;

    TextView textViewTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_location);

        // set each view to their xml layout counterparts
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLongitude = findViewById(R.id.editTextLongitude);
        editTextState = findViewById(R.id.editTextState);
        buttonCreateLocation = findViewById(R.id.buttonCreateLocation);

        // set title and button text to creating new location since layout is re-used
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setText("Create New Location");
        buttonCreateLocation.setText("Create Location");


        // button listener for creating new location and sending data to DB
        buttonCreateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // enter try catch statement since failures here could crash application
                try {
                    // input new user values into locaiton model list
                    LocationModel locationModel = new LocationModel(-1,
                            editTextAddress.getText().toString(),
                            Double.parseDouble(editTextLatitude.getText().toString()),
                            Double.parseDouble(editTextLongitude.getText().toString()),
                            editTextState.getText().toString());

                    // create db helper
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(CreateLocation.this);
                    // add location model to the DB with custom addOne method
                    boolean success = dataBaseHelper.addOne(locationModel);

                    // switch back to main activity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                } catch (Exception e) {
                    Toast.makeText(CreateLocation.this, "Error creating location", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
