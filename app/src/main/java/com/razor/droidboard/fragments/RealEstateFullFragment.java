package com.razor.droidboard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.razor.droidboard.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ruan on 10/14/2016.
 */

public class RealEstateFullFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static RealEstateFullFragment m_fragmentInstance;

    public static RealEstateFullFragment getInstance(Context context)
    {
        if (m_fragmentInstance == null)
        {
            m_fragmentInstance = new RealEstateFullFragment();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, 2);
            m_fragmentInstance.setArguments(args);
//            m_fragmentInstance.setContext(context);
        }

        return m_fragmentInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_real_estate_full, container, false);

//        m_recycleLogbook = (RecyclerView) rootView.findViewById(R.id.recycleLogbooks);
//        m_swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
//        m_progressBar = (ProgressBar) rootView.findViewById(R.id.loading_spinner);
//        m_shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
//        m_imgNoEntries = (AppCompatImageView) rootView.findViewById(R.id.imgNoEntriesFound);
//        m_txtNoEntries = (AppCompatTextView) rootView.findViewById(R.id.tvNoEntries);
//        m_swipeLayout.setVisibility(View.VISIBLE);
//        m_swipeLayout.setOnRefreshListener(this);
//        m_layoutMangager = new LinearLayoutManager(getActivity());
//        m_recycleLogbook.setLayoutManager(m_layoutMangager);
//        m_recycleLogbook.addOnScrollListener(m_recListScroll);
//        m_recycleAdapter = null;
//        m_views = new ArrayList<>();

        int currTabPosition = getArguments().getInt(ARG_SECTION_NUMBER);
//
//        m_swipeLayout.setRefreshing(true);
        return rootView;
    }

    @Override
    public void onRefresh()
    {

    }
}
