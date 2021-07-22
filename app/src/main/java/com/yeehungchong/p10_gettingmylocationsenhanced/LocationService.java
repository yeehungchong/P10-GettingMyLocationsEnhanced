package com.yeehungchong.p10_gettingmylocationsenhanced;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileWriter;

public class LocationService extends Service {

    private FusedLocationProviderClient client;
    private LocationCallback mLocationCallback;
    private String folderLocation;
    boolean started;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d("LocationService", "Service created");
        super.onCreate();
        client = LocationServices.getFusedLocationProviderClient(LocationService.this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (started == false){
            started = true;
            Toast.makeText(LocationService.this, "Detector started", Toast.LENGTH_LONG).show();
            Log.d("LocationService", "Detector started");

            LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setSmallestDisplacement(100);

            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult!= null) {
                        Location data = locationResult.getLastLocation();
                        double lat = data.getLatitude();
                        double lng = data.getLongitude();
                        Toast.makeText(LocationService.this,"New Location Detected\n" + "Lat: " + lat + ", " + " Lat: " + lng, Toast.LENGTH_SHORT).show();
                        Log.d("LocationService", "New Location Detected");

                        folderLocation = getFilesDir().getAbsolutePath() + "/Folder";
                        File targetFile = new File(folderLocation, "data.txt");
                        File folder = new File(folderLocation);
                        if (folder.exists() == false) {
                            boolean result = folder.mkdir();
                            if (result == true) {
                                Log.d("File Read/Write", "Folder created");
                            }
                        }
                        try {
                            FileWriter writer = new FileWriter(targetFile, true);
                            writer.write(lat + ", " + lng + "\n");
                            writer.flush();
                            writer.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            };

            checkPermission();
            client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        } else {
            Toast.makeText(LocationService.this, "Detector already started", Toast.LENGTH_LONG).show();
            Log.d("LocationService", "Detector already started");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(LocationService.this, "Detector stopped", Toast.LENGTH_LONG).show();
        Log.d("LocationService", "Detector stopped");
        super.onDestroy();
        client.removeLocationUpdates(mLocationCallback);
    }

    private boolean checkPermission() {
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED && permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return false;
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode) {
//            case 0: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(LocationService.this, "Permission granted", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(LocationService.this, "Permission not granted", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

}
