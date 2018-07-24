package starbright.com.projectegg.features.ingredients;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import starbright.com.projectegg.R;
import starbright.com.projectegg.data.local.model.Ingredient;

import static starbright.com.projectegg.features.ingredients.IngredientsFragment.EVENT_CART_COUNT_UPDATED;

public class CartBottomSheetDialogFragment extends BottomSheetDialogFragment
        implements IngredientsCartAdapter.Listener {

    public static final String EXTRA_EVENT_CART = "EXTRA_EVENT_CART";

    private static final int SPAN_COUNT = 4;

    @BindView(R.id.tv_total_ingredient)
    TextView tvTotalIngredient;

    @BindView(R.id.tv_empty)
    TextView tvEmpty;

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
        updateCartCountView();
        setupRvIngredientCart();
    }

    private void setupRvIngredientCart() {
        rvIngredientsCart.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT,
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new IngredientsCartAdapter(getActivity(), mCart);
        mAdapter.setListener(this);
        rvIngredientsCart.setAdapter(mAdapter);
        tvEmpty.setVisibility(mCart.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void updateView(int position) {
        mCart.remove(position);
        updateCartCountView();
        updateList();
    }

    private void broadcastUpdatedCart() {
        final Intent intent = new Intent(EVENT_CART_COUNT_UPDATED);
        intent.putParcelableArrayListExtra(EXTRA_EVENT_CART, new ArrayList<>(mCart));
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    private void updateList() {
        mAdapter.notifyDataSetChanged();
        tvEmpty.setVisibility(mCart.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void updateCartCountView() {
        tvTotalIngredient.setText(String.valueOf(mCart.size()));
    }

    public void setCartIngredient(List<Ingredient> cart) {
        mCart = cart;
    }

    @Override
    public void onClearItemClickedListener(int position) {
        updateView(position);
        broadcastUpdatedCart();
    }
}
