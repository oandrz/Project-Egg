package starbright.com.projectegg.features.ingredients;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import starbright.com.projectegg.R;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.util.GlideApp;

class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private Context mContext;
    private List<Ingredient> mIngredients;

    IngredientsAdapter(Context context) {
        mContext = context;
    }

    void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_autocomplete_ingredient, parent, false);
        final IngredientViewHolder viewHolder = new IngredientViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.bind(mIngredients.get(position));
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredient)
        TextView tvIngredient;

        @BindView(R.id.iv_ingredient)
        ImageView ivIngredient;

        IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Ingredient ingredient) {
            tvIngredient.setText(ingredient.getName());
            GlideApp.with(mContext)
                    .load(ingredient.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(ivIngredient);
        }
    }
}