package com.razor.droidboard.screens;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.razor.droidboard.R;
import com.razor.droidboard.models.User;
import com.razor.droidboard.utilities.AppStatics;

public class NotificationScreen extends AppCompatActivity
{
    SwitchCompat m_switchNotifications;
    Account m_account;
    Toolbar m_toolbar;
    RadioGroup m_rgrpFrequency;
    TextView m_tvFrequency, m_tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_screen);

        instantiateScreen();
        populateScreen();

        Long timex = User.getUser().getSyncInterval();
        Log.i("SYNC", "timex: "+timex);
    }

    private void populateScreen()
    {
        User user = User.getUser();
        Boolean enabled = user.getSyncEnabled();

        if (enabled != null && enabled == true)
            m_switchNotifications.setChecked(true);
        else
            m_switchNotifications.setChecked(false);
    }

    private void instantiateScreen()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        m_switchNotifications = (SwitchCompat) findViewById(R.id.switchNotifications);
        m_switchNotifications.setOnCheckedChangeListener(m_switchClick);
        m_tvFrequency = (TextView) findViewById(R.id.tvFrequency);
        m_tvContent = (TextView) findViewById(R.id.tvContent);
//        m_toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (m_account == null) m_account = User.getUserAccount(NotificationScreen.this);
    }

    CompoundButton.OnCheckedChangeListener m_switchClick = new CompoundButton.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            if (isChecked)
                enableSyncAdapter();
            else
                disableSyncAdapter();
        }
    };

    private void disableSyncAdapter()
    {
        m_tvFrequency.setClickable(false);
        m_tvFrequency.setAlpha(0.5f);
        m_tvContent.setClickable(false);
        m_tvContent.setAlpha(0.5f);

        ContentResolver.removePeriodicSync(m_account, this.getResources().getString(R.string.content_authority), Bundle.EMPTY);

        User user = User.getUser();
        user.setSyncEnabled(false);
        user.save();
    }

    private void enableSyncAdapter()
    {
        m_tvFrequency.setClickable(true);
        m_tvFrequency.setAlpha(1f);
        m_tvContent.setClickable(true);
        m_tvContent.setAlpha(1f);

        Long time = AppStatics.SYNC_INTERVAL_DAILY;
        Long userTime = User.getUser().getSyncInterval();
        if (userTime != null) time = userTime;

        ContentResolver.addPeriodicSync(m_account, this.getResources().getString(R.string.content_authority), Bundle.EMPTY, time);

        User user = User.getUser();
        user.setSyncEnabled(true);
        user.save();

        User.updateTimeInterval(time);
    }

    public void frequencyClick(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notification Frequency");

        View popup = LayoutInflater.from(this).inflate(R.layout.notification_frequency_popup, null, false);
        m_rgrpFrequency = (RadioGroup) popup.findViewById(R.id.rgrpFrequency);

        User user = User.getUser();
        Long freq = user.getSyncInterval();

        if (freq != null)
        {
            if (freq == AppStatics.SYNC_INTERVAL_HOUR)
                ((RadioButton) (m_rgrpFrequency.getChildAt(0))).setChecked(true);
            else if (freq == AppStatics.SYNC_INTERVAL_DAILY)
                ((RadioButton) (m_rgrpFrequency.getChildAt(1))).setChecked(true);
            else if (freq == AppStatics.SYNC_INTERVAL_WEEK)
                ((RadioButton) (m_rgrpFrequency.getChildAt(2))).setChecked(true);
        }

        builder.setView(popup);
        builder.setPositiveButton("Confirm", m_frequencyConfirm);
        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    public void contentClick(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notification Content");

        View popup = LayoutInflater.from(this).inflate(R.layout.notification_frequency_popup, null, false);
        m_rgrpFrequency = (RadioGroup) popup.findViewById(R.id.rgrpFrequency);
        TextView tvDescription = (TextView) popup.findViewById(R.id.tvDescription);
        tvDescription.setText("What type of content do you wish to receive notifications for?");

        ((RadioButton) (m_rgrpFrequency.getChildAt(0))).setText("Quotes");
        ((RadioButton) (m_rgrpFrequency.getChildAt(1))).setText("ALL");
        ((RadioButton) (m_rgrpFrequency.getChildAt(2))).setVisibility(View.GONE);

        User user = User.getUser();
        Integer contentType = user.getSyncContent();

        if (contentType != null)
        {
            if (contentType == User.SYNC_CONTENT_QUOTES)
                ((RadioButton) (m_rgrpFrequency.getChildAt(0))).setChecked(true);
            else if (contentType == User.SYNC_CONTENT_ALL)
                ((RadioButton) (m_rgrpFrequency.getChildAt(1))).setChecked(true);
        }

        builder.setView(popup);
        builder.setPositiveButton("Confirm", m_contentConfirm);
        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    DialogInterface.OnClickListener m_frequencyConfirm = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            int selectedId = m_rgrpFrequency.getCheckedRadioButtonId();

            if (selectedId == R.id.radioDaily) activateDaily();
            else if (selectedId == R.id.radioWeekly) activateWeekly();
            else if (selectedId == R.id.radioHourly) activateHourly();
        }
    };

    DialogInterface.OnClickListener m_contentConfirm = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            int selectedId = m_rgrpFrequency.getCheckedRadioButtonId();
            User user = User.getUser();

            if (selectedId == R.id.radioDaily)
                user.setSyncContent(User.SYNC_CONTENT_QUOTES);
            else if (selectedId == R.id.radioWeekly)
                user.setSyncContent(User.SYNC_CONTENT_ALL);

            User.setUser(user);
        }
    };

    private void activateHourly()
    {
        ContentResolver.removePeriodicSync(m_account, this.getResources().getString(R.string.content_authority), Bundle.EMPTY);

        ContentResolver.addPeriodicSync(m_account, this.getResources().getString(R.string.content_authority), Bundle.EMPTY, AppStatics.SYNC_INTERVAL_HOUR);
        User.updateTimeInterval(AppStatics.SYNC_INTERVAL_HOUR);
    }

    private void activateWeekly()
    {
        ContentResolver.removePeriodicSync(m_account, this.getResources().getString(R.string.content_authority), Bundle.EMPTY);

        ContentResolver.addPeriodicSync(m_account, this.getResources().getString(R.string.content_authority), Bundle.EMPTY, AppStatics.SYNC_INTERVAL_WEEK);
        User.updateTimeInterval(AppStatics.SYNC_INTERVAL_WEEK);
    }

    private void activateDaily()
    {
        ContentResolver.removePeriodicSync(m_account, this.getResources().getString(R.string.content_authority), Bundle.EMPTY);

        ContentResolver.addPeriodicSync(m_account, this.getResources().getString(R.string.content_authority), Bundle.EMPTY, AppStatics.SYNC_INTERVAL_DAILY);
        User.updateTimeInterval(AppStatics.SYNC_INTERVAL_DAILY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
