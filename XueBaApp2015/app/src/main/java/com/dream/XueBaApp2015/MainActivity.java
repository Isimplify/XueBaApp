package com.dream.XueBaApp2015;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.Question.AskQuestion;
import com.dream.Question.BestQuestionList;
import com.dream.Question.QuestionList;
import com.dream.Setting.Setting;
import com.dream.User.GlobalVar;
import com.dream.User.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private long exitTime = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("isSigned", "true");
        setContentView(R.layout.activity_main_login);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout_login);
        navigationView = (NavigationView) findViewById(R.id.nav_view_login);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_login);
        TextView nav_header_nick=(TextView)headerView.findViewById(R.id.nav_header_userName_login);
        TextView nav_header_description=(TextView)headerView.findViewById(R.id.nav_header_userDescribe_login);
        if (GlobalVar.user==null){
            Log.e("用户不存在！？", "666");
        } else {
            nav_header_nick.setText(GlobalVar.user.getNick());
            if (GlobalVar.user.getDescription().equals("null")){
                nav_header_description.setText("");
            } else {
                nav_header_description.setText(GlobalVar.user.getDescription());
            }
        }


        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuestion();
                //turn to ask page
            }
        });
        tabLayout.setupWithViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("首页");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mDrawerLayout.isDrawerOpen(navigationView)){
            if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
                mDrawerLayout.closeDrawers();
            }
            return true;
        } else {
            if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
                if((System.currentTimeMillis()-exitTime) > 2000){
                    Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                    System.exit(0);
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void addQuestion() {
        // 添加新问题
        Intent intent = new Intent(MainActivity.this, AskQuestion.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sample, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_sample);
        menuItem.setTitle("设置");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_sample:
                startActivity(new Intent(MainActivity.this, Setting.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new QuestionList(), "未解决问题");
        adapter.addFragment(new BestQuestionList(), "热门问题");
//        adapter.addFragment(new Fragment(), "精彩回答");
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home_login:
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_info_login:
                                Intent intentInfo = new Intent(MainActivity.this, UserInfo.class);
                                startActivity(intentInfo);
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_updownload_login:
                                if (GlobalVar.isSigned) {
                                    //上传下载功能
                                    mDrawerLayout.closeDrawers();
                                    Toast.makeText(MainActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
                                } else {
                                    mDrawerLayout.closeDrawers();
                                    Toast.makeText(MainActivity.this, "您尚未登录，不能使用此功能！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.nav_search_login:
                                if (GlobalVar.isSigned) {
                                    //搜索功能
                                    mDrawerLayout.closeDrawers();
                                    Toast.makeText(MainActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
                                } else {
                                    mDrawerLayout.closeDrawers();
                                    Toast.makeText(MainActivity.this, "您尚未登录，不能使用此功能！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.nav_setting_login:
                                if (GlobalVar.isSigned) {
                                    //设置功能
                                    startActivity(new Intent(MainActivity.this, Setting.class));
                                    mDrawerLayout.closeDrawers();
                                } else {
                                    mDrawerLayout.closeDrawers();
                                    Toast.makeText(MainActivity.this, "您尚未登录，不能使用此功能！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.nav_about_login:
                                if (GlobalVar.isSigned) {
                                    //关于功能
                                    mDrawerLayout.closeDrawers();
                                    Toast.makeText(MainActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
                                } else {
                                    mDrawerLayout.closeDrawers();
                                    Toast.makeText(MainActivity.this, "您尚未登录，不能使用此功能！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.nav_logout_login:
//                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("登出")
                                        .setMessage("真的要登出吗？")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                logout();
                                                refresh();
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).create();
                                alertDialog.show();
                                break;
                        }

                        return true;
                    }
                });
    }



    private void refresh() {
        finish();
        Intent intent = new Intent(MainActivity.this, FakeActivity.class);
        startActivity(intent);

    }
    private void logout(){
        GlobalVar.isSigned = false; // 没有登入
        GlobalVar.user = null; // 清空用户
        SharedPreferences setting = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean("isSigned", false);
        editor.commit();
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
