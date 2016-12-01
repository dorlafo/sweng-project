package ch.epfl.sweng.jassatepfl.injections;

import javax.inject.Singleton;

import ch.epfl.sweng.jassatepfl.BaseActivityWithNavDrawer;
import ch.epfl.sweng.jassatepfl.BaseAppCompatActivity;
import ch.epfl.sweng.jassatepfl.StatsFragment;
import dagger.Component;

/**
 * The Graph interface represents a mapping from the returns of the providers to the field of the
 * injected activity
 *
 * @author Amaury Combes
 */
@Singleton
@Component(modules = {DebugDataModule.class})
public interface Graph {

    /**
     * Injects providers return values to the BaseActivityWithNavDrawer
     *
     * @param activity the activity which will be injected
     */
    void inject(BaseActivityWithNavDrawer activity);

    /**
     * Injects providers return values to the BaseAppCompatActivity
     *
     * @param activity the activity which will be injected
     */
    void inject(BaseAppCompatActivity activity);

    /**
     * Injects providers return values to the BaseAppCompatActivity
     *
     * @param fragment the fragment which will be injected
     */
    void inject(StatsFragment fragment);

    /**
     * Initializer of the graph
     */
    final class Initializer {
        public static Graph init(boolean mockMode) {
            return DaggerGraph.builder().debugDataModule(new DebugDataModule()).build();
        }
    }

}
