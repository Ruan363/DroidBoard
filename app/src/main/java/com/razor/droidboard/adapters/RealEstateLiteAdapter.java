package com.razor.droidboard.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.razor.droidboard.R;
import com.razor.droidboard.models.RealEstate;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ruan on 10/17/2016.
 */

public class RealEstateLiteAdapter extends RecyclerView.Adapter<RealEstateLiteAdapter.RealEstateLiteViewHolder>
{
    private static final String TAG = "RealEstateLiteAdapter";
    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
    ArrayList<RealEstate> m_items;
    ArrayList<RealEstate> m_itemsPendingRemoval;
    Context m_context;
    int m_resource;
    View.OnClickListener m_rowClick;
    int lastInsertedIndex; // so we can add some more items for testing purposes
    boolean undoOn; // is undo on, you can turn it on from the toolbar menu

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<RealEstate, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be


    public RealEstateLiteAdapter(Context context, int resource, ArrayList<RealEstate> procs, View.OnClickListener listener)
    {
        m_context = context;
        m_resource = resource;
        m_items = procs;
        m_itemsPendingRemoval = new ArrayList<>();
        m_rowClick = listener;
        undoOn = true;

        lastInsertedIndex = m_items.size();
    }

    @Override
    public RealEstateLiteViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(m_resource, parent, false);

        RealEstateLiteViewHolder viewHolder = new RealEstateLiteViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RealEstateLiteViewHolder holder, int position)
    {
        RealEstate currItem = m_items.get(position);

        if (holder == null)
            return;

        if (m_rowClick != null)
        {
            holder.mainView.setTag(R.id.identity, currItem.getDate());
            holder.mainView.setOnClickListener(m_rowClick);
        }

        String suburb = currItem.getSuburb();
        String province = currItem.getProvince();
        String date = currItem.getDate();
        String price = currItem.getPrice();
        String amtAttachments = String.valueOf(currItem.getAmountAttachments());

        handleSwipeUi(holder, currItem);

        if (!TextUtils.isEmpty(suburb))
        {
            holder.tvSuburb.setText(suburb);
        }

        if (!TextUtils.isEmpty(province))
        {
            holder.tvProvince.setText(province);
        }

        if (!TextUtils.isEmpty(date))
        {
            holder.tvDate.setText(date);
        }

        if (!TextUtils.isEmpty(price))
        {
            holder.tvPrice.setText(price);
        }

        if (!TextUtils.isEmpty(amtAttachments))
        {
            holder.tvAmtAttachments.setText(amtAttachments);
        }
    }

    private void handleSwipeUi(RealEstateLiteViewHolder viewHolder, final RealEstate item)
    {
        if (m_itemsPendingRemoval.contains(item))
        {
            // we need to show the "undo" state of the row
            viewHolder.itemView.setBackgroundColor(Color.GRAY);
            viewHolder.linTop.setVisibility(View.INVISIBLE);
            viewHolder.linBottom.setVisibility(View.INVISIBLE);
            viewHolder.btnUndo.setVisibility(View.VISIBLE);
            viewHolder.btnUndo.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(item);
                    pendingRunnables.remove(item);
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    m_itemsPendingRemoval.remove(item);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(m_items.indexOf(item));
                }
            });
        } else
        {
            // we need to show the "normal" state
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            viewHolder.linTop.setVisibility(View.VISIBLE);
            viewHolder.linBottom.setVisibility(View.VISIBLE);
            viewHolder.btnUndo.setVisibility(View.GONE);
            viewHolder.btnUndo.setOnClickListener(null);
        }
    }

    public void setRowClick(View.OnClickListener listener)
    {
        m_rowClick = listener;
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position)
    {
        final RealEstate item = m_items.get(position);
        if (!m_itemsPendingRemoval.contains(item))
        {
            m_itemsPendingRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable()
            {
                @Override
                public void run() {
                    remove(m_items.indexOf(item));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    public void remove(int position)
    {
        RealEstate item = m_items.get(position);
        if (m_itemsPendingRemoval.contains(item))
        {
            m_itemsPendingRemoval.remove(item);
        }
        if (m_items.contains(item))
        {
            m_items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position)
    {
        if (m_items == null || position >= m_items.size() || position < 0)
        {
            return false;
        }
        RealEstate item = m_items.get(position);
        return m_itemsPendingRemoval.contains(item);
    }

    @Override
    public int getItemCount()
    {
        return m_items == null? 0 : m_items.size();
    }

    //R.layout.attachment_preview
    public class RealEstateLiteViewHolder extends RecyclerView.ViewHolder
    {
        protected View mainView;
        protected TextView tvSuburb, tvProvince, tvDate, tvPrice, tvAmtAttachments;
        protected LinearLayout linTop, linBottom;
        protected ImageView imgFlag, imgAttachment;
        protected Button btnUndo;

        public RealEstateLiteViewHolder(View itemView)
        {
            super(itemView);
            this.mainView = itemView;
            this.btnUndo = (Button) itemView.findViewById(R.id.undo_button);
            this.linTop = (LinearLayout) itemView.findViewById(R.id.linTopAtts);
            this.linBottom = (LinearLayout) itemView.findViewById(R.id.linBottomAtts);
            this.tvSuburb = (TextView) itemView.findViewById(R.id.tvSuburb);
            this.tvProvince = (TextView) itemView.findViewById(R.id.tvProvince);
            this.tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            this.tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            this.tvAmtAttachments = (TextView) itemView.findViewById(R.id.tvAmmountAttachments);
            this.imgFlag = (ImageView) itemView.findViewById(R.id.imgFlag);
            this.imgAttachment = (ImageView) itemView.findViewById(R.id.imgAttSymbol);
        }
    }
}
