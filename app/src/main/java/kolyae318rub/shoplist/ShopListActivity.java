package kolyae318rub.shoplist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShopListActivity extends AppCompatActivity {

    private final String ATTRIBUTE_IMAGE = "image";
    private final String ATTRIBUTE_ITEM = "item";
    public static ArrayList<String> shopList;
    public static String status = "";
    SimpleAdapter mAdapter;
    ListView listView;
    ImageView img;
    Map<String, Object> map;
    ArrayList<Map<String, Object>> data;
    int bought_false, bought_true;
    int boughtCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        listView = (ListView) findViewById(R.id.listView);
        img = (ImageView) findViewById(R.id.bought);

        int parentActivityID = getIntent().getIntExtra("Parent_activity_id", 1);
        switch (parentActivityID) {
            case CreateListActivity.FROM_CREATE_LIST_ACTIVITY:
                shopList = getIntent().getStringArrayListExtra("shopArrayList");
                break;
            case MainActivity.FROM_MAIN_ACTIVITY:
                shopList = getIntent().getStringArrayListExtra("savedArrayList");
                break;
            default:
                break;
        }

        bought_false = R.drawable.ic_buy_false;
        bought_true = R.drawable.ic_bought_true;
        data = new ArrayList<>(shopList.size());
        for (int i = 0; i < shopList.size(); i++) {
            map = new HashMap<>();
            map.put(ATTRIBUTE_IMAGE, bought_false);
            map.put(ATTRIBUTE_ITEM, shopList.get(i));
            data.add(map);
        }

        mAdapter = new SimpleAdapter(this, data, R.layout.list_item, new String[] { ATTRIBUTE_IMAGE, ATTRIBUTE_ITEM },
                new int[] { R.id.bought, R.id.bought_item});
        listView.setAdapter(mAdapter);

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
                            map.put(ATTRIBUTE_IMAGE, bought_true);
                            data.add(map);
                            listView.setItemChecked(position, false);
                            boughtCount++;
                            listView.setItemChecked(shopList.size() - boughtCount, true);
                        }
                        else {
                            map.put(ATTRIBUTE_IMAGE, bought_false);
                            data.add(0, map);
                            int uppersidePos = shopList.size() - boughtCount;
                            if (position+1 != uppersidePos) {
                                listView.setItemChecked(position, true);
                                listView.setItemChecked(uppersidePos, false);
                            }
                            boughtCount--;
                        }
                        break;
                    }
                }
                mAdapter.notifyDataSetChanged();

                if (listView.getCheckedItemCount() == shopList.size()) {
                    Toast.makeText(getApplicationContext(), R.string.Everything, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    public void showDialog() {
        status = listView.getCheckedItemCount() + "/" + shopList.size();
        new SavingDialog().show(getFragmentManager(), "dialog" );
    }
}
