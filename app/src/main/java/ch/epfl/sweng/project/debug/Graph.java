package ch.epfl.sweng.project.debug;

import javax.inject.Singleton;
import ch.epfl.sweng.project.BaseActivity;
import ch.epfl.sweng.project.BaseFragmentActivity;
import ch.epfl.sweng.project.BaseListActivity;
import ch.epfl.sweng.project.InjectedBaseActivityTest;
import ch.epfl.sweng.project.debug.data.DebugDataModule;
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
     * Injects providers return values to the InjectedBaseActivityTest
     * @param testActivity the test activity which will be injected
     */
    void inject(InjectedBaseActivityTest testActivity);

    /**
     * Initializer of the graph
     */
    public final static class Initializer {
        public static Graph init(boolean mockMode) {
            return DaggerGraph.builder().debugDataModule(new DebugDataModule(mockMode)).build();
        }
    }
}
