package ch.epfl.sweng.jassatepfl.injections;

import javax.inject.Singleton;

import ch.epfl.sweng.jassatepfl.BaseActivityWithNavDrawer;
import ch.epfl.sweng.jassatepfl.BaseAppCompatActivity;
import ch.epfl.sweng.jassatepfl.BaseFragmentActivity;
import ch.epfl.sweng.jassatepfl.BaseListActivity;
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
     * Injects providers return values to the BaseListActivity
     *
     * @param activity the activity which will be injected
     */
    void inject(BaseListActivity activity);

    /**
     * Injects providers return values to the BaseFragmentActivity
     *
     * @param activity the activity which will be injected
     */
    void inject(BaseFragmentActivity activity);

    /**
     * Injects providers return values to the BaseFragmentActivity
     *
     * @param activity the activity which will be injected
     */
    void inject(InjectedBaseActivityTest activity);

    /**
     * Injects providers return values to the BaseAppCompatActivity
     *
     * @param activity the activity which will be injected
     */
    void inject(BaseAppCompatActivity activity);

    /**
     * Initializer of the graph
     */
    public final static class Initializer {
        public static Graph init(boolean mockMode) {
            return DaggerGraph.builder().debugDataModule(new DebugDataModule(mockMode)).build();
        }
    }

}
