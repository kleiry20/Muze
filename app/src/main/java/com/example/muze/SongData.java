package com.example.muze;

public class SongData {

    String songname;
    String songartist;
    String imageurl;

    // Mandatory empty constructor
    // for use of FirebaseUI
    public SongData() {
    }

    //constructor
    public SongData(String songname, String songartist, String imageurl) {
        this.songname = songname;
        this.songartist = songartist;
        this.imageurl = imageurl;
    }



    // Getter and setter methods

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getSongartist() {
        return songartist;
    }

    public void setSongartist(String songartist) {
        this.songartist = songartist;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

}