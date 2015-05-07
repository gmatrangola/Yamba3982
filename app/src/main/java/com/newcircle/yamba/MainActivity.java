package com.newcircle.yamba;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements TimelineFragment.OnTimelineItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate -- initLoader");
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.menu_yamba, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up mButton, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_refresh:
                startService(new Intent(this, RefreshService.class));
                return true;
            case R.id.action_post:
                startActivity(new Intent(this, StatusActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimelineItemSelected(long id) {
        Toast.makeText(this, " Click " + id, Toast.LENGTH_LONG).show();
        DetailsFragment fragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_details);

        if(fragment != null && fragment.isVisible()) {
            fragment.updateView(id);
        }
        else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(StatusContract.Column.ID, id);
            startActivity(intent);
        }
    }
}
