package com.example.muze;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


// Now give reference to UI elements
        LinearLayout L1, L2;
        TextView tv;

        Animation DownToTop, Fade;

        // make xml file for animation .....
        // here on tag line we will give fading animation and on linear layout 2 we will give down to top animation
        // make animation folder



            // LAUNCH SCREEN CODE START

            L1 = (LinearLayout) findViewById(R.id.LL1);
            L2 = (LinearLayout) findViewById(R.id.LL2);

            tv = (TextView) findViewById(R.id.tag);

            DownToTop = AnimationUtils.loadAnimation(this, R.anim.downtotop);
            Fade = AnimationUtils.loadAnimation(this, R.anim.fade);

            L2.setAnimation(DownToTop);
            tv.setAnimation(Fade);

            final Intent i = new Intent(MainActivity.this, HomeScreenActivity.class);

            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(5000); //to stay on splash screen  =>>>> sleep for 5 seconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        startActivity(i);
                        finish();
                    }
                }
            };
            thread.start();


            // LAUNCH SCREEN CODE ENDS


        }
    }
