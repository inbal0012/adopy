package com.example.adopy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adopy.R;
import com.example.adopy.Utilities.MyLocation;
import com.example.adopy.Utilities.Receivers_and_Services.AlarmReceiver;
import com.example.adopy.Utilities.Receivers_and_Services.BootRegisterService;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static com.example.adopy.Utilities.Models.App.ON_BOOT_CHANNEL_ID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "my_MainActivity";

    private static final int FOREGROUND_SERVICE_PERMISSION_REQUEST = 101;
    private static final int LOCATION_PERMISSION_REQUEST = 102;

    private NotificationManagerCompat notificationManager;
    private LocationManager locationManager;
    private AlarmManager alarmManager;

    private PendingIntent alarmIntent;

    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);     //return from splash
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: ");

        //Temp buttons
        TextView userTV = findViewById(R.id.user_TV);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            userTV.setText("hello " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
        else
            userTV.setText("no user connected");

        Button mtPets = findViewById(R.id.myPetsBtn);
        mtPets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyPetsActivity.class));
            }
        });

        Button search = findViewById(R.id.searchBtn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });

        Button saveUpdates = findViewById(R.id.reminderRepeatSave_btn);
        saveUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveUpdates();
            }
        });

        Button cancelUpdatesBtn = findViewById(R.id.reminderRepeatCancel_btn);
        cancelUpdatesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.cancel(pendingIntent);
            }
        });

        Button loginBtn = findViewById(R.id.signinBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SigninActivity.class));
            }
        });

        Button profileBtn = findViewById(R.id.profileBtn);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        Button chatBtn = findViewById(R.id.chatBtn);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChatsActivity.class);
                //intent.putExtra("userid", "Zf0DLpaCtHSP9vEMWly4KQ1bdlU2");
                startActivity(intent);
            }
        });

        Button logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });

        //location permission
        MyLocation myLocation = new MyLocation(this);
        myLocation.getLocation();
    }

    private void SaveUpdates() {
        EditText time = findViewById(R.id.reminderRepeat_et);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // Set the alarm to start now
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // setRepeating() lets you specify a precise custom interval--in this case,
        // user choice in minutes.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * Integer.parseInt(time.getText().toString()) , alarmIntent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == FOREGROUND_SERVICE_PERMISSION_REQUEST) {

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startService(new Intent(this, BootRegisterService.class));
            }
            else {
                Toast.makeText(this, "Sorry, can't work without foreground permission", Toast.LENGTH_LONG).show();
            }
        }
//
//        if(requestCode==LOCATION_PERMISSION_REQUEST) {
//            if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle("Attention").setMessage("The application must have location permission in order for it to work!")
//                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                intent.setData(Uri.parse("package:"+getPackageName()));
//                                startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                finish();
//                            }
//                        }).setCancelable(false).show();
//            }
//        }
    }


    public void sendOnChannel1() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ON_BOOT_CHANNEL_ID)
                .setSmallIcon(R.drawable.foot)
                .setContentTitle(getString(R.string.onBootChannelDesc))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);


        Intent intent = new Intent(MainActivity.this,SearchActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();


        notificationManager.notify(1, notification);
    }

//    //LocationListener funcs START
//    @Override
//    public void onLocationChanged(Location location) {
//
//        final double lat = location.getLatitude();
//        final double lng = location.getLongitude();
//
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//
//                try {
//                    List<Address> addresses = geocoder.getFromLocation(lat,lng,1);
//                    final Address bestAddress = addresses.get(0);
//
//                    Log.d(TAG, "onLocationChanged: run: " + bestAddress.getCountryName()
//                            + " , " + bestAddress.getLocality()
//                            + " , " + bestAddress.getThoroughfare()
//                            + " , " + bestAddress.getSubThoroughfare()
//                            + " , " + bestAddress.getAdminArea());
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }.start();
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
    //LocationListener funcs END
}