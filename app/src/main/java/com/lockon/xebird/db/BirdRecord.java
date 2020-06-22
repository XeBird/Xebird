package com.lockon.xebird.db;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "BirdRecord",
        indices = {@Index("checklistId")},
        foreignKeys = @ForeignKey(entity = Checklist.class,
                parentColumns = "uid",
                childColumns = "checklistId",
                onDelete = ForeignKey.CASCADE))
public class BirdRecord {
    @PrimaryKey
    private int uid;
    private int checklistId;
    private String birdName;
    private int birdCount;
    private String birdComments;
    private float birdLatitude;
    private float birdLongitude;

    public BirdRecord() {
    }

    @Ignore
    public BirdRecord(int uid, int checklistId) {
        this.uid = uid;
        this.checklistId = checklistId;
    }

    @Ignore
    public BirdRecord(String bname, int bcount, String bcomments, float latitude, float longitude) {
        birdName = bname;
        birdCount = bcount;
        birdComments = bcomments;
        birdLatitude = latitude;
        birdLongitude = longitude;
    }

    //以下全是gettrt和setter
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getBirdName() {
        return birdName;
    }

    public void setBirdName(String birdName) {
        this.birdName = birdName;
    }

    public int getBirdCount() {
        return birdCount;
    }

    public void setBirdCount(int birdCount) {
        this.birdCount = birdCount;
    }

    public String getBirdComments() {
        return birdComments;
    }

    public void setBirdComments(String birdComments) {
        this.birdComments = birdComments;
    }

    public float getBirdLatitude() {
        return birdLatitude;
    }

    public void setBirdLatitude(float birdLatitude) {
        this.birdLatitude = birdLatitude;
    }

    public float getBirdLongitude() {
        return birdLongitude;
    }

    public void setBirdLongitude(float birdLongitude) {
        this.birdLongitude = birdLongitude;
    }

    public int getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(int checklistId) {
        this.checklistId = checklistId;
    }
}
