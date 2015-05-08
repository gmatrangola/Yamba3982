package com.newcircle.yamba;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import java.util.Date;

/**
 * Created by geoff on 5/8/15.
 */
public class StatusProviderTest extends ProviderTestCase2<StatusProvider> {

    private MockContentResolver mContentResolver;

    public StatusProviderTest() {
        super(StatusProvider.class, StatusContract.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mContentResolver = getMockContentResolver();
    }

    public void testInsert() {
        int testRecordCount = 5;

        for(int i = 0; i < testRecordCount; i++) {
            final ContentValues item = new ContentValues();
            item.put(StatusContract.Column.ID, i);
            item.put(StatusContract.Column.CREATED_AT, new Date().getTime());
            item.put(StatusContract.Column.USER, "Fake Student");
            item.put(StatusContract.Column.MESSAGE, "Fake Message #" + i);

            Uri uri = mContentResolver.insert(StatusContract.CONTENT_URI, item);
            assertNotNull(uri);
        }

        Cursor c = mContentResolver.query(StatusContract.CONTENT_URI, null, null,null, null);

        assertNotNull(c);
        assertEquals(true, c.moveToFirst());
        assertEquals(testRecordCount, c.getCount());

        c.close();
    }
}
