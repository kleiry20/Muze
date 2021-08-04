package com.example.muze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Button button1 = (Button) findViewById(R.id.external_storage_button);
        Button button2 = (Button) findViewById(R.id.explore_music_button);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.external_storage_button:
                Intent intent1 = new Intent(this, MediaCollectionActivity.class);
                startActivity(intent1);
                break;
            case R.id.explore_music_button:
                Intent intent2 = new Intent(this, ExploreMusicActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
}

