package com.dream.Setting;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.dream.User.GlobalVar;
import com.dream.User.UserNetUtil;
import com.dream.XueBaApp2015.R;

/**
 * Created by 夏目 on 2015/11/14.
 */
public class ModifyInfo extends AppCompatActivity{
    private String newNick;
    private String newName;
    private String newSign;
    private ProgressDialog mDialog;
    private EditText modifyNick;
    private EditText modifyName;
    private EditText modifySign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setTitle("修改信息");
        actionBar.setDisplayHomeAsUpEnabled(true);
        modifyNick = (EditText) findViewById(R.id.activity_modify_info_nick);
        modifyName = (EditText) findViewById(R.id.activity_modify_info_name);
        modifySign = (EditText) findViewById(R.id.activity_modify_info_sign);
        modifyNick.setText(GlobalVar.user.getNick());
        if (!GlobalVar.user.getRealName().isEmpty()){
            modifyName.setText(GlobalVar.user.getRealName());
        }
        if (!GlobalVar.user.getDescription().isEmpty()){
            modifySign.setText(GlobalVar.user.getDescription());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sample, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_sample);
        menuItem.setTitle("保存修改");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_sample:
                saveModify();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveModify(){
        newNick=modifyNick.getText().toString();
        newName=modifyName.getText().toString();
        newSign=modifySign.getText().toString();
        mDialog = ProgressDialog.show(this,"请等待...","正在更新信息...",true);
        new Thread(modifyRunnable).start();
    }
    Runnable modifyRunnable = new Runnable() {
        @Override
        public void run() {
            int uid = GlobalVar.user.getUid();
            int modifyInfo = UserNetUtil.ModifyInformation(uid, GlobalVar.user.getEmail(), newNick, newName, newSign);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putInt("modifyInfo",modifyInfo);
            msg.setData(data);
            modifyHandler.sendMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler modifyHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            int modifyInfo = data.getInt("modifyInfo");
            if (modifyInfo>0){
                mDialog.dismiss();
                finish();
            } else if (modifyInfo==UserNetUtil.SERVLET_ERROR){
                Toast.makeText(ModifyInfo.this,"服务器异常！",Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            } else {
                mDialog.dismiss();
                Toast.makeText(ModifyInfo.this,"修改失败！此昵称已被他人使用！",Toast.LENGTH_SHORT).show();
            }
        }
    };
}
