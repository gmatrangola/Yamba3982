package com.newcircle.yamba;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by geoff on 5/7/15.
 */
public class DetailsFragment extends Fragment {

    private static final String TAG = DetailsFragment.class.getSimpleName();
    private TextView mTextUser;
    private TextView mTextMessage;
    private TextView mTextCreatedAt;

    public static DetailsFragment newInstance(long statusId) {
        DetailsFragment fragment = new DetailsFragment();

        Bundle args = new Bundle();
        args.putLong(StatusContract.Column.ID, statusId);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        mTextUser = (TextView) view.findViewById(R.id.text_user);
        mTextMessage = (TextView) view.findViewById(R.id.text_message);
        mTextCreatedAt = (TextView) view.findViewById(R.id.text_created_at);

        return view;
    }

    public void updateView(long id) {
        if(id == -1) {
            Log.w(TAG, "Invalid log ID");
            return;
        }

        Uri uri = ContentUris.withAppendedId(StatusContract.CONTENT_URI, id);
        // content://com.example.android.yamba.StatusProvider/status/23
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);

        if(!cursor.moveToFirst()) {
            cursor.close();
            Log.e(TAG, "Invalid ID " + id);
            return;
        }

        String user = cursor.getString(cursor.getColumnIndex(StatusContract.Column.USER));
        String message = cursor.getString(cursor.getColumnIndex(StatusContract.Column.MESSAGE));
        long createdAt = cursor.getLong(cursor.getColumnIndex(StatusContract.Column.CREATED_AT));

        mTextUser.setText(user);
        mTextMessage.setText(message);
        mTextCreatedAt.setText(DateUtils.getRelativeTimeSpanString(createdAt));
    }

    public void clearView() {
        mTextUser.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getArguments() != null) {
            long id = getArguments().getLong(StatusContract.Column.ID);
            updateView(id);
        }
    }
}
