package com.mynanodegreeapps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mynanodegreeapps.movies.MoviesActivity;

public class MainActivity extends AppCompatActivity {

    private final String messageHeader = "This button will launch: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void popularMovies(View view){
        //showToast(messageHeader+getString(R.string.text_Popular_Movies));
        Intent intent = new Intent(getApplicationContext(),MoviesActivity.class);
        startActivity(intent);

    }
    public void bakingApp(View view){
        showToast(messageHeader+getString(R.string.text_Baking_App));
    }
    public void buildItBigger(View view){
        showToast(messageHeader+getString(R.string.text_Build_IT_Bigger));
    }
    public void makeYourAppMaterial(View view){
        showToast(messageHeader+getString(R.string.text_Make_Your_APP_Material));
    }
    public void capstoneMyOwnProject(View view){
        showToast(messageHeader+getString(R.string.text_Capstone_My_Own_Project));
    }

    void showToast(CharSequence message){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }


}
