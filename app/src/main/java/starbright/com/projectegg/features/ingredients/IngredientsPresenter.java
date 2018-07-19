package starbright.com.projectegg.features.ingredients;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import starbright.com.projectegg.data.AppRepository;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.util.ClarifaiHelper;
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider;

class IngredientsPresenter implements IngredientsContract.Presenter, ClarifaiHelper.Callback {

    private final ClarifaiHelper mClarifaiHelper;
    private final AppRepository mRepository;
    private final IngredientsContract.View mView;
    private final BaseSchedulerProvider mSchedulerProvider;
    private CompositeDisposable mCompositeDisposable;

    private List<Ingredient> mCart;

    IngredientsPresenter(
            AppRepository repo,
            IngredientsContract.View view,
            BaseSchedulerProvider schedulerProvider,
            ClarifaiHelper clarifaiHelper) {
        mRepository = repo;
        mView = view;
        mView.setPresenter(this);
        mSchedulerProvider = schedulerProvider;
        mCompositeDisposable = new CompositeDisposable();
        mClarifaiHelper = clarifaiHelper;
    }

    @Override
    public void start() {
        mView.setupEtSearchIngredients();
        mView.setupRvIngredientSuggestion();
        mView.setupBottomSheetDialogFragment();
    }

    @Override
    public void handleActionButtonClicked(String query) {
        if (query.isEmpty()) {
            mView.openCamera();
        } else {
            mView.clearEtSearchQuery();
        }
    }

    @Override
    public void handleCartTvClicked() {
        mView.setCartItem(mCart);
        mView.showBottomSheetDialog();
    }

    @Override
    public void handleOnSuggestionTextChanged(String query) {
        mView.hideSearchSuggestion();
        if (query.isEmpty()) {
            mView.hideSuggestionProgressBar();
            mView.showActionCamera();
        } else {
            mView.hideActionButton();
            mView.showSuggestionProgressBar();
        }
    }

    @Override
    public void handleSuggestionItemClicked(Ingredient ingredient) {
        if (mCart == null) {
            mCart = new ArrayList<>();
        }

        if (mCart.contains(ingredient)) {
            mView.showDuplicateItemToast();
        } else {
            mCart.add(ingredient);
            mView.updateIngredientCount(mCart.size());
        }
    }

    @Override
    public void searchIngredient(String query) {
        if (query.isEmpty()) {
            return;
        }

        mCompositeDisposable
                .add(mRepository.searchIngredient(query)
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Consumer<List<Ingredient>>() {
                    @Override
                    public void accept(List<Ingredient> ingredients) {
                        mView.hideSuggestionProgressBar();
                        mView.hideSoftkeyboard();
                        if (ingredients.isEmpty()) {
                            mView.showItemEmptyToast();
                        }
                        mView.showActionClear();
                        mView.showSearchSuggestion(ingredients);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        mView.hideSuggestionProgressBar();
                        mView.showActionClear();
                    }
                })
        );
    }

    @Override
    public void setCart(List<Ingredient> cart) {
        mCart = cart;
        mView.updateIngredientCount(cart.size());
    }

    @Override
    public ArrayList<Ingredient> getCart() {
        return new ArrayList<>(mCart);
    }

    @Override
    public void handleCameraResult(String filePath) {
        mView.setupMaterialProgressDialog();
        mView.showMaterialProgressDialog();
        mClarifaiHelper.predict(Uri.fromFile(new File(filePath)), this);
    }

    @Override
    public void onPredictionCompleted(String ingredients) {
        mView.hideMaterialProgressDialog();
    }
}
