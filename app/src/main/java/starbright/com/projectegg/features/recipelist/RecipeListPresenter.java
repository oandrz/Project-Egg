package starbright.com.projectegg.features.recipelist;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import starbright.com.projectegg.data.AppRepository;
import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider;

/**
 * Created by Andreas on 4/14/2018.
 */

class RecipeListPresenter implements RecipeListContract.Presenter {

    private final AppRepository mRepository;
    private final RecipeListContract.View mView;
    private final BaseSchedulerProvider mSchedulerProvider;
    private CompositeDisposable mCompositeDisposable;

    RecipeListPresenter(AppRepository repo,
                               RecipeListContract.View view,
                               BaseSchedulerProvider schedulerProvider) {
        mRepository = repo;
        mView = view;
        mView.setPresenter(this);
        mSchedulerProvider = schedulerProvider;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void start() {

    }

    @Override
    public void getRecipesBasedIngredients(String ingredients) {
        mCompositeDisposable.add(
                mRepository.getRecipes(ingredients)
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Consumer<List<Recipe>>() {
                    @Override
                    public void accept(List<Recipe> recipes) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                })
        );
    }
}
