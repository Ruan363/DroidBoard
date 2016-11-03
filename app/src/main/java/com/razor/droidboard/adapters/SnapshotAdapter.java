package com.razor.droidboard.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import com.bumptech.glide.Glide;
import com.razor.droidboard.R;
import com.razor.droidboard.models.Snapshot;
import com.razor.droidboard.utilities.ScreenUtils;
import com.razor.droidboard.utilities.Utils;

/**
 * Created by ruan on 5/17/2016.
 */
public class SnapshotAdapter extends RecyclerView.Adapter<SnapshotAdapter.SnapshotViewHolder>
{
    private ArrayList<Snapshot> m_items;
    private ArrayList<Bitmap> m_images;
    private Context m_context;

    private Integer m_seekBarProgress;

    private EditText m_edtDescription;

    public SnapshotAdapter(Context context, ArrayList<Snapshot> itemList) {
        this.m_items = itemList;
        this.m_context = context;
        m_images = new ArrayList<>();
    }

    @Override
    public SnapshotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.snapshot_item, null);
        SnapshotViewHolder rcv = new SnapshotViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(SnapshotViewHolder holder, int position) {
        Snapshot item = m_items.get(position);

        holder.tvSnapDescription.setText(item.getDescription());

        String filePath = item.getFilePath();

        Bitmap myBitmap = null;

        if (m_images.size() < position+1)
        {
            myBitmap = getImageFromFilepath(filePath, item, holder);
            m_images.add(position, myBitmap);
        }
        else
        {
            myBitmap = m_images.get(position);

            if (myBitmap == null)
            {
                myBitmap = getImageFromFilepath(filePath, item, holder);
                m_images.add(position, myBitmap);
            }

        }

        holder.imgSnapPhoto.setImageBitmap(myBitmap);

        holder.mainView.setTag(position);
    }

    @Override
    public int getItemCount()
    {
        return m_items == null? 0 : m_items.size();
    }

    private Bitmap getImageFromFilepath(String filePath, Snapshot item, SnapshotViewHolder holder)
    {
        File file = new File(filePath);
        if (!file.exists())
        {
            holder.imgSnapPhoto.setImageResource(R.drawable.snap_test);
            return null;
        }

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inSampleSize = 4;

        Bitmap myBitmap = BitmapFactory.decodeFile(filePath, o);

        int finalHeight = item.getHeight();
        int finalWidth = item.getWidth();

        if (finalHeight == 0 || finalWidth == 0)
        {
            int h = myBitmap.getHeight();
            int w = myBitmap.getWidth();

            int smallHeight = h;
            int smallWidth = w;

            while (smallHeight > 250)
            {
                smallHeight = (smallHeight * 3) / 4;
                smallWidth = (smallWidth * 3) / 4;
            }

            Random r = new Random();
            int scale = r.nextInt(50);

            smallHeight += scale;
            smallWidth += scale;

//            finalHeight = (smallHeight * scale)+(h-smallHeight);
//            finalWidth = (smallWidth * scale)+(w-smallWidth);
            finalHeight = smallHeight;
            finalWidth = smallWidth;

//            int dpHeight = Utils.pxToDp(m_context, finalHeight);
            if (finalHeight > 300)
            {
//                finalHeight = Utils.dpToPx(m_context, 300);
                finalHeight = 300;
            }
            else if (finalHeight < 100)
            {
                finalHeight = 100;
            }

            item.setHeight(finalHeight);
            item.setWidth(finalWidth);
        }

        myBitmap = Bitmap.createScaledBitmap(myBitmap, finalWidth, finalHeight, true);

        return myBitmap;
    }

    public class SnapshotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvSnapDescription;
        public ImageView imgSnapPhoto;
        public View mainView;

        public SnapshotViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mainView = itemView;
            tvSnapDescription = (TextView) itemView.findViewById(R.id.tvSnapDesc);
            imgSnapPhoto = (ImageView) itemView.findViewById(R.id.imgSnapPhoto);
        }

        @Override
        public void onClick(View view)
        {
            showOptions();
        }

        private void showOptions()
        {
            ArrayList<String> options = new ArrayList<>();
            options.add("Change Size");
            options.add("Edit Title");
            options.add("Remove");

            ScreenUtils.showListDialog(m_context, options, "Options", m_optionsClick, "Cancel", null);
        }

        DialogInterface.OnClickListener m_optionsClick = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == 0)
                {
                    showEditSizePopup();
                }
                else if (which == 1)
                {
                    showEditTitlePopup();
                }
                else if (which == 2)
                {
                    showRemovePopup();
                }
            }
        };

        private void showRemovePopup()
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(m_context);

            String imgName = tvSnapDescription.getText().toString();

            if (Utils.stringIsNullOrEmpty(imgName))
                builder.setTitle("Remove Snapshot");
            else
                builder.setTitle("Remove "+imgName);

            builder.setMessage("Are you sure?");

            builder.setPositiveButton("YES", m_posClick);

            builder.setNegativeButton("NO", null);

            builder.show();
        }

        DialogInterface.OnClickListener m_posClick = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int position = getAdapterPosition();

                Snapshot item = m_items.get(position);

                Snapshot foundItem = Snapshot.findById(Snapshot.class, item.getId());
                foundItem.delete();

                if (m_images.size() > position)
                {
                    m_images.remove(position);
                }

                m_items.remove(position);

                notifyDataSetChanged();
            }
        };

        private void showEditSizePopup()
        {
            View v = LayoutInflater.from(m_context).inflate(R.layout.edit_size_popup, null, false);
//            m_edtDescription = (EditText) v.findViewById(R.id.edtDesc);
//            ImageView imgSnap = (ImageView) v.findViewById(R.id.imgSnap);

            SeekBar seekBar = (SeekBar) v.findViewById(R.id.seekBar);
            seekBar.setOnSeekBarChangeListener(m_seekBarChange);

            TextView tvProgress = (TextView) v.findViewById(R.id.tvCurrSize);

            seekBar.setTag(R.id.textview, tvProgress);

            int position = getAdapterPosition();
            Snapshot item = m_items.get(position);

            int height = item.getHeight();

            seekBar.setProgress(height - 100);

//            tvProgress.setText(String.valueOf(height));

            AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
            builder.setView(v);
            builder.setPositiveButton("Apply", m_applySizeClick);
            builder.setNegativeButton("Cancel", null);

            builder.show();
        }

        DialogInterface.OnClickListener m_applySizeClick = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int position = getAdapterPosition();
                Snapshot item = m_items.get(position);

                int diffWidth = 0;
                Boolean increasedSize = true;
                if (m_seekBarProgress != null && m_seekBarProgress > 0)
                {
                    int height = m_seekBarProgress;

                    double diff = m_seekBarProgress - item.getHeight();

                    if (diff >= 0)
                    {
                        increasedSize = true;
                    }
                    else
                    {
                        increasedSize = false;
                        diff = Math.abs(diff);
                    }

                    double d = diff / ((double) item.getHeight());

                    int scale = (int) ((d) * 100);

                    diffWidth = (scale * item.getWidth()) / 100;

                }

                item.setHeight(m_seekBarProgress);

                int newWidth = 0;

                if (increasedSize)
                {
                    newWidth = item.getWidth() + diffWidth;
                }
                else
                {
                    newWidth = item.getWidth() - diffWidth;
                }

                item.setWidth(newWidth);

                m_items.set(position, item);

                m_images.add(position, null);

                notifyItemChanged(position);
            }
        };

        SeekBar.OnSeekBarChangeListener m_seekBarChange = new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                m_seekBarProgress = progress + 100;

                TextView tvProg = (TextView) seekBar.getTag(R.id.textview);
                tvProg.setText(String.valueOf(m_seekBarProgress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        };

        private void showEditTitlePopup()
        {
            int position = getAdapterPosition();

            Snapshot item = m_items.get(position);

            String filePath = item.getFilePath();

            View v = LayoutInflater.from(m_context).inflate(R.layout.snapshot_popup, null, false);
            m_edtDescription = (EditText) v.findViewById(R.id.edtDesc);
            m_edtDescription.setText(item.getDescription());

            ImageView imgSnap = (ImageView) v.findViewById(R.id.imgSnap);

            File file = new File(filePath);
            if (!file.exists())
                return;

            Glide.with(m_context)
                    .load(file)
                    .into(imgSnap);

            AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
            builder.setView(v);
            builder.setPositiveButton("Apply", m_applyChangeTitle);
            builder.setNegativeButton("Cancel", null);

            builder.show();
        }

        DialogInterface.OnClickListener m_applyChangeTitle = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int position = getAdapterPosition();

                Snapshot item = m_items.get(position);

                item.setDescription(m_edtDescription.getText().toString());

                m_items.set(position, item);

                notifyItemChanged(position);
            }
        };
    }
}
