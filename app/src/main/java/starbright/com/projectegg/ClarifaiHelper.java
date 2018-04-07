package starbright.com.projectegg;

import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.TimeUnit;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiImage;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import okhttp3.OkHttpClient;

/**
 * Created by Andreas on 4/7/2018.
 */

public class ClarifaiHelper {

    private static final int CLARIFAI_REQUEST_TIMEOUT = 30;

    private ClarifaiClient mClarifaiClient;

    public ClarifaiHelper() {
        mClarifaiClient = new ClarifaiBuilder(BuildConfig.CLARIFAI_KEY)
                .client(new OkHttpClient.Builder()
                        .readTimeout(CLARIFAI_REQUEST_TIMEOUT, TimeUnit.SECONDS)
                        .build()
                ).buildSync();
    }

    public void predict(byte[] imageBytes) {
        new ImageRecognizerTask(imageBytes, mClarifaiClient).execute();
    }

    static class ImageRecognizerTask extends AsyncTask<
            Void, Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>> {
        private byte[] mImageBytes;
        private ClarifaiClient mClarifaiClient;

        ImageRecognizerTask(byte[] imageBytes, ClarifaiClient clarifaiClient) {
            mImageBytes = imageBytes;
            mClarifaiClient = clarifaiClient;
        }

        @Override
        protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(Void... params) {
            final ConceptModel foodModel = mClarifaiClient.getDefaultModels().foodModel();
            return foodModel.predict()
                    .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(mImageBytes)))
                    .executeSync();
        }

        @Override
        protected void onPostExecute(
                ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {
            super.onPostExecute(response);
            final List<ClarifaiOutput<Concept>> predicions = response.get();
        }
    }
}
