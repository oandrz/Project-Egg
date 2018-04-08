package starbright.com.projectegg.di;

import javax.inject.Singleton;

import dagger.Component;
import starbright.com.projectegg.networking.NetworkModule;

/**
 * Created by Andreas on 4/8/2018.
 */

@Singleton
@Component(modules = {NetworkModule.class})
public interface Deps {
}
