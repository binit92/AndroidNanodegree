package com.mynanodegreeapps.bakingapp.util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mynanodegreeapps.R;
import com.mynanodegreeapps.bakingapp.BakingDetailActivity;
import com.mynanodegreeapps.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeImageAdapter extends RecyclerView.Adapter<RecipeImageAdapter.ViewHolder> {

    private Context c;
    List<Recipe> recipeList;
    private int source = 1;

    public static final int SOURCE_NETWORK = 1;
    public static final int SOURCE_DB = 2;

    private static final String LOG_TAG = RecipeImageAdapter.class.getSimpleName();

    public RecipeImageAdapter(Context c, List<Recipe> recipeList, int source){
        this.c=c;
        this.recipeList = recipeList;
        this.source = source;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View recipeImageView = inflater.inflate(R.layout.bakingrecipe_image,parent,false);

        // Return a new holder instance
        RecipeImageAdapter.ViewHolder viewHolder = new RecipeImageAdapter.ViewHolder(recipeImageView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Recipe selectedRecipe = recipeList.get(position);

        // Set item views based on your views and data model
        ImageView imageView = viewHolder.recipeImage;

        if(source == SOURCE_DB) {
            /* Todo : Get details from Database !

            */
        }else{
            // Get Details from Network
            String imageUrl = selectedRecipe.getImage();
            Log.d(LOG_TAG,"imageUrl is" + imageUrl);

            //Automatically creates background thread and loads image
            try {
                Picasso.with(c).load(imageUrl).into(imageView);
            }catch (Throwable t) {
                Log.i(LOG_TAG, " No Image URL in json, loading default image.");
                Picasso.with(c).load(R.drawable.baking_junkfood).into(imageView);
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(c,BakingDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("recipe",selectedRecipe);
                    c.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        ImageView recipeImage;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            recipeImage = (ImageView) itemView.findViewById(R.id.recipeImage);
        }
    }
}
