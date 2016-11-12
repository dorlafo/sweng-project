package ch.epfl.sweng.jassatepfl.notification;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import ch.epfl.sweng.jassatepfl.server.ServerInterface;

/**
 * Class implementing registration to notification server
 */
public class JassTokenService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String sciper = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            ServerInterface.getInstance().registerSciperToken(sciper, refreshedToken);
        }
    }
}
