package com.marknote.android.SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.marknote.android.viewPager.fragment.Bill.billAdapter.Bill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SQLiteUtil {
    private static MyDatabaseHelper dbHelper;
    private static SQLiteDatabase db;
    private static Cursor cursor;


    //静态初始化dbHelper，要在主活动中添加SQLiteUtil.init(this);
    public static void init(Context context) {
        if (dbHelper == null) {
            dbHelper = new MyDatabaseHelper(context.getApplicationContext(), "Note.db", null, 10);
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
    public static void insertBill(String tableName, String time, String money, String type, String comment, String category) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("time", time);
        values.put("money", money);
        values.put("type", type);
        values.put("comment", comment);
        values.put("category", category);
        db.insert(tableName, null, values);
        db.close();

    }

    public static Cursor queryBill(String startTime, String endTime, String comment, String category) {
        // 构建 SQL 查询语句
        StringBuilder sql = new StringBuilder("SELECT * FROM bill WHERE 1=1");
        if (!startTime.isEmpty()) {
            sql.append(" AND time >= '").append(startTime).append("'");
        }
        if (!endTime.isEmpty()) {
            sql.append(" AND time <= '").append(endTime).append("'");
        }
        if (!comment.isEmpty()) {
            sql.append(" AND comment LIKE '%").append(comment).append("%'");
        }
        if (!category.isEmpty()) {
            sql.append(" AND category = '").append(category).append("'");
        }
        db = dbHelper.getWritableDatabase();
        // 执行查询
        return db.rawQuery(sql.toString(), null);
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
    public static int checkBillId(String tableName, String time, String money, String type, String comment) {
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(tableName, null, "time=? AND money=? AND type=? AND comment=?", new String[]{time, money, type, comment}, null, null, null);
        int id = 0;
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

    //获取当天，周，月的支出账单
    public static List<Bill> getPaysForToday() {
        List<Bill> bills = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        String query = "SELECT * FROM Bill WHERE type = '支出' AND time LIKE '" + today + "%'";
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String money = cursor.getString(cursor.getColumnIndex("money"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String comment = cursor.getString(cursor.getColumnIndex("comment"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                bills.add(new Bill(time, money, type, comment, category));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bills;
    }

    public static List<Bill> getPaysForThisWeek() {
        List<Bill> bills = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 将日期设置为周一
        String startOfWeek = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

        calendar.add(Calendar.WEEK_OF_YEAR, 1); // 移动到下周
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); // 将日期设置为周日
        String endOfWeek = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM Bill WHERE type = '支出' AND time BETWEEN '" + startOfWeek + "' AND '" + endOfWeek + "'";
        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                String time = cursor.getString(cursor.getColumnIndex("time"));
                String money = cursor.getString(cursor.getColumnIndex("money"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String comment = cursor.getString(cursor.getColumnIndex("comment"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                bills.add(new Bill(time, money, type, comment, category));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bills;
    }

    public static List<Bill> getPaysForThisMonth() {
        List<Bill> bills = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        String startOfMonth = new SimpleDateFormat("yyyy-MM-01").format(calendar.getTime());
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String endOfMonth = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        String query = "SELECT * FROM Bill WHERE type = '支出' AND time BETWEEN '" + startOfMonth + "' AND '" + endOfMonth + "'";
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String money = cursor.getString(cursor.getColumnIndex("money"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String comment = cursor.getString(cursor.getColumnIndex("comment"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                bills.add(new Bill(time, money, type, comment, category));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bills;
    }

    //获取当天，周，月的收入账单
    public static List<Bill> getIncomesForToday() {
        List<Bill> bills = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        String query = "SELECT * FROM Bill WHERE type = '收入' AND time LIKE '" + today + "%'";
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String money = cursor.getString(cursor.getColumnIndex("money"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String comment = cursor.getString(cursor.getColumnIndex("comment"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                bills.add(new Bill(time, money, type, comment, category));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bills;
    }

    public static List<Bill> getIncomesForThisWeek() {
        List<Bill> bills = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 将日期设置为周一
        String startOfWeek = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

        calendar.add(Calendar.WEEK_OF_YEAR, 1); // 移动到下周
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); // 将日期设置为周日
        String endOfWeek = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM Bill WHERE type = '收入' AND time BETWEEN '" + startOfWeek + "' AND '" + endOfWeek + "'";
        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                String time = cursor.getString(cursor.getColumnIndex("time"));
                String money = cursor.getString(cursor.getColumnIndex("money"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String comment = cursor.getString(cursor.getColumnIndex("comment"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                bills.add(new Bill(time, money, type, comment, category));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bills;
    }

    public static List<Bill> getIncomesForThisMonth() {
        List<Bill> bills = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        String startOfMonth = new SimpleDateFormat("yyyy-MM-01").format(calendar.getTime());
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String endOfMonth = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        String query = "SELECT * FROM Bill WHERE type = '收入' AND time BETWEEN '" + startOfMonth + "' AND '" + endOfMonth + "'";
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String money = cursor.getString(cursor.getColumnIndex("money"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String comment = cursor.getString(cursor.getColumnIndex("comment"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                bills.add(new Bill(time, money, type, comment, category));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bills;
    }

    //Task部分
    //添加事项
    public static void insertTask(String tableName, String startTime, String endTime, String content) {
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

    // 删除事项
    public static void deleteTask(String tableName, String whereClause, String[] whereArgs) {
        db = dbHelper.getWritableDatabase();
        db.delete(tableName, whereClause, whereArgs);
        db.close();
    }

    //修改事项
    public static void updateTask(String tableName, String oldTaskContent, String newTaskContent, String newStartTime, String newEndTime) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", newTaskContent);
        values.put("startTime", newStartTime);
        values.put("endTime", newEndTime);
        db.update(tableName, values, "content=?", new String[]{oldTaskContent});
        db.close();
    }
}