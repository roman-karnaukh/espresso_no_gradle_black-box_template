package com.espresso.black_box.activity;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.espresso.black_box.R;


/**
 * Created by Karnaukh Roman on 16.08.2016.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class NotifyService extends IntentService {

    String TAG = "ROBO_message";

    public NotifyService() {
        super("NotifyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String message = intent.getExtras().getString("message");

        try {
            if(intent.getStringExtra("type").equals("notify")){
                makeNotify(message);
            }else{
                makeToast(message);
            }
        }catch ( RuntimeException err){
            makeNotify(message);
            makeToast(message);
        }

        return super.onStartCommand(intent,flags,startId);
    }


    public void makeNotify(String message) {

        Intent notificationIntent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = this.getResources();
        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.coffe_small)
                // большая картинка
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.coffee_beans))
                //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                .setTicker(message)
//                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                .setContentTitle("test status")
                //.setContentText(res.getString(R.string.notifytext))
                .setContentText(message); // Текст уведомления

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(5, notification);
    }

    public void makeToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);

        LinearLayout toastContainer = (LinearLayout) toast.getView();

        ImageView espressoImageView = new ImageView(this);
        LinearLayout imageContainer = new LinearLayout(this);
        TextView toastMessage = (TextView) toastContainer.findViewById(android.R.id.message);

        espressoImageView.setImageResource(R.mipmap.espresso);
        espressoImageView.setLayoutParams(new LinearLayout.LayoutParams(35, 35));

        imageContainer.addView(espressoImageView);
        toastContainer.addView(imageContainer);

        toastMessage.setTextSize(20);
        toastMessage.setTextColor(Color.parseColor("#30D5C8"));

        ((ViewGroup)toastMessage.getParent()).removeView(toastMessage);//The specified child already has a parent. You must call removeView() on the child's parent first.
        imageContainer.addView(toastMessage);
        toastContainer.setBackgroundColor(Color.TRANSPARENT);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 1500);
    }
}
