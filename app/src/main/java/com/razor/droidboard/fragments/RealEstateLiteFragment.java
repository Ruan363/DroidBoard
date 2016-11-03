package com.razor.droidboard.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.razor.droidboard.R;
import com.razor.droidboard.adapters.RealEstateLiteAdapter;
import com.razor.droidboard.models.RealEstate;

import java.util.ArrayList;

/**
 * Created by ruan on 10/14/2016.
 */

public class RealEstateLiteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static RealEstateLiteFragment m_fragmentInstance;

    private RecyclerView m_recycleRealEstateLite;

    private RealEstateLiteAdapter m_recLiteAdapter;

    private SwipeRefreshLayout m_swipeLayout;

    private LinearLayoutManager m_layoutMangager;

    private ArrayList<RealEstate> m_realEstates;

    public static RealEstateLiteFragment getInstance(Context context)
    {
        if (m_fragmentInstance == null)
        {
            m_fragmentInstance = new RealEstateLiteFragment();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, 1);
            m_fragmentInstance.setArguments(args);
        }

        return m_fragmentInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_real_estate_lite, container, false);

        m_recycleRealEstateLite = (RecyclerView) rootView.findViewById(R.id.recRealLite);
        m_recycleRealEstateLite.setHasFixedSize(true);

        m_swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRealEstate);
        m_swipeLayout.setVisibility(View.VISIBLE);
        m_swipeLayout.setOnRefreshListener(this);
        m_layoutMangager = new LinearLayoutManager(getActivity());
        m_recycleRealEstateLite.setLayoutManager(m_layoutMangager);
        m_recLiteAdapter = null;
        m_realEstates = new ArrayList<>();

        int currTabPosition = getArguments().getInt(ARG_SECTION_NUMBER);

        setUpItemTouchHelper();

        setUpAnimationDecoratorHelper();

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        m_swipeLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                m_swipeLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    /**
     * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
     * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
     * background will be visible. That is rarely an desired effect.
     */
    private void setUpItemTouchHelper()
    {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT)
        {
            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.GRAY);
                xMark = ContextCompat.getDrawable(getActivity(), R.mipmap.ic_add_white_round);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) getActivity().getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
            {
                int position = viewHolder.getAdapterPosition();
                RealEstateLiteAdapter testAdapter = (RealEstateLiteAdapter)recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position))
                {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                RealEstateLiteAdapter adapter = (RealEstateLiteAdapter) m_recycleRealEstateLite.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn)
                {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
//                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + (int) dX, itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

//                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
//                int xMarkRight = itemView.getRight() - xMarkMargin;

                int xMarkLeft = itemView.getLeft() + xMarkMargin;
                int xMarkRight = itemView.getLeft() + xMarkMargin + intrinsicWidth;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(m_recycleRealEstateLite);
    }

    private void setUpAnimationDecoratorHelper()
    {
        m_recycleRealEstateLite.addItemDecoration(new RecyclerView.ItemDecoration()
        {
            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.GRAY);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0; // TODO: 10/17/2016
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }

    @Override
    public void onRefresh()
    {
        m_realEstates = new ArrayList<>();

        m_realEstates.add(new RealEstate("R 150 000", "Sinoville", "Gauteng", "South Africa", 4, "10/10/2016"));
        m_realEstates.add(new RealEstate("R 350 000", "Montana", "Gauteng", "South Africa", 2, "11/10/2016"));
        m_realEstates.add(new RealEstate("R 450 000", "Ritz", "California", "America", 6, "14/10/2016"));
        m_realEstates.add(new RealEstate("R 550 000", "Ritz", "California", "America", 6, "14/10/2016"));
        m_realEstates.add(new RealEstate("R 650 000", "Ritz", "California", "America", 6, "14/10/2016"));
        m_realEstates.add(new RealEstate("R 750 000", "Ritz", "California", "America", 6, "14/10/2016"));
        m_realEstates.add(new RealEstate("R 850 000", "Ritz", "California", "America", 6, "14/10/2016"));
        m_realEstates.add(new RealEstate("R 950 000", "Ritz", "California", "America", 6, "14/10/2016"));
        m_realEstates.add(new RealEstate("R 1050 000", "Ritz", "California", "America", 6, "14/10/2016"));
        m_realEstates.add(new RealEstate("R 1150 000", "Ritz", "California", "America", 6, "14/10/2016"));
        m_realEstates.add(new RealEstate("R 1250 000", "Ritz", "California", "America", 6, "14/10/2016"));

        m_recLiteAdapter = new RealEstateLiteAdapter(getActivity(), R.layout.real_estate_lite_row, m_realEstates, null);

        m_recycleRealEstateLite.setAdapter(m_recLiteAdapter);

        m_swipeLayout.setRefreshing(false);
    }
}
