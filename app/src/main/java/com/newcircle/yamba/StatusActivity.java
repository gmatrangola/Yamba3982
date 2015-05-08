package com.newcircle.yamba;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class StatusActivity extends AppCompatActivity implements TextWatcher {

    private static final String TAG = StatusActivity.class.getSimpleName();
    private EditText mStatusText;
    private TextView mTextCount;
    private int mDefaultColor;
    private MenuItem mPostMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate " + this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);


        setContentView(R.layout.activity_status);
        mStatusText = (EditText) findViewById(R.id.status_text);
        mStatusText.addTextChangedListener(this);
        mTextCount = (TextView) findViewById(R.id.status_text_count);

        mDefaultColor = mTextCount.getTextColors().getDefaultColor();
        String oldMessage = getIntent().getStringExtra(StatusUpdateService.EXTRA_MESSAGE);
        if(oldMessage != null) mStatusText.setText(oldMessage);

//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(StatusActivity.this, "Button Pressed", Toast.LENGTH_LONG).show();
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void afterTextChanged(Editable s) {
        int count = 140 - s.length();
        mTextCount.setText(Integer.toString(count));
        if(count < 10) {
            mTextCount.setTextColor(Color.RED);
        }
        else {
            mTextCount.setTextColor(mDefaultColor);
        }
        mPostMenuItem.setEnabled(count >= 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        mPostMenuItem = menu.findItem(R.id.action_post);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up mButton, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_post:
                String status = mStatusText.getText().toString();
                Intent intent = new Intent(this, StatusUpdateService.class);
                intent.putExtra(StatusUpdateService.EXTRA_MESSAGE, status);

                startService(intent);
                mStatusText.getText().clear();
                finish();
                return true;
            case R.id.action_refresh:
                startService(new Intent(this, RefreshService.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
