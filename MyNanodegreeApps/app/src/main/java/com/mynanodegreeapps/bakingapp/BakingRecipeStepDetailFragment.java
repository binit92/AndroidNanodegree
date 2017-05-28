package com.mynanodegreeapps.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

import com.mynanodegreeapps.R;
import com.mynanodegreeapps.bakingapp.recipe.Ingredient;
import com.mynanodegreeapps.bakingapp.recipe.Step;
import com.mynanodegreeapps.bakingapp.util.IRecipeStepCallback;
import com.squareup.picasso.Picasso;

import java.util.List;


/*  This Fragment will the show the details of Recipe Step i.e. details of "ingredients"
    and details of each recipe step that includes picture/video, recipe instruction and navigation etc"
 */

public class BakingRecipeStepDetailFragment extends Fragment implements OnPreparedListener {


    View rootView;
    LinearLayout videoLayout;
    LinearLayout descriptionLayout;
    List<Ingredient> ingredients ;
    List<Step> steps;
    Step step;
    int position;

    private VideoView videoView;
    private IRecipeStepCallback recipeStepCallback;

    Button btnPrev;
    Button btnNext;

    private static final String LOG_TAG = BakingRecipeStepDetailFragment.class.getSimpleName();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        videoView.release();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)           {
        rootView = inflater.inflate(R.layout.bakingrecipestepdetail_fragment,container,false);
        Bundle b = getArguments();

        if(rootView != null){
            ingredients = b.getParcelableArrayList("ingredients");
            steps = b.getParcelableArrayList("steps");
            position = b.getInt("position");
            setPosition(position);

            if(!getResources().getBoolean(R.bool.isTablet)) {
                btnPrev = (Button) rootView.findViewById(R.id.btn_prev);
                btnNext = (Button) rootView.findViewById(R.id.btn_next);
                buttonClickListeners();
            }

        }
        return rootView;
    }

    public void createLayouts(){

        if(videoLayout == null) {
            videoLayout = (LinearLayout) rootView.findViewById(R.id.videoContainer);
        }

        if(descriptionLayout == null) {
            descriptionLayout = (LinearLayout) rootView.findViewById(R.id.recipestepsdetailscontainer);
        }

        if (ingredients != null && position<0) {
            descriptionLayout.removeAllViews();
            videoLayout.setVisibility(View.GONE);
            createIngredientLayout(ingredients, descriptionLayout);
        }else{
            descriptionLayout.removeAllViews();
            videoLayout.setVisibility(View.VISIBLE);
            createVideoLayout(step, videoLayout,descriptionLayout);
        }
    }

    public void createIngredientLayout(List<Ingredient> ingredients , LinearLayout parentLayout){

        for(Ingredient ingredient : ingredients){
            LinearLayout linearLayout = new LinearLayout(getActivity().getApplicationContext());
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.LEFT);
            linearLayout.setPadding(5,5,5,5);

            TextView quantityTextView = new TextView(getActivity().getApplicationContext());
            TextView measureTextView = new TextView(getActivity().getApplicationContext());
            TextView ingredientTextView = new TextView(getActivity().getApplicationContext());

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

    public void createVideoLayout(Step step, LinearLayout videoLayout, LinearLayout parentLayout){

        if(videoView == null) {
            videoView = (VideoView) videoLayout.findViewById(R.id.video_view1);
        }
        videoView.setVideoURI(Uri.parse(step.getVideoUrl()));
        videoView.setOnPreparedListener(this);


        if(step.getDescription() != null) {
            TextView stepDescription = new TextView(getActivity().getApplicationContext());
            parentLayout.addView(stepDescription);
            stepDescription.setText(step.getDescription());
        }

        if(step.getThumbnailUrl() != null || !("").equals(step.getThumbnailUrl())){
            ImageView   stepImageView = new ImageView(getActivity().getApplicationContext());
            parentLayout.addView(stepImageView);
            Picasso.with(getActivity().getApplicationContext()).load(Uri.parse(step.getThumbnailUrl())).into(stepImageView);

        }
    }

    public void buttonClickListeners(){
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    setPosition(step.getStepId()-1);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPosition(step.getStepId()+1);
            }
        });
    }
    public void setPosition(int pos){
        System.out.println("--> pos is "+ pos);
        position = pos;
        if(position>=0 && position<steps.size()) {
            step = steps.get(position);
        }else{
            Toast.makeText(getActivity().getApplicationContext(),"Not Applied",Toast.LENGTH_SHORT);
        }
        createLayouts();
    }

    @Override
    public void onPrepared() {
        videoView.start();
    }


}
