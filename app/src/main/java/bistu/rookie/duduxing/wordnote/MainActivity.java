package bistu.rookie.duduxing.wordnote;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import android.support.v7.app.AlertDialog;

import bistu.rookie.u_nity.wordnote.R;


public class MainActivity extends AppCompatActivity
        implements WordItemFragment.OnFragmentInteractionListener, View.OnClickListener{

    //声明变量
    public static Handler handler_main;
    public static WordsDBOperator dbOperator;

    private ListView lv_fm_words_list;
    private DialogInflater dialogInflater;
    private Button btn_search;
    private EditText et_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbOperator = WordsDBOperator.getDBOperator(this,new WordsDBHelper(this));
        lv_fm_words_list = (ListView) findViewById(R.id.lv_fm_words_list);
        dialogInflater = new DialogInflater(this);
        registerForContextMenu(lv_fm_words_list);

        //ListView设置数据
        setWordsListView(dbOperator.getAll());
        handler_main = new Handler(Looper.getMainLooper()){

           //有道词典搜索
            @Override
            public void handleMessage(Message msg) {
                //Log.i("world", msg.toString());
                super.handleMessage(msg);

                //Log.i("hello", msg.obj.toString());

                //请求有道词典，输出有道词典搜索结果
                new AlertDialog.Builder(MainActivity.this).setTitle("搜索结果:").setMessage(msg.obj.toString()).setPositiveButton("确定", null).show();
            }
        };

        //控件实例化
        btn_search = (Button) findViewById(R.id.btn_search);
        et_search = (EditText) findViewById(R.id.et_search);
        btn_search.setOnClickListener(this);
    }

    /**
     * 给ListView设置要显示的数据
     * @param items items表示要显示的数据
     */
    public void setWordsListView(List<Map<String,String>> items){
        SimpleAdapter adapter;

        //定义竖屏时的显示方式
        if (getResources().getConfiguration().orientation == 1){
            adapter = new SimpleAdapter(this,items,R.layout.words_display_port,new String[]{
                    WordsDB.Word.COLUMN_NAME_WORD,
                    WordsDB.Word.COLUMN_NAME_MEANING,
                    WordsDB.Word.COLUMN_NAME_SAMPLE},
                    new int[]{R.id.tv_fm_words_details_word,R.id.tv_fm_words_details_meaning,R.id.tv_fm_words_details_sample});
        }
        else{
            //定义横屏时的显示方式
            adapter = new SimpleAdapter(this,items,R.layout.words_display_land,new String[]{
                    WordsDB.Word.COLUMN_NAME_WORD},
                    new int[]{R.id.tv_fm_words_details_word});
        }
        lv_fm_words_list.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbOperator.close();
    }

    //加载菜单项：增加、有道单词搜索、刷新单词列表
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;
    }

    //菜单选项按钮：添加、有道、刷新单词列表
    //逻辑判断
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_add:
                dialogInflater.inflateAddDialog();
                break;
            case R.id.menu_refresh:
                setWordsListView(dbOperator.getAll());
                break;
            case R.id.menu_youdao:
                dialogInflater.inflateSearchDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //功能未实现：编辑、删除
    //编辑和删除列表中的单词
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.context_menu_edit:
                dialogInflater.inflateEditDialog(item);
                if (getResources().getConfiguration().orientation == 1)
                    setWordsListView(dbOperator.getAll());
                break;
            case R.id.context_menu_del:
                deleteSelectedWord(item);
                break;
        }
        return super.onContextItemSelected(item);
    }

    //功能未实现：编辑、删除
    private void deleteSelectedWord(MenuItem item){
        String str_delete_word;
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        View view = info.targetView;
        str_delete_word = ((TextView)view.findViewById(R.id.tv_fm_words_details_word)).getText().toString();
        dbOperator.deleteWords(str_delete_word);
        setWordsListView(dbOperator.getAll());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //菜单布局文件：删除、更改
        getMenuInflater().inflate(R.menu.menu_context,menu);
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * 点击搜索，能够进行模糊查找
     * @param v
     */
    //从在单词列表中搜索单词
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search:
                String str_search = et_search.getText().toString();
                if (!str_search.equals(""))
                    setWordsListView(dbOperator.likeQuery(str_search));
                else
                    setWordsListView(dbOperator.getAll());
                break;
        }
    }

}