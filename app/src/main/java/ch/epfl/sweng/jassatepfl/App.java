package ch.epfl.sweng.jassatepfl;

import android.app.Application;

import ch.epfl.sweng.jassatepfl.injections.DaggerGraph;
import ch.epfl.sweng.jassatepfl.injections.DebugDataModule;
import ch.epfl.sweng.jassatepfl.injections.Graph;

public class App extends Application {

    private static App sInstance;
    private Graph graph;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        graph = DaggerGraph.builder().debugDataModule(new DebugDataModule()).build();
    }

    public static App getInstance() {
        return sInstance;
    }

    public void setGraph(Graph newGraph) {
        graph = newGraph;
    }

    public Graph graph() {
        return graph;
    }

    public void setMockMode(boolean useMock) {
        graph = Graph.Initializer.init(useMock);
    }

}
