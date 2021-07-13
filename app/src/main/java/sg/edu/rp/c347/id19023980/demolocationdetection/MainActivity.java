package sg.edu.rp.c347.id19023980.demolocationdetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    Button btnGetLastLocation,btnGetLocationUpdate,btnRemoveLocationUpdate;
    FusedLocationProviderClient client;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGetLastLocation = (Button) findViewById(R.id.btnGetLastLocation);
        btnGetLocationUpdate = (Button) findViewById(R.id.btnGetLocationUpdate);
        btnRemoveLocationUpdate = (Button) findViewById(R.id.btnRemoveLocationUpdate);
//        mLocationRequest = LocationRequest.create();
        mLocationRequest = new LocationRequest();
        //
        mLocationCallback = new LocationCallback(){
            public void onLocationResult(LocationResult locationResult){
                if(locationResult != null){
                    Location data = locationResult.getLastLocation();
                    double lat = data.getLatitude();
                    double log = data.getLongitude();
                    Toast.makeText(MainActivity.this,lat + "\n" + log,Toast.LENGTH_SHORT).show();
                }
            };
        };

        btnGetLastLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                client = LocationServices.getFusedLocationProviderClient(MainActivity.this);

                Task<Location> task = client.getLastLocation();
                task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                       // Log.i("check",location.toString());
                        String msg;
                        if (location != null){
                            msg = "Lat : " + location.getLatitude() + " Lng : " + location.getLongitude();
                        }else{
                            msg = "No last known location found";
                        }
                        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnGetLocationUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    mLocationRequest.setInterval(100);
                    mLocationRequest.setFastestInterval(50);
                    mLocationRequest.setSmallestDisplacement(0);
                    client = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                    client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                }

            }
        });
        btnRemoveLocationUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermission()) {
                    client.removeLocationUpdates(mLocationCallback);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                }
            }
        });
    }
    private boolean checkPermission(){
//        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
//                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (
//                permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTE ||
                permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED
                ) {
            return true;
        } else {
//            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},0);
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            return false;
        }
    }


}