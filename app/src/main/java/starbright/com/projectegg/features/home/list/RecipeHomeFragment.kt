package starbright.com.projectegg.features.home.list

import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.FragmentComponent
import starbright.com.projectegg.features.base.BaseFragment

class RecipeHomeFragment: BaseFragment<RecipeHomeContract.View, RecipeHomePresenter>(), RecipeHomeContract.View {
    override fun getLayoutRes(): Int = R.layout.fragment_recipe_home

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun getViewContract(): RecipeHomeContract.View = this

    companion object {
        const val TAG = "Recipe Home"
        fun newInstance(): RecipeHomeFragment {
            return RecipeHomeFragment()
        }
    }
}