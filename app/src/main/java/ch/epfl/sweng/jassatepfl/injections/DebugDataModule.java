package ch.epfl.sweng.jassatepfl.injections;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;

import dagger.Module;
import dagger.Provides;


/**
 * DebugDataModule is module (i.e. a class that defines a set of providers which are the method
 * annotated with @Provides). The providers can provide their objects in normal mod or mocked mod.
 *
 * @author Amaury Combes
 */
@Module
public final class DebugDataModule {

    /**
     * A DatabaseReference provider
     *
     * @return returns a DatabaseReference that can be mocked or not
     */
    @Provides
    @Singleton
    public DBReferenceWrapper provideDBReference() {
            return new DBReferenceWrapper(FirebaseDatabase.getInstance().getReference());
    }

    /**
     * A firebase authentification provider
     *
     * @return returns a firebase authentification that can be mocked or not
     */
    @Provides
    @Singleton
    public FirebaseAuth provideDBAuth() {
            return FirebaseAuth.getInstance();
    }
}
