package com.razor.droidboard.screens;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bumptech.glide.Glide;
import com.razor.droidboard.R;
import com.razor.droidboard.adapters.SnapshotAdapter;
import com.razor.droidboard.models.Snapshot;
import com.razor.droidboard.screens.base.BaseDrawerActivity;
import com.razor.droidboard.utilities.RealPathUtil;
import com.razor.droidboard.utilities.Utils;

public class SnapshotScreen extends BaseDrawerActivity
{
    StaggeredGridLayoutManager m_staggeredGridLayoutManager;
    SnapshotAdapter m_adapter;
    RecyclerView m_recyclerView;

    ArrayList<Snapshot> m_snapshots;
    String m_imageFilePath;
    String m_imageDescription;

    EditText m_edtDescription;

    File m_imageDestination;

    public final static int TAKE_PICTURE = 1, SELECT_FILE = 2;

//    private String m_resultUrl = "result.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState, R.layout.activity_snapshot_screen);

        instantiateScreen();

        populateScreen();
    }

    private void populateScreen()
    {
        m_snapshots = (ArrayList<Snapshot>) Snapshot.listAll(Snapshot.class);

        m_adapter = new SnapshotAdapter(this, m_snapshots);
        m_recyclerView.setAdapter(m_adapter);
    }

    private void instantiateScreen()
    {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.viewContent);

        m_recyclerView = (RecyclerView) relativeLayout.findViewById(R.id.recSnapshots);
        m_recyclerView.setHasFixedSize(true);

        m_staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        m_recyclerView.setLayoutManager(m_staggeredGridLayoutManager);
    }

    public void addSnapClick(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileUri = getOutputMediaFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, TAKE_PICTURE);
    }

    public void addSnapGalleryClick(View view)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        startActivityForResult(intent, SELECT_FILE);
    }

    private Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    private File getOutputMediaFile()
    {
        String name =  Utils.formatDateForDisplayFromDate(new Date(),"yyyy-MM-dd-hh-mm-ss");
        m_imageDestination = new File(Environment.getExternalStorageDirectory(), name + ".jpg");

//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Logbook OCR Images");

//        if (! mediaStorageDir.exists()){
//            if (! mediaStorageDir.mkdirs()){
//                return null;
//            }
//        }

//        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "image.jpg" );

        return m_imageDestination;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == TAKE_PICTURE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                onActivityTakePicture();
            }
        }
        else if (data != null)
        {
            if (requestCode == SELECT_FILE)
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    onActivitySelectFile(data);
                }
            }
        }
    }

    private void onActivitySelectFile(Intent data)
    {
        Uri imageUri = data.getData();

        // SDK < API11
        if (Build.VERSION.SDK_INT < 11)
        {
            m_imageFilePath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());
        }
        else if (Build.VERSION.SDK_INT < 19)
        {
            m_imageFilePath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
        }
        else        //4.4
        {
            m_imageFilePath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
        }

//        //Remove output file
//        deleteFile(m_resultUrl);

        showPopup();
    }

    private void onActivityTakePicture()
    {
        m_imageFilePath = m_imageDestination.getAbsolutePath();

//        deleteFile(m_resultUrl);

        showPopup();
    }

    private void showPopup()
    {
        View v = getLayoutInflater().inflate(R.layout.snapshot_popup, null, false);
        m_edtDescription = (EditText) v.findViewById(R.id.edtDesc);
        ImageView imgSnap = (ImageView) v.findViewById(R.id.imgSnap);

        File file = new File(m_imageFilePath);
        if (!file.exists())
            return;

//        Bitmap myBitmap = BitmapFactory.decodeFile(m_imageFilePath);
//
//        imgSnap.setImageBitmap(myBitmap);

        Glide.with(this)
                .load(file)
                .into(imgSnap);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setPositiveButton("Add", m_addSnapPositive);
        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    DialogInterface.OnClickListener m_addSnapPositive = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            String description = m_edtDescription.getText().toString();

            Snapshot snapshot = new Snapshot(description, m_imageFilePath);
            snapshot.save();

            m_snapshots.add(snapshot);

            m_adapter.notifyDataSetChanged();
        }
    };
}
