package com.dxk.dxkapplication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import static android.content.ContentValues.TAG;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationProviderClient;
    public GoogleMap gMap;
    private boolean permissionGranted;
    private Location lastKnownLocation;
    private final LatLng defaultLocation =  new LatLng(47.6989, -122.3327);
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public ArrayList<Camera> cameraLocations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap gMap) {
        this.gMap = gMap;
        updateLocationUI();
        getCamData();
        getLocationPermission();
        getDeviceLocation();
    }

    public void showMarkers(){
        for(Camera cam : cameraLocations){
            Double[] latLong = cam.getCoordinates();
            double lat = latLong[0];
            double lng = latLong[1];
            gMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(cam.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }

    public void getCamData(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected()) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String cameraApiURL = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, cameraApiURL, null,
                    response -> {
                        try {
                            JSONArray features = response.getJSONArray("Features");
                            for (int i = 0; i < features.length(); i++) {
                                JSONObject point = features.getJSONObject(i);
                                JSONArray pointCoordinates = point.getJSONArray("PointCoordinate");
                                Double[] coordinates = new Double[2];

                                for(int k = 0; k < pointCoordinates.length(); k++){
                                    coordinates[k] = pointCoordinates.getDouble(k);
                                }

                                JSONArray camerasArray = point.getJSONArray("Cameras");
                                for (int j = 0; j < camerasArray.length(); j++) {
                                    JSONObject camera = camerasArray.getJSONObject(j);
                                    Camera trafficCam = new Camera();
                                    trafficCam.setDescription(camera.getString("Description"));
                                    String imageURL = camera.getString("ImageUrl");
                                    String type = camera.getString("Type");
                                    if (type.equals("sdot"))
                                        trafficCam.setImageURL( "https://www.seattle.gov/trafficcams/images/" + imageURL);
                                    else {
                                        trafficCam.setImageURL( "https://images.wsdot.wa.gov/nw/" + imageURL);
                                    }
                                    trafficCam.setCoordinates(coordinates);
                                    cameraLocations.add(trafficCam);
                                }
                            }
                            Log.d("camCount", String.valueOf(cameraLocations));
                            showMarkers();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> Log.d("JSON", "Error: " + error.getMessage()));
            queue.add(objectRequest);
        } else {
            Toast.makeText(MapActivity.this, "The Network Connection Could NOT Be Established!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true; }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getDeviceLocation() {
        try {
            if (permissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                Log.d("location", String.valueOf(locationResult));
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            Log.d("Location", String.valueOf(lastKnownLocation));
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude() ), 12));
                            Log.d("Location", String.valueOf(defaultLocation));
                            gMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()))
                                    .title("Current position")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));}
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        gMap.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, 12));
                        gMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Security Exception: %s", e.getMessage(), e);
        }
    }

    private void updateLocationUI() {
        if (gMap == null) {
            return; }
        try {
            if (permissionGranted) {
                gMap.setMyLocationEnabled(true);
                gMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                gMap.setMyLocationEnabled(false);
                gMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Security Exception: %s", e.getMessage());
        }
    }
}