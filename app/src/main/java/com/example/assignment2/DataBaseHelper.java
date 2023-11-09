package com.example.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(@Nullable Context context) {
        // send hard-coded values for db version and name
        super(context, "locations", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table statement, raw Sql
        String createTableStatement = "CREATE TABLE LOCATIONS_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, ADDRESS TEXT, LATITUDE DOUBLE, LONGITUDE DOUBLE, STATE TEXT)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // I never used this, but if I wanted to add a new column or table or something you can here

    }

    // custom addOne method for adding a locationmodel to the DB
    public boolean addOne(LocationModel locationModel) {
        // open a writable db
        SQLiteDatabase db = this.getWritableDatabase();

        //String queryString = "INSERT INTO LOCATIONS_TABLE VALUES ("+address+", "+latitude+", "+longitude+", "+state+")";

        // create contentvalues to make key value pairs for db insertion
        ContentValues cv = new ContentValues();

        // input values from locationModel object into content values object
        cv.put("ADDRESS", locationModel.getAddress());
        cv.put("LATITUDE", locationModel.getLatitude());
        cv.put("LONGITUDE", locationModel.getLongitude());
        cv.put("STATE", locationModel.getState());

        // call insert function and grab success status to return
        long insert = db.insert("LOCATIONS_TABLE", null, cv);

        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    //custom delete one method for deleting a row based on primary key ID
    public boolean deleteOne(LocationModel locationModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        // almost raw sql to simplify this.
        String queryString = "DELETE FROM LOCATIONS_TABLE WHERE ID = " + locationModel.getID();

        // return status if success or not
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    // custom get all emthod for returning a array list of location models of each row in teh DB
    public ArrayList<LocationModel> getAll() {

        // instantiate an arraylist of location models (an array of rows in teh DB)
        ArrayList<LocationModel> returnList = new ArrayList<>();

        // get data
        String queryString = "SELECT * FROM LOCATIONS_TABLE";
        SQLiteDatabase db = this.getReadableDatabase();


        // store results in a cursor to parse through
        Cursor cursor = db.rawQuery(queryString, null);

        // start loop if the cursor is not null
        if (cursor.moveToFirst()) {
            // loop through cursor and create new object for each note
            // teh do while here will stop when the next line is null
            do {
                // grab values form cursor object
                int ID = cursor.getInt(0);
                String address = cursor.getString(1);
                double latitude = cursor.getDouble(2);
                double longitude = cursor.getDouble(3);
                String state = cursor.getString(4);

                // insert values into locationmodel
                LocationModel locationModel = new LocationModel(ID, address, latitude, longitude, state);
                // add the location model to the list
                returnList.add(locationModel);

            } while (cursor.moveToNext());

        } else {
            //failure. do not add anything to list
        }
        //close cursor and db
        cursor.close();
        db.close();

        // return the whole list
        return returnList;
    }

    // custom method to get a row by primary key ID
    public ArrayList<LocationModel> getOneByID(Integer ID) {

        ArrayList<LocationModel> returnList = new ArrayList<>();

        // get data
        String queryString = "SELECT * FROM LOCATIONS_TABLE WHERE ID = "+ID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            // loop through cursor and create new object for each note
            do {
                int locationID = cursor.getInt(0);
                String address = cursor.getString(1);
                String latitude = cursor.getString(2);
                String longitude = cursor.getString(3);
                String state = cursor.getString(4);

                LocationModel locationModel = new LocationModel(locationID, address, Double.parseDouble(latitude), Double.parseDouble(longitude), state);
                returnList.add(locationModel);

            } while (cursor.moveToNext());

        } else {
            //failure. do not add anything to list
        }
        //close cursor and db
        cursor.close();
        db.close();

        return returnList;
    }

    // custom method to update a row in the db with new parameters for each column
    public boolean updateOne(LocationModel locationModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        // grabbing values from NoteModel object
        int id = locationModel.getID();
        String address = locationModel.getAddress();
        double latitude = locationModel.getLatitude();
        double longitude = locationModel.getLongitude();
        String state = locationModel.getState();

        // creating querystring
        String queryString = "UPDATE LOCATIONS_TABLE SET ADDRESS = '"+address+"', LATITUDE = "+latitude+", LONGITUDE = "+longitude+", STATE = '"+state+"' WHERE ID = "+id;

        // execute querystring, return success, close resources
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return false;
        } else {
            cursor.close();
            db.close();
            return true;
        }

        //long insert = db.update(NOTES_TABLE, cv, "WHERE "+COLUMN_ID+" = " + noteModel.getId(), null);
    }

    // here is a custom method for getting a row based on address instead of ID
    public ArrayList<LocationModel> getOneBySearch(String streetAddress) {

        ArrayList<LocationModel> returnList = new ArrayList<>();

        // get data, but specify that it is not case-sensitive, decreasing user frustration
        String queryString = "SELECT * FROM LOCATIONS_TABLE WHERE ADDRESS = '"+streetAddress+"' COLLATE NOCASE";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            // loop through cursor and create new object for each note
            do {
                int locationID = cursor.getInt(0);
                String address = cursor.getString(1);
                String latitude = cursor.getString(2);
                String longitude = cursor.getString(3);
                String state = cursor.getString(4);

                LocationModel locationModel = new LocationModel(locationID, address, Double.parseDouble(latitude), Double.parseDouble(longitude), state);
                returnList.add(locationModel);

            } while (cursor.moveToNext());

        } else {
            //failure. do not add anything to list
        }
        //close cursor and db
        cursor.close();
        db.close();

        return returnList;
    }

    // custom method to delete all rows from the table in the db, very simple
    public boolean deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM LOCATIONS_TABLE";

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }
}
