/**
 * Created by Andreas on 15/8/2018.
 */

package starbright.com.projectegg.features.detail;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import starbright.com.projectegg.data.AppRepository;
import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider;

class RecipeDetailPresenter implements RecipeDetailContract.Presenter {

    private final AppRepository mRepository;
    private final RecipeDetailContract.View mView;
    private final BaseSchedulerProvider mSchedulerProvider;
    private CompositeDisposable mCompositeDisposable;

    RecipeDetailPresenter(AppRepository repository,
                          RecipeDetailContract.View view,
                          BaseSchedulerProvider schedulerProvider) {
        mRepository = repository;
        mView = view;
        mView.setPresenter(this);
        mSchedulerProvider = schedulerProvider;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void start() {
    }

    @Override
    public void getRecipeDetailInformation(String recipeId) {
        mView.showProgressBar();
        mCompositeDisposable
                .add(mRepository.getRecipeDetailInformation(recipeId)
                        .subscribeOn(mSchedulerProvider.computation())
                        .observeOn(mSchedulerProvider.ui())
                        .subscribe(new Consumer<Recipe>() {
                            @Override
                            public void accept(Recipe recipe) {
                                mView.hideProgressBar();
                                mView.updateView(recipe);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                mView.hideProgressBar();
                            }
                        }));
    }
}
