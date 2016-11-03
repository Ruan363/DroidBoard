package com.razor.droidboard.sync;

import android.accounts.Account;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.razor.droidboard.R;
import com.razor.droidboard.models.Quote;
import com.razor.droidboard.models.User;
import com.razor.droidboard.screens.HomeScreen;
import com.razor.droidboard.utilities.AppStatics;
import com.razor.droidboard.utilities.Utils;

/**
 * Created by Ruan on 2016/04/28.
 */
public class InviteSyncAdapter extends AbstractThreadedSyncAdapter
{
    Context m_context;
    ContentResolver m_contentResolver;

    public InviteSyncAdapter(Context context, boolean autoInitialize)
    {
        super(context, autoInitialize);
        m_context = context;
        m_contentResolver = context.getContentResolver();
    }

    @Override
    public void onSecurityException(Account account, Bundle extras, String authority, SyncResult syncResult)
    {
        super.onSecurityException(account, extras, authority, syncResult);
        Log.i("SYNC", "onSecurityException");
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        Log.i("SYNC", "onPerformSync");

        User user = User.getUser();

        if (user == null) return;

        Integer contentType = user.getSyncContent();

        String notificationTitle = null;
        String notificationText = null;

        if (contentType.equals(User.SYNC_CONTENT_QUOTES) || contentType.equals(User.SYNC_CONTENT_ALL))
        {
            List<Quote> quoteList = Quote.listAll(Quote.class);

            if (quoteList != null && quoteList.size() > 0)
            {
                Random r = new Random();
                int quoteNum = r.nextInt(quoteList.size());

                Quote quoteOfTheDay = quoteList.get(quoteNum);
                notificationTitle = quoteOfTheDay.getAuthor();
                notificationText = quoteOfTheDay.getQuote();
            }
        }

        if (!TextUtils.isEmpty(notificationText))
        {
            Log.i("SYNC", "Notification text: " + notificationText);

            if (TextUtils.isEmpty(notificationTitle))
            {
                notificationTitle = Utils.getApplicationName(getContext());
            }

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(getContext().getResources().getColor(R.color.colorPrimary))
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
//                    .setOnlyAlertOnce(true)
//                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

            Intent intent = new Intent(getContext(), HomeScreen.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);

            NotificationManager mNotifyMgr = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyMgr.notify(AppStatics.INVITE_NOTIFICATION_ID, mBuilder.build());
        }
    }
}
