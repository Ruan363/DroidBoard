package com.razor.droidboard.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Grant on 2016/04/19.
 */
public class SyncService extends Service
{
    private static InviteSyncAdapter sInviteSyncAdapter = null;

    private static final Object sInviteSyncAdapterLock = new Object();

    public SyncService()
    {
        super();
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        synchronized (sInviteSyncAdapterLock)
        {
            if (sInviteSyncAdapter == null)
            {
                sInviteSyncAdapter = new InviteSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return sInviteSyncAdapter.getSyncAdapterBinder();
    }
}
