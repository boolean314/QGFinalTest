package com.marknote.android.SQLiteDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    //新建一个表存放账号密码
    public static final String CREATE_ACCOUNTANDPASSWORD = "create table AccountAndPassword ("
            + "id integer primary key autoincrement,"
            + "account text,"
            + "password text,"
            + "remember Integer)";
    public static final String CREATE_BILL = "create table Bill ("
            +"id integer primary key autoincrement,"
            +"time DATETIME,"
            +"money text,"
            +"type text,"
            +"comment text,"
            +"category text)";
    public static final String CREATE_TASK = "create table Task ("
            +"id integer primary key autoincrement,"
            +"startTime DATETIME,"
            +"endTime DATETIME,"
            +"content text)";



    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACCOUNTANDPASSWORD);
        db.execSQL(CREATE_BILL);
        db.execSQL(CREATE_TASK);
        Toast.makeText(mContext, "数据库成功创建", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
if(oldVersion<10){
     {
        db.execSQL("drop table if exists Bill");
        db.execSQL(CREATE_BILL);
        Toast.makeText(mContext, "Bill 表更新成功", Toast.LENGTH_SHORT).show();
    }

}
    }
}
