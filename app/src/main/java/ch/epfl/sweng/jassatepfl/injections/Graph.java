package ch.epfl.sweng.jassatepfl.injections;

import javax.inject.Singleton;

import ch.epfl.sweng.jassatepfl.BaseActivity;
import ch.epfl.sweng.jassatepfl.BaseFragmentActivity;
import ch.epfl.sweng.jassatepfl.BaseListActivity;
import ch.epfl.sweng.jassatepfl.injections.DaggerGraph;
import dagger.Component;

/**
 * The Graph interface represents a mapping from the returns of the providers to the field of the
 * injected activity
 */
@Singleton
@Component(modules = {DebugDataModule.class})
public interface Graph {

    /**
     * Injects providers return values to the BaseActivity
     * @param activity the activity which will be injected
     */
    void inject(BaseActivity activity);

    /**
     * Injects providers return values to the BaseListActivity
     * @param activity the activity which will be injected
     */
    void inject(BaseListActivity activity);

    /**
     * Injects providers return values to the BaseFragmentActivity
     * @param activity the activity which will be injected
     */
    void inject(BaseFragmentActivity activity);

    /**
     * Initializer of the graph
     */
    public final static class Initializer {
        public static Graph init(boolean mockMode) {
            return DaggerGraph.builder().debugDataModule(new DebugDataModule(mockMode)).build();
        }
    }

}
