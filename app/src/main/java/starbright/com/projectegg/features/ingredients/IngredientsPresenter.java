package starbright.com.projectegg.features.ingredients;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import starbright.com.projectegg.data.AppRepository;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider;

class IngredientsPresenter implements IngredientsContract.Presenter {

    private final AppRepository mRepository;
    private final IngredientsContract.View mView;
    private final BaseSchedulerProvider mSchedulerProvider;
    private CompositeDisposable mCompositeDisposable;

    IngredientsPresenter(
            AppRepository repo,
            IngredientsContract.View view,
            BaseSchedulerProvider schedulerProvider) {
        mRepository = repo;
        mView = view;
        mView.setPresenter(this);
        mSchedulerProvider = schedulerProvider;
        mCompositeDisposable = new CompositeDisposable();
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

    @Override
    public void searchIngredient(String query) {
        mCompositeDisposable.add(
                mRepository.searchIngredient(query)
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Consumer<List<Ingredient>>() {
                    @Override
                    public void accept(List<Ingredient> ingredients) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                })
        );
    }
}
