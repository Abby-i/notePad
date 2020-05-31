package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener {
    TextView et_edit;
    Button btn_save, btn_quit;
    //对数据库进行初始化
    NoteHelper noteHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        et_edit = findViewById(R.id.et_edit);
        btn_save = findViewById(R.id.btn_save);
        btn_quit = findViewById(R.id.btn_quit);

        btn_quit.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        noteHelper = new NoteHelper(this);
        db = noteHelper.getWritableDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                //获取edittext中的内容,存入sqlite
                 /*String  content = getIntent().getStringExtra("content");*/
               String  content = et_edit.getText().toString();
                //日期
                Date d = new Date();
                //指定格式
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
                String date = format.format(d);
                //把数据字段写入数据表
                ContentValues contentValues = new ContentValues();
                contentValues.put("content",content);
                contentValues.put("date",date);
                db.insert("note",null,contentValues);
                 //保存后清空可编辑区域
                et_edit.setText(" ");
        }
    }
}
