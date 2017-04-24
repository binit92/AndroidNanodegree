package com.mynanodegreeapps.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.mynanodegreeapps.data.MovieContract.MovieEntry;
import com.mynanodegreeapps.data.MovieContract.TrailerEntry;
import com.mynanodegreeapps.data.MovieContract.ReviewEntry;

public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider
    private static final UriMatcher mUriMatcher = buildUriMatcher();
    private MovieDBHelper mOpenHelper;

    static final int MOVIE = 100;
    static final int MOVIE_WITH_POSTERPATH = 101;
    static final int MOVIE_BY_MOVIEID = 102;
    static final int TRAILERS = 200;
    static final int TRAILERS_BY_MOVIEID = 201;
    static final int REVIEWS = 300;
    static final int REVIEWS_BY_MOVIEID = 301;


    /*
    Todo: remove this !
    private static final SQLiteQueryBuilder mMovieTrailerAndReviewsByMovieID;

    static {
        mMovieTrailerAndReviewsByMovieID= new SQLiteQueryBuilder();

        mMovieTrailerAndReviewsByMovieID.setTables(
                MovieEntry.TABLE_NAME + " INNER JOIN " +
                        TrailerEntry.TABLE_NAME +
                        " ON " + MovieEntry.TABLE_NAME + "." + MovieEntry.COLUMN_MOVIE_ID +
                        " = " + TrailerEntry.TABLE_NAME + "." + TrailerEntry.COLUMN_MOVIE_ID +
                        " INNER JOIN " + ReviewEntry.TABLE_NAME + " ON " + MovieEntry.TABLE_NAME +
                        "." + MovieEntry.COLUMN_MOVIE_ID + " = " + ReviewEntry.TABLE_NAME +
                        "." + ReviewEntry.COLUMN_MOVIE_ID);
    }
    */

    private static final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();


    private static final String[] mALLMoviePosterPathProjection  = {
                MovieEntry.TABLE_NAME + "." + MovieEntry.COLUMN_MOVIE_POSTER} ;

    private static final String mMoviesByMovieIdSelection =
                MovieEntry.TABLE_NAME + "." + MovieEntry.COLUMN_MOVIE_ID + " = ? ";

    private static final String mTrailerByMovieIdSelection =
                TrailerEntry.TABLE_NAME + "." + TrailerEntry.COLUMN_MOVIE_ID + " = ? ";

    private static final String mReviewByMovieIdSelection =
                ReviewEntry.TABLE_NAME + "." + ReviewEntry.COLUMN_MOVIE_ID + " = ? ";


    // Get Movie By Movie ID
    private Cursor getMovieByMovieID(Uri uri, String[] projection, String sortOrder){
        queryBuilder.setTables(MovieEntry.TABLE_NAME);
        return queryBuilder.query(mOpenHelper.getReadableDatabase(),projection,mMoviesByMovieIdSelection,null,null,null,sortOrder);
    }

    // Get Trailers By Movie ID
    private Cursor getTrailerByMovieId(Uri uri, String[] projection, String sortOrder){
        queryBuilder.setTables(TrailerEntry.TABLE_NAME);
        return queryBuilder.query(mOpenHelper.getReadableDatabase(),projection,mTrailerByMovieIdSelection,null,null,null,sortOrder);
    }

    // Get Reviews By Movie ID
    private Cursor getReviewsByMovieId(Uri uri, String[] projection, String sortOrder){
        queryBuilder.setTables(ReviewEntry.COLUMN_MOVIE_ID);
        return queryBuilder.query(mOpenHelper.getReadableDatabase(),projection,mReviewByMovieIdSelection,null,null,null,sortOrder);
    }

    /*
        Here is where you need to create the UriMatcher. This UriMatcher will
        match each URI to the MOVIE, integer constants defined above.  You can test this by uncommenting the
        testUriMatcher test within TestUriMatcher.
     */
    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.

        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // 2) Use the addURI function to match each of the types.  Use the constants from
        uriMatcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        uriMatcher.addURI(authority, MovieContract.PATH_MOVIE+"/*", MOVIE_WITH_POSTERPATH);
        uriMatcher.addURI(authority, MovieContract.PATH_MOVIE+"/#", MOVIE_BY_MOVIEID);
        uriMatcher.addURI(authority, MovieContract.PATH_TRAILER, TRAILERS);
        uriMatcher.addURI(authority, MovieContract.PATH_TRAILER+"/#", TRAILERS_BY_MOVIEID);
        uriMatcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEWS);
        uriMatcher.addURI(authority, MovieContract.PATH_REVIEW+"/#", REVIEWS_BY_MOVIEID);

        // 3) Return the new matcher!
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // Use the UriMatcher to decide what kind of URL this is
        final int match = mUriMatcher.match(uri);

        switch (match) {
            case MOVIE_WITH_POSTERPATH:
                return MovieEntry.CONTENT_ITEM_TYPE;

            case MOVIE_BY_MOVIEID:
                return MovieEntry.CONTENT_TYPE;

            case TRAILERS_BY_MOVIEID:
                return TrailerEntry.CONTENT_TYPE;

            case REVIEWS_BY_MOVIEID:
                return ReviewEntry.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor retCursor;
        switch (mUriMatcher.match(uri)){
            case MOVIE: {
                //todo  : Is setting projection is right here ?
                retCursor = db.query(MovieEntry.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;
            }

            case MOVIE_BY_MOVIEID: {
                retCursor = getMovieByMovieID(uri,projection,sortOrder);
                break;
            }

            case TRAILERS_BY_MOVIEID: {
                retCursor = getTrailerByMovieId(uri, projection, sortOrder);
                break;
            }

            case REVIEWS_BY_MOVIEID: {
                retCursor = getReviewsByMovieId(uri,projection,sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return  retCursor;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {
                long _id = db.insert(MovieEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0 )
                    returnUri = MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILERS:{
                long _id2 = db.insert(TrailerEntry.TABLE_NAME, null, contentValues);
                if( _id2 > 0 )
                    returnUri = TrailerEntry.buildTrailersUri(_id2);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEWS:{
                long _id3 = db.insert(ReviewEntry.TABLE_NAME,null,contentValues);
                if (_id3 > 0)
                    returnUri = ReviewEntry.buildReviewsUri(_id3);
                else
                    throw new android.database.SQLException("Failed to insert row into "+ uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        //todo : skipping this for now
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        // todo : skipping this for now
        return 0;
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
