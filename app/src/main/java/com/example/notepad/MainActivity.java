package com.example.notepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    ListView note_list;
    List<Map<String, Object>> datalist;
    NoteHelper noteHelper;
    SQLiteDatabase db;
    Button btnadd;
    EditText et_user ,et_pwd;
    private String etId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //初始化listiew列表V，使用什么适配器 simpleadapter
        note_list = findViewById(R.id.lv_nodeList);
        btnadd = findViewById(R.id.btn_add);

        btnadd.setOnClickListener(this);
        //给listview设置点击以及长按事件
        note_list.setOnItemClickListener(this);
        note_list.setOnItemLongClickListener(this);


        //封装好datalist
        //从sqlite中读取备忘录内容,只需要读取不需要操作数据库
        noteHelper = new NoteHelper(this);
        //初始化datalist
        datalist = new ArrayList<Map<String, Object>>();
        //可读
        db = noteHelper.getReadableDatabase();
        //查询（表名 ， 字段（为空则表示查询全部），返回行 ， ,排序方式）
        Cursor cursor = db.query("note", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String cont = cursor.getString(cursor.getColumnIndex("content"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            //把得到的数据写入datalist’
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("content", cont);
            map.put("date", date);
            //将map写入datelist
            datalist.add(map);
        }

        //创建simpleAdapter
        SimpleAdapter adapter = new SimpleAdapter(this, datalist, R.layout.note_item, new String[]{"content", "date"}, new int[]{R.id.tv_content, R.id.tv_date});
        //将适配器给到listview
        note_list.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                //activity实现页面跳转
                Intent intent = new Intent(this, NoteActivity.class);
                //判断按钮未添加 还是修改 判断edittext 是否为空
                startActivity(intent);


        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            //提示框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除备忘录");
        builder.setMessage("确定删除备忘录？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
        return true;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //获取当前项内容并且跳转到另一个页面实现编辑功能
        String content = note_list.getItemIdAtPosition(position)+ "";
        //截取字符串的方式，substr()
//        content = content.substring();
        Intent intent = new Intent(this, NoteActivity.class);
        //通过intent把内容传到另一个页面
        //intent.putExtra()
        intent.putExtra("content",content);
        startActivity(intent);
    }


    //登录
    @SuppressLint("WrongConstant")
    public void login(View view) {
        // 给他一个意图跳转到欢迎界面WellComeActivity
        Intent intent = new Intent(getApplicationContext(), NoteActivity.class);

        // 在activity中要获取xml文件某按钮或者EditText就用findViewById(R.id.xml文件中对应的id)
        EditText editText = (EditText) findViewById(R.id.et_user);
        EditText editText2 = (EditText) findViewById(R.id.et_pwd);

        // 获取到输入框的内容
        String user = editText.getText().toString();
        String pwd = editText2.getText().toString();
        UserService service = new UserService(getApplicationContext());
        // this.getApplicationContext（）取的是这个应 用程序的（Context）上下文
        // getApplicationContext() 返回应用的上下文，生命周期是整个应用，应用摧毁它才摧毁
        // List集合， 里面存的是Person类型的对象，和数组类似，但是他的空间不是固定的，自增长
        List<javaBean> list = service.findbysql("");
        if (!user.equals("")) {
            if (!pwd.equals("")) {
                if (list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        String user1 = list.get(i).getUser();
                        String pwd1 = list.get(i).getPassword();
                        if (user.equals(user1)) {
                            if (pwd.equals(pwd1)) {
                                intent.putExtra("user", user);// putExtra
                                // 只是用来设定各种不同类型的附加数据
                                startActivity(intent);// Intent用于告诉系统，我们要做“什么”动作，
                                Toast.makeText(getApplicationContext(), "登录成功",
                                        1).show();
                                break;
                            } else {
                                Toast.makeText(getApplicationContext(), "密码错误",
                                        1).show();// Toast这个语句给出提示窗口
                                break;
                            }
                        } else {
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "本机没有账号，请注册", 1)
                            .show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "请输入密码", 1).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "请输入账号", 1).show();
        }

    }
}
