package starbright.com.projectegg.features.home.list

import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.dagger.scope.FragmentScope
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BasePresenter
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.RxSchedulerProvider
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Inject

@FragmentScope
class RecipeHomePresenter @Inject constructor(
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    schedulerProvider: SchedulerProviderContract,
    private val repository: AppRepository
): BasePresenter<RecipeHomeContract.View>(schedulerProvider, compositeDisposable, networkHelper) {
    override fun onCreateScreen() {
        view.run {
            setupSearchView()
            setupList()
            populateList(mutableListOf(
                Recipe(
                    1,
                    10,
                    10,
                    "Salmon with Fried Chicken",
                    "https://www.narcity.com/uploads/8c04d404157c9ee528cee0aa21dea4955c786459.jpg_facebook.jpg",
                    1000,
                    "",
                    "Testing"
                ),
                Recipe(
                    1,
                    10,
                    10,
                    "Salmon with Fried Chicken",
                    "https://www.narcity.com/uploads/8c04d404157c9ee528cee0aa21dea4955c786459.jpg_facebook.jpg",
                    1000,
                    "",
                    "Testing"
                ),
                Recipe(
                    1,
                    10,
                    10,
                    "Salmon with Fried Chicken",
                    "https://www.narcity.com/uploads/8c04d404157c9ee528cee0aa21dea4955c786459.jpg_facebook.jpg",
                    1000,
                    "",
                    "Testing"
                ),
                Recipe(
                    1,
                    10,
                    10,
                    "Salmon with Fried Chicken",
                    "https://www.narcity.com/uploads/8c04d404157c9ee528cee0aa21dea4955c786459.jpg_facebook.jpg",
                    1000,
                    "",
                    "Testing"
                ),
                Recipe(
                    1,
                    10,
                    10,
                    "Salmon with Fried Chicken",
                    "https://www.narcity.com/uploads/8c04d404157c9ee528cee0aa21dea4955c786459.jpg_facebook.jpg",
                    1000,
                    "",
                    "Testing"
                ),
                Recipe(
                    1,
                    10,
                    10,
                    "Salmon with Fried Chicken",
                    "https://www.narcity.com/uploads/8c04d404157c9ee528cee0aa21dea4955c786459.jpg_facebook.jpg",
                    1000,
                    "",
                    "Testing"
                ),
                Recipe(
                    1,
                    10,
                    10,
                    "Salmon with Fried Chicken",
                    "https://www.narcity.com/uploads/8c04d404157c9ee528cee0aa21dea4955c786459.jpg_facebook.jpg",
                    1000,
                    "",
                    "Testing"
                ),
                Recipe(
                    1,
                    10,
                    10,
                    "Salmon with Fried Chicken",
                    "https://www.narcity.com/uploads/8c04d404157c9ee528cee0aa21dea4955c786459.jpg_facebook.jpg",
                    1000,
                    "",
                    "Testing"
                ),
                Recipe(
                    1,
                    10,
                    10,
                    "Salmon with Fried Chicken",
                    "https://www.narcity.com/uploads/8c04d404157c9ee528cee0aa21dea4955c786459.jpg_facebook.jpg",
                    1000,
                    "",
                    "Testing"
                ),
                Recipe(
                    1,
                    10,
                    10,
                    "Salmon with Fried Chicken",
                    "https://www.narcity.com/uploads/8c04d404157c9ee528cee0aa21dea4955c786459.jpg_facebook.jpg",
                    1000,
                    "",
                    "Testing"
                ),
                Recipe(
                    1,
                    10,
                    10,
                    "Salmon with Fried Chicken",
                    "https://www.narcity.com/uploads/8c04d404157c9ee528cee0aa21dea4955c786459.jpg_facebook.jpg",
                    1000,
                    "",
                    "Testing"
                ),
                Recipe(
                    1,
                    10,
                    10,
                    "Salmon with Fried Chicken",
                    "https://www.narcity.com/uploads/8c04d404157c9ee528cee0aa21dea4955c786459.jpg_facebook.jpg",
                    1000,
                    "",
                    "Testing"
                )
            ))
        }
    }
}