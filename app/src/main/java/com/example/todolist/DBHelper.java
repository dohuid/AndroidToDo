package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper
{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "KimDohui.db";

    public DBHelper(@Nullable Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //데이터 베이스가 생성이 될 때 호출
        //데이터베이스 -> 테이블 -> 컬럼 -> 값
        //id, title 제목 널값 불가, context 널값 가능,d작성 날짜 널값 불가 로 데이터 설정
        db.execSQL("CREATE TABLE IF NOT EXISTS TodoList (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, content TEXT, writeDate TEXT NOT NULL)");
    }

    //SELECT 문 (할일 목록들을 조회)
    public ArrayList<TodoItem> getTodoList() {
        ArrayList<TodoItem> todoItems = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TodoList ORDER BY writeDate DESC", null);
        if(cursor.getCount() != 0) {
            //조회된 데이터가 있을 때 내부 수행
            //데이터를 전부 가져옴, 다음 데이터가 없으면 while문 종료
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String writeDate = cursor.getString(cursor.getColumnIndex("writeDate"));

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onCreate(db);
    }

    //특정 상황에서 데이터를 넣어야 함
    // INSERT 문 (할 일 목록은 DB에 넣음)
    // id 값은 오토로 생략 가능하다. 자동 입력 가능
    public void InsertTodo(String _title, String _content, String _writeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO TodoList(title, content, writeDate) VALUES('" + _title + "', '" + _content + "', '" + _writeDate + "');");
    }

    //UPDATE 문 (할일 목록을 수정한다.)
    public void UpdateTodo(String _title, String _content, String _writeDate, String _beforeDate) {
        SQLiteDatabase db = getWritableDatabase();
        //id가 같으면 갱신해 줌
        db.execSQL("UPDATE TodoList SET title ='" + _title + "', content ='" + _content + "', writeDate ='" + _writeDate + "' WHERE writeDate = '" + _beforeDate + "'");
    }

    //DELETE 문 (할일 목록을 제거한다.)
    public void deleteTodo(String _beforeDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM TodoList WHERE writeDate = '"+ _beforeDate +"'");
    }
}
