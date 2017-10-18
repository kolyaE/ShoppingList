package kolyae318rub.shoplist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateListActivity extends AppCompatActivity {

    public static final int FROM_CREATE_LIST_ACTIVITY = 1;
    final String ATTRIBUTE_IMAGE = "image";
    final String ATTRIBUTE_ITEM = "item";
    public ArrayList<String> shopList = new ArrayList<>();
    ListView listView;
    SimpleAdapter mAdapter;
    EditText editText;
    Map<String, Object> map;
    ArrayList<Map<String, Object>> data;
    int checkedGrey, checkedGreen;
    int checkedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);
        final String ATTRIBUTE_IMAGE = "image";
        final String ATTRIBUTE_ITEM = "item";
        listView = (ListView) findViewById(R.id.listView);
        editText = (EditText) findViewById(R.id.editNewProd);
        checkedCount = 0;

        String[] supArray = getResources().getStringArray(R.array.supArray);
        checkedGrey = R.drawable.ic_cheked;
        checkedGreen = R.drawable.ic_checked_green;
        data = new ArrayList<>(supArray.length);

        for (int i = 0; i < supArray.length; i++) {
            addItem(supArray[i], checkedGrey);
            data.add(map);
        }

        mAdapter = new SimpleAdapter(this, data, R.layout.prod_item, new String[] { ATTRIBUTE_IMAGE, ATTRIBUTE_ITEM },
                new int[] { R.id.checked, R.id.prodListItem });
        listView.setAdapter(mAdapter);

        //Add Item by Enter press
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        addClick(checkedGreen);
                        return true;
                    }
                }
                return false;
            }
        });

        //Choosing products
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SparseBooleanArray chosen = listView.getCheckedItemPositions();
                for (int i = 0; i < chosen.size(); i++) {
                    if (listView.getAdapter().getItem(chosen.keyAt(i)).toString().equals(listView.getItemAtPosition(position).toString())) {
                        String checkedItem = data.get(position).get("item").toString();
                        data.remove(position);
                        map = new HashMap<>();
                        map.put(ATTRIBUTE_ITEM, checkedItem);
                        if (chosen.valueAt(i)) {
                            map.put(ATTRIBUTE_IMAGE, checkedGreen);
                            data.add(0, map);
                            listView.setItemChecked(position, false);
                            listView.setItemChecked(checkedCount, true);
                            checkedCount++;
                        }
                        else {
                            map.put(ATTRIBUTE_IMAGE, checkedGrey);
                            data.add(checkedCount, map);
                            if (position+1 != checkedCount) {
                                listView.setItemChecked(position, true);
                                listView.setItemChecked(checkedCount-1, false);
                            }
                            checkedCount--;
                        }
                        break;
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.DoneList:
                SparseBooleanArray chosen = listView.getCheckedItemPositions();
                for (int i = 0; i < chosen.size(); i++) {
                    if (chosen.valueAt(i)) {
                        shopList.add(data.get(chosen.keyAt(i)).get("item").toString());
                    }
                }
                Intent intentCreateToShop = new Intent(CreateListActivity.this, ShopListActivity.class);
                intentCreateToShop.putExtra("shopArrayList", shopList);
                intentCreateToShop.putExtra("Parent_activity_id", FROM_CREATE_LIST_ACTIVITY);
                startActivity(intentCreateToShop);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onAddClick(View view) {
        addClick(checkedGreen);
    }

    public void addItem(String item, int checkedColor) {
        map = new HashMap<>();
        map.put(ATTRIBUTE_ITEM, item);
        map.put(ATTRIBUTE_IMAGE, checkedColor);
    }

    public void addClick(int checkedColor) {
        String text = editText.getText().toString();
        if (!text.isEmpty()) {
            addItem(text, checkedColor);
            data.add(0, map);
            listView.setItemChecked(0, true);
            checkedCount++;
            listView.setItemChecked(checkedCount - 1, true);
            mAdapter.notifyDataSetChanged();
            editText.setText("");
        }
    }
}
