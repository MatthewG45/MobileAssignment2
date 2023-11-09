package com.example.assignment2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // im creating all of my views outside of methods here since they are re-used across the entire class
    ListView listView;
    Button buttonReadFile, buttonNewLocation, buttonDeleteAll;
    android.widget.SearchView searchView;
    TextView textViewTitle;

    listViewAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiate all views to their corresponding element in the xml file

        listView = (ListView) findViewById(R.id.listView);
        buttonReadFile = (Button) findViewById(R.id.buttonReadFile);
        buttonNewLocation = (Button) findViewById(R.id.buttonNewLocation);
        buttonDeleteAll = (Button) findViewById(R.id.buttonDeleteAll);
        searchView = (SearchView) findViewById(R.id.searchView);


        //create object of databasehelper class
        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);

        //make and populate an arraylist from database custom getAll() method
        ArrayList<LocationModel> listall  = dataBaseHelper.getAll();

        //display arraylist listall using custom arrayadapter
        arrayAdapter = new listViewAdapter(this, listall);
        listView.setAdapter(arrayAdapter);

        //make a text listener for the search bar and create the methods later
        searchView.setOnQueryTextListener(MainActivity.this);

        // THis is the button listener for finding the desired file.
        // The user can select any file,
        // but this is of course supposed to be a csv.
        // I did figure out that you can limit the file type with setType,
        // but the csv plaintext option was not working with my file type
        buttonReadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // call intent with action get content to retrieve a file
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                // I set desired file type here (everything is included)
                intent.setType("*/*");

                //start activity for result so that the second you get a result you can do something with it
                // in teh corresponding method onactivityresult
                startActivityForResult(intent, 100);

            }
        });

        // item click listener for the list view.
        // this part is surprisingly simple, just grab the position of the item clicked,
        // then grab the location model of that position, then send the clicked location ID through the intent
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocationModel clickedLocation = (LocationModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, EditLocation.class);
                intent.putExtra("clickedLocationID", clickedLocation.getID());
                startActivity(intent);
            }
        });

        // long click listener to delete selected entry
        // this does not open a menu, it just instantly deletes the entry on long click
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // get position of clicked note and delete clicked note
                LocationModel clickedNote = (LocationModel) parent.getItemAtPosition(position);
                dataBaseHelper.deleteOne(clickedNote);

                // redraw the array adapter to redisplay the table
                ArrayList<LocationModel> listall  = dataBaseHelper.getAll();
                listViewAdapter arrayAdapter = new listViewAdapter(MainActivity.this, listall);
                listView.setAdapter(arrayAdapter);
                return true;
            }
        });

        // button listener for a new location, very simple, just starting the activity
        buttonNewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateLocation.class);
                startActivity(intent);
            }
        });

        // button listener for deleting every row in teh table
        // this is so that you can try again and keep re-importing
        buttonDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call to the delete all function in my DB
                dataBaseHelper.deleteAll();
                // redrawing the table (should now display nothing)
                ArrayList<LocationModel> listall  = dataBaseHelper.getAll();
                arrayAdapter = new listViewAdapter(MainActivity.this, listall);
                listView.setAdapter(arrayAdapter);
            }
        });

    }

    // here is the method that will run after the startactivityforresult has returned
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // ,ake a new database helper for functionality
        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);

        // this is all done in a try catch since many app crashing errors can happen during file reading
        try {
            // first, we create an inputstream.
            // then, since we grabbed the file with the content provider, we need to use a content resolver to find the correct URI
            // now, grab the URI generated form the selected file through the method and pass it to the content resolver to get an input stream.
            InputStream input = getContentResolver().openInputStream(data.getData());

            // create a new inputstreamreader with an input of our inputstream for a bufferedreader
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // read a single line to avoid the column names of the csv.
            reader.readLine();
            String line ="";

            // start a while loop to parse through the csv as long as the next line is not empty or null
            while ( (line = reader.readLine()) != null) {
                // split the lines by the commas (thats why we use a .csv (comma delimited list))
                String[] tokens = line.split(",");

                //Here, we create the geocoder in our activity context
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                //Here, we are passing each line through the geocoder to get the address before parsing it into the database
                // put the geocoder address into a list (since it is MANY different location values)
                List<Address> ListAddress=geocoder.getFromLocation(Double.parseDouble(tokens[0]),Double.parseDouble(tokens[1]),1);
                if(ListAddress != null && ListAddress.size() > 0 ) {
                    // here is the full address, including everything from phone # to postal code. We do not use this.
                    Address address = ListAddress.get(0);

                    // Here is the street name, some lat/long pairs will not have one unfortunately.
                    String street = address.getThoroughfare();

                    // put the data into a location model.
                    LocationModel locationModel = new LocationModel(-1, street, Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]), tokens[2]);

                    // send the data to the db using our custom addone method
                    boolean success = dataBaseHelper.addOne(locationModel);
                }
            }


            // catch all the exceptions so that it does not crash the app
        } catch (Exception e) {
            // write a message containing the error text
            Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
            textViewTitle.setText(e.getMessage());
        }
        // redraw the table
        ArrayList<LocationModel> listall  = dataBaseHelper.getAll();
        arrayAdapter = new listViewAdapter(this, listall);
        listView.setAdapter(arrayAdapter);
    }

    // here is the method made for our search view.
    // this is run once the searchview button is pressed, or the user hits enter.
    @Override
    public boolean onQueryTextSubmit(String query) {

        // create a db helper for db functions
        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
        // listen for if the search query is empty. THis is actually hardcoded not to work in the searchView library
        // therefore, I implemented the exact same functionality in the onquery text change listener
        if (query.length() < 1) {
            // redraw full list if search box empty
            ArrayList<LocationModel> listall  = dataBaseHelper.getAll();
            arrayAdapter = new listViewAdapter(this, listall);
            listView.setAdapter(arrayAdapter);
        } else {
            // redraw only list generated by searching with query

            // generate results form the db with the query within the search box
            ArrayList<LocationModel> locationModel = dataBaseHelper.getOneBySearch(query);
            arrayAdapter = new listViewAdapter(this, locationModel);
            listView.setAdapter(arrayAdapter);
        }


        return false;
    }

    // this method contains the exact same code as the onquerytextsubmitted method
    // so that the search box functions while its being typed into
    // instead of just on pressing enter or search
    @Override
    public boolean onQueryTextChange(String query) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
        if (query.length() < 1) {
            ArrayList<LocationModel> listall  = dataBaseHelper.getAll();
            arrayAdapter = new listViewAdapter(this, listall);
            listView.setAdapter(arrayAdapter);
        } else {
            ArrayList<LocationModel> locationModel = dataBaseHelper.getOneBySearch(query);
            arrayAdapter = new listViewAdapter(this, locationModel);
            listView.setAdapter(arrayAdapter);
        }
        return false;
    }
}