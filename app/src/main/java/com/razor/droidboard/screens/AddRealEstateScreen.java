package com.razor.droidboard.screens;

import android.Manifest;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.razor.droidboard.R;
import com.razor.droidboard.adapters.CountrySpinnerAdapter;
import com.razor.droidboard.adapters.GenericSpinnerAdapter;
import com.razor.droidboard.adapters.HorizontalAttachmentAdapter;
import com.razor.droidboard.interfaces.IBaseMethods;
import com.razor.droidboard.interfaces.IMainConttroller;
import com.razor.droidboard.models.Attachment;
import com.razor.droidboard.models.Country;
import com.razor.droidboard.models.Location;
import com.razor.droidboard.screens.base.BaseActivity;
import com.razor.droidboard.utilities.AnalyticsUtils;
import com.razor.droidboard.utilities.RetrofitUtils;
import com.razor.droidboard.utilities.ScreenUtils;
import com.razor.droidboard.utilities.Utils;
import com.soundcloud.android.crop.Crop;
import com.soundcloud.android.crop.CropImageActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.razor.droidboard.utilities.ImageUtilities.decodeSampledBitmapFromFile;

public class AddRealEstateScreen extends BaseActivity implements IBaseMethods
{
    IMainConttroller m_mainController;

    ArrayList<Country> m_countries;

    ArrayList<String> m_provinces;

    ArrayList<Attachment> m_attachments;

    AppCompatSpinner m_spinCountries, m_spinProvinces;

    GenericSpinnerAdapter m_countryAdapter, m_provinceAdapter;

    RecyclerView m_recycleAttachments;

    HorizontalAttachmentAdapter m_recAttachmentAdapter;

    LinearLayoutManager m_layoutMangager;

    ImageView m_imgMainAttachment;

    Toolbar m_toolbar;

    AppBarLayout m_appBarLayout;

    CollapsingToolbarLayout m_collapsingToolbarLayout;

    String m_imagePath;

    public static final int CAMERA_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_real_estate_screen);

        instantiateScreen();

        populateScreen();
    }

    @Override
    public void instantiateScreen()
    {
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);

        m_appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        m_appBarLayout.setExpanded(false);

        m_toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(m_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        m_collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        m_collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));

        RetrofitUtils.setBaseUrl("https://project-9138099718536031788.firebaseio.com");
        m_mainController = RetrofitUtils.build().create(IMainConttroller.class);

        m_imgMainAttachment = (ImageView) findViewById(R.id.imgMainAttachment);

        m_spinCountries = (AppCompatSpinner) findViewById(R.id.spinCountry);
        m_spinProvinces = (AppCompatSpinner) findViewById(R.id.spinProvince);

        m_recycleAttachments = (RecyclerView) findViewById(R.id.recAttachments);
        m_layoutMangager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        m_recycleAttachments.setLayoutManager(m_layoutMangager);
    }

    @Override
    public void populateScreen()
    {
        getCountries();

        getProvinces();

        getAttachments();
    }

    private void getAttachments()
    {
        m_attachments = new ArrayList<>();
        m_attachments.add(new Attachment(-1, 0, "Add Photo", false));

        loadAttachments();
    }

    private void loadAttachments()
    {
        m_recAttachmentAdapter = new HorizontalAttachmentAdapter(this, R.layout.attachment_preview, m_attachments, m_attachmentPreviewClick);
        m_recycleAttachments.setAdapter(m_recAttachmentAdapter);
    }

    View.OnClickListener m_attachmentPreviewClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            ArrayList<String> options = new ArrayList<>();
            options.add("From Camera");
            options.add("From Gallery");

            ScreenUtils.showListDialog(AddRealEstateScreen.this, options, "Add Photo", m_photoOptionsClick, "CANCEL", null, true);
        }
    };

    DialogInterface.OnClickListener m_photoOptionsClick = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            if (which == 0)
            {
                choosePhotoFromCamera();
            }
            else
            {
                choosePhotoFromGallery();
            }
        }
    };

    public void choosePhotoFromCamera()
    {
        Boolean cameraPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        if (!cameraPermissionGranted)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA))
            {
                showExplanationPopup();
                Log.i("", "");
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            }
        }
        else
        {
            Crop.takePhoto(AddRealEstateScreen.this);
        }
    }

    public void choosePhotoFromGallery()
    {
        Crop.pickImage(AddRealEstateScreen.this);
    }

    private void showExplanationPopup()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission to open Camera?");
        builder.setMessage("In order to take a picture, Logbox requires access to the device's camera and file storage. Would you like to allow access to this feature?");
        builder.setPositiveButton("YES, CONTINUE", m_retryPermission);
        builder.setNegativeButton("NO, DON'T OPEN", m_returnPermission);
        builder.show();
    }

    DialogInterface.OnClickListener m_retryPermission = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            ActivityCompat.requestPermissions(AddRealEstateScreen.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    };

    DialogInterface.OnClickListener m_returnPermission = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
