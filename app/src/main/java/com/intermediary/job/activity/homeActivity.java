package com.intermediary.job.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioGroup;

import com.intermediary.job.R;
import com.intermediary.job.base.BasePager;
import com.intermediary.job.base.impl.CompanyPager;
import com.intermediary.job.base.impl.JobPager;
import com.intermediary.job.base.impl.MessagePager;
import com.intermediary.job.base.impl.MyPager;

import java.util.ArrayList;

/**
 * Created by kalogchen on 2016/12/21.
 */

public class homeActivity extends Activity {

    private RadioGroup rgGroup;
    private ArrayList<BasePager> mPagerList;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        rgGroup = (RadioGroup) findViewById(R.id.rg_group);
        mViewPager = (ViewPager) findViewById(R.id.vp_content);

        initData();
    }

    //初始化数据
    public void initData() {
        //默认勾选职位
        rgGroup.check(R.id.rb_job);

        /**
         * 初始化4个子页面
         */
        mPagerList = new ArrayList<>();
        mPagerList.add(new JobPager(homeActivity.this));
        mPagerList.add(new CompanyPager(homeActivity.this));
        mPagerList.add(new MessagePager(homeActivity.this));
        mPagerList.add(new MyPager(homeActivity.this));

        mViewPager.setAdapter(new ContentAdapter());

        /**
         * 监听RadioGroup的选择事件
         */
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_job:
                        //设置当前页面，并去掉动画
                        mViewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_company:
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_message:
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_my:
                        mViewPager.setCurrentItem(3, false);
                        break;
                }
            }
        });

        //监听页面改变
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //获取当前被选中的页面，初始化该页面数据
                mPagerList.get(position).initData();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //手动初始化首页，一开始不会调用onPageSelected方法，所以数据无法初始化
        mPagerList.get(0).initData();

    }

    public class ContentAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = mPagerList.get(position);
            //获取当前页面的布局对象
            View rootView = basePager.mRootView;
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
