package com.mynanodegreeapps.bakingapp.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mynanodegreeapps.R;
import com.mynanodegreeapps.bakingapp.BakingActivity;
import com.mynanodegreeapps.bakingapp.BakingRecipeDetailFragment;
import com.mynanodegreeapps.bakingapp.model.Ingredient;
import com.mynanodegreeapps.bakingapp.model.Recipe;
import com.mynanodegreeapps.bakingapp.util.ResponseReader;

import org.json.JSONArray;

import java.util.List;
import java.util.Random;

import static com.mynanodegreeapps.bakingapp.BakingActivity.recipeArrayList;

public class BakingAppRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    JsonArrayRequest recipeListRequest;
    RequestQueue recipeListRequestQueue;


    public  BakingAppRemoteViewFactory(Context c){
        mContext = c;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        getData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipeArrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int pos) {

        Log.v(mContext.getClass().getSimpleName(), "pos: "+pos);

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.baking_widget_item);
        rv.setTextViewText(R.id.recipeName, recipeArrayList.get(pos).getName());

        int id = recipeArrayList.get(pos).getRecipeId();

        // Fill in the onClick PendingIntent Template for each recipe
/*
        Bundle extras = new Bundle();
        extras.putInt("id",id);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.recipeName,fillInIntent);
*/

        List<Ingredient> ingredients = getIngredients(id-1);
        StringBuffer buffer = new StringBuffer();
        buffer.append("INGREDIENTS : ");
        for(int i=0; i<ingredients.size(); i++){
            buffer.append(ingredients.get(i).getIngredient() + "");
        }
        rv.setTextViewText(R.id.ingredients,buffer.toString());
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void getData(){
        recipeListRequestQueue =  Volley.newRequestQueue(mContext);
        Uri requestUri = Uri.parse(mContext.getString(R.string.SERVER_URL));
        recipeListRequest = new JsonArrayRequest(Request.Method.GET
                ,requestUri.toString()
                , null
                ,new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {

                ResponseReader reader = new ResponseReader();
                List<Recipe> recipes = reader.parseJSON(response);
                recipeArrayList = recipes;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        recipeListRequest.setTag(BakingAppRemoteViewFactory.class.getSimpleName());
        recipeListRequestQueue.add(recipeListRequest);
    }

    public List<Ingredient> getIngredients(int recipeId){

        if(recipeArrayList.isEmpty()){

            Uri requestUri = Uri.parse(mContext.getString(R.string.SERVER_URL));
            RequestQueue recipeListRequestQueue  =  Volley.newRequestQueue(mContext);;
            JsonArrayRequest recipeListRequest = new JsonArrayRequest(Request.Method.GET
                    ,requestUri.toString()
                    , null
                    ,new Response.Listener<JSONArray>(){
                @Override
                public void onResponse(JSONArray response) {

                    ResponseReader reader = new ResponseReader();
                    List<Recipe> recipes = reader.parseJSON(response);
                    recipeArrayList = recipes;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
        return recipeArrayList.get(recipeId).getIngredients();

    }
}
