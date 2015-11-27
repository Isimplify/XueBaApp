package com.dream.User;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.dream.Helper.FormatValidator;
import com.dream.XueBaApp2015.R;

public class Register extends AppCompatActivity{
    private String username;
    private String nick;
    private String password;
    private ProgressDialog progressDialog;
    private TextInputLayout usernameWrapper;
    private TextInputLayout nickWrapper;
    private TextInputLayout passwordWrapper;
    private TextInputLayout passwordAgainWrapper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameWrapper = (TextInputLayout)findViewById(R.id.activity_register_username);
        nickWrapper = (TextInputLayout)findViewById(R.id.activity_register_nick);
        passwordWrapper = (TextInputLayout)findViewById(R.id.activity_register_password);
        passwordAgainWrapper = (TextInputLayout)findViewById(R.id.activity_register_passwordAgain);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setTitle("注册");

        usernameWrapper.getEditText().addTextChangedListener(new MyUserNameTextWatcher(usernameWrapper,
                "非法的用户名！"));
        nickWrapper.getEditText().addTextChangedListener(new MyNickTextWatcher(nickWrapper,
                "非法的昵称！"));
        passwordWrapper.getEditText().addTextChangedListener(new MyPasswordTextWatcher(passwordWrapper,
                "只能使用字母、数字和下划线，长度不能小于6位或大于20位！"));
        passwordAgainWrapper.getEditText().addTextChangedListener(new MyPasswordAgainTextWatcher(passwordWrapper,
                passwordAgainWrapper,
                "两次输入的密码不相同！"));
    }

    Runnable registerRunnable=new Runnable() {
        @Override
        public void run() {
            int uid = UserNetUtil.Register(username,nick,password);
            Message msg = new Message(); // 向Handler发送消息
            Bundle data = new Bundle(); // data的载体
            data.putInt("uid",uid);
            msg.setData(data);
            registerHandler.sendMessage(msg);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler registerHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            int uid = data.getInt("uid");
            if(uid>0){
                Toast.makeText(Register.this,"注册成功！",Toast.LENGTH_SHORT).show();
                finish();
            } else if (uid==UserNetUtil.SERVLET_ERROR){
                Toast.makeText(Register.this, "服务器异常！", Toast.LENGTH_SHORT)
                        .show();
            }
            progressDialog.dismiss();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sample,menu);
        MenuItem menuItem = menu.findItem(R.id.menu_sample);
        menuItem.setTitle("注册");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_sample:
                username=usernameWrapper.getEditText().getText().toString();
                nick=nickWrapper.getEditText().getText().toString();
                password=passwordWrapper.getEditText().getText().toString();
                String passwordAgain=passwordAgainWrapper.getEditText().getText().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(passwordAgainWrapper.getWindowToken(), 0);
                if (passwordAgain.equals(password)
                        && FormatValidator.ValidateEmail(username)
                        && FormatValidator.ValidatePassword(password)){
                    progressDialog=ProgressDialog.show(Register.this,"请等待...","正在注册...",true);
                    new Thread(registerRunnable).start();
                } else {
                    Toast.makeText(Register.this,"注册信息有误！请检查无误后再注册！",Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyUserNameTextWatcher implements TextWatcher {
        private TextInputLayout textInputLayout;
        private String errorInfo;
        public MyUserNameTextWatcher(TextInputLayout textInputLayout,String errorInfo){
            this.textInputLayout=textInputLayout;
            this.errorInfo=errorInfo;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!FormatValidator.ValidateEmail(s.toString())) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(errorInfo);
            } else {
                textInputLayout.setErrorEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class MyNickTextWatcher implements TextWatcher{
        private TextInputLayout textInputLayout;
        private String errorInfo;
        public MyNickTextWatcher(TextInputLayout textInputLayout,String errorInfo){
            this.textInputLayout=textInputLayout;
            this.errorInfo=errorInfo;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length()>20){
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(errorInfo);
            } else {
                textInputLayout.setErrorEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
    private class MyPasswordTextWatcher implements TextWatcher{
        private TextInputLayout textInputLayout;
        private String errorInfo;
        public MyPasswordTextWatcher(TextInputLayout textInputLayout,String errorInfo){
            this.textInputLayout=textInputLayout;
            this.errorInfo=errorInfo;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!FormatValidator.ValidatePassword(s.toString())){
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(errorInfo);
            } else {
                textInputLayout.setErrorEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
    private class MyPasswordAgainTextWatcher implements TextWatcher{
        private TextInputLayout pswLayout;
        private TextInputLayout pswAgainLayout;
        private String errorInfo;
        public MyPasswordAgainTextWatcher(TextInputLayout pswLayout,TextInputLayout pswAgainLayout, String errorInfo){
            this.pswLayout=pswLayout;
            this.pswAgainLayout=pswAgainLayout;
            this.errorInfo=errorInfo;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(pswLayout.getEditText().getText().toString())){
                pswAgainLayout.setErrorEnabled(true);
                pswAgainLayout.setError(errorInfo);
            } else {
                pswAgainLayout.setErrorEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
