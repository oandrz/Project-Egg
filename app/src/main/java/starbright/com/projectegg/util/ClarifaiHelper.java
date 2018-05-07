package starbright.com.projectegg.util;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import starbright.com.projectegg.BuildConfig;

/**
 * Created by Andreas on 4/7/2018.
 */

public class ClarifaiHelper {

    private static final int CLARIFAI_REQUEST_TIMEOUT = 30;
    private static final int BYTE_BUFFER_SIZE = 1024;
    private static final float MINIMUM_PREDICTION_PERCENTAGE = 0.99f;

    private final ClarifaiClient mClarifaiClient;
    private Context mContext;

    public ClarifaiHelper(Context context) {
        mClarifaiClient = new ClarifaiBuilder(BuildConfig.CLARIFAI_KEY)
                .client(new OkHttpClient.Builder()
                        .readTimeout(CLARIFAI_REQUEST_TIMEOUT, TimeUnit.SECONDS)
                        .build()
                ).buildSync();
        mContext = context;
    }

    public void predict(Uri uri, Callback callback) {
        try {
            final InputStream iStream = mContext.getContentResolver().openInputStream(uri);
            final byte[] bytesImage = retrieveImage(iStream);
            if (bytesImage != null) {
                new ImageRecognizerTask(bytesImage, mClarifaiClient, callback).execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] retrieveImage(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        final byte[] buffer = new byte[BYTE_BUFFER_SIZE];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public interface Callback {
        void onPredictionCompleted(String ingredients);
    }

    private static class ImageRecognizerTask extends AsyncTask<
            Void, Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>> {
        private byte[] mImageBytes;

        private ClarifaiClient mClarifaiClient;
        private Callback mCallback;

        ImageRecognizerTask(byte[] imageBytes, ClarifaiClient clarifaiClient, Callback callback) {
            mImageBytes = imageBytes;
            mClarifaiClient = clarifaiClient;
            mCallback = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            final List<Concept> data = response.get().get(0).data();
            final List<String> predictions = new ArrayList<>();
            for (Concept concept : data) {
                if (concept.value() > MINIMUM_PREDICTION_PERCENTAGE) {
                    predictions.add(concept.name());
                }
            }
            mCallback.onPredictionCompleted(TextUtils.join(Constants.COMMA, predictions));
        }
    }
}
