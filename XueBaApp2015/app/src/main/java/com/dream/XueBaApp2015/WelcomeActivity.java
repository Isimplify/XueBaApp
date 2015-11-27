package com.dream.XueBaApp2015;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dream.Update.Config;
import com.dream.Update.NetworkTool;
import com.dream.User.GlobalVar;
import com.dream.User.UserNetUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 夏目 on 2015/11/8.
 */
@SuppressLint("HandlerLeak")
public class WelcomeActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        checkUpdate();

    }

    private void signInAutomatically() {
        SharedPreferences setting = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        final int uid = setting.getInt("uid", 0);
        boolean isSigned = setting.getBoolean("isSigned", false);
        if (!GlobalVar.isSigned && isSigned) {
            new Thread(new Runnable() {
                public void run() {
                    GlobalVar.user = UserNetUtil.GetUserById(uid);
                    Log.e("user is null23333?",GlobalVar.user==null?"true":"false");
                    GlobalVar.isSigned = true;
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).start();
        } else {
            Intent intent = new Intent(WelcomeActivity.this, FakeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isUpdate = false;
                if (getServerVerCode()) {
                    int vercode = Config.getVerCode(WelcomeActivity.this);
                    if (newVerCode > vercode) {
                        // doNewVersionUpdate();
                        isUpdate = true;
                    }
                }
                Message message = new Message();
                Bundle data = new Bundle(); // data的载体
                data.putBoolean("isUpdate", isUpdate);
                message.setData(data);
                updateHandler.sendMessage(message);// 具体消息中包含什么东西并不重要，因为接收的函数不需要该参数
            }
        }).start();
    }

    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean isUpdate = data.getBoolean("isUpdate");
            if (isUpdate)
                doNewVersionUpdate();
            else
                welcomeUI();
        }
    };

    private void welcomeUI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Message message = new Message();
                    welHandler.sendMessage(message);// 具体消息中包含什么东西并不重要，因为接收的函数不需要该参数
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    Handler welHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            welcomeFunction();
        }
    };

    public void welcomeFunction() {
        SharedPreferences preferences1 = getSharedPreferences(
                "xfy_downloadinfo", MODE_PRIVATE);
        String Savepath = preferences1.getString("savepath", "-1000");
        if (Savepath.equals("-1000")) {
            SharedPreferences.Editor edit = preferences1.edit();
            edit.putString("savepath", Environment.getExternalStorageDirectory().getPath()+"/XueBa");
            edit.commit();
        }
        signInAutomatically();

    }

    private static final String TAG = "Update";
    public ProgressDialog pBar;
    private Handler handler = new Handler();

    private int newVerCode = 0;
    private String newVerName = "";

    private boolean getServerVerCode() {
        try {
            String verjson = NetworkTool.getContent(Config.UPDATE_SERVER
                    + Config.UPDATE_VERJSON);
            JSONArray array = new JSONArray(verjson);
            if (array.length() > 0) {
                JSONObject obj = array.getJSONObject(0);
                try {
                    newVerCode = Integer.parseInt(obj.getString("verCode"));
                    newVerName = obj.getString("verName");
                } catch (Exception e) {
                    newVerCode = -1;
                    newVerName = "";
                    return false;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        return true;
    }

    private void doNewVersionUpdate() {
        int verCode = Config.getVerCode(this);
        String verName = Config.getVerName(this);
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本:");
        sb.append(verName);
        sb.append(" Code:");
        sb.append(verCode);
        sb.append(", 发现新版本:");
        sb.append(newVerName);
        sb.append(" Code:");
        sb.append(newVerCode);
        sb.append(", 是否更新?");
        Dialog dialog = new AlertDialog.Builder(WelcomeActivity.this)
                .setTitle("软件更新")
                .setMessage(sb.toString())
                        // 设置内容
                .setPositiveButton("更新",// 设置确定按钮
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                pBar = new ProgressDialog(WelcomeActivity.this);
                                pBar.setTitle("正在下载");
                                pBar.setMessage("请稍候...");
                                pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                downFile(Config.UPDATE_SERVER
                                        + Config.UPDATE_APKNAME);
                            }

                        })
                .setNegativeButton("暂不更新",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                welcomeUI();
                            }
                        }).create();// 创建
        // 显示对话框
        dialog.show();
    }

    void downFile(final String url) {
        pBar.show();
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {

                        File file = new File(
                                Environment.getExternalStorageDirectory(),
                                Config.UPDATE_SAVENAME);
                        fileOutputStream = new FileOutputStream(file);

                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int count = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            count += ch;
                            if (length > 0) {
                            }
                        }

                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }.start();

    }

    void down() {
        handler.post(new Runnable() {
            public void run() {
                pBar.cancel();
                update();
            }
        });

    }

    void update() {
        // 这个广播注册了没用= =留给后人解决吧
        // GetBroadcast.registerReceiver(getApplicationContext());//
        // 注册广播，用于监听应用是否安装完成
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), Config.UPDATE_SAVENAME)),
                "application/vnd.android.package-archive");
        startActivity(intent);
        System.exit(0);
    }
}
