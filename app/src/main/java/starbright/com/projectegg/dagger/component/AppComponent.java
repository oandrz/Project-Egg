/**
 * Created by Andreas on 7/10/2018.
 */

package starbright.com.projectegg.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import starbright.com.projectegg.dagger.module.AppModule;
import starbright.com.projectegg.dagger.module.DataModule;
import starbright.com.projectegg.data.local.AppLocalDataStore;
import starbright.com.projectegg.data.remote.AppRemoteDataStore;
import starbright.com.projectegg.features.detail.RecipeDetailFragment;
import starbright.com.projectegg.features.ingredients.IngredientsFragment;
import starbright.com.projectegg.features.recipelist.RecipeListFragment;
import starbright.com.projectegg.features.userAccount.UserAccountFragment;

@Singleton
@Component(modules = {AppModule.class, DataModule.class})
public interface AppComponent {
    void inject(AppRemoteDataStore appRemoteDataStore);

    void inject(AppLocalDataStore appLocalDataStore);

    void inject(RecipeListFragment fragment);
    void inject(UserAccountFragment fragment);
    void inject(IngredientsFragment fragment);

    void inject(RecipeDetailFragment fragment);
}
