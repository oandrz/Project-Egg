package starbright.com.projectegg.networking;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Andreas on 4/8/2018.
 */

public interface NetworkService {
    @GET("api")
    Flowable<BaseResponse> getRecipes(@Query("i") String ingredients);
}
