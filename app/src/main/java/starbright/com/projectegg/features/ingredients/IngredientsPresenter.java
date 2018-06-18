package starbright.com.projectegg.features.ingredients;

class IngredientsPresenter implements IngredientsContract.Presenter {

    private final IngredientsContract.View mView;

    public IngredientsPresenter(IngredientsContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.setupEtSearchIngredients();
    }

    @Override
    public void onActionButtonClicked(String query) {
        if (query.isEmpty()) {
            mView.openCamera();
        } else {
            mView.clearEtSearchQuery();
        }
    }
}
