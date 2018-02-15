package com.example.innoz.iotapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Ahn on 2018-01-03.
 */

public class SQLiteService extends SQLiteOpenHelper {

    private String TAG = "SQLiteService";

    private Context context;

    public SQLiteService(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE BLUETOOTH_INFO (");
        sb.append("NUM INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append("ADDRESS TEXT, ");
        sb.append("NAME TEXT, ");
        sb.append("CURRENT_PER INTEGER, ");
        sb.append("MAX_VALUE INTEGER, ");
        sb.append("USER_PER INTEGER); ");
        db.execSQL(sb.toString());
    }

    /**
     * Application의 버전이 올라가서 * Table 구조가 변경되었을 때 실행된다. * @param db * @param oldVersion * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(context, "버전이 올라갔습니다.", Toast.LENGTH_SHORT).show();
        db.execSQL("DROP TABLE BLUETOOTH_INFO;");
        db.close();
    }

    public void testDB() {
        SQLiteDatabase db = getReadableDatabase();
    }

    public void insert(String address, String room_name, int current_per, int max_value) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO BLUETOOTH_INFO(ADDRESS,NAME,CURRENT_PER,MAX_VALUE) VALUES('"+address+"','"+room_name+"','"+current_per+"','"+max_value +"');");
        Log.d(TAG,"INSERT INTO BLUETOOTH_INFO(ADDRESS,NAME,CURRENT_PER,MAX_VALUE) VALUES('"+ address +"','"+ room_name +"','"+ current_per +"','"+ max_value +"');");
        db.close();
    }

    public void update_address(String room_name, int address) {
        SQLiteDatabase db = getWritableDatabase();
        // 방은 같으나 다른 기기로 수정.
        db.execSQL("UPDATE BLUETOOTH_INFO SET ADDRESS=" + address + " WHERE NAME='" + room_name + "';");
        db.close();
    }
    public void update_name(String room_name, int address) {
        SQLiteDatabase db = getWritableDatabase();
        // 주소는 같으나 방 이름 수정.
        db.execSQL("UPDATE BLUETOOTH_INFO SET NAME=" + room_name + " WHERE ADDRESS='" + address + "';");
        db.close();
    }

    public void update_current_val(String room_name, int current_val) {
        SQLiteDatabase db = getWritableDatabase();
        // 방이름 기준 현재 퍼센트 수정
        db.execSQL("UPDATE BLUETOOTH_INFO SET CURRENT_PER=" + current_val + " WHERE NAME='" + room_name + "';");
        db.close();
    }

    public void delete(String room_name) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM BLUETOOTH_INFO WHERE NAME='" + room_name + "';");
        db.close();
    }

    public void delete_all() {
        SQLiteDatabase db = getWritableDatabase();
        // 데이터베이스 모든 정보 삭제.
        db.execSQL("DELETE FROM BLUETOOTH_INFO;");
        db.close();
    }

    public String select_last() {
        SQLiteDatabase db = getWritableDatabase();
        //마지막 번호 읽기
        String result="";
        Cursor cursor = db.rawQuery("SELECT * FROM BLUETOOTH_INFO ORDER BY NUM DESC LIMIT 1", null);
        while (cursor.moveToNext()) {
            result += cursor.getInt(0);
        }
        db.close();
        return  result;
    }

    public String select_first() {
        SQLiteDatabase db = getWritableDatabase();
        //마지막 번호 읽기
        String result="";
        Cursor cursor = db.rawQuery("SELECT * FROM BLUETOOTH_INFO ORDER BY NUM ASC LIMIT 1", null);
        while (cursor.moveToNext()) {
            result += cursor.getInt(0);
        }
        db.close();
        return  result;
    }

    public String get_room_name(int i) {
        SQLiteDatabase db = getWritableDatabase();
        //마지막 번호 읽기
        String result="";
        Cursor cursor = db.rawQuery("SELECT NAME FROM BLUETOOTH_INFO WHERE NUM='"+i+"';", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0);
        }
        db.close();
        return  result;
    }

    public String getDetail_Result(int i) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM BLUETOOTH_INFO WHERE NUM='"+i+"';", null);
        while (cursor.moveToNext()) {
            result += cursor.getInt(0)
                    + "|"
                    + cursor.getString(1)
                    + "|"
                    + cursor.getString(2)
                    + "|"
                    + cursor.getInt(3)
                    + "|"
                    + cursor.getInt(4)
                    + "|";
        }
        return result;
    }



    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM BLUETOOTH_INFO", null);
        while (cursor.moveToNext()) {
            result += cursor.getInt(0)
                    + " 번째 "
                    + cursor.getString(1)
                    + "-주소"
                    + cursor.getString(2)
                    + "-방이름 "
                    + cursor.getInt(3)
                    + "-퍼센트"
                    + cursor.getInt(4)
                    + "-맥스\n";
        }
        return result;
    }
}
