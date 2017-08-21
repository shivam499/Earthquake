package com.pathantalabs.earthquake;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthQuakeAdapter extends ArrayAdapter<EarthQuakes> {

    private static final String LOCATION_SEPARATOR = " of ";
    String primaryLocation;
    String locationOffset;

    public EarthQuakeAdapter(Context context, ArrayList<EarthQuakes> earthQuakes) {
        super(context,0,earthQuakes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View earthView = convertView;
        if(earthView == null){
            earthView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        EarthQuakes quakes = getItem(position);

        String originalLocation = quakes.getEarthQuakePlace();

        if (originalLocation.contains(LOCATION_SEPARATOR)){
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0]+ LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        }else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }

        TextView primaryLocationView = (TextView) earthView.findViewById(R.id.primary_location);
        primaryLocationView.setText(primaryLocation);

        TextView offSetLocation = (TextView) earthView.findViewById(R.id.location_offset);
        offSetLocation.setText(locationOffset);


        //showing the date
        Date date = new Date(quakes.getEarthQuakeDate());
        TextView dateView = (TextView) earthView.findViewById(R.id.earthQuakeDate);
        String formattedDate = formatDate(date);
        dateView.setText(formattedDate);

        //magnitude or rating of earth quake
        TextView magnitude = (TextView) earthView.findViewById(R.id.earthQuakeRating);
        String fromatedMagnitude = formatMagnitude(quakes.getEarthQuakeRating());
        magnitude.setText(fromatedMagnitude);

        //color the magnitude circle
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();
        int magnitudeColor = getMagnitudeColor(quakes.getEarthQuakeRating());
        magnitudeCircle.setColor(magnitudeColor);

        //showing only time
        TextView timeView = (TextView) earthView.findViewById(R.id.time);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(date);
        // Display the time of the current earthquake in that TextView
        timeView.setText(formattedTime);

        return earthView;
    }

    private int getMagnitudeColor(double magnitude) {

        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }
}


