package com.razor.droidboard.utilities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.razor.droidboard.R;
import com.razor.droidboard.models.User;
import com.razor.droidboard.screens.HomeScreen;

/**
 * Created by Grant on 2016/05/04.
 */
public class AppStatics
{
    public static final int INVITE_NOTIFICATION_ID = 34521234;
    public static final String PREFS_NAME = "SyncPreferences";

    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L;
    public static final long SYNC_INTERVAL_HOUR = SYNC_INTERVAL_IN_MINUTES * SECONDS_PER_MINUTE;
    public static final long SYNC_INTERVAL_DAILY = SYNC_INTERVAL_HOUR * 24;
    public static final long SYNC_INTERVAL_WEEK = SYNC_INTERVAL_DAILY * 7;
    public static final long SYNC_INTERVAL_DEV = 60L;

    public static void loadImage(Context context, String imgUrl, ImageView imgvwProfilePic)
    {
        String baseurl = null;
//        String baseurl = (new ApiClient()).getBasePath();
        if (imgUrl.contains("logboxrest_v2"))
        {
            baseurl = baseurl.replace("logboxrest_v2", "");
        }
        if (!imgUrl.contains(baseurl))
        {
            imgUrl = baseurl + imgUrl;
        }

        Log.i("IMAGE_URL", imgUrl);

        Glide.with(context).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).centerCrop().crossFade().error(R.color.lightGrey).into(imgvwProfilePic);
    }

    public static void logUserOut(Context context)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(INVITE_NOTIFICATION_ID);

        AccountManager am = AccountManager.get(context);
        Account[] accounts = am.getAccounts();

        for (Account account : accounts)
        {
            if (account.type.equalsIgnoreCase("com.razor.droidboard"))      //package name
            {
                String accountName = User.getUser().getUsername();

                Account newAccount = new Account(accountName, context.getResources().getString(R.string.sync_account_type));

                if (account == newAccount)
                    Log.i("","");

                ContentResolver.removePeriodicSync(account, context.getResources().getString(R.string.content_authority), Bundle.EMPTY);
                if (Build.VERSION.SDK_INT < 22)
                {
                    am.removeAccount(account, null, null);
                }
                else
                {
                    am.removeAccountExplicitly(account);
                }
            }
        }

        SharedPreferences settings = context.getSharedPreferences(AppStatics.PREFS_NAME, 0);

        SharedPreferences.Editor editor = settings.edit();
        editor.remove("lastChecked");
        editor.remove("inviteCount");
        editor.remove("questionnaireCount");

        editor.apply();

        User.clearUser();

        Intent i = new Intent(context, HomeScreen.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);

    }

    public static void startInboxChecker(Context context)
    {
        Account account = CreateSyncAccount(context);

        Long time = SYNC_INTERVAL_DAILY;

        String versionName = "";
        try
        {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pInfo.versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        User user = User.getUser();

        if (versionName.equalsIgnoreCase("BETA"))
        {
            time = SYNC_INTERVAL_DEV;
        }
        else
        {
            Long userTime = user.getSyncInterval();
            if (userTime != null) time = userTime;
        }

        User.updateTimeInterval(time);

        Integer contentType = user.getSyncContent();
        if (contentType == null)
        {
            user.setSyncContent(User.SYNC_CONTENT_ALL);
        }

        if (user.getSyncEnabled() == null)
        {
            user.setSyncEnabled(true);
        }

        user.setSyncInterval(time);

        User.setUser(user);

        Boolean syncEnabled = user.getSyncEnabled();
        if (syncEnabled == null || syncEnabled == true)
        {
            ContentResolver.addPeriodicSync(account, context.getResources().getString(R.string.content_authority), Bundle.EMPTY, time);
        }
    }

    public static Account CreateSyncAccount(Context context)
    {
        String accountName = User.getUser().getUsername();

        Account newAccount = new Account(accountName, context.getResources().getString(R.string.sync_account_type));

        AccountManager accountManager = (AccountManager) context.getSystemService(context.ACCOUNT_SERVICE);

        boolean succeed = true;

        try
        {
            succeed = accountManager.addAccountExplicitly(newAccount, null, null);
        }
        catch (IllegalArgumentException arg)
        {
            succeed = false;
        }
        catch (Exception ex)
        {
            succeed = false;
        }

        if (succeed)
        {
            Log.i("SYNC", "Account Added");

            ContentResolver.setIsSyncable(newAccount, context.getResources().getString(R.string.content_authority), 1);
            ContentResolver.setSyncAutomatically(newAccount, context.getResources().getString(R.string.content_authority), true);
        }
        else
        {
            Log.i("SYNC", "Account not Added");
        }

        return newAccount;
    }
}
