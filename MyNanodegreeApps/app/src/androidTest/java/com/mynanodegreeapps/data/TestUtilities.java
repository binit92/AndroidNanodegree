package com.mynanodegreeapps.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

import com.mynanodegreeapps.utils.PollingCheck;

import com.mynanodegreeapps.data.MovieContract.MovieEntry;
import com.mynanodegreeapps.data.MovieContract.TrailerEntry;
import com.mynanodegreeapps.data.MovieContract.ReviewEntry;

/*
    Students: These are functions and some test data to make it easier to test your database and
    Content Provider.  Note that you'll want your WeatherContract class to exactly match the one
    in our solution to use these as-given.
 */
public class TestUtilities extends AndroidTestCase {
    static final String TEST_LOCATION = "99705";
    static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
        Use this to create some default Movie Values for your database tests.
     */
    static ContentValues createMovieValues() {
        ContentValues movieValues = new ContentValues();

        movieValues.put(MovieEntry.COLUMN_MOVIE_ID,321612);
        movieValues.put(MovieEntry.COLUMN_MOVIE_NAME,"Beauty and the Beast");
        movieValues.put(MovieEntry.COLUMN_MOVIE_POSTER,"/45Y1G5FEgttPAwjTYic6czC9xCn.jpg");
        movieValues.put(MovieEntry.COLUMN_MOVIE_RELEASEDATE,"2017-03-15");
        movieValues.put(MovieEntry.COLUMN_MOVIE_PLOT_SYNOPSIS,"In the near future, a weary Logan cares for an ailing Professor X in a hide out on the Mexican border. But Logan's attempts to hide from the world and his legacy are up-ended when a young mutant arrives, being pursued by dark forces.");
        movieValues.put(MovieEntry.COLUMN_MOVIE_VOTEAVERAGE,7.2);

        return movieValues;
    }

    /*
    *  Use this to check default values for Trailers Table
    *  Note:- the movieID is the foreign key ...
    * */

    static ContentValues createTrailerValues(int movieID){
        ContentValues trailerValues = new ContentValues();
        trailerValues.put(TrailerEntry.COLUMN_MOVIE_ID,movieID);
        trailerValues.put(TrailerEntry.COLUMN_TRAILER_NAME,"Official Trailer ");
        trailerValues.put(TrailerEntry.COLUMN_TRAILER_KEY,"alsjkdhfasdgfasldhf");

        return trailerValues;
    }

    static ContentValues createReviewValues(int movieID){
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(ReviewEntry.COLUMN_MOVIE_ID,movieID);
        reviewValues.put(ReviewEntry.COLUMN_REVIEW_AUTHOR,"binit92");
        reviewValues.put(ReviewEntry.COLUMN_REVIEW_CONTENT,"Hermionie is so stunning in this movie");

        return reviewValues;
    }


    static long insertMovieValues(Context context){
        // insert the test record into the database
        MovieDBHelper dbHelper = new MovieDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createMovieValues();

        long movieRowId;
        movieRowId = db.insert(MovieEntry.TABLE_NAME,null,testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert a Movie Value",movieRowId != -1);
        return movieRowId;
    }




    /*
        The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
