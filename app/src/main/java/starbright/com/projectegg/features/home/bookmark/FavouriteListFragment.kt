/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.features.home.bookmark

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_favourite.*
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.FragmentComponent
import starbright.com.projectegg.features.base.BaseFragment

class FavouriteListFragment : BaseFragment<FavouriteListContract.View, FavouriteListPresenter>(),
    FavouriteListContract.View {
    override fun getLayoutRes(): Int = R.layout.fragment_favourite

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun getViewContract(): FavouriteListContract.View = this

    override fun setupView() {
        setupToolbar()
    }

    private fun setupToolbar() {
        with((activity as AppCompatActivity)) {
            setSupportActionBar(toolbar)
            supportActionBar?.title = resources.getString(R.string.favourite_text_title_toolbar)
        }
    }

    companion object {
        const val TAG = "Favourite"
        fun newInstance(): FavouriteListFragment = FavouriteListFragment()
    }
}