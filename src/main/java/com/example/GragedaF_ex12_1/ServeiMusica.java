package com.example.GragedaF_ex12_1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ServeiMusica  extends Service {
    public static MediaPlayer reproductor;
    private static final int ID_NOTIFICACIO_CREAR=1;

    @Override
    public void onCreate() {
        super.onCreate();
        reproductor=MediaPlayer.create(this,R.raw.musica);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int idArranc) {
        super.onStartCommand(intent, flags, idArranc);
        //Creació de la notificació
        //Notification.Builder notificacio=new Notification.Builder(this)
        //        .setContentTitle("Creant Servei de Música")//títol que descriu la notificació
        //        .setSmallIcon(R.mipmap.ic_launcher)//icono a visualitzar
        //        .setContentText("Informació addicional") //informació més detallada
        //        .setLargeIcon(BitmapFactory.decodeResource(getResources()
        //                ,android.R.drawable.ic_media_play))
        //        .setWhen(System.currentTimeMillis()+1000*60*60)
        //        .setContentInfo("més info")
        //        .setTicker("Text en barra d'estat");

        //PendingIntent intencioPendent=PendingIntent.getActivity(this,0,
        //        new Intent(this,MainActivity.class),0);
        //notificacio.setContentIntent(intencioPendent);

        //Referència que permet manejar les notificacions del sistema
        //NotificationManager nm;
        //nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //    NotificationChannel channel = new NotificationChannel("id", "nom", NotificationManager.IMPORTANCE_HIGH);
        //    notificacio.setChannelId("id");
        //    nm.createNotificationChannel(channel);
        //}
        //Igual que (SensorManager)getSystemService(SENSOR_SERVICE);
        //Igual que (LocationManager)getSystemService(LOCATION_SERVICE);
        //Llança la notificació
        //nm.notify(ID_NOTIFICACIO_CREAR,notificacio.build());

        //El primer paràmetre indica un id per identificar aquesta notificació en el futur, i el segon la notificació
        reproductor.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reproductor.stop();
        //Elimina la notificació si el servei deixa d'estar actiu.
        NotificationManager nm;
        nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(ID_NOTIFICACIO_CREAR);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}