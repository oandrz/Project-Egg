package starbright.com.projectegg.features.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.features.base.BaseActivity
import starbright.com.projectegg.features.base.NormalToolbar
import java.lang.ref.WeakReference

class SearchRecipeActivity : BaseActivity<SearchRecipeContract.View, SearchRecipePresenter>(), SearchRecipeContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        setToolbarBehavior(
            NormalToolbar(WeakReference(this), R.id.toolbar, R.string.recipelist_title, false)
        )
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int = R.layout.activity_search_recipe

    override fun getView(): SearchRecipeContract.View = this

    override fun injectDependencies(activityComponent: ActivityComponent) = activityComponent.inject(this)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_recipe, menu)
        val searchView = menu?.findItem(R.id.menu_search)?.actionView as? SearchView
        searchView?.run {
            setIconifiedByDefault(true)
            isIconified = false
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean { return false }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SearchRecipeActivity::class.java)
    }
}