//            m_canUseContactSelector = false;
        }
    };

    private void getProvinces()
    {
        m_provinces = new ArrayList<>();
        m_provinces.add("Eastern Cape");
        m_provinces.add("Free State");
        m_provinces.add("Gauteng");
        m_provinces.add("KwaZulu-Natal");
        m_provinces.add("Limpopo");
        m_provinces.add("Mpumalanga");
        m_provinces.add("Northern Cape");
        m_provinces.add("North West");
        m_provinces.add("Ruanstown");
        m_provinces.add("Western Cape");

        loadProvinces();
    }

    private void loadProvinces()
    {
        m_provinceAdapter = new GenericSpinnerAdapter(this, R.layout.dialog_text_row, m_provinces);

        m_spinProvinces.setAdapter(m_provinceAdapter);
    }

    private void getCountries()
    {
        RetrofitUtils.setBaseUrl("https://restcountries.eu/rest/");
        IMainConttroller conttroller = RetrofitUtils.build().create(IMainConttroller.class);

        Call<ArrayList<Country>> call = conttroller.getCountries();
        call.enqueue(new Callback<ArrayList<Country>>()
        {
            @Override
            public void onResponse(Call<ArrayList<Country>> call, Response<ArrayList<Country>> response)
            {
                m_countries = response.body();
                loadCountries();
            }

            @Override
            public void onFailure(Call<ArrayList<Country>> call, Throwable t)
            {

            }
        });
    }

    private void loadCountries()
    {
        ArrayList<String> countryNames = Utils.getStringList(m_countries);

        m_countryAdapter = new GenericSpinnerAdapter(this, R.layout.dialog_text_row, countryNames);

        m_spinCountries.setAdapter(m_countryAdapter);
    }

    private void beginCrop(Uri source)
    {
        Date date = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyyyy_HHmmss");

        String timeNow = dateFormatter.format(date);

        Uri destination = Uri.fromFile(new File(getCacheDir(), timeNow+".jpg"));
        Crop.of(source, destination).asSquare().withMaxSize(512,512).start(this);
    }

    private void handleCrop(int resultCode, Intent result)
    {
        if (resultCode == RESULT_OK)
        {
            final Uri rawData = Crop.getOutput(result);

            String path = rawData.getPath();

            m_imagePath = path;

            if (m_attachments == null)
            {
                m_attachments = new ArrayList<>();
                m_attachments.add(new Attachment(-1, 0, "Add Photo", false));
            }

            Boolean isMain = false;
            if (m_attachments.size() == 1)
            {
                isMain = true;

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        m_imgMainAttachment.setImageDrawable(null);
                        m_imgMainAttachment.setImageURI(rawData);
                    }
                });
            }
            Attachment selectedAttachment = new Attachment(0, m_attachments.size(), "Photo Description", isMain, path);

            if (m_recycleAttachments == null)
            {
                m_attachments.add(selectedAttachment);
                m_recAttachmentAdapter = new HorizontalAttachmentAdapter(this, R.layout.attachment_preview, m_attachments, m_attachmentPreviewClick);
            }
            else
            {
                m_recAttachmentAdapter.addItem(selectedAttachment);
            }
//            m_recycleAttachments.setAdapter(m_recAttachmentAdapter);
        }
        else if (resultCode == Crop.RESULT_ERROR)
        {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void onActivityTakePicture()
    {
        String imageFilePath = getOutputMediaFileUri().getPath();

        if (scaleImage(imageFilePath, 1024,1024,true))
        {
            beginCrop(getOutputMediaFileUri());
        }
    }

    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile()
    {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Logbox Images");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "image.jpg" );

        return mediaFile;
    }

    public Boolean scaleImage(String path, int width, int height, boolean keepAspectRatio)
    {
        Bitmap bitmap = null;

        int orientation;
        ExifInterface exif = null;
        Matrix m = new Matrix();

        try
        {
            exif = new ExifInterface(path);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (exif != null)
        {
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            if ((orientation == ExifInterface.ORIENTATION_ROTATE_180))
            {
                m.postRotate(180);
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
            {
                m.postRotate(90);
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
            {
                m.postRotate(270);
            }
        }

        if (!TextUtils.isEmpty(path))
        {
            bitmap = decodeSampledBitmapFromFile(path, width, height);
        }

        if (bitmap == null) return false;

        if (!keepAspectRatio)
        {
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        }
        else
        {
            int oldWidth = bitmap.getWidth();
            int oldHeight = bitmap.getHeight();


            int newWidth = 0;
            int newHeight = 0;

            if (oldWidth > oldHeight)
            {
                newWidth = width;

                double ratio = (double) oldWidth / (double) newWidth;

                newHeight = (int) (oldHeight / ratio);
            }
            else
            {
                newHeight = height;

                double ratio = (double) oldHeight / (double) newHeight;

                newWidth = (int) (oldWidth / ratio);
            }

            if (newHeight > oldHeight && newWidth > oldWidth)
            {
                return true;
            }
            else
            {
                bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
            }

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, newWidth, newHeight, m, true);
        }

        FileOutputStream out;
        try
        {
            out = new FileOutputStream(new File(path));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            bitmap.recycle();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            if (requestCode == Crop.REQUEST_PICK)
            {
                beginCrop(data.getData());
            }
            else if (requestCode == Crop.REQUEST_PHOTO)
            {
                onActivityTakePicture();
            }
            else if (requestCode == Crop.REQUEST_CROP)
            {
                handleCrop(resultCode, data);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case CAMERA_PERMISSION_CODE:
            {
                if (grantResults.length == 0)
                {
                    break;
                }

                Crop.takePhoto(AddRealEstateScreen.this);

                break;
            }
        }
    }

}
