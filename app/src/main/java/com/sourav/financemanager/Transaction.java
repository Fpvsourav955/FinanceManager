package com.sourav.financemanager;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public double amount;
    public String type; // income / expense
    public String category;
    public String note;
    public long date;

    public Transaction(double amount, String category, long date, String note, String type) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
        this.type = type;
    }
}