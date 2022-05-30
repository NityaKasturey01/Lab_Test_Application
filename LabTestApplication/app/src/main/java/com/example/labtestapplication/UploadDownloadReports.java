package com.example.labtestapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.labtestapplication.util.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UploadDownloadReports extends AppCompatActivity {

    private AppCompatTextView testname, members, date;
    private AppCompatButton downloadBtn, uploadBtn;
    private AppCompatImageView pdfImage;
    private Bundle bundle;
    private SharedPreferences preferences;
    private VolleySingleton singleton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String KEY_TESTNAME = "testname";
    private static final String KEY_MEMBER = "members";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_REPORT = "report";
    private Bitmap bitmap;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int REQUEST_CODE = 999;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_download_reports);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Upload Reports");
        preferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        init();
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgress(0);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
    }

    private void init() {
        testname = findViewById(R.id.downloadTestname);
        members = findViewById(R.id.downloadMembers);
        uploadBtn = findViewById(R.id.downloadUbtn);
        downloadBtn = findViewById(R.id.downloadDbtn);
        pdfImage = findViewById(R.id.downloadPDFImage);
        date = findViewById(R.id.downloadDate);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bundle = getIntent().getExtras();
        testname.setText(bundle.getString("testname"));
        members.setText(bundle.getString("members"));
        date.setText(bundle.getString("date")+", "+bundle.getString("time"));
        if (preferences.contains("username")){
            downloadBtn.setVisibility(View.VISIBLE);
            uploadBtn.setVisibility(View.GONE);
            downloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grantPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                    new DownloadImage().execute();
                }
            });

        }else if (preferences.contains("dname")){
            downloadBtn.setVisibility(View.GONE);
            uploadBtn.setVisibility(View.VISIBLE);
            pdfImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickImage();
                }
            });
            uploadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUser();
                }
            });

        }
    }

    public void pickImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST);
    }
    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                pdfImage.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                pdfImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateUser() {
        String imgUrl = getStringImage(bitmap);
        if (imgUrl!=null){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPLOAD_PDF,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("INFO", response);
                            if (response.equals("success")){
                                Toast.makeText(getApplicationContext(), "Reports Uploaded", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), PathologyDetailsActivity.class));
                            }else {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERROR", error.toString());
                }
            })
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashmap = new HashMap<>();
                    hashmap.put(KEY_TESTNAME, bundle.getString("testname"));
                    hashmap.put(KEY_MEMBER, bundle.getString("members"));
                    hashmap.put(KEY_DATE, bundle.getString("date"));
                    hashmap.put(KEY_TIME, bundle.getString("time"));
                    hashmap.put(KEY_REPORT, imgUrl);
                    return hashmap;
                }
            };
            singleton = VolleySingleton.getInstance(this);
            singleton.addToRequestQueue(stringRequest);
        }
    }


    private void grantPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission)== PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);

        } else {
            Toast.makeText(getApplicationContext(), "Permission Already Granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == 101)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(), "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadImage extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            Bitmap bmp = null;
            InputStream is;
            try{
                is = new URL(bundle.getString("report")).openStream();
                bmp = BitmapFactory.decodeStream(is);
                //File storage = new File(Environment.getExternalStorageDirectory()+File.separator+"/Images/");
                File storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                Log.i("INFO","storage: "+storage);
                Log.i("INFO","path: "+storage.getAbsolutePath());
                if (!storage.exists()){
                    storage.mkdir();
                }
                String filname = "/"+ bundle.getString("testname") + bundle.getString("date") + ".jpg";
                FileOutputStream fos = new FileOutputStream(storage + filname);
                bmp.compress(Bitmap.CompressFormat.JPEG, 85, fos);
                String filePath = storage + filname;
                File fileCheck = new File(filePath);
                long fileSize = fileCheck.length();
                Log.i("FILESIZE", String.valueOf(fileSize));
                fos.flush();
                fos.close();
            }catch(IOException e){
                Log.e("ERROR", e.getMessage());
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap res) {
            super.onPostExecute(res);
            progressDialog.dismiss();
            pdfImage.setImageBitmap(res);
        }
    }
}