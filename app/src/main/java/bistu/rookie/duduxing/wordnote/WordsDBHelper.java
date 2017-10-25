package bistu.rookie.duduxing.wordnote;

// 数据库辅助类

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;



public class WordsDBHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "wordsdb";//数据库名字
    private final static int DATABASE_VERSION = 1;//数据库版本

    //建表SQL
    //表： Words.Word.TABLE_NAME 
    //该表中共4个字段：_ID,COLUMN_NAME_WORD,COLUMN_NAME_MEANING,COLUMN_NAME_SAMPLE
    private final static String SQL_CREATE_DATABASE = "CREATE TABLE " +
            WordsDB.Word.TABLE_NAME + " (" +
            WordsDB.Word._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            WordsDB.Word.COLUMN_NAME_WORD + " TEXT" + "," +
            WordsDB.Word.COLUMN_NAME_MEANING + " TEXT" + ","+
            WordsDB.Word.COLUMN_NAME_SAMPLE + " TEXT" + " )";

    //删表SQL
    private final static String SQL_DELETE_DATABASE = "DROP TABLE IF EXISTS " + WordsDB.Word.TABLE_NAME;

    public WordsDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建数据库
        sqLiteDatabase.execSQL(SQL_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //当数据库升级时被调用，首先删除旧表，然后调用OnCreate()创建新表
        sqLiteDatabase.execSQL(SQL_DELETE_DATABASE);
        onCreate(sqLiteDatabase);
    }
}
