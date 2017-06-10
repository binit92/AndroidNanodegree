package com.mynanodegreeapps.bakingapp.util;

import com.mynanodegreeapps.bakingapp.model.Ingredient;
import com.mynanodegreeapps.bakingapp.model.Recipe;
import com.mynanodegreeapps.bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class ResponseReader {

    public ResponseReader() {
    }

    public List<Recipe> parseJSON(JSONArray response) {
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
                for (int j = 0; j < ingredients.length(); j++) {
                    JSONObject ingredientObject = ingredients.getJSONObject(j);
                    String quantity = ingredientObject.getString("quantity");
                    String measure = ingredientObject.getString("measure");
                    String ingredient = ingredientObject.getString("ingredient");

                    ingredientList.add(new Ingredient(quantity, measure, ingredient));
                }

                List<Step> stepList = new ArrayList<>();
                for (int k = 0; k < steps.length(); k++) {
                    JSONObject stepsObject = steps.getJSONObject(k);
                    int stepId = stepsObject.getInt("id");
                    String shortDesc = stepsObject.getString("shortDescription");
                    String desc = stepsObject.getString("description");
                    String videoUrl = stepsObject.getString("videoURL");
                    String thumbnailUrl = stepsObject.getString("thumbnailURL");

                    stepList.add(new Step(stepId, shortDesc, desc, videoUrl, thumbnailUrl));
                }

                recipes.add(new Recipe(recipeid, name, ingredientList, stepList, servings, image));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return recipes;

    }
}