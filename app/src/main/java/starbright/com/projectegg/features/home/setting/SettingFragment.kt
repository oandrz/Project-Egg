package starbright.com.projectegg.features.home.setting

import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.FragmentComponent
import starbright.com.projectegg.features.base.BaseFragment

class SettingFragment: BaseFragment<SettingContract.View, SettingPresenter>(), SettingContract.View {
    override fun getLayoutRes(): Int = R.layout.fragment_setting

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun getViewContract(): SettingContract.View = this

    companion object {
        const val TAG = "Setting"
        fun newInstance(): SettingFragment = SettingFragment()
    }
}