/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.features.userAccount

import starbright.com.projectegg.features.base.BasePresenter
import starbright.com.projectegg.features.base.BaseView

class UserAccountContract {

    interface View : BaseView<Presenter> {
        fun updateView(isLogin: Boolean)

        fun navigateToSearchRecipePage()

        fun disableView()

        fun enableView()

        fun setupProgressBar()

        fun showProgressBar()

        fun hideProgressBar()

        fun showEmailEmptyErrorToast()

        fun showEmailFormatWrongErrorToast()

        fun showPasswordErrorToast()

        fun showLoginErrorToast(errorMessage: String?)

        fun showVerificationEmailSentDialog()
    }

    interface Presenter : BasePresenter {

        fun handleVerificationEmailDialogClicked()

        fun handleAuthenticationButtonClicked(email: String, password: String)

        fun handleOnBackPressedClicked(): Boolean

        fun updateUserAuthRole()

        fun onDestroy()
    }
}
