package com.razor.droidboard.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.razor.droidboard.fragments.RealEstateFullFragment;
import com.razor.droidboard.fragments.RealEstateLiteFragment;

/**
 * Created by ruan on 10/14/2016.
 */

public class RealEstatePagerAdapter extends FragmentPagerAdapter
{
    Context m_context;

    public RealEstatePagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        m_context = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        if (position == 0)
        {
            return RealEstateLiteFragment.getInstance(m_context);
        }
        else
        {
            return RealEstateFullFragment.getInstance(m_context);
        }
    }

//    public LogbookRecycleAdapter getFragmentAdapter()
//    {
//        LogbookSectionFragment currFragment = LogbookSectionFragment.newInstance(1, m_context);
//        LogbookRecycleAdapter fragmentAdapter = currFragment.getAdapter();
//
//        return fragmentAdapter;
//    }

    @Override
    public int getItemPosition(Object object)
    {
        int position = 0;

        if (object.getClass().equals(RealEstateLiteFragment.class))
        {
            position = 0;
        }
        else if (object.getClass().equals(RealEstateFullFragment.class))
        {
            position = 1;
        }

        return position;
    }

    @Override
    public int getCount()
    {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Lite";
            case 1:
                return "Full";
        }
        return null;
    }
}
