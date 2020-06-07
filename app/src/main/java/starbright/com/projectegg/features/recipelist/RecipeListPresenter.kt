/**
 * Created by Andreas on 22/9/2018.
 */

package starbright.com.projectegg.features.recipelist

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.R
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.RecipeConfig
import starbright.com.projectegg.data.model.SortOption
import starbright.com.projectegg.enum.RecipeSortCategory
import starbright.com.projectegg.features.base.BasePresenter
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Inject

private const val FIRESTORE_NAME_KEY = "name"
private const val FIRESTORE_DOCUMENT_KEY = "sortOption"
private const val FIRESTORE_IMAGE_URL_KEY = "imageUrl"
class RecipeListPresenter @Inject constructor(
    schedulerProvider: SchedulerProviderContract,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val repository: AppRepository
) : BasePresenter<RecipeListContract.View>(schedulerProvider, compositeDisposable, networkHelper), RecipeListContract.Presenter {

    private var cuisines: MutableSet<String> = mutableSetOf()

    private lateinit var sortOption: List<SortOption>
    private lateinit var firstVersionConfig: RecipeConfig
    private lateinit var config: RecipeConfig

    override fun onCreateScreen() {
        view.run {
            config = provideSearchConfig()
            setupView()
            showFooterLoading()
        }
        loadSortOption()
        loadRecipe(0)
    }

    override fun handleListItemClicked(selectedRecipeId: String) {
        view.showDetail(selectedRecipeId)
    }

    override fun handleRefresh() {
        resetList()
        config = firstVersionConfig
        loadRecipe(0)
    }

    override fun handleLoadMore(lastPosition: Int) {
        view.showFooterLoading()
        loadRecipe(lastPosition)
    }

    override fun handleSortActionClicked() {
        view.showSortBottomSheet(ArrayList(sortOption), config.sortCategory.type)
    }

    override fun handleSortItemSelected(sortType: RecipeSortCategory) {
        config.sortCategory = sortType
        resetList()
    }

    override fun handleFilterActionClicked() {
        view.showFilterBottomSheet(cuisines.toList(), config.cuisine)
    }

    override fun handleFilterItemSelected(cuisine: String) {
        config = firstVersionConfig
        config.cuisine = cuisine
        loadRecipe(0)
    }

    private fun loadSortOption() {
        Firebase.firestore.collection(FIRESTORE_DOCUMENT_KEY)
            .get()
            .addOnSuccessListener { snapshot ->
                sortOption = snapshot
                    .map {
                        SortOption(
                            it.get(FIRESTORE_NAME_KEY).toString(),
                            it.get(FIRESTORE_IMAGE_URL_KEY).toString()
                        )
                    }
            }
            .addOnFailureListener {

            }
    }

    private fun loadRecipe(pos: Int) {
        if (!isConnectedToInternet()) view.showError(R.string.server_connection_error)

        compositeDisposable.add(
            repository.getRecipes(config, pos)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ recipesResult ->
                    recipesResult.forEach { it.cuisines?.mapTo(cuisines) { cuisine -> cuisine } }
                    view.appendRecipes(recipesResult)
                }, { _ ->
                    view.run {
                        hideFooterLoading()
                        showError(text = "Our services are under maintenance, please retry after some time")
                    }
                })
        )
    }

    private fun resetList() {
        view.run {
            clearRecipe()
            showFooterLoading()
        }
    }
}
