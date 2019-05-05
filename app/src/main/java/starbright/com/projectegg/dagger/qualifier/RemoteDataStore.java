/**
 * Created by Andreas on 5/5/2019.
 */

package starbright.com.projectegg.dagger.qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

@Qualifier
@Retention(RetentionPolicy.SOURCE)
public @interface RemoteDataStore {
}
