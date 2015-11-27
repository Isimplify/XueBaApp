package com.dream.XueBaApp2015;

import android.content.Intent;
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

import com.dream.Question.BestQuestionList;
import com.dream.Question.QuestionList;
import com.dream.User.Login;

import java.util.ArrayList;
import java.util.List;

public class FakeActivity extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("isSigned", "true");
        setContentView(R.layout.activity_main_no_login);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout_no_login);
        navigationView = (NavigationView) findViewById(R.id.nav_view_no_login);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_no_login);
        TextView nav_header_nick=(TextView) headerView.findViewById(R.id.nav_header_userName_no_login);
        TextView nav_header_description=(TextView)headerView.findViewById(R.id.nav_header_userDescribe_no_login);
        nav_header_nick.setText("未登录");
        nav_header_description.setText("");
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FakeActivity.this, "你还没有登录！请登录后再提问",
                        Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(FakeActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(FakeActivity.this, "您尚未登录，不能使用此功能！", Toast.LENGTH_SHORT).show();
                break;
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
                            case R.id.nav_home_no_login:
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_login_no_login:
                                Intent intentLogin = new Intent(FakeActivity.this, Login.class);
                                startActivity(intentLogin);
                                mDrawerLayout.closeDrawers();
                                finish();
                                break;
                            case R.id.nav_updownload_no_login:
                                mDrawerLayout.closeDrawers();
                                Toast.makeText(FakeActivity.this, "您尚未登录，不能使用此功能！", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_search_no_login:
                                mDrawerLayout.closeDrawers();
                                Toast.makeText(FakeActivity.this, "您尚未登录，不能使用此功能！", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_setting_no_login:
                                mDrawerLayout.closeDrawers();
                                Toast.makeText(FakeActivity.this, "您尚未登录，不能使用此功能！", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_about_no_login:
                                mDrawerLayout.closeDrawers();
                                Toast.makeText(FakeActivity.this, "您尚未登录，不能使用此功能！", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });
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
