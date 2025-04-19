package com.marknote.android.SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteUtil {
    private static MyDatabaseHelper dbHelper;
    private static SQLiteDatabase db;
    private static Cursor cursor;


    //静态初始化dbHelper，要在主活动中添加SQLiteUtil.init(this);
    public static void init(Context context) {
        if (dbHelper == null) {
            dbHelper = new MyDatabaseHelper(context.getApplicationContext(), "Note.db", null, 7);
        }
    }

//登录注册部分
    //把注册的账号密码添加到数据库对应的表
    public static void insertData(String tableName, String data1, String data2) {

        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("account", data1);
        values.put("password", data2);
        values.put("remember", 0);
        db.insert(tableName, null, values);
        db.close();
    }

    //在数据库中查询用户注册的账号是否已经存在
    public static boolean isAccountExist(String tableName, String setAccount) {
        db = dbHelper.getWritableDatabase();
        cursor = db.query(tableName, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String existAccount = cursor.getString(cursor.getColumnIndex("account"));
                if (existAccount.equals(setAccount)) {
                    cursor.close();
                    db.close();
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return false;
    }

    //在数据库中查询用户输入的账号密码是否匹配
    public static boolean isMatch(String tableName, String account, String password) {
        db = dbHelper.getWritableDatabase();
        cursor = db.query(tableName, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String existAccount = cursor.getString(cursor.getColumnIndex("account"));
                String existPassword = cursor.getString(cursor.getColumnIndex("password"));
                if (existAccount.equals(account) && existPassword.equals(password)) {
                    cursor.close();
                    db.close();
                    return true;
                }
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return false;
    }

    //把设置为记住密码的用户的remember改为1
    public static void setRemember(String tableName, String account) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("remember", 1);
        db.update(tableName, values, "account=?", new String[]{account});
        db.close();
    }

    //把没有设置为记住密码用户的remember改为0
    public static void setNotRemember(String tableName, String account) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("remember", 0);
        db.update(tableName, values, "account=?", new String[]{account});
        db.close();
    }

    //查找remember为1的账号
    public static String getRememberAccount(String tableName) {
        db = dbHelper.getWritableDatabase();
        cursor = db.query(tableName, new String[]{"account", "remember"}, "remember=1", null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String account = cursor.getString(cursor.getColumnIndex("account"));
            cursor.close();
            db.close();
            return account;
        }
        cursor.close();
        db.close();
        return null;

    }

    //查找remember为1的账号对应的密码
    public static String getRememberPassword(String tableName) {
        db = dbHelper.getWritableDatabase();
        cursor = db.query(tableName, new String[]{"password", "remember"}, "remember=1", null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String password = cursor.getString(cursor.getColumnIndex("password"));
            cursor.close();
            db.close();

            return password;

        }
        cursor.close();
        db.close();
        return null;
    }


    //Bill部分
    //新增账单记录
    public static void insertBill(String tableName, String time, String money, String type, String comment) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("time", time);
        values.put("money", money);
        if (type.equals("支出")) {
            values.put("type", "支出");
        } else if (type.equals("收入")) {
            values.put("type", "收入");
        }
        values.put("comment", comment);
        db.insert(tableName, null, values);
        db.close();

    }

    //查询某天收支情况
    public static Cursor queryBill(String tableName, String time) {
        db = dbHelper.getReadableDatabase();
        String startTime = time + " 00:00:00";
        String endTime = time + " 23:59:59";
        Cursor cursor = db.query(tableName, null, "time BETWEEN ? AND ?", new String[]{startTime, endTime}, null, null, null);
        return cursor;
    }

    //查询某时间段的收支情况
    public static Cursor queryBill(String tableName, String startTime, String endTime) {
        db = dbHelper.getWritableDatabase();
        Cursor cursor=db.query(tableName, null, "time >= ? AND time <= ?", new String[]{startTime, endTime}, null, null, null);

        return cursor;
    }
//获得所有收支情况
    public static Cursor queryBill(String tableName) {
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        return cursor;
    }
    // 删除账单记录
    public static void deleteBill(String tableName, String limit, String[] whereArgs) {
        db = dbHelper.getWritableDatabase();
        db.delete(tableName, limit, whereArgs);
        db.close();
    }
    //找出要修改记录在数据库中的id
    public static int checkBillId(String tableName,String time,String money,String type,String comment){
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(tableName, null, "time=? AND money=? AND type=? AND comment=?", new String[]{time,money,type,comment}, null, null, null);
        int id=0;
        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex("id"));
            db.close();
            return id;
        }
        return id;
    }
    //根据id对数据的记录进行更新
    public static void updateBill(String tableName, int id, String time, String money, String type, String comment) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("time", time);
        values.put("money", money);
        values.put("type", type);
        values.put("comment", comment);
        db.update(tableName, values, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }



    //Task部分
    //添加事项
    public static void insertTask(String tableName, String startTime,String endTime, String content) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("startTime", startTime);
        values.put("endTime", endTime);
values.put("content", content);
        db.insert(tableName, null, values);
        db.close();
    }

    //查找所有事项
    public static Cursor queryTask(String tableName) {
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        return cursor;
    }

    //查找某天的事项
    public static Cursor queryDayTask(String tableName, String date) {
        db = dbHelper.getWritableDatabase();
        String startDate = date + " 00:00:00";
        String endDate = date + " 23:59:59";
        Cursor cursor = db.query(tableName, null, "startTime BETWEEN ? AND ?", new String[]{startDate, endDate}, null, null, null);
        return cursor;
    }

    //查找某月的事项
    public static Cursor queryMonthTask(String tableName, String yearMonth) {
        db = dbHelper.getWritableDatabase();

        // 解析年月（格式：yyyy-MM）
        String[] parts = yearMonth.split("-");
        if (parts.length != 3) {
            return null;
        }

        String year = parts[0];
        String month = parts[1];

        // 构造查询条件：匹配yyyy-MM开头的日期
        String selection = "strftime('%Y-%m', startTime) = ?";
        String[] selectionArgs = new String[]{year + "-" + month};

        return db.query(tableName, null, selection, selectionArgs, null, null, null);
    }
}