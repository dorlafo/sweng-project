package ch.epfl.sweng.jassatepfl.test_utils.injectionsTest;

import javax.inject.Singleton;

import ch.epfl.sweng.jassatepfl.InjectedBaseActivityTest;
import ch.epfl.sweng.jassatepfl.injections.Graph;
import dagger.Component;

/**
 * @author Amaury Combes
 */
@Singleton
@Component(modules = {
        FakeModules.class
})
public interface FakeGraph extends Graph {

    void inject(InjectedBaseActivityTest activityTest);

}
