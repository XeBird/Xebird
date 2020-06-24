package com.lockon.xebird.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BirdRecordDao {

    @Query("SELECT * FROM BirdRecord")
    List<BirdRecord> getAllBirdRecord();

    @Query("SELECT * FROM BirdRecord WHERE checklistId == :cid")
    List<BirdRecord> getAllByCid(String cid);

    @Query("SELECT * FROM Checklist")
    List<Checklist> getAllChecklist();

    @Insert
    void insertToBirdRecord(BirdRecord... birdRecord);

    @Insert
    void insertToChecklist(Checklist... checklist);

    @Delete
    void deleteFromBirdRecord(BirdRecord... birdRecords);

    @Delete
    void deleteFromChecklist(Checklist... checklists);

    @Update
    void updateInBirdRecord(BirdRecord... birdRecords);

    @Update
    void updateInChecklist(Checklist... checklists);

}
