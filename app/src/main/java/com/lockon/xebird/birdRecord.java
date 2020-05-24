package com.lockon.xebird;

public class birdRecord {
    private String birdName;
    private int birdCount;
    private String birdComments;
    private float birdLatitude, birdLongitude;

    public birdRecord(String bname, int bcount, String bcomments){
        birdName=bname;
        birdCount=bcount;
        birdComments=bcomments;
    }

    public String getBirdName(){
        return birdName;
    }

    public int getBirdCount(){
        return birdCount;
    }

    public int editBirdCount(int bcount){
        birdCount=bcount;
        return birdCount;
    }

    public String getBirdComments(){
        return birdComments;
    }

    public String editBirdComments(String bcomments){
        birdComments=bcomments;
        return birdComments;
    }
}
