/**
 * Created by Andreas on 15/8/2018.
 */

package starbright.com.projectegg.features.detail;

import io.reactivex.disposables.CompositeDisposable;
import starbright.com.projectegg.data.AppRepository;
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider;

class RecipeDetailPresenter implements RecipeDetailContract.Presenter {

    private final AppRepository mRepository;
    private final RecipeDetailContract.View mView;
    private final BaseSchedulerProvider mSchedulerProvider;
    private CompositeDisposable mCompositeDisposable;

    RecipeDetailPresenter(AppRepository repository,
                          RecipeDetailContract.View view,
                          BaseSchedulerProvider schedulerProvider) {
        this.mRepository = repository;
        this.mView = view;
        this.mSchedulerProvider = schedulerProvider;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void start() {

    }
}
