package com.gfcommunity.course.gfcommunity.recyclerView.recipes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.activities.recipes.RecipeDetailsActivity;
import com.gfcommunity.course.gfcommunity.data.SharingInfoContract;
import com.gfcommunity.course.gfcommunity.data.recipes.RecipesContentProvider;
import com.gfcommunity.course.gfcommunity.model.Recipe;
import com.gfcommunity.course.gfcommunity.recyclerView.SelectableAdapter;
import com.gfcommunity.course.gfcommunity.utils.DateFormatUtil;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Provide views to RecyclerView with data from recipeList.
 */
public class RecipesAdapter extends SelectableAdapter<RecipesAdapter.ViewHolder> {
    static Cursor cursor;
    static Context context;
    private ViewHolder.ClickListener clickListener;

    private ArrayList<Recipe> getFilteredList(CharSequence constraint) {

        String query = constraint.toString().toLowerCase();
        String selection = SharingInfoContract.RecipesEntry.CATEGORY + " LIKE '" + query +"'";

        Cursor cursor = context.getContentResolver().query(RecipesContentProvider.RECIPES_CONTENT_URI, null, selection, null, null);

        ArrayList<Recipe> recipesList = new ArrayList<>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    recipesList.add(setRecipeValues(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return recipesList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener{
        private TextView title, subTitle, text;
        private CircularImageView recipeImg;
        private ClickListener listener;
        private RelativeLayout relativeLayout;

        private static SparseArray<Recipe> recipesMap = new SparseArray<Recipe>();//Recipes map mapped by recipe ID

        public ViewHolder(View view, ClickListener listener) {
            super(view);
            title = (TextView) view.findViewById(R.id.row_title);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.listRowCard);
            subTitle = (TextView) view.findViewById(R.id.row_subtitle);
            text = (TextView) view.findViewById(R.id.row_text);
            recipeImg = (CircularImageView) view.findViewById(R.id.row_img);
            this.listener = listener;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        /**
         * Click on RecyclerView item start recipe details activity:
         * Creating recipes map mapped by recipe ID
         * @param v
         */
        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            cursor.moveToPosition(position);
            int recipeID = cursor.getInt(cursor.getColumnIndex(SharingInfoContract.RecipesEntry._ID));
            Recipe recipe = recipesMap.get(recipeID);
            //Set the recipe only if it's the first clicking (the recipe is not initialized to map)
            if(recipe == null){
                recipe = setRecipeValues(cursor);
                recipesMap.put(recipeID, recipe);
            }

            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra("selected_item",recipe); //Pass selected recipe to RecipeDetailsActivity
            intent.putExtra("selectedItemId",position); //Pass selected recipe id to RecipeDetailsActivity

            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            if (listener != null) {
                int position = this.getAdapterPosition();
                cursor.moveToPosition(position);
                int recipeID = cursor.getInt(cursor.getColumnIndex(SharingInfoContract.RecipesEntry._ID));
                return listener.onItemLongClicked(position,recipeID);
            }

            return false;
        }

        public interface ClickListener {
            public void onItemClicked(int position);
            public boolean onItemLongClicked(int position, int recipeID);
        }
    }

    /**
     * Set recipe values (to Recipe model by cursor)
     * @param cursor
     * @return Recipe model
     */
    public static Recipe setRecipeValues(Cursor cursor){
        Recipe recipe = new Recipe();
        if(cursor != null) {
            recipe.setRecipeName(cursor.getString(cursor.getColumnIndex(SharingInfoContract.RecipesEntry.RECIPE_NAME)));
            recipe.setIngredients(cursor.getString(cursor.getColumnIndex(SharingInfoContract.RecipesEntry.INGREDIENTS)));
            recipe.setInstructions(cursor.getString(cursor.getColumnIndex(SharingInfoContract.RecipesEntry.INSTRUCTIONS)));
            recipe.setRecipeImgUri(cursor.getString(cursor.getColumnIndex(SharingInfoContract.RecipesEntry.RECIPE_IMAGE_URl)));
            recipe.setPreparationTime(cursor.getString(cursor.getColumnIndex(SharingInfoContract.RecipesEntry.PREPARATION_TIME)));
            recipe.setDinersNumber(cursor.getInt(cursor.getColumnIndex(SharingInfoContract.RecipesEntry.DINERS_NUMBER)));
            recipe.setDifficultyPreparation(cursor.getString(cursor.getColumnIndex(SharingInfoContract.RecipesEntry.DIFFICULTY_PREPARATION)));
            recipe.setCategory(cursor.getString(cursor.getColumnIndex(SharingInfoContract.RecipesEntry.CATEGORY)));
            recipe.setRecipeStory(cursor.getString(cursor.getColumnIndex(SharingInfoContract.RecipesEntry.RECIPE_STORY)));
            recipe.setUserID(cursor.getString(cursor.getColumnIndex(SharingInfoContract.RecipesEntry.USER_ID)));
            recipe.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(SharingInfoContract.RecipesEntry.CREATED_AT))));
        } else {
            Log.e(RecipesAdapter.class.getName(), "Failed to set recipe details");
        }
        return recipe;
    }
    public RecipesAdapter(Context context, Cursor cursor, ViewHolder.ClickListener clickListener) {
        this.context = context;
        this.cursor = cursor;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new ViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);

        holder.title.setText(cursor.getString(cursor.getColumnIndex(SharingInfoContract.RecipesEntry.RECIPE_NAME)));

        //Set subtitle to preparationTime or difficultyPreparation or hide it
        String preparationTime = cursor.getString(cursor.getColumnIndex(SharingInfoContract.RecipesEntry.PREPARATION_TIME));
        if(!TextUtils.isEmpty(preparationTime)) {
            holder.subTitle.setText(context.getString(R.string.preparation_time) + ": " + preparationTime);
        } else {
            String difficultyPreparation = cursor.getString(cursor.getColumnIndex(SharingInfoContract.RecipesEntry.DIFFICULTY_PREPARATION));
            if(!TextUtils.isEmpty(difficultyPreparation)) {
                holder.subTitle.setText(!TextUtils.isEmpty(difficultyPreparation) ? context.getString(R.string.difficulty_preparation) + ": " + difficultyPreparation : "");
            } else {
                holder.subTitle.setVisibility(View.GONE);
            }
        }

        //Build added by string
        try {
            String userId = cursor.getString(cursor.getColumnIndex(SharingInfoContract.RecipesEntry.USER_ID)); //TODO: GET USER NAME
            String text = String.format(context.getResources().getString(R.string.user_uploaded_with_date_text),
                    !TextUtils.isEmpty(userId) ? "user name" : context.getString(R.string.app_name),
                    DateFormatUtil.DATE_FORMAT_DDMMYYYY.format(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(SharingInfoContract.RecipesEntry.CREATED_AT)))));
            holder.text.setText(text);
        }catch(Exception e) {}


        //Set recipe image by Glide
        String recipeImgPath = cursor.getString(cursor.getColumnIndex(SharingInfoContract.RecipesEntry.RECIPE_IMAGE_URl));
        //String recipeImgPath = "https://firebasestorage.googleapis.com/v0/b/gf-community.appspot.com/o/images%2Fproduct_img146697445826120160610_141506.jpg?alt=media&token=5130c587-38af-4e03-a401-cfea8afc08dc";
        if(!TextUtils.isEmpty(recipeImgPath)) {
            Glide.with(context).load(recipeImgPath)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.recipe_circle_img)
                    .error(R.drawable.recipe_circle_img)
                    .into(holder.recipeImg);
        } else {
            holder.recipeImg.setImageResource(R.drawable.recipe_circle_img);
        }
        if(isSelected(position)){
            holder.relativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.greenAppColor));
        }
        else{
            holder.relativeLayout.setBackgroundColor(Color.TRANSPARENT);
        }

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
