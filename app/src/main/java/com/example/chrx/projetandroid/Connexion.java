package com.example.chrx.projetandroid;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Connexion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        Button buttonNotif = findViewById(R.id.buttonNotif);
        buttonNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Notification added", Toast.LENGTH_SHORT).show();
                createNotification();
            }
        });
    }

    private void createNotification(){
        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final Intent intentNotification = new Intent(this, Connexion.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this,1 , intentNotification,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(this)
                .setWhen(System.currentTimeMillis())
                .setTicker("U just got a notif braw")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(getResources().getString(R.string.notification_title))
                .setContentText(getResources().getString(R.string.notification_desc))
                .setContentIntent(pendingIntent);

        notificationManager.notify(1, builder.build());
    }

}