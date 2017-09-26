package com.example.administrator.httpdemo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.administrator.httpdemo.CustomView.PagerSlidingTabStrip;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.DensityUtils;

public class NetWorkFragment extends Fragment{
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private Fragment[] mFragments = {new RecommendFragment(), new SongListFragment()
        ,new TopListFragment()};

    private String[] titles = {"个人推荐", "歌单", "排行榜"};

    public NetWorkFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){

        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.net_pagerSliding);
        tabs.addTextTab(titles);
        tabs.setExpand(true);

        pager = (ViewPager) view.findViewById(R.id.net_pager);
        adapter = new MyPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        int pageMargin = DensityUtils.dp2px(getContext(), 4);
        pager.setPageMargin(pageMargin);
        tabs.setViewPager(pager);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Override
        public Fragment getItem(int position) {

            return mFragments[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
        }

    }

}
