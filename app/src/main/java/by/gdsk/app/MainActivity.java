package by.gdsk.app;
import android.app.*;
import android.os.*;
import android.content.*;
import android.webkit.*;
import androidx.annotation.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends Activity {
 private WebView web;
 @Override public void onCreate(Bundle state){super.onCreate(state);if(Build.VERSION.SDK_INT>=33&&checkSelfPermission("android.permission.POST_NOTIFICATIONS")!=android.content.pm.PackageManager.PERMISSION_GRANTED)requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"},41);
  web=new WebView(this);WebSettings s=web.getSettings();s.setJavaScriptEnabled(true);s.setDomStorageEnabled(true);s.setAllowFileAccess(true);web.addJavascriptInterface(new NativeBridge(),"GDSKNative");web.setWebChromeClient(new WebChromeClient());web.setWebViewClient(new WebViewClient());setContentView(web);web.loadUrl("file:///android_asset/index.html");
 }
 public class NativeBridge {
  @JavascriptInterface public void notifyAt(long millis,String title,String body){if(millis<=System.currentTimeMillis())return;Intent i=new Intent(MainActivity.this,AlarmReceiver.class);i.putExtra("title",title);i.putExtra("body",body);int id=(int)(millis%Integer.MAX_VALUE);PendingIntent p=PendingIntent.getBroadcast(MainActivity.this,id,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);if(am!=null){if(Build.VERSION.SDK_INT>=23)am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,millis,p);else am.setExact(AlarmManager.RTC_WAKEUP,millis,p);}}
 }
 @Override public void onBackPressed(){if(web!=null&&web.canGoBack())web.goBack();else super.onBackPressed();}
}