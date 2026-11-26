package by.gdsk.app;
import android.content.*;
import android.app.*;
import android.os.Build;
public class AlarmReceiver extends BroadcastReceiver {
 @Override public void onReceive(Context context,Intent intent){
  String title=intent.getStringExtra("title"),body=intent.getStringExtra("body");
  String channel="gdsk_schedule";
  NotificationManager nm=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
  if(Build.VERSION.SDK_INT>=26)nm.createNotificationChannel(new NotificationChannel(channel,"Расписание и звонки",NotificationManager.IMPORTANCE_HIGH));
  Notification.Builder b=Build.VERSION.SDK_INT>=26?new Notification.Builder(context,channel):new Notification.Builder(context);
  b.setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle(title==null?"GDSK":title).setContentText(body==null?"":body).setAutoCancel(true);
  nm.notify((int)(System.currentTimeMillis()%Integer.MAX_VALUE),b.build());
 }
}