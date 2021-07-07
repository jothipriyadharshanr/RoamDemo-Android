package com.roam.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.roam.sdk.Roam;
import com.roam.sdk.RoamPublish;
import com.roam.sdk.RoamTrackingMode;
import com.roam.sdk.callback.RoamCallback;
import com.roam.sdk.callback.RoamTripCallback;
import com.roam.sdk.models.RoamError;
import com.roam.sdk.models.RoamUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnStartTracking, btnStopTracking,btnStartTrip,btnPauseTrip,btnResumeTrip,btnStopTrip;
    private EditText edtTripId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Roam.disableBatteryOptimization();
        btnStartTracking = findViewById(R.id.btnStartTracking);
        btnStopTracking = findViewById(R.id.btnStopTracking);

        edtTripId = findViewById(R.id.edtTripId);
        btnStartTrip = findViewById(R.id.btnStartTrip);
        btnPauseTrip = findViewById(R.id.btnPauseTrip);
        btnResumeTrip = findViewById(R.id.btnResumeTrip);
        btnStopTrip = findViewById(R.id.btnStopTrip);

        btnStartTracking.setOnClickListener(this);
        btnStopTracking.setOnClickListener(this);

        btnStartTrip.setOnClickListener(this);
        btnPauseTrip.setOnClickListener(this);
        btnResumeTrip.setOnClickListener(this);
        btnStopTrip.setOnClickListener(this);
        Roam.notificationOpenedHandler(getIntent());
        createUser();
        Roam.allowMockLocation(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartTracking:
                tracking();
                break;
            case R.id.btnStopTracking:
                stopTracking();
                break;

            case R.id.btnStartTrip:
               startTrip();
                break;
            case R.id.btnPauseTrip:
               pauseTrip();
                break;
            case R.id.btnResumeTrip:
               resumeTrip();
                break;
            case R.id.btnStopTrip:
               stopTrip();
                break;
        }
    }

    private void startTrip() {
        if (TextUtils.isEmpty(edtTripId.getText().toString())) {
            Toast.makeText(this, "Enter tripId", Toast.LENGTH_SHORT).show();
        }
        else
        {
           Roam.startTrip(edtTripId.getText().toString(), null, new RoamTripCallback() {
               @Override
               public void onSuccess(String s) {
                   Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
               }

               @Override
               public void onFailure(RoamError roamError) {
                   Log.e("TAG", "onStartTripFailure: "+roamError.getMessage() );
               }
           });
        }

    }

    private void pauseTrip() {
        if (TextUtils.isEmpty(edtTripId.getText().toString())) {
            Toast.makeText(this, "Enter tripId", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Roam.pauseTrip(edtTripId.getText().toString(), new RoamTripCallback() {
                @Override
                public void onSuccess(String s) {
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(RoamError roamError) {
                    Log.e("TAG", "onPauseTripFailure: "+roamError.getMessage() );
                }
            });
        }
    }

    private void resumeTrip() {
        if (TextUtils.isEmpty(edtTripId.getText().toString())) {
            Toast.makeText(this, "Enter tripId", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Roam.resumeTrip(edtTripId.getText().toString(), new RoamTripCallback() {
                @Override
                public void onSuccess(String s) {
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(RoamError roamError) {
                    Log.e("TAG", "onResumeTripFailure: "+roamError.getMessage() );
                }
            });
        }
    }

    private void stopTrip() {
        if (TextUtils.isEmpty(edtTripId.getText().toString())) {
            Toast.makeText(this, "Enter tripId", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Roam.stopTrip(edtTripId.getText().toString(),  new RoamTripCallback() {
                @Override
                public void onSuccess(String s) {
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(RoamError roamError) {
                    Log.e("TAG", "onStopTripFailure: "+roamError.getMessage() );
                }
            });
        }
    }

    private void createUser() {
        Roam.createUser("User1", new RoamCallback() {
            @Override
            public void onSuccess(RoamUser roamUser) {
                Log.e("TAG", "onSuccess: "+ roamUser.getUserId() );


            }

            @Override
            public void onFailure(RoamError roamError) {
                Log.e("TAG", "onFailure: "+roamError.getMessage() );
            }
        });
    }

    private void toggleEvents() {
        Roam.toggleEvents(true, true, true, true, new RoamCallback() {
            @Override
            public void onSuccess(RoamUser roamUser) {
                Log.e("TAG", "onSuccessToggle: "+roamUser.getLocationEvents() );

                toggleListener();
            }

            @Override
            public void onFailure(RoamError roamError) {
                Log.e("TAG", "onToggleFailure: "+roamError.getMessage() );
            }
        });
    }

    private void toggleListener() {
        Roam.toggleListener(true, true, new RoamCallback() {
            @Override
            public void onSuccess(RoamUser roamUser) {
                Log.e("TAG", "onListenerSuccess: "+roamUser.getEventListenerStatus() );
                Roam.subscribe(Roam.Subscribe.LOCATION, roamUser.getUserId());
                RoamPublish roamPublish = new RoamPublish.Builder()
                        .build();
                Roam.publishAndSave(roamPublish);
            }

            @Override
            public void onFailure(RoamError roamError) {
                Log.e("TAG", "onListenerFailure: "+roamError.getMessage() );
            }
        });
    }

    private void tracking() {
        toggleEvents(); // enabling events and listener before start tracking

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkPermissionsQ();
        } else {
            checkPermissions();
        }
    }

    private void checkPermissions(){
        if (!Roam.checkLocationPermission()) {
            Roam.requestLocationPermission(this);
        } else if (!Roam.checkLocationServices()) {
            Roam.requestLocationServices(this);
        } else {
            startTracking();
        }
    }

    private void checkPermissionsQ() {
        if (!Roam.checkLocationPermission()) {
            Roam.requestLocationPermission(this);
        } else if (!Roam.checkBackgroundLocationPermission()) {
            Roam.requestBackgroundLocationPermission(this);
        } else if (!Roam.checkLocationServices()) {
            Roam.requestLocationServices(this);
        } else {
            startTracking();
        }
    }

    private void startTracking() {
        // Define a custom tracking method with desired time interval and accuracy
        RoamTrackingMode roamTrackingMode = new RoamTrackingMode.Builder(2, 30)
                .setDesiredAccuracy(RoamTrackingMode.DesiredAccuracy.HIGH)
                .build();
        // Start the tracking with the above created custom tracking method
        Roam.startTracking(roamTrackingMode);
        trackingStatus();
    }

    private void stopTracking() {
        Roam.stopTracking();
        trackingStatus();
    }

    private void trackingStatus() {
        if (Roam.isLocationTracking()) {
            startService(new Intent(this, ForegroundService.class));
            btnStartTracking.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
            btnStopTracking.setBackground(getResources().getDrawable(R.drawable.bg_button_enable));
            btnStartTracking.setEnabled(false);
            btnStopTracking.setEnabled(true);
        } else {
            stopService(new Intent(this, ForegroundService.class));
            btnStartTracking.setBackground(getResources().getDrawable(R.drawable.bg_button_enable));
            btnStopTracking.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
            btnStartTracking.setEnabled(true);
            btnStopTracking.setEnabled(false);
        }
    }
}