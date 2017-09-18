package kolyae318rub.shoplist;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class LoadListActivity extends ListActivity {

    protected ArrayList<String> loadList;
    public final int PARENT_ACTIVITY_ID = 2;
    public static ArrayList<String> listOfLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listOfLists = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.LOAD_TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            listOfLists.add(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_SLOT)));
        }
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listOfLists);
            setListAdapter(mAdapter);
        cursor.close();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        loadList = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase slDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = slDatabase.query(l.getItemAtPosition(position).toString(), null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            loadList.add(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_SLOT)));
        }
        cursor.close();

        Intent intent = new Intent(this, ShopListActivity.class);
        intent.putExtra("savedArrayList", loadList);
        intent.putExtra("Parent_activity_id", PARENT_ACTIVITY_ID);
        startActivity(intent);
    }
}
