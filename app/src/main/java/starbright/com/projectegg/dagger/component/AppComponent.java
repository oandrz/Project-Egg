/**
 * Created by Andreas on 7/10/2018.
 */

package starbright.com.projectegg.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import starbright.com.projectegg.dagger.module.AppModule;
import starbright.com.projectegg.dagger.module.NetworkModule;
import starbright.com.projectegg.dagger.module.RepositoryModule;
import starbright.com.projectegg.dagger.module.StorageModule;
import starbright.com.projectegg.features.detail.RecipeDetailFragment;
import starbright.com.projectegg.features.ingredients.IngredientsFragment;
import starbright.com.projectegg.features.recipelist.RecipeListFragment;
import starbright.com.projectegg.features.userAccount.UserAccountFragment;

/**
 * Created by Andreas on 4/8/2018.
 */

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, StorageModule.class, RepositoryModule.class})
public interface AppComponent {
    void inject(RecipeListFragment fragment);
    void inject(UserAccountFragment fragment);
    void inject(IngredientsFragment fragment);

    void inject(RecipeDetailFragment fragment);
}
