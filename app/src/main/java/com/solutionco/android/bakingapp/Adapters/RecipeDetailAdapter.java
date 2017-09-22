package com.solutionco.android.bakingapp.Adapters;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.solutionco.android.bakingapp.Data.Recipe;
import com.solutionco.android.bakingapp.R;

/**
 * Created by Ahmed on 6/1/2017.
 */


public class RecipeDetailAdapter  extends RecyclerView.Adapter<RecipeDetailAdapter.RecipeDetailViewHolder> {

    private int selectedPos = -1;
    Recipe recipe ;
    private final RecipeDetailClickListener listener;
    Context context;
    public interface RecipeDetailClickListener{
        void onStepClicked(int id);
    }

    public RecipeDetailAdapter(Context con , Recipe recipe, RecipeDetailClickListener listener) {
        this.recipe = recipe;
        this.listener = listener;
        context = con;
    }

    @Override
    public RecipeDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int id = R.layout.recipe_detail_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(id , parent ,false);
        RecipeDetailAdapter.RecipeDetailViewHolder viewHolder = new RecipeDetailAdapter.RecipeDetailViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeDetailViewHolder holder, int position) {

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return recipe.getIngredients().size() + recipe.getSteps().size() + 2;

    }

    public class RecipeDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView ingredientName;
        TextView ingredientQuantity;
        TextView ingredientMeasure;

        TextView stepDescription;

        TextView typeTitle;

        public RecipeDetailViewHolder(View itemView) {
            super(itemView);
            ingredientName = (TextView) itemView.findViewById(R.id.ingredient_name);
            ingredientQuantity = (TextView) itemView.findViewById(R.id.ingredient_quantity);
            ingredientMeasure = (TextView) itemView.findViewById(R.id.ingredient_measure);
            stepDescription = (TextView) itemView.findViewById(R.id.recipe_detail_step_text);
            typeTitle = (TextView) itemView.findViewById(R.id.text_view_title);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            if(listIndex == 0){
                ingredientName.setVisibility(View.GONE);
                ingredientQuantity.setVisibility(View.GONE);
                ingredientMeasure.setVisibility(View.GONE);

                stepDescription.setVisibility(View.GONE);

                typeTitle.setVisibility(View.VISIBLE);
                typeTitle.setText("Ingredients");
                typeTitle.setBackgroundColor(ContextCompat.getColor(context , R.color.colorPrimaryLight));
                return;
            }
            if (listIndex - 1 < recipe.getIngredients().size()){
                ingredientName.setVisibility(View.VISIBLE);
                ingredientQuantity.setVisibility(View.VISIBLE);
                ingredientMeasure.setVisibility(View.VISIBLE);
                ingredientName.setText(recipe.getIngredients().get(listIndex-1).getIngredient());
                ingredientQuantity.setText(String.valueOf("Quantity : "+recipe.getIngredients().get(listIndex-1).getQuantity()));
                ingredientMeasure.setText("Measure : "+recipe.getIngredients().get(listIndex-1).getMeasure());
                stepDescription.setVisibility(View.GONE);
                typeTitle.setVisibility(View.GONE);
            }
            else{
                if(listIndex - 1 == recipe.getIngredients().size()){
                    ingredientName.setVisibility(View.GONE);
                    ingredientQuantity.setVisibility(View.GONE);
                    ingredientMeasure.setVisibility(View.GONE);

                    stepDescription.setVisibility(View.GONE);

                    typeTitle.setVisibility(View.VISIBLE);
                    typeTitle.setText("Steps");
                    typeTitle.setBackgroundColor(ContextCompat.getColor(context , R.color.colorPrimaryLight));
                    return;
                }else{
                    stepDescription.setVisibility(View.VISIBLE);
                    int temp = listIndex - recipe.getIngredients().size()-2;
                    stepDescription.setText(recipe.getSteps().get(temp).getShortDescription());
                    ingredientName.setVisibility(View.GONE);
                    ingredientQuantity.setVisibility(View.GONE);
                    ingredientMeasure.setVisibility(View.GONE);
                    typeTitle.setVisibility(View.GONE);

                    if(selectedPos == listIndex)
                        stepDescription.setBackgroundColor(ContextCompat.getColor(context , R.color.colorPrimaryVeryLight));
                    else
                        stepDescription.setBackgroundColor(Color.TRANSPARENT);
                }

            }
        }


        @Override
        public void onClick(View v) {
            int index = getAdapterPosition()-2;
            if(! (index < recipe.getIngredients().size() )){
                notifyItemChanged(selectedPos);
                selectedPos = getAdapterPosition();
                notifyItemChanged(selectedPos);
                listener.onStepClicked(Integer.parseInt(recipe.getSteps().get(index - recipe.getIngredients().size()).getId()));
            }
        }


    }
}
