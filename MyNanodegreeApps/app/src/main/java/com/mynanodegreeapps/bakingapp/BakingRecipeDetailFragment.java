package com.mynanodegreeapps.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mynanodegreeapps.R;

public class BakingRecipeDetailFragment extends Fragment {

    View rootview;
    RecyclerView recipeStepsView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.bakingrecipedetail_fragment,container,false);
        if(rootview!= null) {
            recipeStepsView = (RecyclerView) rootview.findViewById(R.id.recipeSteps);
            recipeStepsView.setLayoutManager(new GridLayoutManager(getContext(),2));
            recipeStepsView.setClickable(true);
        }

        return rootview;
    }
}