package com.razor.droidboard.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.razor.droidboard.R;
import com.razor.droidboard.models.Attachment;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ruan on 10/11/2016.
 */

public class HorizontalAttachmentAdapter extends RecyclerView.Adapter<HorizontalAttachmentAdapter.AttachmentViewHolder>
{
    private static final String TAG = "LogbookRecycleAdapter";
    //    ArrayList<LogbookProcedure> m_procedures;
    ArrayList<Attachment> m_items;
    Context m_context;
    int m_resource;
    View.OnClickListener m_rowClick;

    public HorizontalAttachmentAdapter(Context context, int resource, ArrayList<Attachment> procs, View.OnClickListener listener)
    {
        m_context = context;
        m_resource = resource;
        m_items = procs;
        m_rowClick = listener;
    }

    @Override
    public AttachmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(m_resource, parent, false);

        AttachmentViewHolder viewHolder = new AttachmentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AttachmentViewHolder holder, int position)
    {
        Attachment currItem = m_items.get(position);

        currItem.setPosition(position);

        if (holder == null)
            return;

        if (m_rowClick != null)
        {
            holder.mainView.setTag(R.id.identity, currItem.getId());
            holder.mainView.setOnClickListener(m_rowClick);
        }

        if (!TextUtils.isEmpty(currItem.getImagePath()))
        {
            loadAttachment(holder.imgAttachmentPreview, currItem.getAttachmentDescription());
        }

        if (!TextUtils.isEmpty(currItem.getAttachmentDescription()))
        {
            holder.tvAttDescription.setText(currItem.getAttachmentDescription());
        }

        String imagePath = currItem.getImagePath();
        if (!TextUtils.isEmpty(imagePath))
        {
            loadAttachment(holder.imgAttachmentPreview, imagePath);
        }
    }

    public void addItem(Attachment attachment)
    {
        if (m_items == null)
        {
            m_items = new ArrayList<>();
        }

        m_items.add(attachment);

        notifyItemInserted(m_items.size()-1);
    }

    private void loadAttachment(CircleImageView imgAttachmentPreview, String imagePath)
    {
        Glide.with(m_context)
                .load(new File(imagePath))
                .into(imgAttachmentPreview);
    }

    public void setRowClick(View.OnClickListener listener)
    {
        m_rowClick = listener;
    }

    public ArrayList<Attachment> getItems()
    {
        return m_items;
    }

    @Override
    public int getItemCount()
    {
        return (m_items != null ? m_items.size() : 0);
    }

    //R.layout.attachment_preview
    public class AttachmentViewHolder extends RecyclerView.ViewHolder
    {
        protected View mainView;
        protected CircleImageView imgAttachmentPreview;
        protected TextView tvAttDescription;

        public AttachmentViewHolder(View itemView)
        {
            super(itemView);
            this.mainView = itemView;
            this.imgAttachmentPreview = (CircleImageView) itemView.findViewById(R.id.imgAttPreview);
            this.tvAttDescription = (TextView) itemView.findViewById(R.id.tvAttDescription);
        }
    }
}
