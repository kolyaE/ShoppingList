package kolyae318rub.shoplist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLoadPreviousListButtonClick(View view)
    {
        Intent intentMainToLoad = new Intent(MainActivity.this, LoadListActivity.class);
        startActivity(intentMainToLoad);
    }

    public void onCreateNewListButtonClick(View view)
    {
        Intent intentMainToCreate = new Intent(MainActivity.this, CreateListActivity.class);
        startActivity(intentMainToCreate);
    }
}
