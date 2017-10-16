package com.hagame.e758moneyapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hagame.e758moneyapp.common.LoginResult;
import com.hagame.e758moneyapp.common.view.SlidingTabLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 //
 //                                  _oo8oo_
 //                                 o8888888o
 //                                 88" . "88
 //                                 (| -_- |)
 //                                 0\  =  /0
 //                               ___/'==='\___
 //                             .' \\|     |// '.
 //                            / \\|||  :  |||// \
 //                           / _||||| -:- |||||_ \
 //                          |   | \\\  -  /// |   |
 //                          | \_|  ''\---/''  |_/ |
 //                          \  .-\__  '-'  __/-.  /
 //                        ___'. .'  /--.--\  '. .'___
 //                     ."" '<  '.___\_<|>_/___.'  >' "".
 //                    | | :  `- \`.:`\ _ /`:.`/ -`  : | |
 //                    \  \ `-.   \_ __\ /__ _/   .-` /  /
 //                =====`-.____`.___ \_____/ ___.`____.-`=====
 //                                  `=---=`
 //
 //
 //       ~~~~~~~Powered by https://github.com/ottomao/bugfreejs~~~~~~~
 //
 //                          佛祖保佑         永無bug
 //
 */

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private static Context mContext;

    private List<FragmentItem> mTabs = new ArrayList<FragmentItem>();
    private List<DayDataFragmentItem> mTabs2 = new ArrayList<DayDataFragmentItem>();

    private static LoginResult l;

    private static AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    private static AppSectionsPagerAdapter2 mDayDataPagerAdapter;

    private static ViewPager mViewPager;

    private CharSequence mTitle;

    private SlidingTabLayout mSlidingTabLayout;

    public static int OnpositionClick = 0;
    public static int OnDayDataClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        l = (LoginResult) getIntent().getSerializableExtra("LoginResult");


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        initToolbar();

        //toolbar綁定左側選單
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        mDrawerLayout.setDrawerListener(drawerToggle);

        initLeftMenu();


        //BTN1
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.test_viewpager);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        //BTN2
        mDayDataPagerAdapter = new AppSectionsPagerAdapter2(getSupportFragmentManager());

        for (int i = 0; i < l.g.size(); ++i) {
            mTabs.add(new FragmentItem(
                    l.g.get(i).gameName, // Title
                    Color.WHITE, // Indicator color
                    Color.GRAY // Divider color
            ));

            mTabs2.add(new DayDataFragmentItem(l.d.get(i).gameName, // Title
                    Color.WHITE, // Indicator color
                    Color.GRAY // Divider color
            ));
        }

        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    //螢幕旋轉不更新Active
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 什麼都不用寫
        }
        else {
            // 什麼都不用寫
        }
    }


    //初始化toolbar
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setLogo(R.mipmap.gin3);
        mToolbar.setTitle("1758後台");
        //mToolbar.setSubtitle("副标题");

        // ...
        // 这个方法很重要，不能少，让toolbar取代ActionBar
        setSupportActionBar(mToolbar);
    }

    //初始化左側選單
    private void initLeftMenu() {
        ListView menuList = (ListView) findViewById(R.id.left_drawer);
        menuList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1
                , new String[]{"每日營收", "當日數據報表", "離開程式"}));

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                {

                    Log.d("gaga", "onItemClick: " + position);

                    if (OnpositionClick == 1) {
                        mViewPager.setAdapter(mAppSectionsPagerAdapter);
                        mSlidingTabLayout.setViewPager(mViewPager);
                    }
                    else if (OnpositionClick == 1 && OnDayDataClick==1 ){
                        mViewPager.setAdapter(mAppSectionsPagerAdapter);
                        mSlidingTabLayout.setViewPager(mViewPager);
                    }
                    else
                    {

                        mViewPager.setAdapter(mAppSectionsPagerAdapter);
                        mSlidingTabLayout.setViewPager(mViewPager);

                        mTitle = getTitle();

                        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                            @Override
                            public int getIndicatorColor(int position) {
                                return mTabs.get(position).getIndicatorColor();
                            }

                            @Override
                            public int getDividerColor(int position) {
                                return mTabs.get(position).getDividerColor();
                            }
                        });

                        OnpositionClick = 1;

                        OnDayDataClick = 1;

                    }
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }

                if (position == 1) {


                    Log.d("gaga", "onItemClick: " + position);

                    if (OnDayDataClick == 1) {
                        mViewPager.setAdapter(mDayDataPagerAdapter);
                        mSlidingTabLayout.setViewPager(mViewPager);
                    }

                    else {

                        mViewPager.setAdapter(mDayDataPagerAdapter);
                        mSlidingTabLayout.setViewPager(mViewPager);

                        mTitle = getTitle();

                        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                            @Override
                            public int getIndicatorColor(int position) {
                                return mTabs2.get(position).getIndicatorColor();
                            }

                            @Override
                            public int getDividerColor(int position) {
                                return mTabs2.get(position).getDividerColor();
                            }
                        });

                        OnDayDataClick = 1;
                    }

                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
                if (position == 2)
                {
                    Log.d("gaga", "onItemClick: " + position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("離開程式？");
                    builder.setCancelable(false);
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });

                    builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            return;
                        }
                    });

                    builder.show();

                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });
    }


    private class AppSectionsPagerAdapter extends FragmentStatePagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mTabs.get(i).createFragment(i);
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position).getTitle();
        }
    }

    static class FragmentItem {
        private final CharSequence mTitle;
        private final int mIndicatorColor;
        private final int mDividerColor;

        FragmentItem(CharSequence title, int indicatorColor, int dividerColor) {
            mTitle = title;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
        }

        Fragment createFragment(int i) {
            Bundle args = new Bundle();
            args.putInt("gameId", l.g.get(i).gameId);
            args.putString("otp", l.otp);

            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            Calendar c = Calendar.getInstance();
            String eDate = df.format(c.getTime());
            c.add(Calendar.DATE, -14);
            String sDate = df.format(c.getTime());
            args.putString("sDate", sDate);
            args.putString("eDate", eDate);
            DayCountFragment t = new DayCountFragment();
            t.setArguments(args);
            t.getTag();
            return t;
        }

        CharSequence getTitle() {
            return mTitle;
        }

        int getIndicatorColor() {
            return mIndicatorColor;
        }

        int getDividerColor() {
            return mDividerColor;
        }
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }



    private class AppSectionsPagerAdapter2 extends FragmentStatePagerAdapter {

        public AppSectionsPagerAdapter2(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mTabs2.get(i).createFragmentDayData(i);
        }

        @Override
        public int getCount() {
            return mTabs2.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs2.get(position).getTitle();
        }
    }

    static class DayDataFragmentItem {
        private final CharSequence mTitle2;
        private final int mIndicatorColor;
        private final int mDividerColor;

        DayDataFragmentItem(CharSequence title, int indicatorColor, int dividerColor) {
            mTitle2 = title;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
        }

        Fragment createFragmentDayData(int i) {
            Bundle args = new Bundle();
            args.putInt("gameId", l.d.get(i).gameId);
            DayDataFragment t = new DayDataFragment();
            t.setArguments(args);
            return t;
        }

        CharSequence getTitle() {
            return mTitle2;
        }

        int getIndicatorColor() {
            return mIndicatorColor;
        }

        int getDividerColor() {
            return mDividerColor;
        }
    }



}
