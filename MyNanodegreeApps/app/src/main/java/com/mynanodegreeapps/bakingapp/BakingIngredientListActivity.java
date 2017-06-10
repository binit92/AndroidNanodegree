package com.mynanodegreeapps.bakingapp;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mynanodegreeapps.R;
import com.mynanodegreeapps.bakingapp.model.Ingredient;

import java.util.List;

import static com.mynanodegreeapps.bakingapp.BakingActivity.recipeArrayList;

/*  This activity can be opened only when the
    recipe is clicked on the widget
* */
public class BakingIngredientListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bakingingredient_activity);

        Intent intent = getIntent();
        if(intent != null) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ingredients_layout);
            createIngredientLayout(intent.getIntExtra("id",0), linearLayout);
        }

    }

    public void createIngredientLayout(int recipeId, LinearLayout parentLayout){

        List<Ingredient> ingredients = recipeArrayList.get(recipeId).getIngredients();

        for(Ingredient ingredient : ingredients){
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.LEFT);
            linearLayout.setPadding(5,5,5,5);

            TextView quantityTextView = new TextView(getApplicationContext());
            TextView measureTextView = new TextView(getApplicationContext());
            TextView ingredientTextView = new TextView(getApplicationContext());

            quantityTextView.setText(ingredient.getQuantity());
            quantityTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            quantityTextView.setPadding(2,2,2,2);
            measureTextView.setText(ingredient.getMeasure());
            measureTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            measureTextView.setPadding(2,2,2,2);
            ingredientTextView.setText(ingredient.getIngredient());
            ingredientTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            ingredientTextView.setPadding(2,2,2,2);

            linearLayout.addView(quantityTextView);
            linearLayout.addView(measureTextView);
            linearLayout.addView(ingredientTextView);

            parentLayout.addView(linearLayout);
        }

    }
}
