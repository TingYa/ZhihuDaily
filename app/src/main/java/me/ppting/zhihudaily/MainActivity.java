package me.ppting.zhihudaily;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends ActionBarActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.navigationview)
    NavigationView mNavigationView;
    @Bind(R.id.drawerlayout)
    DrawerLayout mDrawerLayout;
//    @Bind(R.id.recyclerview)
//    RecyclerView mRecyclerview;
    @Bind(R.id.tablayout)
    TabLayout mTablayout;
    @Bind(R.id.viewpager)
    ViewPager mViewpager;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private String mUrl;
    private static String TAG = MainActivity.class.getName();
    private String mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getDate();
        setupToolbar();
        if (mNavigationView == null) {
            setupDrawerContent();
        }
        //getZhihuInfo();

        setupTablayout();
    }

    private void getDate() {
        Time mTime = new Time();
        mTime.setToNow();
        int mYear = mTime.year;
        int mMonth = mTime.month + 1;
        int mDay = mTime.monthDay + 1;
        Log.d("MainActivity", "mMonth is " + mMonth);
        if (mTime.monthDay < 10) {
            mDate = mYear + "" + mMonth + "0" + mDay;
        } else {
            mDate = mYear + "" + mMonth + mDay;
        }
        Log.d("MainActivity", "today's date is " + mDate);
    }

    private void setupTablayout() {
        List<String> mDateList = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            int mId = Integer.parseInt(mDate);
            String mIdList = String.valueOf(mId - i);
            mDateList.add(mIdList);
            Log.d("MainActivity", "mIdList is " + mIdList);
        }
        mTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        for (int k = 0; k < mDateList.size(); k++) {
            mTablayout.addTab(mTablayout.newTab().setText(mDateList.get(k)));
        }
        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mDateList);
        mViewpager.setAdapter(mPagerAdapter);
        mTablayout.setupWithViewPager(mViewpager);
        mTablayout.setTabsFromPagerAdapter(mPagerAdapter);
    }

    //获取知乎返回的json
//    private void getZhihuInfo() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpClient httpClient = new DefaultHttpClient();
//                Log.d("getZhiHuInfo", "mDate is " + mDate);
//                mUrl = "http://news.at.zhihu.com/api/1.2/news/before/" + mDate;
//                HttpGet mHttpGet = new HttpGet(mUrl);
//                try {
//                    HttpResponse httpResponse = httpClient.execute(mHttpGet);
//                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                        HttpEntity entity = httpResponse.getEntity();
//                        String response = EntityUtils.toString(entity, "UTF-8");
//                        Log.d("MainActivity", "response is " + response);
//                        //AnalyzeJson mAnalyzeJson = new AnalyzeJson();
//                        //mAnalyzeJson.AnalyzeData(response);
//                        //异步加载标题和缩略图
//                        MyAsyncTask myAsyncTask = new MyAsyncTask();
//                        myAsyncTask.execute(response);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

    private void setupDrawerContent() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked())
                    menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Closing drawer on item click
                mDrawerLayout.closeDrawers();
                //Check to see which item was being clicked and perform appropriate action
//                switch (menuItem.getItemId()) {
//                    case R.id.movie:
//                        return startIntent(MovieActivity.class);
//                    case R.id.music:
//                        return startIntent(MusicActivity.class);
//                    case R.id.life:
//                        return startIntent(LifeActivity.class);
//                    case R.id.aboutme:
//                        return startIntent(AboutMeActivity.class);
//                    default:
                return true;
//                }
            }
        });
    }

    private void setupToolbar() {
        toolbar.setTitle("知乎日报");
        toolbar.setTitleTextColor(Color.parseColor("#FFEB3B"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键监听
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    private boolean startIntent(Class mClass) {
        startActivity(new Intent(MainActivity.this, mClass));
        return true;
    }

//    class MyAsyncTask extends AsyncTask<String, Void, List<ZhihuBean>> {
//        @Override
//        protected List<ZhihuBean> doInBackground(String... params) {
//            Log.d(TAG, "传入的JsonData是 " + params[0]);
//            List<ZhihuBean> response = new AnalyzeJson().AnalyzeData(params[0]);
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(final List<ZhihuBean> zhihuBeans) {
//            super.onPostExecute(zhihuBeans);
//            Log.d(TAG, "zhihuBeans is " + zhihuBeans);
//            ZhihuAdapter zhihuAdapter = new ZhihuAdapter(MainActivity.this, zhihuBeans);
//            mRecyclerview.setAdapter(zhihuAdapter);
//            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
//            mRecyclerview.setLayoutManager(mLayoutManager);
//            zhihuAdapter.setOnClickListener(new ZhihuAdapter.OnItemClickListener() {
//                @Override
//                public void OnItemClick(View view, int position) {
//                    Log.d(TAG, "Click item");
//                    Intent intent = new Intent(MainActivity.this, ConmentActivity.class);
//                    intent.putExtra("title", zhihuBeans.get(position).title);
//                    intent.putExtra("image", zhihuBeans.get(position).imageUrl);
//                    intent.putExtra("content", zhihuBeans.get(position).shareUrl);
//                    startActivity(intent);
//                }
//            });
//        }
//
//
//    }

}
