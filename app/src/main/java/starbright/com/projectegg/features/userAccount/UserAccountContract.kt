/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.features.userAccount

class UserAccountContract {

    interface View {
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

    interface Presenter {

        fun handleVerificationEmailDialogClicked()

        fun handleAuthenticationButtonClicked(email: String, password: String)

        fun handleOnBackPressedClicked(): Boolean

        fun updateUserAuthRole()

        fun onDestroy()
    }
}
