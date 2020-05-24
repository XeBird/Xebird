package com.lockon.xebird.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "MainDATA")
public class BirdData {
    @PrimaryKey
    @ColumnInfo(name = "ID")
    private int uid;
    @ColumnInfo(name = "NAME_CN")
    private String nameCN;
    @ColumnInfo(name = "NAME_EN")
    private String nameEN;
    @ColumnInfo(name = "NAME_LA")
    private String nameLA;
    @ColumnInfo(name = "NAME_P")
    private String nameP;
    @ColumnInfo(name = "NAME_POP")
    private String namePop;
    @ColumnInfo(name = "MAIN_INFO")
    private String mainInfo;
    @ColumnInfo(name = "ORDER_CN")
    private String orderCN;
    @ColumnInfo(name = "ORDER_EN")
    private String orderEN;
    @ColumnInfo(name = "FAMILY_CN")
    private String famliyCN;
    @ColumnInfo(name = "FAMILY_EN")
    private String familyEN;
    @ColumnInfo(name = "SUBFAMILY_CN")
    private String subfamilyCN;
    @ColumnInfo(name = "SUBFAMILY_EN")
    private String subfamilyEN;
    @ColumnInfo(name = "TRIBE_CN")
    private String tribeCN;
    @ColumnInfo(name = "TRIBE_EN")
    private String tribeEN;
    @ColumnInfo(name = "GENUS_CN")
    private String genusCN;
    @ColumnInfo(name = "TWEET_INFO")
    private String tewwtInfo;
    @ColumnInfo(name = "RANGE_INFO")
    private String rangeInfo;
    @ColumnInfo(name = "STATE_INFO")
    private String stateInfo;
    @ColumnInfo(name = "HABIT_INFO")
    private String habitInfo;
    @ColumnInfo(name = "EXP_INFO")
    private String expInfo;
    @ColumnInfo(name = "RECODER")
    private String recoder;
    private BirdData(){}
    @Ignore
    private BirdData(int id){
        this.uid=id;
    }

    public int getUid() {
        return uid;
    }

    public String getNameCN() {
        return nameCN;
    }

    public String getNameEN() {
        return nameEN;
    }

    public String getNameLA() {
        return nameLA;
    }

    public String getNameP() {
        return nameP;
    }

    public String getNamePop() {
        return namePop;
    }

    public String getMainInfo() {
        return mainInfo;
    }

    public String getOrderCN() {
        return orderCN;
    }

    public String getOrderEN() {
        return orderEN;
    }

    public String getFamliyCN() {
        return famliyCN;
    }

    public String getFamilyEN() {
        return familyEN;
    }

    public String getSubfamilyCN() {
        return subfamilyCN;
    }

    public String getSubfamilyEN() {
        return subfamilyEN;
    }

    public String getTribeCN() {
        return tribeCN;
    }

    public String getTribeEN() {
        return tribeEN;
    }

    public String getGenusCN() {
        return genusCN;
    }

    public String getTewwtInfo() {
        return tewwtInfo;
    }

    public String getRangeInfo() {
        return rangeInfo;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public String getHabitInfo() {
        return habitInfo;
    }

    public String getExpInfo() {
        return expInfo;
    }

    public String getRecoder() {
        return recoder;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setNameCN(String nameCN) {
        this.nameCN = nameCN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public void setNameLA(String nameLA) {
        this.nameLA = nameLA;
    }

    public void setNameP(String nameP) {
        this.nameP = nameP;
    }

    public void setNamePop(String namePop) {
        this.namePop = namePop;
    }

    public void setMainInfo(String mainInfo) {
        this.mainInfo = mainInfo;
    }

    public void setOrderCN(String orderCN) {
        this.orderCN = orderCN;
    }

    public void setOrderEN(String orderEN) {
        this.orderEN = orderEN;
    }

    public void setFamliyCN(String famliyCN) {
        this.famliyCN = famliyCN;
    }

    public void setFamilyEN(String familyEN) {
        this.familyEN = familyEN;
    }

    public void setSubfamilyCN(String subfamilyCN) {
        this.subfamilyCN = subfamilyCN;
    }

    public void setSubfamilyEN(String subfamilyEN) {
        this.subfamilyEN = subfamilyEN;
    }

    public void setTribeCN(String tribeCN) {
        this.tribeCN = tribeCN;
    }

    public void setTribeEN(String tribeEN) {
        this.tribeEN = tribeEN;
    }

    public void setGenusCN(String genusCN) {
        this.genusCN = genusCN;
    }

    public void setTewwtInfo(String tewwtInfo) {
        this.tewwtInfo = tewwtInfo;
    }

    public void setRangeInfo(String rangeInfo) {
        this.rangeInfo = rangeInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public void setHabitInfo(String habitInfo) {
        this.habitInfo = habitInfo;
    }

    public void setExpInfo(String expInfo) {
        this.expInfo = expInfo;
    }

    public void setRecoder(String recoder) {
        this.recoder = recoder;
    }
}
