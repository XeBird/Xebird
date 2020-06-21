package com.lockon.xebird;

public class birdRecord {
    private String birdName;
    private int birdCount;
    private String birdComments;
    private float birdLatitude, birdLongitude;

    public birdRecord(String bname, int bcount, String bcomments, float latitude, float longitude){
        birdName=bname;
        birdCount=bcount;
        birdComments=bcomments;
        birdLatitude=latitude;
        birdLongitude=longitude;
    }

    public String getBirdName(){
        return birdName;
    }

    public int getBirdCount(){
        return birdCount;
    }

    public int setBirdCount(int bcount){
        birdCount=bcount;
        return birdCount;
    }

    public String getBirdComments(){
        return birdComments;
    }

    public String setBirdComments(String bcomments){
        birdComments=bcomments;
        return birdComments;
    }
}
