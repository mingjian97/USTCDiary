package com.grouptwo.ustcdiary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import com.grouptwo.ustcdiary.topicActivity.MemoEntity;

public class DBHelper extends SQLiteOpenHelper{

    public DBHelper(Context context,String name,CursorFactory factory,int version){
        super(context,name,factory,version);
    }

    //创建备忘录的数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table memo(_id integer primary key autoincrement,content text,isDeleted integer,ordernum integer)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
