package bistu.rookie.duduxing.wordnote;

///数据库操作类

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WordsDBOperator {

    //声明变量
    private static WordsDBHelper words_db_helper;
    private static WordsDBOperator dbOperator;
    private Context context;

    private WordsDBOperator(Context context,WordsDBHelper dbHelper){
        this.context = context;
        this.words_db_helper = dbHelper;
    }

    public static WordsDBOperator getDBOperator(Context context,WordsDBHelper dbHelper){
        return dbOperator == null?new WordsDBOperator(context,dbHelper):dbOperator;
    }

    //在数据库表中插入单词
    public void insertWords(String name,String meaning,String sample){
        Log.d("Debug","insert:"+name);
        String sql = "insert into words(word,meaning,sample)values(?,?,?)";
        SQLiteDatabase db = words_db_helper.getWritableDatabase();
        db.execSQL(sql,new String[]{name,meaning,sample});
        //刷新
        ((MainActivity)context).setWordsListView(getAll());
    }

    //在数据库表中删除单词（未实现）
    public void deleteWords(String name){
        String sql = "delete from words where word = ?";
        SQLiteDatabase db = words_db_helper.getWritableDatabase();
        db.execSQL(sql,new String[]{name});
        ((MainActivity)context).setWordsListView(getAll());
    }

    //在数据库表中更新单词
    public void updateWords(String old_name,String name,String meaning,String sample){
        String sql = "update words set word = ?,meaning = ?,sample = ? where word = ?";
        SQLiteDatabase db = words_db_helper.getWritableDatabase();
        db.execSQL(sql,new String[]{name,meaning,sample,old_name});
        ((MainActivity)context).setWordsListView(getAll());
    }

    /**
     * 当横屏时，点击左侧的单词列表，调用查询方法，查询到指定单词的数据，封装在一个word对象中。
     * @param word_name 待查询的单词名
     * @return 返回一个List里面有查询后的word对象
     */
    public List<Words> query(String word_name){
        List<Words> result = new LinkedList<>();
        SQLiteDatabase db = words_db_helper.getReadableDatabase();
        Words word_item;
        String str_word = "";
        String str_meaning = "";
        String str_sample = "";
        Cursor cursor = db.query(WordsDB.Word.TABLE_NAME,new String[]{WordsDB.Word.COLUMN_NAME_WORD,WordsDB.Word.COLUMN_NAME_MEANING,WordsDB.Word.COLUMN_NAME_SAMPLE},WordsDB.Word.COLUMN_NAME_WORD+"=?",new String[]{word_name},null,null,null);
        while (cursor.moveToNext()){
            str_word = cursor.getString(cursor.getColumnIndex(WordsDB.Word.COLUMN_NAME_WORD));
            str_meaning = cursor.getString(cursor.getColumnIndex(WordsDB.Word.COLUMN_NAME_MEANING));
            str_sample = cursor.getString(cursor.getColumnIndex(WordsDB.Word.COLUMN_NAME_SAMPLE));
            word_item = new Words(str_word,str_meaning,str_sample);
            result.add(word_item);
        }
        return result;
    }

    /**
     * 模糊查询，输入单词的部分能够查询到单词
     * @param searchContent 查询的单词的部分
     * @return
     */
    public List<Map<String,String>> likeQuery(String searchContent){
        List<Map<String,String>> result = new LinkedList<>();
        SQLiteDatabase db = words_db_helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+WordsDB.Word.TABLE_NAME+" where "+WordsDB.Word.COLUMN_NAME_WORD+" like ?",new String[]{"%"+searchContent+"%"});
        Map<String,String> item;
        while(cursor.moveToNext()){
            item = new HashMap<>();
            String word = cursor.getString(cursor.getColumnIndex(WordsDB.Word.COLUMN_NAME_WORD));
            String meaning = cursor.getString(cursor.getColumnIndex(WordsDB.Word.COLUMN_NAME_MEANING));
            String sample = cursor.getString(cursor.getColumnIndex(WordsDB.Word.COLUMN_NAME_SAMPLE));
            item.put(WordsDB.Word.COLUMN_NAME_WORD,word);
            item.put(WordsDB.Word.COLUMN_NAME_MEANING,meaning);
            item.put(WordsDB.Word.COLUMN_NAME_SAMPLE,sample);
            result.add(item);
        }
        return result;
    }

    public void deleteAll(){
        String sql = "delete from words";
        SQLiteDatabase db = words_db_helper.getWritableDatabase();
        db.execSQL(sql);
        ((MainActivity)context).setWordsListView(getAll());
    }

    /**
     * 从数据库中查询到所有数据，并且封装在List<Map>中
     * @return result
     */
    public List<Map<String,String>> getAll(){
        List<Map<String,String>> items = new LinkedList<>();
        SQLiteDatabase sqldb = words_db_helper.getReadableDatabase();
        String order = WordsDB.Word.COLUMN_NAME_WORD+" ASC";
        Cursor c = sqldb.query(WordsDB.Word.TABLE_NAME,new String[]{WordsDB.Word.COLUMN_NAME_WORD, WordsDB.Word.COLUMN_NAME_MEANING, WordsDB.Word.COLUMN_NAME_SAMPLE},null,null,null,null,order);
        String str_word = "";
        String str_meaning = "";
        String str_sample = "";
        Map<String,String> map_item;
        while (c.moveToNext()){
            map_item = new HashMap<>();
            str_word = c.getString(c.getColumnIndex(WordsDB.Word.COLUMN_NAME_WORD));
            str_meaning = c.getString(c.getColumnIndex(WordsDB.Word.COLUMN_NAME_MEANING));
            str_sample = c.getString(c.getColumnIndex(WordsDB.Word.COLUMN_NAME_SAMPLE));
            map_item.put(WordsDB.Word.COLUMN_NAME_WORD,str_word);
            map_item.put(WordsDB.Word.COLUMN_NAME_MEANING,str_meaning);
            map_item.put(WordsDB.Word.COLUMN_NAME_SAMPLE,str_sample);
            items.add(map_item);
        }
        return items;
    }

    public void close(){
        words_db_helper.close();
    }
}
