/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.features.userAccount

import android.content.Context
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.fragment_user_account.*
import starbright.com.projectegg.MyApp
import starbright.com.projectegg.R
import starbright.com.projectegg.features.base.IOnBackPressedListener
import java.util.*
import javax.inject.Inject

class UserAccountFragment : Fragment(), UserAccountContract.View, IOnBackPressedListener {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var mPresenter: UserAccountContract.Presenter
    private var mFragmentListener: FragmentListener? = null

    override fun onAttach(context: Context?) {
        MyApp.getAppComponent().inject(this)
        super.onAttach(context)
        mFragmentListener = context as FragmentListener?
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        UserAccountPresenter(this, true)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_account, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View,
                               savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.start()
    }

    override fun onDetach() {
        mFragmentListener = null
        super.onDetach()
    }

    override fun onDestroyView() {
        mPresenter.onDestroy()
        super.onDestroyView()
    }

    @OnClick(R.id.tv_navigation, R.id.btn_confirm)
    internal fun onButtonClicked(view: View) {
        when (view.id) {
            R.id.tv_navigation -> mPresenter.updateUserAuthRole()
            R.id.btn_confirm -> mPresenter.handleAuthenticationButtonClicked(
                    et_email.text.toString(),
                    et_password.text.toString()
            )
        }
    }

    override fun setPresenter(presenter: UserAccountContract.Presenter) {
        mPresenter = presenter
    }

    override fun updateView(isLogin: Boolean) {
        if (isLogin) {
            tv_login_title.setText(R.string.login_title)
            btn_confirm.setText(R.string.login_button_text)
            tv_navigation.setText(R.string.login_navigation)
        } else {
            tv_login_title.setText(R.string.register_title)
            btn_confirm.setText(R.string.register_button_text)
            tv_navigation.setText(R.string.register_navigation)
        }
    }

    override fun navigateToSearchRecipePage() {
        mFragmentListener!!.navigateToIngredientsActivity()
    }

    override fun disableView() {
        tv_navigation.isEnabled = false
        et_password.isEnabled = false
        btn_confirm.isEnabled = false
        et_email.isEnabled = false
    }

    override fun enableView() {
        tv_navigation.isEnabled = true
        btn_confirm.isEnabled = true
        et_password.isEnabled = true
        et_email.isEnabled = true
    }

    override fun showLoginErrorToast(errorMessage: String?) {
        Snackbar.make(root_layout, errorMessage!!, Snackbar.LENGTH_SHORT)
                .setAction(R.string.general_retry_label) {
                    mPresenter.handleAuthenticationButtonClicked(
                            et_email.text.toString(),
                            et_password.text.toString()
                    )
                }
                .setActionTextColor(resources.getColor(R.color.lightRed))
                .show()
    }

    override fun showVerificationEmailSentDialog() {
        showDialogWithPositiveButtonOnly(
                R.string.register_verification_dialog_success_title,
                R.string.register_verification_dialog_success_message,
                R.string.register_verification_dialog_positive,
                MaterialDialog.SingleButtonCallback { dialog, which -> mPresenter!!.handleVerificationEmailDialogClicked() }
        )
    }

    override fun showProgressBar() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progress_bar.visibility = View.GONE
    }

    override fun setupProgressBar() {
        if (MyApp.isPreLolipop()) {
            val drawableProgress = DrawableCompat.wrap(
                    progress_bar.indeterminateDrawable)
            DrawableCompat.setTint(
                    drawableProgress,
                    ContextCompat.getColor(Objects.requireNonNull<FragmentActivity>(activity), R.color.red)
            )
            progress_bar.indeterminateDrawable = DrawableCompat.unwrap(drawableProgress)
        } else {
            progress_bar.indeterminateDrawable.setColorFilter(
                    ContextCompat.getColor(Objects.requireNonNull<FragmentActivity>(activity),
                            R.color.red), PorterDuff.Mode.SRC_IN)
        }
    }

    override fun showEmailEmptyErrorToast() {
        showToast(R.string.general_error_email_empty)
    }

    override fun showEmailFormatWrongErrorToast() {
        showToast(R.string.general_error_email_format)
    }

    override fun showPasswordErrorToast() {
        showToast(R.string.general_error_password_empty)
    }

    private fun showToast(@StringRes messageRes: Int) {
        Toast.makeText(activity, getString(messageRes), Toast.LENGTH_SHORT).show()
    }

    private fun showDialogWithPositiveButtonOnly(@StringRes titleResId: Int,
                                                 @StringRes messageResId: Int,
                                                 @StringRes positiveTextResId: Int,
                                                 callback: MaterialDialog.SingleButtonCallback
    ) {
        MaterialDialog.Builder(activity!!)
                .title(titleResId)
                .content(messageResId)
                .contentColor(ContextCompat.getColor(activity!!, R.color.black))
                .positiveText(positiveTextResId)
                .onPositive(callback)
                .canceledOnTouchOutside(false)
                .build()
                .show()
    }

    override fun onBackPressed(): Boolean {
        return mPresenter.handleOnBackPressedClicked()
    }

    internal interface FragmentListener {
        fun navigateToIngredientsActivity()
    }

    companion object {

        fun newInstance(): UserAccountFragment {
            return UserAccountFragment()
        }
    }
}
