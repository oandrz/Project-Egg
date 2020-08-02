/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.util

import android.text.TextUtils
import clarifai2.api.ClarifaiBuilder
import clarifai2.api.ClarifaiClient
import clarifai2.dto.input.ClarifaiInput
import clarifai2.dto.model.output.ClarifaiOutput
import clarifai2.dto.prediction.Concept
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.OkHttpClient
import starbright.com.projectegg.BuildConfig
import java.io.File
import java.util.*
import javax.inject.Inject

/**
 * Created by Andreas on 4/7/2018.
 */

class IngredientRecognizer @Inject constructor(
    client: OkHttpClient
) {
    private val recognizerClient: ClarifaiClient = ClarifaiBuilder(BuildConfig.CLARIFAI_KEY)
        .client(client).buildSync()

    fun predict(image: File, onSuccess: (String) -> Unit, onError: () -> Unit) {
        val foodModel = recognizerClient.defaultModels.foodModel()
        foodModel.predict()
            .withInputs(ClarifaiInput.forImage(image))
            .executeAsync({
                val predictions = filterConcept(it)
                AndroidSchedulers.mainThread().scheduleDirect {
                    onSuccess(TextUtils.join(Constants.COMMA, predictions))
                }
            }, {
                AndroidSchedulers.mainThread().scheduleDirect {
                    onError()
                }
            })
    }

    private fun filterConcept(concepts: MutableList<ClarifaiOutput<Concept>>): ArrayList<String> {
        val data = concepts[0].data()
        val predictions = ArrayList<String>()
        for (concept in data) {
            val conceptName = concept.name()
            if (concept.value() > MINIMUM_PREDICTION_PERCENTAGE && conceptName != null) {
                predictions.add(conceptName)
            }
        }
        return predictions
    }

    companion object {
        private const val MINIMUM_PREDICTION_PERCENTAGE = 0.97f
    }
}
