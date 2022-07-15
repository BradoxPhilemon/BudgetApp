package com.mybudgetmanager.mybudget.settings.notifications;

import android.content.Context;

import com.allyants.notifyme.NotifyMe;
import com.mybudgetmanager.mybudget.R;


public class Notifications {

    private final Context context;
    private final NotifyMe notifyMe;

    public Notifications(Context context) {
        this.context = context;
        this.notifyMe = buildNotify();
    }

    private NotifyMe buildNotify() {
        NotifyMe.Builder notifyMe = new NotifyMe.Builder(this.context);

        notifyMe.title(context.getResources().getString(R.string.notification_content_title));
        notifyMe.content(context.getResources().getString(R.string.notification_content_text));
        notifyMe.rrule("FREQ=DAILY;INTERVAL=1");
        notifyMe.small_icon(R.drawable.logo);
        notifyMe.delay(0);
        return notifyMe.build();
    }
}
