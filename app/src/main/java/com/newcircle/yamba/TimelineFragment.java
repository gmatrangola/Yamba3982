package com.newcircle.yamba;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by geoff on 5/7/15.
 */
public class TimelineFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        SimpleCursorAdapter.ViewBinder{
    private static String TAG = TimelineFragment.class.getSimpleName();

    public interface OnTimelineItemSelectedListener {
        public void onTimelineItemSelected(long id);
    }

    private static final int LOADER_ID = 999;
    private SimpleCursorAdapter mAdapter;

    private OnTimelineItemSelectedListener mItemSelectedListener;

    private static final String[] FROM = {
            StatusContract.Column.USER,
            StatusContract.Column.MESSAGE,
            StatusContract.Column.CREATED_AT
    };

    private static final int[] TO = {
            R.id.text_user,
            R.id.text_message,
            R.id.text_created_at
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item, null, FROM, TO, 0);

        setListAdapter(mAdapter);
        mAdapter.setViewBinder(this);
        getLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mItemSelectedListener = (OnTimelineItemSelectedListener) activity;
        }
        catch (ClassCastException e) {
            throw new IllegalArgumentException(activity.getClass().getSimpleName() +
                    " must implement" + OnTimelineItemSelectedListener.class.getSimpleName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().startService(new Intent(getActivity(), RefreshService.class));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader - pass in the CONTENT_URI");
        return new CursorLoader(getActivity(), StatusContract.CONTENT_URI, null, null, null,
                StatusContract.DEFAULT_SORT);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished " + data.getCount());
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        switch (view.getId()) {
            case R.id.text_created_at:
                long timestamp = cursor.getLong(columnIndex);
                CharSequence relTime = DateUtils.getRelativeTimeSpanString(timestamp);
                ((TextView)view).setText(relTime);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mItemSelectedListener.onTimelineItemSelected(id);
    }
}
