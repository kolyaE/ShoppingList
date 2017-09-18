package kolyae318rub.shoplist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

public class CreateListActivity extends AppCompatActivity {

    public final int PARENT_ACTIVITY_ID = 1;
    protected ArrayAdapter<String> mAdapter;
    public ArrayList<String> shopList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);
        final ListView listView = (ListView) findViewById(R.id.listView);
        final EditText editText = (EditText) findViewById(R.id.editNewProd) ;
        String[] supArray = getResources().getStringArray(R.array.supArray);

        final ArrayList<String> prodList = new ArrayList<String>();
        for (int i=0; i<supArray.length; i++)
        {
            prodList.add(supArray[i]);
        }
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, prodList);
        listView.setAdapter(mAdapter);

        //Adding products by Enter press
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        shopList.add(editText.getText().toString());
                        mAdapter.notifyDataSetChanged();
                        editText.setText("");
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
                for (int i=0; i<chosen.size(); i++) {
                    if (chosen.valueAt(i)) {
                        String pos = listView.getAdapter().getItem(chosen.keyAt(i)).toString();
                        int count=0;
                        for (int j = 0; j < shopList.size(); j++) {
                            if (pos.equals(shopList.get(j))) {
                                count++;
                                break;
                            }
                        }
                            if (count==0)
                            shopList.add(pos);
                    }
                }
            }
        });
    }

    public void onCreateListClick(View view) {
        Intent intent = new Intent(CreateListActivity.this, ShopListActivity.class);
        intent.putExtra("shopArrayList", shopList);
        intent.putExtra("Parent_activity_id", PARENT_ACTIVITY_ID);
        startActivity(intent);
    }
}
