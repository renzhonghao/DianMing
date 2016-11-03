package com.example.administrator.test;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.administrator.test.Person;

public class MainActivity extends Activity {

    List<Person> personList;
    MyOpenHelper mOpenHelper;
    SQLiteDatabase db;
    MyAdapter myAdapter;
    ListView lv;
    Spinner spinner;
    Button bu_add;
    AlertDialog alertDialog;
    //View addView;
    EditText e_id,e_name,e_banji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context c = this;
        spinner = (Spinner) findViewById(R.id.spinner);
        createspinner();
//        LayoutInflater la=LayoutInflater.from(this);
//        addView=la.inflate(R.layout.add,null);
//        e_id=(EditText)addView.findViewById(R.id.edit_id) ;
//        e_name=(EditText)addView.findViewById(R.id.edit_name) ;
//        e_banji=(EditText)addView.findViewById(R.id.edit_banji) ;

        ImageView image1 = (ImageView) findViewById(R.id.imageView);
//        image1.setImageResource(R.drawable.ic_launcher);
        lv = (ListView) findViewById(R.id.listView);
        lv.setOnCreateContextMenuListener(listviewLongPress);
        personList = new ArrayList<Person>();
        // 创建MyOpenHelper实例
        mOpenHelper = new MyOpenHelper(this);
        // 得到数据库
        db = mOpenHelper.getWritableDatabase();
        // 插入数据
        Insert();
//        alertDialog= new Dialog(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                //Query(spinner.getSelectedItem().toString());
                //Toast.makeText(MainActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                personList.clear();
                Query(spinner.getSelectedItem().toString());
                // 创建MyAdapter实例
                myAdapter = new MyAdapter(c);
                // 向listview中添加Adapter
                myAdapter.notifyDataSetChanged();
                lv.setAdapter(myAdapter);
                //Toast.makeText(MainActivity.this, "触发结束", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        bu_add = (Button) findViewById(R.id.bu_add);
        //bu_add.setOnClickListener(li);

        bu_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View addView;
                LayoutInflater la=LayoutInflater.from(c);
                addView=la.inflate(R.layout.add,null);
                e_id=(EditText)addView.findViewById(R.id.edit_id) ;
                e_name=(EditText)addView.findViewById(R.id.edit_name) ;
                e_banji=(EditText)addView.findViewById(R.id.edit_banji) ;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(null);
                builder.
                        setTitle("添加学生信息").
                        setIcon(R.drawable.ic_launcher).setView(addView).
                        setPositiveButton("保存", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    db.execSQL("insert into person(_id,name,banji) values (?,?,?)", new Object[]{e_id.getText(), e_name.getText(), e_banji.getText()});
                                    personList.clear();
                                    Query(spinner.getSelectedItem().toString());
                                    // 创建MyAdapter实例
                                    //myAdapter = new MyAdapter(c);
                                    // 向listview中添加Adapter
                                    myAdapter.notifyDataSetChanged();
                                    try {
                                        Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                        field.setAccessible(true);
                                        field.set(dialog, false);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(getApplicationContext(), "信息保存成功", Toast.LENGTH_SHORT).show();
                                    e_banji.setText("");
                                    e_name.setText("");
                                    e_id.setText("");
                                    //lv.setAdapter(myAdapter);
                                }
                                catch(Exception e){
                                    Toast.makeText(getApplicationContext(), "信息输入错误", Toast.LENGTH_SHORT).show();
                                }
                                //dialog.dismiss();
                                //alertDialog.dismiss();

                            }
                        }).setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        try {
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            field.set(dialog, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).setNeutralButton("清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        e_banji.setText("");
                        e_name.setText("");
                        e_id.setText("");
                        try {
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            field.set(dialog, false);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).create().show();

            }
        });
    }
//    View.OnClickListener li=new View.OnClickListener(){

