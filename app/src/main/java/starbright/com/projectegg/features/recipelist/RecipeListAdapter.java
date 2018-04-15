package starbright.com.projectegg.features.recipelist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import starbright.com.projectegg.R;
import starbright.com.projectegg.data.local.model.Recipe;

/**
 * Created by Andreas on 4/15/2018.
 */

class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private Context mContext;
    private List<Recipe> mRecipe;

    RecipeListAdapter(Context context) {
        mContext = context;
        mRecipe = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_recipe, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mRecipe.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_thumbnail)
        ImageView imgThumbnail;

        @BindView(R.id.tv_recipe_name)
        TextView recipeName;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }

        private void bindView(Recipe recipe) {

        }
    }
}
