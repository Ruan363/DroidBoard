package com.razor.droidboard.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by ruan on 4/28/2016.
 */
public class InviteAuthenticatorService extends Service
{
    // Instance field that stores the authenticator object
    private InviteAuthenticator mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new InviteAuthenticator(this);
    }
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
