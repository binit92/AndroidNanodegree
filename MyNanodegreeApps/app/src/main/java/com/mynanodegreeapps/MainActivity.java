package com.mynanodegreeapps;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {

    private final String messageHeader = "This button will launch";

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
        showToast(messageHeader+"Popular Movies Project");
    }
    public void stockHawk(View view){
        showToast(messageHeader+"Stock Hawk Project");
    }
    public void buildItBigger(View view){
        showToast(messageHeader+"Build It Bigger Project");
    }
    public void makeYourAppMaterial(View view){
        showToast(messageHeader+"Make Your App Material Project");
    }
    public void goUbiquotous(View view){
        showToast(messageHeader+"Go Ubiquotous Project");
    }
    public void capstoneMyOwnProject(View view){
        showToast(messageHeader+"Capstone Project");
    }

    void showToast(CharSequence message){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
