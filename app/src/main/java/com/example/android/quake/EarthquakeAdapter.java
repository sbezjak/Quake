package com.example.android.quake;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.EarthquakeAdapterViewHolder> {

    private static final String LOCATION_SEPARATOR = " of ";

    private final Context mContext;

    private final EarthquakeAdapterOnClickHandler mClickHandler;

    private List<Earthquake> mEarthquakes;

    public interface EarthquakeAdapterOnClickHandler {
        void onClick(long date);
    }

    public EarthquakeAdapter(Context context, EarthquakeAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;

    }

    class EarthquakeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView magnitudeView;
        final TextView locationOffsetView;
        final TextView primaryLocationView;
        final TextView dateView;
        final TextView timeView;

        EarthquakeAdapterViewHolder(View view) {
            super(view);

            magnitudeView = view.findViewById(R.id.magnitude);
            locationOffsetView = view.findViewById(R.id.location_offset);
            primaryLocationView = view.findViewById(R.id.primary_location);
            dateView = view.findViewById(R.id.date);
            timeView = view.findViewById(R.id.time);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            Earthquake currentEarthquake = mEarthquakes.get(adapterPosition);

            // Convert the String URL into a URI object (to pass into the Intent constructor)
            Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

            // Create a new intent to view the earthquake URI
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

            // Send the intent to launch a new activity
            mContext.startActivity(websiteIntent);

        }
    }

    public void setEarthquakes(List<Earthquake> earthquakes) {
        mEarthquakes = earthquakes;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public EarthquakeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate
                (R.layout.earthquake_list_item, parent, false);
        return new EarthquakeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EarthquakeAdapter.EarthquakeAdapterViewHolder holder, int position) {
        final Earthquake currentEarthquake = mEarthquakes.get(position);

        // Format the magnitude to show 1 decimal place
        String formattedMagnitude = formatMagnitude(currentEarthquake.getMagnitude());
        holder.magnitudeView.setText(formattedMagnitude);

        // Set the proper background color on the magnitude circle.
        GradientDrawable magnitudeCircle = (GradientDrawable) holder.magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        // Get the original location string from the Earthquake object,
        String originalLocation = currentEarthquake.getLocation();

        String primaryLocation;
        String locationOffset;

        // Check whether the originalLocation string contains the " of " text
        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            // Split the string into different parts (as an array of Strings)
            // based on the " of " text.
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        } else {
            // Otherwise, there is no " of " text in the originalLocation string.
            // Hence, set the default location offset to say "Near the"
            locationOffset = mContext.getString(R.string.near_the);
            // The primary location will be the full location string
            primaryLocation = originalLocation;
        }

        holder.primaryLocationView.setText(primaryLocation);
        holder.locationOffsetView.setText(locationOffset);

        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(currentEarthquake.getTimeInMilliseconds());
        String formattedDate = formatDate(dateObject);
        holder.dateView.setText(formattedDate);

        String formattedTime = formatTime(dateObject);
        holder.timeView.setText(formattedTime);

    }

    @Override
    public int getItemCount() {
        if (mEarthquakes != null) {
            return mEarthquakes.size();
        } else return 0;
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

        return ContextCompat.getColor(mContext, magnitudeColorResourceId);
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
