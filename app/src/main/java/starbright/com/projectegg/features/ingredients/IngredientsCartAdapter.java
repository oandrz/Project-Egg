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
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import starbright.com.projectegg.R;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.util.GlideApp;

class IngredientsCartAdapter extends RecyclerView.Adapter<IngredientsCartAdapter.ViewHolder> {

    private Context mContext;
    private List<Ingredient> mIngredientCart;
    private Listener mListener;

    IngredientsCartAdapter(Context context, List<Ingredient> cart) {
        mContext = context;
        mIngredientCart = cart;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cart, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClearItemClickedListener(viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mIngredientCart.get(position));
    }

    @Override
    public int getItemCount() {
        return mIngredientCart.size();
    }

    void setListener(Listener listener) {
        mListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_ingredient)
        ImageView ivIngredient;

        @BindView(R.id.tv_ingredient_name)
        TextView tvIngredientName;

        @BindView(R.id.iv_clear)
        ImageView ivClear;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Ingredient ingredient) {
            tvIngredientName.setText(ingredient.getName());
            GlideApp.with(mContext)
                    .load(ingredient.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions.circleCropTransform())
                    .centerCrop()
                    .into(ivIngredient);
        }
    }

    interface Listener {
        void onClearItemClickedListener(int position);
    }
}
