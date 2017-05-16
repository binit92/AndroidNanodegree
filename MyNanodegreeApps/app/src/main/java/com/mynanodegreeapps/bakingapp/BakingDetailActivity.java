package com.mynanodegreeapps.bakingapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mynanodegreeapps.R;
import com.mynanodegreeapps.bakingapp.recipe.Ingredient;
import com.mynanodegreeapps.bakingapp.recipe.Recipe;
import com.mynanodegreeapps.bakingapp.recipe.Step;

import java.util.ArrayList;
import java.util.List;

public class BakingDetailActivity extends AppCompatActivity {

    Recipe recipe;
    List<Ingredient> ingredients ;
    List<Step> steps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bakingdetail_activity);

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        recipe = getIntent().getParcelableExtra("recipe");
        ingredients = recipe.getIngredients();
        steps = recipe.getSteps();

        Bundle recipeDetails = new Bundle();
        recipeDetails.putParcelableArrayList("ingredients",(ArrayList<? extends Parcelable>)ingredients);
        recipeDetails.putParcelableArrayList("steps",(ArrayList<? extends Parcelable>)steps);

        if(savedInstanceState == null) {
            BakingRecipeDetailFragment bRDF = new BakingRecipeDetailFragment();
            bRDF.setArguments(recipeDetails);

            if (isTablet) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipeDetailsRecyclerFragment, bRDF)
                        .add(R.id.recipeStepsDetailFragment, bRDF)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipedetailcontainer, bRDF)
                        .commit();
            }
        }

    }
}
