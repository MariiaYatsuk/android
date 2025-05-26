package com.example.apitest;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "phone_validation")
public class DB {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "number")
    public String number;

    @ColumnInfo(name = "valid")
    public boolean valid;

    @ColumnInfo(name = "country_name")
    public String countryName;

    @ColumnInfo(name = "carrier")
    public String carrier;

    @ColumnInfo(name = "line_type")
    public String lineType;

    @ColumnInfo(name = "location")
    public String location;
}