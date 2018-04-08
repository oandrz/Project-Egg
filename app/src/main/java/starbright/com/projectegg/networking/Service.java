package starbright.com.projectegg.networking;

import io.reactivex.Flowable;

/**
 * Created by Andreas on 4/8/2018.
 */

public class Service {
    private final NetworkService mNetworkService;

    public Service(NetworkService networkService) {
        mNetworkService = networkService;
    }

    public Flowable<BaseResponse> getRecipes(String ingredients) {
        return mNetworkService.getRecipes(ingredients);
    }
}
