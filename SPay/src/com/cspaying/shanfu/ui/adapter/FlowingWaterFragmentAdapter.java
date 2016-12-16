package com.cspaying.shanfu.ui.adapter;

import java.util.ArrayList;  
import java.util.List;  
  
import android.support.v4.app.Fragment;  
import android.support.v4.app.FragmentManager;  
import android.support.v4.app.FragmentPagerAdapter;  
  
public class FlowingWaterFragmentAdapter extends FragmentPagerAdapter {  
  
    List<Fragment> fragmentList = new ArrayList<Fragment>();  
    public FlowingWaterFragmentAdapter(FragmentManager fm,List<Fragment> fragmentList) {  
        super(fm);  
        this.fragmentList = fragmentList;  
    }  
  
    @Override  
    public Fragment getItem(int position) {  
        return fragmentList.get(position);  
    }  
  
    @Override  
    public int getCount() {  
        return fragmentList.size();  
    }  
  
}  
