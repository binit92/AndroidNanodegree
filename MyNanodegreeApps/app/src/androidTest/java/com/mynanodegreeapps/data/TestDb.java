package com.mynanodegreeapps.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

import com.mynanodegreeapps.data.MovieContract.MovieEntry;
import com.mynanodegreeapps.data.MovieContract.TrailerEntry;
import com.mynanodegreeapps.data.MovieContract.ReviewEntry;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieEntry.TABLE_NAME);
        tableNameHashSet.add(TrailerEntry.TABLE_NAME);
        tableNameHashSet.add(ReviewEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDBHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without Movie,Trailer and Reviews Tables ",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> movieColumnHashSet = new HashSet<String>();
        movieColumnHashSet.add(MovieEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(MovieEntry.COLUMN_MOVIE_NAME);
        movieColumnHashSet.add(MovieEntry.COLUMN_MOVIE_POSTER);
        movieColumnHashSet.add(MovieEntry.COLUMN_MOVIE_VOTEAVERAGE);
        movieColumnHashSet.add(MovieEntry.COLUMN_MOVIE_RELEASEDATE);
        movieColumnHashSet.add(MovieEntry.COLUMN_MOVIE_PLOT_SYNOPSIS);

        /*
        movieColumnHashSet.add(TrailerEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(TrailerEntry.COLUMN_TRAILER_NAME);
        movieColumnHashSet.add(TrailerEntry.COLUMN_TRAILER_KEY);

        movieColumnHashSet.add(ReviewEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(ReviewEntry.COLUMN_REVIEW_AUTHOR);
        movieColumnHashSet.add(ReviewEntry.COLUMN_REVIEW_CONTENT);
        */

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                movieColumnHashSet.isEmpty());
        db.close();
    }

    public void testMovieTable(){
        // First step: Get reference to writable database
        SQLiteDatabase db = new MovieDBHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        ContentValues cv = TestUtilities.createMovieValues();

        // Insert ContentValues into database and get a row ID back
        long row = db.insert(MovieEntry.TABLE_NAME,"",cv);
        assertTrue("Success: Successful row insertion ", row != -1);

        // Query the database and receive a Cursor back
        Cursor movieCursor =  db.query(MovieEntry.TABLE_NAME,null,null,null,null,null,null);
        // Move the cursor to a valid database row
        movieCursor.moveToFirst();


        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCursor("Error", movieCursor,cv);

        /*
        assertTrue("Location Setting",locationCursor.getString(locationCursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING))== TEST_LOCATION);
        assertTrue("Name",locationCursor.getString(locationCursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_CITY_NAME)).equals("North Pole"));
        assertTrue("Latitude",locationCursor.getInt(locationCursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_COORD_LAT))== 64.748);
        assertTrue("Longitude",locationCursor.getInt(locationCursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_COORD_LONG))== -147.353);
        */

        // Finally, close the cursor and database
        movieCursor.close();
        db.close();
    }

    public void testTrailersTable(){

        // First step: Get reference to writable database
        SQLiteDatabase db = new MovieDBHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        ContentValues cv = TestUtilities.createTrailerValues(321612);

        // Insert ContentValues into database and get a row ID back
        long row = db.insert(TrailerEntry.TABLE_NAME,"",cv);
        assertTrue("Success: Successful row insertion ", row != -1);

        // Query the database and receive a Cursor back
        Cursor trailerCursor =  db.query(TrailerEntry.TABLE_NAME,null,null,null,null,null,null);
        // Move the cursor to a valid database row
        trailerCursor.moveToFirst();


        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCursor("Error", trailerCursor,cv);

        /*
        assertTrue("Location Setting",locationCursor.getString(locationCursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING))== TEST_LOCATION);
        assertTrue("Name",locationCursor.getString(locationCursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_CITY_NAME)).equals("North Pole"));
        assertTrue("Latitude",locationCursor.getInt(locationCursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_COORD_LAT))== 64.748);
        assertTrue("Longitude",locationCursor.getInt(locationCursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_COORD_LONG))== -147.353);
        */

        // Finally, close the cursor and database
        trailerCursor.close();
        db.close();

    }

    public void testReviewTable(){
        // First step: Get reference to writable database
        SQLiteDatabase db = new MovieDBHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        ContentValues cv = TestUtilities.createReviewValues(321612);

        // Insert ContentValues into database and get a row ID back
        long row = db.insert(ReviewEntry.TABLE_NAME,"",cv);
        assertTrue("Success: Successful row insertion ", row != -1);

        // Query the database and receive a Cursor back
        Cursor reviewCursor =  db.query(ReviewEntry.TABLE_NAME,null,null,null,null,null,null);
        // Move the cursor to a valid database row
        reviewCursor.moveToFirst();


        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCursor("Error", reviewCursor,cv);

        /*
        assertTrue("Location Setting",locationCursor.getString(locationCursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING))== TEST_LOCATION);
        assertTrue("Name",locationCursor.getString(locationCursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_CITY_NAME)).equals("North Pole"));
        assertTrue("Latitude",locationCursor.getInt(locationCursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_COORD_LAT))== 64.748);
        assertTrue("Longitude",locationCursor.getInt(locationCursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_COORD_LONG))== -147.353);
        */

        // Finally, close the cursor and database
        reviewCursor.close();
        db.close();
    }

}
