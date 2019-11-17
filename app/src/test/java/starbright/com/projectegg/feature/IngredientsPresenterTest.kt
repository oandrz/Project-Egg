/**
 * Created by Andreas on 17/11/2019.
 */

package starbright.com.projectegg.feature

import android.arch.core.executor.testing.InstantTaskExecutorRule
import id.zelory.compressor.Compressor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.features.ingredients.IngredientsContract
import starbright.com.projectegg.features.ingredients.IngredientsPresenter
import starbright.com.projectegg.util.IngredientRecognizer
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.TestSchedulerProvider

@RunWith(MockitoJUnitRunner::class)
class IngredientsPresenterTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockNetworkHelper: NetworkHelper

    @Mock
    private lateinit var compressor: Compressor

    @Mock
    private lateinit var repository: AppRepository

    @Mock
    private lateinit var imageRecognizer: IngredientRecognizer

    private lateinit var presenter: IngredientsContract.Presenter
    private lateinit var testScheduler: TestScheduler

    @Before
    fun setup() {
        testScheduler = TestScheduler()
        val schedulerProvider = TestSchedulerProvider(testScheduler)
        presenter = IngredientsPresenter(
            schedulerProvider,
            CompositeDisposable(),
            mockNetworkHelper,
            compressor,
            repository,
            imageRecognizer
        )
    }

    @Test
    fun test() {

    }
}