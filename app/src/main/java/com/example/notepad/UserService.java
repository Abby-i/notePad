package com.example.notepad;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private Context context;

    public UserService(Context context) {
        this.context = context;//上下文
    }

    ////////////////////保存注册
    public void savezc(String user, String pwd) {

        NoteHelper noteHelper = new NoteHelper(context);// 对应NotePadData.java
        //SQLiteDatabase数据存储
        SQLiteDatabase database = noteHelper.getReadableDatabase();
        String sql = "insert into person (user,pwd) values('" + user + "','"
                + pwd + "')";
        database.execSQL(sql);
    }
//定义了一个List类型的参数用来传递值

    public List<javaBean> findbysql(String sql) {
        List<javaBean> list = new ArrayList<javaBean>();
        NoteHelper noteHelper = new NoteHelper(context);

        SQLiteDatabase db = noteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from person ", null);
//////////////////////////////////////cursor游标，移动到下一个
        while (cursor.moveToNext()) {

            String person1 = cursor.getString(cursor.getColumnIndex("user"));
            String pwd1 = cursor.getString(cursor.getColumnIndex("pwd"));
            javaBean personlist = new javaBean(person1, pwd1);
            list.add(personlist);//
        }

        return list;

    }

}
