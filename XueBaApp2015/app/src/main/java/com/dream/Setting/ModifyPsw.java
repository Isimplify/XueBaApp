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
public class ModifyPsw extends AppCompatActivity{
    private String oldPsw;
    private String newPsw;
    private String newPswRepeat;
    private ProgressDialog mDialog;
    private EditText modifyPswOld;
    private EditText modifyPswNew;
    private EditText modifyPswRepeat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_psw);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setTitle("修改密码");
        actionBar.setDisplayHomeAsUpEnabled(true);
        modifyPswOld = (EditText) findViewById(R.id.activity_modify_psw_old);
        modifyPswNew = (EditText) findViewById(R.id.activity_modify_psw_new);
        modifyPswRepeat = (EditText) findViewById(R.id.activity_modify_psw_repeat);
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
        oldPsw = modifyPswOld.getText().toString();
        newPsw = modifyPswNew.getText().toString();
        newPswRepeat = modifyPswRepeat.getText().toString();
        if (oldPsw.isEmpty()){
            Toast.makeText(ModifyPsw.this,"旧密码不能为空！",Toast.LENGTH_SHORT).show();
        } else if (newPsw.length()<6){
            Toast.makeText(ModifyPsw.this,"新密码长度不能小于6！",Toast.LENGTH_SHORT).show();
        } else if (!newPsw.equals(newPswRepeat)){
            Toast.makeText(ModifyPsw.this,"两次输入的新密码不相同！",Toast.LENGTH_SHORT).show();
        } else {
            mDialog = ProgressDialog.show(this, "请等待...", "正在更新密码...", true);
            new Thread(modifyRunnable).start();
        }
    }
    Runnable modifyRunnable = new Runnable() {
        @Override
        public void run() {
            int uid = GlobalVar.user.getUid();
            int modifyPsw = UserNetUtil.ModifyPassword(uid, oldPsw, newPsw);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putInt("modifyPsw",modifyPsw);
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
            int modifyPsw = data.getInt("modifyPsw");
            if (modifyPsw>0){
                mDialog.dismiss();
                finish();
            } else if (modifyPsw==UserNetUtil.SERVLET_ERROR){
                Toast.makeText(ModifyPsw.this, "服务器异常！", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            } else {
                mDialog.dismiss();
                Toast.makeText(ModifyPsw.this,"修改失败！错误的旧密码！",Toast.LENGTH_SHORT).show();
            }
        }
    };
}
