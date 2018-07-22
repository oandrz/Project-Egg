package starbright.com.projectegg.features.ingredients;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import starbright.com.projectegg.R;
import starbright.com.projectegg.data.local.model.Ingredient;

public class CartBottomSheetDialogFragment extends BottomSheetDialogFragment {

    @BindView(R.id.tv_total_ingredient)
    TextView tvTotalIngredient;

    @BindView(R.id.rv_ingredients_cart)
    RecyclerView rvIngredientsCart;

    private IngredientsCartAdapter mAdapter;
    private List<Ingredient> mCart;

    public CartBottomSheetDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.partial_bottom_sheet, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTotalIngredient.setText(String.valueOf(mCart.size()));
        setupRvIngredientCart();
    }

    private void setupRvIngredientCart() {
        rvIngredientsCart.setLayoutManager(new GridLayoutManager(getActivity(), 4,
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new IngredientsCartAdapter(getActivity(), mCart);
        rvIngredientsCart.setAdapter(mAdapter);
    }

    public void setCartIngredient(List<Ingredient> cart) {
        mCart = cart;
    }
}
