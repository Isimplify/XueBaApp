package com.dream.User;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.XueBaApp2015.FakeActivity;
import com.dream.XueBaApp2015.MainActivity;
import com.dream.XueBaApp2015.R;

public class Login extends AppCompatActivity{
    private EditText usernameEditText;
    private EditText passwordEditText;
    private CheckBox checkBox;
    private ProgressDialog progressDialog;
    private String username,password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("登录");
        TextInputLayout usernameWrapper=(TextInputLayout)findViewById(R.id.activity_login_username);
        usernameEditText=usernameWrapper.getEditText();
        TextInputLayout passwordWrapper=(TextInputLayout)findViewById(R.id.activity_login_password);
        passwordEditText=passwordWrapper.getEditText();

        checkBox=(CheckBox)findViewById(R.id.activity_login_ifRemember);
        TextView registerTextView=(TextView)findViewById(R.id.activity_login_register);

        usernameWrapper.setHint("用户名");
        passwordWrapper.setHint("密码");



        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

    }

    Runnable loginRunnable=new Runnable() {
        @Override
        public void run() {
            int uid = UserNetUtil.Login(username, password);
            Message msg = new Message(); // 向Handler发送消息
            Bundle data = new Bundle(); // data的载体
            data.putInt("uid",uid);
            msg.setData(data);
            loginHandler.sendMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler loginHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            int uid = data.getInt("uid");
            if (uid > 0) {
                rememberAccount(checkBox, uid);
                Toast.makeText(Login.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
                finish();

            } else if (uid == UserNetUtil.SERVLET_ERROR) {
                Toast.makeText(Login.this, "服务器异常！", Toast.LENGTH_SHORT).show();
            } else if (uid == 0) {
                Toast.makeText(Login.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    };

    private void rememberAccount(CheckBox checkBox,int uid){
        if(checkBox.isChecked()){
            SharedPreferences setting = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=setting.edit();
            editor.putInt("uid",uid);
            editor.putBoolean("isSigned",true);
            editor.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sample,menu);
        MenuItem menuItem = menu.findItem(R.id.menu_sample);
        menuItem.setTitle("登录");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(Login.this, FakeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_sample:
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(passwordEditText.getWindowToken(), 0);
                progressDialog = ProgressDialog.show(Login.this, "请等待...", "正在登录...", true);
                new Thread(loginRunnable).start();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent = new Intent(Login.this, FakeActivity.class);
            startActivity(intent);
            finish();
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
