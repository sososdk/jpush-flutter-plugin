package com.jiguang.jpush;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 接收第三方厂商通知
 */
public class NativePushActivity extends Activity {
  private static final String TAG = "OpenClickActivity";
  /** 消息Id **/
  private static final String KEY_MSGID = "msg_id";
  /** 该通知的下发通道 **/
  private static final String KEY_WHICH_PUSH_SDK = "rom_type";
  /** 通知标题 **/
  private static final String KEY_TITLE = "n_title";
  /** 通知内容 **/
  private static final String KEY_CONTENT = "n_content";
  /** 通知附加字段 **/
  private static final String KEY_EXTRAS = "n_extras";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    finish();

    handIntent(getIntent());
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    handIntent(intent);
  }

  private void handIntent(Intent intent) {
    try {
      String extra = null;

      //获取华为平台附带的jpush信息
      if (getIntent().getData() != null) {
        extra = getIntent().getData().toString();
      }

      //获取fcm/oppo/小米/vivo 平台附带的jpush信息
      if (TextUtils.isEmpty(extra) && getIntent().getExtras() != null) {
        extra = getIntent().getExtras().getString("JMessageExtra");
      }

      JSONObject json = new JSONObject(extra);
      String msgId = json.optString(KEY_MSGID);
      byte whichPushSDK = (byte) json.optInt(KEY_WHICH_PUSH_SDK);
      String title = json.optString(KEY_TITLE);
      String content = json.optString(KEY_CONTENT);
      Map<String, Object> extras = toMap(json.getJSONObject(KEY_EXTRAS));

      JPushPlugin.transmitNotificationOpen(title, content, extras);
      Intent launch = getPackageManager().getLaunchIntentForPackage(getPackageName());
      if (launch != null) {
        launch.addCategory(Intent.CATEGORY_LAUNCHER);
        launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(launch);
      }

      JPushInterface.reportNotificationOpened(this, msgId, whichPushSDK);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  private String getPushSDKName(byte whichPushSDK) {
    String name;
    switch (whichPushSDK) {
      case 1:
        name = "xiaomi";
        break;
      case 2:
        name = "huawei";
        break;
      case 3:
        name = "meizu";
        break;
      case 4:
        name = "oppo";
        break;
      case 5:
        name = "vivo";
        break;
      case 8:
        name = "fcm";
        break;
      default:
        name = "jpush";
    }
    return name;
  }

  public static Map<String, Object> toMap(JSONObject json) throws JSONException {
    Map<String, Object> map = new HashMap<>();
    Iterator<String> keys = json.keys();
    while (keys.hasNext()) {
      String key = keys.next();
      Object value = json.get(key);
      if (value instanceof JSONArray) {
        value = toList((JSONArray) value);
      } else if (value instanceof JSONObject) {
        value = toMap((JSONObject) value);
      }
      map.put(key, value);
    }
    return map;
  }

  public static List<Object> toList(JSONArray array) throws JSONException {
    List<Object> list = new ArrayList<>();
    for (int i = 0; i < array.length(); i++) {
      Object value = array.get(i);
      if (value instanceof JSONArray) {
        value = toList((JSONArray) value);
      } else if (value instanceof JSONObject) {
        value = toMap((JSONObject) value);
      }
      list.add(value);
    }
    return list;
  }
}
