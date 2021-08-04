package com.example.muze;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MediaCollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_collection);



        final ListView listView;
        listView =findViewById(R.id.listView);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        Toast.makeText(MediaCollectionActivity.this, "Runtime permission given", Toast.LENGTH_SHORT).show();
                        final ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());//making an array for putting songs from external storage directory
                        String [] items = new String[mySongs.size()]; //showing songs names by using items in our listview
                        for(int i=0; i<mySongs.size(); i++){
                            items[i] = mySongs.get(i).getName().replace( ".mp3", "");   //listing song names by excluding .mp3
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>( MediaCollectionActivity.this,android.R.layout.simple_list_item_1, items);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                Intent intent = new Intent(MediaCollectionActivity.this,PlaySong.class);   //going to play song activity
                                String currentSong = listView.getItemAtPosition(position).toString();
                                intent.putExtra("songList", mySongs);
                                intent.putExtra("currentSong", currentSong);
                                intent.putExtra("position", position);
                                startActivity(intent);  // to start the activity
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }


    public ArrayList<File> fetchSongs(File file) {      //list of all songs
        ArrayList arrayList = new ArrayList();
        File [] songs= file.listFiles();       //listing of all the files in the directory
        if(songs != null){      //for any reason if song not present don't give me error
            for(File myFile:songs){     //fetch me the songs that are given in the songs array
                if(!myFile.isHidden() && myFile.isDirectory()){     //if myFile is a directory and not hidden
                    arrayList.addAll(fetchSongs(myFile));   //fetch me all the song in the directory
                }
                else{
                    if(myFile.getName().endsWith(".mp3")&& !myFile.getName().startsWith(".")){  //songs with .mp3 extension are only fetched from myFile directory
                        arrayList.add(myFile);      //adding songs to media array list
                    }
                }
            }
        }
        return arrayList;
    }

}
