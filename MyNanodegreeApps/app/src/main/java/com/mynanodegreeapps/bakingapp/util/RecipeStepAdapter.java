package com.mynanodegreeapps.bakingapp.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mynanodegreeapps.R;
import com.mynanodegreeapps.bakingapp.recipe.RecipeStep;

import java.util.List;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.ViewHolder> {

    Context c;
    List<RecipeStep> recipeStepList;
    private IRecipeStepCallback recipeStepCallback;

    public RecipeStepAdapter(Context c, List<RecipeStep> recipeSteps, IRecipeStepCallback callback){
        this.c=c;
        this.recipeStepList = recipeSteps;
        this.recipeStepCallback = callback;
    }

    @Override
    public RecipeStepAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View recipeImageView = inflater.inflate(R.layout.bakingrecipestep_text,parent,false);

        // Return a new holder instance
        RecipeStepAdapter.ViewHolder viewHolder = new RecipeStepAdapter.ViewHolder(recipeImageView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeStepAdapter.ViewHolder viewHolder, final int position) {
        final RecipeStep recipeStep = recipeStepList.get(position);

        TextView textView = viewHolder.stepDesc;
        textView.setText(recipeStep.getShortDesc());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("--> text clicked is "+recipeStep.getShortDesc());
                recipeStepCallback.onRecipeStepSelect(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeStepList.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView stepDesc;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            stepDesc = (TextView) itemView.findViewById(R.id.stepDesc);
        }
    }
}
