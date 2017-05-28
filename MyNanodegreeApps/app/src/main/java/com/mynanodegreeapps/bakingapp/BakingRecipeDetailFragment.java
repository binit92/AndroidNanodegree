package com.mynanodegreeapps.bakingapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mynanodegreeapps.R;
import com.mynanodegreeapps.bakingapp.recipe.Ingredient;
import com.mynanodegreeapps.bakingapp.recipe.RecipeStep;
import com.mynanodegreeapps.bakingapp.recipe.Step;
import com.mynanodegreeapps.bakingapp.util.IRecipeStepCallback;
import com.mynanodegreeapps.bakingapp.util.RecipeStepAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.recyclerview.R.attr.layoutManager;

/* This RecipeDetailFragment is responsible to show the Steps*/

public class BakingRecipeDetailFragment extends Fragment implements IRecipeStepCallback{

    View rootview;
    RecyclerView recipeStepsView;
    List<Ingredient> ingredients ;
    List<Step> steps;
    List<RecipeStep> recipeSteps = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        ingredients = bundle.getParcelableArrayList("ingredients");
        steps = bundle.getParcelableArrayList("steps");

        // add recipeSteps for adapter
        recipeSteps.add(new RecipeStep(0,"Ingredients"));
        for(Step step : steps){
             recipeSteps.add(new RecipeStep(step.getStepId(), step.getShortDescription()));
        }

        rootview = inflater.inflate(R.layout.bakingrecipedetail_fragment,container,false);
        if(rootview!= null) {
            recipeStepsView = (RecyclerView) rootview.findViewById(R.id.recipeSteps);
            recipeStepsView.setLayoutManager(new LinearLayoutManager(getContext()));
            recipeStepsView.setClickable(true);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recipeStepsView.getContext(),
                        DividerItemDecoration.VERTICAL);
            recipeStepsView.addItemDecoration(dividerItemDecoration);

            RecipeStepAdapter recipeStepAdapter = new RecipeStepAdapter(getActivity().getApplicationContext(), recipeSteps,this);
            recipeStepsView.setAdapter(recipeStepAdapter);
        }
        return rootview;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recipeSteps.clear();
    }

    @Override
    public void onRecipeStepSelect(int recipeStep) {

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        Bundle b = new Bundle();

        b.putParcelableArrayList("ingredients",(ArrayList<? extends Parcelable>)ingredients);
        b.putParcelableArrayList("steps",(ArrayList<? extends Parcelable>)steps);
        b.putInt("position",recipeStep-1);


        BakingRecipeStepDetailFragment bRSDF = new BakingRecipeStepDetailFragment();
        bRSDF.setArguments(b);

        if(isTablet){
            /* Load the BakingRecipeDetailFragment
            */
            getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.recipeStepsDetailFragment,bRSDF)
                        .commit();
        }else{
            /* Replace the Current Fragment, with a new Fragment
               and push trasaction onto a backstack (this preserve the backbutton behavior)
               Note:- Creating a new "Activity" really defeats the whole purpose to use fragments anyway ...
            */
            FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                                                    .beginTransaction();

            // Replace whatever is in the fragment container view with this fragment
            // and add the transaction to a back-stack
            transaction.replace(R.id.recipedetailcontainer, bRSDF);
            transaction.addToBackStack(null);

            //commit the transaction
            transaction.commit();
        }
    }
}