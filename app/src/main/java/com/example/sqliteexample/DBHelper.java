package com.example.sqliteexample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "hongdroid.db";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 데이터베이스가 생성이 될 때 호출
        // 데이터베이스 -> 테이블 -> 컬럼 -> 값
        db.execSQL("CREATE TABLE IF NOT EXISTS TodoList (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, content TEXT NOT NULL, writeDate TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    // SELECT (할일 목록을 조회한다)
    @SuppressLint("Range")
    public ArrayList<TodoItem> getTodoList() {
        ArrayList<TodoItem> todoItems = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TodoList ORDER BY writeDate DESC", null);
        if (cursor.getCount() != 0) {
            // 조회된 데이터가 있을 때 내부 수행
            while (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String writeDate = cursor.getString(cursor.getColumnIndex("writeDate"));

                TodoItem todoItem = new TodoItem();
                todoItem.setId(id);
                todoItem.setTitle(title);
                todoItem.setContent(content);
                todoItem.setWriteDate(writeDate);
                todoItems.add(todoItem);
            }
        }
        cursor.close();
        return todoItems;
    }

    // INSERT (할일 목록을 DB에 넣는다)
    public void insertTodo(String _title, String _content, String _writeDate) {
        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("INSERT INTO TodoList (title, content, writeDate) VALUES('" + _title + "', '" + _content + "', '" + _writeDate + "');");
        db.execSQL("INSERT INTO TodoList (title, content, writeDate) VALUES('" + _title + "','" + _content + "','" + _writeDate + "');");
    }

    // UPDATA (할일 목록을 수정한다)
    public void updateTodo(String _title, String _content, String _writeDate, String _beforeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE TodoList SET title='" + _title + "', content='" + _content + "', writeDate='" + _writeDate + "' WHERE writeDate='" + _beforeDate + "'");
    }

    // DELETE (할일 목록을 제거한다)
    public void deleteTodo(String _beforeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM TodoList WHERE writeDate = '" + _beforeDate + "'");
    }
}
