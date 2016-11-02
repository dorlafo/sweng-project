package ch.epfl.sweng.project;

import javax.inject.Singleton;

import ch.epfl.sweng.project.data.DebugDataModule;
import dagger.Component;

@Singleton
@Component(modules = {DebugDataModule.class})
public interface Graph {

    void inject(BaseActivity activity);

    void inject(InjectedBaseActivityTest test);

    public final static class Initializer {
        public static Graph init(boolean mockMode) {
            return DaggerGraph.builder().debugDataModule(new DebugDataModule(mockMode)).build();
        }
    }

}
