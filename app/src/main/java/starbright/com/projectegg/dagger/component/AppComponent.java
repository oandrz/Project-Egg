package starbright.com.projectegg.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import starbright.com.projectegg.dagger.module.AppModule;
import starbright.com.projectegg.dagger.module.DataModule;
import starbright.com.projectegg.data.remote.AppRemoteDataStore;
import starbright.com.projectegg.features.recipelist.RecipeListActivity;
import starbright.com.projectegg.features.recipelist.RecipeListFragment;

/**
 * Created by Andreas on 4/8/2018.
 */

@Singleton
@Component(modules = {AppModule.class, DataModule.class})
public interface AppComponent {
    void inject(AppRemoteDataStore appRemoteDataStore);
    void inject(RecipeListFragment fragment);
}
