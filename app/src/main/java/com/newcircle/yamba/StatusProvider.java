package com.newcircle.yamba;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class StatusProvider extends ContentProvider {
    private String TAG = StatusProvider.class.getSimpleName();
    private DbHelper mDbHelper;

    public StatusProvider() {
    }

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        // content://com.example.android.yamba.StatusProvider/status return 2
        sURIMatcher.addURI(StatusContract.AUTHORITY, StatusContract.TABLE, StatusContract.STATUS_DIR);
        // content://com.example.android.yamba.StatusProvider/status/1234 return 1
        sURIMatcher.addURI(StatusContract.AUTHORITY, StatusContract.TABLE + "/#", StatusContract.STATUS_ITEM);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri ret= null;

        if(sURIMatcher.match(uri) != StatusContract.STATUS_DIR) {
            throw new IllegalArgumentException("Illegal uri: " + uri);
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long rowId = db.insertWithOnConflict(StatusContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        if(rowId != -1) {
            long id = values.getAsLong(StatusContract.Column.ID);
            ret = ContentUris.withAppendedId(uri, id);

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return ret;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables( StatusContract.TABLE);

        switch (sURIMatcher.match(uri)) {
            case StatusContract.STATUS_DIR:
                break;
            case StatusContract.STATUS_ITEM:
                qb.appendWhere(StatusContract.Column.ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Illegal uri: " + uri);
        }

        String orderBy = (TextUtils.isEmpty(sortOrder))?StatusContract.DEFAULT_SORT:sortOrder;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        Log.d(TAG, "queried records: " + cursor.getCount());
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
