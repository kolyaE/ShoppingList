package kolyae318rub.shoplist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import kolyae318rub.shoplist.data.DB;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int FROM_MAIN_ACTIVITY = 2;
    ActionMode mActionMode;
    DB db;
    ListView loadList;
    SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadList = (ListView) findViewById(R.id.listView);

        db = new DB(this);
        db.openDB();
        mAdapter = new SimpleCursorAdapter(this, R.layout.main_activity_list_item, null,
                new String[]{DB.KEY_LIST, DB.KEY_STATUS}, new int[]{R.id.listName, R.id.progress}, 0);
        loadList.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(0, null, this);

        loadList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentToShop = new Intent(MainActivity.this, ShopListActivity.class);
                LinearLayout parentLayout = (LinearLayout) view;
                TextView listName = (TextView) parentLayout.getChildAt(0);
                intentToShop.putExtra("savedArrayList", db.getListFromDB(listName.getText().toString()));
                intentToShop.putExtra("Parent_activity_id", FROM_MAIN_ACTIVITY);
                startActivity(intentToShop);
            }
        });

        loadList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.delete):
                        deleteList();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mActionMode = null;
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.closeDB();
    }

    public void deleteList() {
        SparseBooleanArray chosen = loadList.getCheckedItemPositions();
        for (int i = 0; i < chosen.size(); i++) {
            if (chosen.valueAt(i)) {
                Cursor curs = mAdapter.getCursor();
                curs.moveToPosition(chosen.keyAt(i));
                String del = curs.getString(curs.getColumnIndex(DB.KEY_LIST));
                db.deleteList(del);
            }
        }
        getSupportLoaderManager().getLoader(0).forceLoad();
        showSnackbar();
    }

    public void showSnackbar() {
        Snackbar.make(loadList.getRootView(), R.string.Deleted, Snackbar.LENGTH_LONG).setAction(R.string.Cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.ABCreateList):
                startActivity(new Intent(MainActivity.this, CreateListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyLoader(this, db);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private static class MyLoader extends CursorLoader {
        DB db;

        public MyLoader(Context context, DB db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            return db.getMainActivityCursor();
        }
    }
}
