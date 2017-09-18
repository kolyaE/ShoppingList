package kolyae318rub.shoplist;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class ShopListActivity extends AppCompatActivity {

    protected ArrayList<String> shopList;
    protected ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        final ListView listView = (ListView) findViewById(R.id.listView);

        int parentActivityID = getIntent().getIntExtra("Parent_activity_id", 1);
        switch (parentActivityID) {
            case 1:
                shopList = getIntent().getStringArrayListExtra("shopArrayList");
                mAdapter = new ArrayAdapter<>(this, R.layout.list_item, shopList);
                break;
            case 2:
                shopList = getIntent().getStringArrayListExtra("savedArrayList");
                mAdapter = new ArrayAdapter<>(this, R.layout.list_item, shopList);
                break;
            default:
                break;
        }
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ( ! listView.isItemChecked(position)) {
                    listView.setItemChecked(position, false);
                }

              //Dino debugged this
                //  SparseBooleanArray chosen = listView.getCheckedItemPositions();

                if (listView.getCheckedItemCount() == shopList.size()) {
                    Toast.makeText(getApplicationContext(), "You've bought everything from your shoplist!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    public void showDialog() {
        SavingDialog dialog = new SavingDialog();
        dialog.show(getFragmentManager(), "dialog" );
    }

    protected void savingList() {
        String newTableName = SaveNameDialog.slName;
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase slDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValuesList = new ContentValues();
        slDatabase.execSQL("CREATE TABLE " + newTableName + " (" + DBHelper.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBHelper.KEY_SLOT + " TEXT);");

        for (int i=0; i<shopList.size(); i++) {
            contentValues.put(DBHelper.KEY_SLOT, shopList.get(i));
            slDatabase.insert(newTableName, null, contentValues);
        }
        contentValuesList.put(DBHelper.KEY_SLOT, newTableName);
        slDatabase.insert(DBHelper.LOAD_TABLE_NAME, null, contentValuesList);
        slDatabase.close();
    }
}
