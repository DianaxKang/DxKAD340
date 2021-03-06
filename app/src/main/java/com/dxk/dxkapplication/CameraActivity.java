package com.dxk.dxkapplication;

import android.annotation.SuppressLint;
import android.net.NetworkInfo;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import android.util.Log;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        ArrayList<Camera> cameraList = new ArrayList<>();
        RecyclerView cameraListRecyclerView = findViewById(R.id.cameraListRecyclerView);

        CameraAdapter cameraAdapter = new CameraAdapter(cameraList);
        cameraListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cameraListRecyclerView.setAdapter(cameraAdapter);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected()) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String cameraApiURL = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";
            @SuppressLint("NotifyDataSetChanged") JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, cameraApiURL, null,
                    response -> {
                        try {
                            JSONArray features = response.getJSONArray("Features");

                            for (int i = 0; i < features.length(); i++) {
                                JSONObject point = features.getJSONObject(i);
                                JSONArray pointCoordinates = point.getJSONArray("PointCoordinate");
                                Double[] coordinates = new Double[2];

                                for(int k = 0; k < pointCoordinates.length(); k++) {
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
                                    cameraList.add(trafficCam);
                                }
                            }
                            cameraAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> Log.d("JSON", "Error: " + error.getMessage()));
            queue.add(objectRequest);
        } else {
            Toast.makeText(CameraActivity.this, "Network Connection Could NOT Be Established!", Toast.LENGTH_LONG).show();
        }
    }
}