//        @Override
//        public void onClick(View v) {
//            View addView;
//            LayoutInflater la=LayoutInflater.from(this);
//            addView=la.inflate(R.layout.add,null);
//            e_id=(EditText)addView.findViewById(R.id.edit_id) ;
//            e_name=(EditText)addView.findViewById(R.id.edit_name) ;
//            e_banji=(EditText)addView.findViewById(R.id.edit_banji) ;
//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//            builder.setView(null);
//            builder.
//                    setTitle("添加学生信息").
//                    setIcon(R.drawable.ic_launcher).setView(addView).
//                    setPositiveButton("保存", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            try{
//                                db.execSQL("insert into person(_id,name,banji) values (?,?,?)", new Object[]{e_id.getText(), e_name.getText(), e_banji.getText()});
//                                personList.clear();
//                                Query(spinner.getSelectedItem().toString());
//                                // 创建MyAdapter实例
//                                //myAdapter = new MyAdapter(c);
//                                // 向listview中添加Adapter
//                                myAdapter.notifyDataSetChanged();
//                                try {
//                                    Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
//                                    field.setAccessible(true);
//                                    field.set(dialog, false);
//
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                Toast.makeText(getApplicationContext(), "信息保存成功", Toast.LENGTH_SHORT).show();
//                                e_banji.setText("");
//                                e_name.setText("");
//                                e_id.setText("");
//                                //lv.setAdapter(myAdapter);
//                            }
//                            catch(Exception e){
//                                Toast.makeText(getApplicationContext(), "信息输入错误", Toast.LENGTH_SHORT).show();
//                            }
//                            //dialog.dismiss();
//                            //alertDialog.dismiss();
//
//                        }
//                    }).setNegativeButton("取消",new DialogInterface.OnClickListener(){
//                @Override
//                public void onClick(DialogInterface dialog,int which){
//                    try {
//                        Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
//                        field.setAccessible(true);
//                        field.set(dialog, true);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }).setNeutralButton("清空", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    e_banji.setText("");
//                    e_name.setText("");
//                    e_id.setText("");
//                    try {
//                        Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
//                        field.setAccessible(true);
//                        field.set(dialog, false);
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }).create().show();
//
//        }
//    };

    OnCreateContextMenuListener listviewLongPress = new OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenuInfo menuInfo) {
            // TODO Auto-generated method stub
            final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            new AlertDialog.Builder(MainActivity.this)
                    /* 弹出窗口的最上头文字 */
                    .setTitle("删除当前数据")
                    /* 设置弹出窗口的图式 */
                    .setIcon(android.R.drawable.ic_dialog_info)
                    /* 设置弹出窗口的信息 */
                    .setMessage("确定删除当前记录")
                    .setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    // 获取位置索引
                                    //try{
                                    //Toast.makeText(getApplicationContext(), "进入", Toast.LENGTH_SHORT).show();
                                    int mListPos = info.position;
                                    // 获取对应HashMap数据内容
//                                    List<Person> map = personList
//                                            .get(mListPos);
                                    Person map = personList.get(mListPos);
                                    // 获取id
                                    //int id = Integer.valueOf((map.get_id().toString()));
                                    //String str=map.get_id();
                                    //int id = Integer.valueOf((map.get_id().toString()));
                                    // 获取数组具体值后,可以对数据进行相关的操作,例如更新数据
                                    if (Delete(map.get_id())) {
                                        // 移除listData的数据
                                        personList.remove(mListPos);

                                        myAdapter.notifyDataSetChanged();
                                    }
                                }
                            })
                    .setNegativeButton("否",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    // 什么也没做

                                }
                            }).show();
        }
    };

    // 创建MyAdapter继承BaseAdapter
    class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        public MyAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return personList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Person p = personList.get(position);
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.listitem_layout, null);
                viewHolder.txt_id = (TextView) convertView
                        .findViewById(R.id.tv_id);
                viewHolder.txt_name = (TextView) convertView
                        .findViewById(R.id.tv_name);
                viewHolder.txt_banji = (TextView) convertView
                        .findViewById(R.id.tv_class);
                viewHolder.txt_phone = (TextView) convertView
                        .findViewById(R.id.tv_phone);
                viewHolder.txt_salary = (TextView) convertView
                        .findViewById(R.id.tv_salary);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //向TextView中插入数据
            viewHolder.txt_id.setText(p.get_id());
            viewHolder.txt_name.setText(p.getName());
            viewHolder.txt_banji.setText(p.getBanji());
            viewHolder.txt_phone.setText(p.getPhone());
            viewHolder.txt_salary.setText(p.getSalary());

            return convertView;
        }
    }

    class ViewHolder {
        private TextView txt_id;
        private TextView txt_banji;
        private TextView txt_name;
        private TextView txt_phone;
        private TextView txt_salary;
    }

    // 插入数据
    public void Insert() {
        for (int i = 1; i < 10; i++) {
            ContentValues values = new ContentValues();
            values.put("_id", "63140601010" + i);
            values.put("name", "任中豪" + i);
            values.put("banji", "计科一班");
            values.put("salary", "123" + i + i);
            values.put("phone", "151" + i + i);
            db.insert("person", null, values);
        }
        for (int i = 1; i < 10; i++) {
            ContentValues values = new ContentValues();
            values.put("_id", "63140601020" + i);
            values.put("name", "无名氏" + i);
            values.put("banji", "计科二班");
            values.put("salary", "123" + i + i);
            values.put("phone", "151" + i + i);
            db.insert("person", null, values);
        }
    }

    // 查询数据
    public void Query() {
        Cursor cursor = db.query("person", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String _id = cursor.getString(0);
            String name = cursor.getString(1);
            String banji = cursor.getString(2);
            String salary = cursor.getString(3);
            String phone = cursor.getString(4);
            Person person = new Person(_id, name, banji, phone, salary);
            personList.add(person);
        }
    }

    public void Query(String str) {
        String[] selectionArgs = new String[]{str};
        Cursor cursor = db.query("person", null, "banji=?", selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            String _id = cursor.getString(0);
            String name = cursor.getString(1);
            String banji = cursor.getString(2);
            String salary = cursor.getString(3);
            String phone = cursor.getString(4);
            Person person = new Person(_id, name, banji, phone, salary);
            personList.add(person);
        }
    }

    public boolean Delete(String id) {
        //Toast.makeText(getApplicationContext(),"进入数据库",Toast.LENGTH_SHORT).show();
        String whereClause = "_id=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        //db.delete("person",whereClause,whereArgs);
        try {
            db.delete("person", whereClause, whereArgs);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "删除数据库失败",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void createspinner() {
        //Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item); //系统sdk里面的R文件
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("计科一班");
        adapter.add("计科二班");
        adapter.add("计科三班");
        adapter.add("计科四班");

        spinner.setAdapter(adapter);
    }


}
