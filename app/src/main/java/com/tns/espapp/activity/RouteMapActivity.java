package com.tns.espapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.LatLongData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RouteMapActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    GoogleMap googleMap;
    List<LatLongData> points = new ArrayList<>();
    DatabaseHandler databaseHandler;
    private ArrayList<LatLng> latLngArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fm.getMapAsync(this);
        Intent intent = getIntent();
        String fromNumber = intent.getStringExtra(AppConstraint.SELECTEDFORMNUMBER);
        databaseHandler = new DatabaseHandler(this);
        points = databaseHandler.getLatLong(fromNumber);
     


    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        showMarker();


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }



    private void showMarker() {
        for (int i = 0; i < points.size(); i++) {
            LatLongData latLongData = points.get(i);
            LatLng point = new LatLng(Double.parseDouble(latLongData.getLat()),Double.parseDouble(latLongData.getLongi()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
            MarkerOptions markerOptions = new MarkerOptions();

            // Setting latitude and longitude of the marker position
            markerOptions.position(point);
          // Setting titile of the infowindow of the marker
            markerOptions.title("Location");
            // Setting the content of the infowindow of the marker
            markerOptions.snippet("Latitude :"+Double.parseDouble(latLongData.getLat())+" Longitude :"+Double.parseDouble(latLongData.getLongi())+"\n"+" Date :"+latLongData.getDate()+" Time: " +latLongData.getCurrent_time_str()+" Speed: "+latLongData.getSpeed());
            // Instantiating the class PolylineOptions to plot polyline in the map
            PolylineOptions polylineOptions = new PolylineOptions();

            // Setting the color of the polyline
            polylineOptions.color(Color.RED);

            // Setting the width of the polyline
            polylineOptions.width(3);
            latLngArrayList.add(point);

            // Adding the taped point to the ArrayList
           // Setting points of polyline
            polylineOptions.addAll(latLngArrayList);

            // Adding the polyline to the map
            googleMap.addPolyline(polylineOptions);

            // Adding the marker to the map
            googleMap.addMarker(markerOptions);
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    LinearLayout info = new LinearLayout(RouteMapActivity.this);
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(RouteMapActivity.this);
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(RouteMapActivity.this);
                    title.setGravity(Gravity.CENTER);
                    snippet.setTextColor(Color.BLACK);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);
                    return info;
                }
            });


        }

    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }
    private void createData()
    {
        LatLongData latLong ;
        latLong = new LatLongData();
        latLong .setFormno("17333/020817/002");
        latLong.setDate("02-08-17");
        latLong.setLat("28.637855");
        latLong.setLongi("77.400925");
        latLong.setCurrent_time_str("08:40:13");
        latLong.setSpeed("6.5888");
        points.add(latLong);


        latLong = new LatLongData();
        latLong .setFormno("17333/020817/002");
        latLong.setDate("02-08-17");
        latLong.setLat("28.637871");
        latLong.setLongi("77.400773");
        latLong.setCurrent_time_str("08:42:13");
        latLong.setSpeed("6.588");
        points.add(latLong);



        latLong = new LatLongData();
        latLong .setFormno("17333/020817/002");
        latLong.setDate("02-08-17");
        latLong.setLat("28.637874");
        latLong.setLongi("77.400654");
        latLong.setCurrent_time_str("08:42:21");
        latLong.setSpeed("10.080");
        points.add(latLong);




        latLong = new LatLongData();
        latLong .setFormno("17333/020817/002");
        latLong.setDate("02-08-17");
        latLong.setLat("28.637832");
        latLong.setLongi("77.401014");
        latLong.setCurrent_time_str("08:40:05");
        latLong.setSpeed("6.264");
        points.add(latLong);

        latLong = new LatLongData();
        latLong .setFormno("17333/020817/002");
        latLong.setDate("02-08-17");
        latLong.setLat("28.637836");
        latLong.setLongi("77.400513");
        latLong.setCurrent_time_str("08:42:29");
        latLong.setSpeed("5.040");
        points.add(latLong);


        latLong = new LatLongData();
        latLong .setFormno("17333/020817/002");
        latLong.setDate("02-08-17");
        latLong.setLat("28.637832");
        latLong.setLongi("77.401014");
        latLong.setCurrent_time_str("08:40:05");
        latLong.setSpeed("6.264");
        points.add(latLong);

        latLong = new LatLongData();
        latLong .setFormno("17333/020817/002");
        latLong.setDate("02-08-17");
        latLong.setLat("28.637785");
        latLong.setLongi("77.400397");
        latLong.setCurrent_time_str("08:42:42");
        latLong.setSpeed("4.608");
        points.add(latLong);




        latLong = new LatLongData();
        latLong .setFormno("17333/020817/002");
        latLong.setDate("02-08-17");
        latLong.setLat("28.629053");
        latLong.setLongi("77.379481");
        latLong.setCurrent_time_str("09:04:32");
        latLong.setSpeed("6.264");
        points.add(latLong);



    }

}
