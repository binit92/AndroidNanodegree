package com.mynanodegreeapps.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mynanodegreeapps.R;
import com.mynanodegreeapps.bakingapp.util.IVolleyCallback;
import com.mynanodegreeapps.bakingapp.util.RecipeImageAdapter;
import com.mynanodegreeapps.bakingapp.model.Ingredient;
import com.mynanodegreeapps.bakingapp.model.Recipe;
import com.mynanodegreeapps.bakingapp.model.Step;
import com.mynanodegreeapps.bakingapp.util.ResponseReader;
import com.mynanodegreeapps.bakingapp.widget.BakingAppWidgetProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BakingActivity extends AppCompatActivity implements IVolleyCallback{

    private String LOG_TAG = BakingActivity.class.getSimpleName();

    JsonArrayRequest recipeListRequest;
    RequestQueue recipeListRequestQueue;

    RecyclerView recipeGrid;
    RecipeImageAdapter recipeImageAdapter;
    public static List<Recipe> recipeArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baking_activity);

        recipeGrid = (RecyclerView) findViewById(R.id.recipeGrid);

        if (getResources().getBoolean(R.bool.isTablet)) {
            recipeGrid.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        } else {
            recipeGrid.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        }

        recipeGrid.setClickable(true);
        recipeListRequestQueue =  Volley.newRequestQueue(getApplicationContext());
        getRecipes(this);

    }

    private void getRecipes(final IVolleyCallback callback){
        Uri requestUri = Uri.parse(getString(R.string.SERVER_URL));
        recipeListRequest = new JsonArrayRequest(Request.Method.GET
                ,requestUri.toString()
                , null
                ,new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {

                        // Todo : May be use a 3rdparty GSON library here !
                        ResponseReader reader = new ResponseReader();
                        List<Recipe> recipes = reader.parseJSON(response);
                        recipeArrayList = recipes;

                        recipeImageAdapter = new RecipeImageAdapter(getApplicationContext(), recipeArrayList, RecipeImageAdapter.SOURCE_NETWORK);
                        recipeGrid.setAdapter(recipeImageAdapter);
                        if(response != null) {
                            callback.markSuccess();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
            });

        recipeListRequest.setTag(LOG_TAG);
        recipeListRequestQueue.add(recipeListRequest);
    }

    @Override
    public boolean markSuccess() {
        Log.d(LOG_TAG," Successful reply ! ");
        return true;
    }

    public RequestQueue getRequestQueue(){
        return recipeListRequestQueue;
    }
}
