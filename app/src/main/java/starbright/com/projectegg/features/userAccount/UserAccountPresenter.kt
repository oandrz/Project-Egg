/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.features.userAccount

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import starbright.com.projectegg.util.Constants
import java.util.regex.Pattern

class UserAccountPresenter(
        private val mView: UserAccountContract.View,
        isLogin: Boolean
) : UserAccountContract.Presenter {

    private val mAuth: FirebaseAuth

    private var isLoginAuthentication: Boolean = false

    private val mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        if (isUserEmailVerified(firebaseAuth)) {
            mView.navigateToSearchRecipePage()
        }
    }

    init {
        mView.setPresenter(this)
        isLoginAuthentication = isLogin
        mAuth = FirebaseAuth.getInstance()
    }

    override fun updateUserAuthRole() {
        isLoginAuthentication = !isLoginAuthentication
        mView.updateView(isLoginAuthentication)
    }

    override fun handleVerificationEmailDialogClicked() {
        if (!isLoginAuthentication) {
            updateUserAuthRole()
        }
    }

    override fun handleAuthenticationButtonClicked(email: String, password: String) {
        when {
            email.isEmpty() -> {
                mView.showEmailEmptyErrorToast()
                return
            }
            validateEmailFormat(email) -> {
                mView.showEmailFormatWrongErrorToast()
                return
            }
            password.isEmpty() -> {
                mView.showPasswordErrorToast()
                return
            }
        }

        mView.showProgressBar()
        mView.disableView()
        val authSubscriber = if (isLoginAuthentication)
            login(email, password)
        else
            signup(email, password)

        authSubscriber
                .addOnSuccessListener { authResult ->
                    if (!isLoginAuthentication) {
                        sendUserVerificationEmail(authResult.user)
                    } else {
                        mView.hideProgressBar()
                        mView.enableView()
                        if (!authResult.user.isEmailVerified) {
                            mView.showVerificationEmailSentDialog()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    mView.hideProgressBar()
                    mView.enableView()
                    mView.showLoginErrorToast(e.message)
                }
    }

    override fun handleOnBackPressedClicked(): Boolean {
        return if (!isLoginAuthentication) {
            updateUserAuthRole()
            true
        } else {
            false
        }
    }

    override fun onDestroy() {
        mAuth.removeAuthStateListener(mAuthStateListener)
    }

    override fun start() {
        mAuth.addAuthStateListener(mAuthStateListener)
        mView.setupProgressBar()
        mView.updateView(isLoginAuthentication)
    }

    private fun login(email: String, password: String): Task<AuthResult> {
        return mAuth.signInWithEmailAndPassword(email, password)
    }

    private fun signup(email: String, password: String): Task<AuthResult> {
        return mAuth.createUserWithEmailAndPassword(email, password)
    }

    private fun sendUserVerificationEmail(user: FirebaseUser) {
        user.sendEmailVerification()
                .addOnSuccessListener {
                    mView.hideProgressBar()
                    mView.enableView()
                    mView.showVerificationEmailSentDialog()
                }
                .addOnFailureListener { e ->
                    mView.hideProgressBar()
                    mView.enableView()
                    mView.showLoginErrorToast(e.message)
                }
    }

    private fun isUserEmailVerified(firebaseAuth: FirebaseAuth): Boolean {
        return firebaseAuth.currentUser?.isEmailVerified ?: false
    }

    private fun validateEmailFormat(email: String): Boolean {
        return Pattern.compile(Constants.EMAIL_FORMAT).matcher(email).matches()
    }
}
