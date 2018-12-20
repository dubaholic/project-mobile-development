package org.ap.edu.reportingapp.services;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIIDServiceDemo";
    public String[] name;

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        System.out.println("my firebase token " + token );
    }
    private void sendRegistrationToServer(String token) {

    }
}