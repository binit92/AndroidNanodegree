package com.mynanodegreeapps.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mynanodegreeapps.R;
import com.mynanodegreeapps.bakingapp.util.RecipeImageAdapter;
import com.mynanodegreeapps.bakingapp.recipe.Ingredient;
import com.mynanodegreeapps.bakingapp.recipe.Recipe;
import com.mynanodegreeapps.bakingapp.recipe.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BakingActivity extends AppCompatActivity{

    //Todo: Use newurl
    private static final String SERVER_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/March/58d1537b_baking/baking.json";
    private String LOG_TAG = BakingActivity.class.getSimpleName();

    JsonArrayRequest recipeListRequest;
    RequestQueue recipeListRequestQueue;

    RecyclerView recipeGrid;
    RecipeImageAdapter recipeImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baking_activity);

        recipeGrid = (RecyclerView) findViewById(R.id.recipeGrid);
        recipeGrid.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        recipeGrid.setClickable(true);

        /* Todo:
                1. Get the recipe
                2. Send it to BakingDetailActivity when clicked on Image Adapter
         */
        recipeListRequestQueue =  Volley.newRequestQueue(getApplicationContext());
        getRecipes();
    }

    private void getRecipes(){
        Uri requestUri = Uri.parse(SERVER_URL);
        recipeListRequest = new JsonArrayRequest(Request.Method.GET
                ,requestUri.toString()
                , null
                ,new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {

                        List<Recipe> recipes = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject recipesObject = response.getJSONObject(i);

                                int recipeid = recipesObject.getInt("id");
                                String name = recipesObject.getString("name");
                                JSONArray ingredients = recipesObject.getJSONArray("ingredients");
                                JSONArray steps = recipesObject.getJSONArray("steps");
                                String servings = recipesObject.getString("servings");
                                String image = recipesObject.getString("image");

                                List<Ingredient> ingredientList = new ArrayList<>();
                                for(int j=0 ; j< ingredients.length();j++){
                                    JSONObject ingredientObject = ingredients.getJSONObject(j);
                                    String quantity = ingredientObject.getString("quantity");
                                    String measure = ingredientObject.getString("measure");
                                    String ingredient = ingredientObject.getString("ingredient");

                                    ingredientList.add(new Ingredient(quantity,measure,ingredient));
                                }

                                List<Step> stepList = new ArrayList<>();
                                for(int k=0; k<steps.length();k++){
                                    JSONObject stepsObject = steps.getJSONObject(i);
                                    int stepId= stepsObject.getInt("id");
                                    String shortDesc = stepsObject.getString("shortDescription");
                                    String desc = stepsObject.getString("description");
                                    String videoUrl = stepsObject.getString("videoURL");
                                    String thumbnailUrl = stepsObject.getString("thumbnailURL");

                                    stepList.add(new Step(stepId,shortDesc,desc,videoUrl,thumbnailUrl));
                                }

                                recipes.add(new Recipe(recipeid,name,ingredientList,stepList,servings,image));
                            }
                        }catch (JSONException je){
                            je.printStackTrace();
                        }

                        recipeImageAdapter = new RecipeImageAdapter(getApplicationContext(),recipes,RecipeImageAdapter.SOURCE_NETWORK);
                        recipeGrid.setAdapter(recipeImageAdapter);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
            });

            recipeListRequest.setTag(LOG_TAG);
            recipeListRequestQueue.add(recipeListRequest);
    }
}
