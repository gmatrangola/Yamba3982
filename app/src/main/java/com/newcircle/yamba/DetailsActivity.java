package com.newcircle.yamba;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by geoff on 5/7/15.
 */
public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            long statusId = getIntent().getLongExtra(StatusContract.Column.ID, -1);
            DetailsFragment fragment = DetailsFragment.newInstance(statusId);
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment,
                    fragment.getClass().getSimpleName()).commit();

        }
    }
}
