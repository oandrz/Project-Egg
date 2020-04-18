package starbright.com.projectegg.features.home

import android.content.Context
import android.content.Intent
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.features.base.BaseActivity

class HomeActivity : BaseActivity<HomeContract.View, HomePresenter>(), HomeContract.View {

    override fun getLayoutRes(): Int = R.layout.activity_home

    override fun getView(): HomeContract.View = this

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, HomeActivity::class.java)
    }
}
