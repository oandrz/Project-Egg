package starbright.com.projectegg.features.recipelist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import starbright.com.projectegg.R;
import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.util.GlideApp;

/**
 * Created by Andreas on 4/15/2018.
 */

class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private Context mContext;
    private List<Recipe> mRecipes;

    RecipeListAdapter(Context context) {
        mContext = context;
        mRecipes = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_recipe, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(mRecipes.get(position));
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    void setRecipes(List<Recipe> recipes) {
        mRecipes.clear();
        mRecipes.addAll(recipes);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_thumbnail)
        ImageView imgThumbnail;

        @BindView(R.id.tv_recipe_name)
        TextView tvRecipeName;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindView(Recipe recipe) {
            tvRecipeName.setText(recipe.getTitle());
            GlideApp.with(mContext)
                    .load(recipe.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(imgThumbnail);
        }
    }
}