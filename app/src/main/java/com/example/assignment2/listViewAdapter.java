package com.example.assignment2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class listViewAdapter extends ArrayAdapter<LocationModel> {

    Context mContext;
    LayoutInflater inflater;
    private List<LocationModel> locationModelList = null;
    private ArrayList<LocationModel> arraylist;
    public listViewAdapter(@NonNull Context context, ArrayList<LocationModel> arrayList) {
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.itemlist, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        LocationModel currentNumberPosition = getItem(position);

        // then according to the position of the view assign the desired text for the same
        TextView address = currentItemView.findViewById(R.id.address);
        address.setText(currentNumberPosition.getAddress());

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView latitude = currentItemView.findViewById(R.id.latitude);
        latitude.setText(Double.toString(currentNumberPosition.getLatitude()));

        // then according to the position of the view assign the desired TextView 2 for the same
        TextView longitude = currentItemView.findViewById(R.id.longitude);
        longitude.setText(Double.toString(currentNumberPosition.getLongitude()));

        // then according to the position of the view assign the desired text for the same
        TextView state = currentItemView.findViewById(R.id.state);
        state.setText(currentNumberPosition.getState());

        // then return the recyclable view
        return currentItemView;
    }
}

