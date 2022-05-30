package com.example.labtestapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.labtestapplication.util.Constants;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView profileImage;
    private TextView profileEmail, profileUsername, contactProfile;
    private EditText profileEmailEdit, profileUsernameEdit, contactEdit;
    private AppCompatButton updateBtn;
    private StringRequest stringRequest;
    private VolleySingleton singleton;
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_CONTACT = "contact";
    private static final int PICK_IMAGE_REQUEST = 1;
    private SharedPreferences preferences;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("PROFILE");
        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        init();
    }

    private void init() {
        profileImage = findViewById(R.id.user_profile_photo);
        profileEmail = findViewById(R.id.user_profile_email);
        contactProfile = findViewById(R.id.user_profile_contact);
        contactEdit = findViewById(R.id.contactProfile);
        profileUsername = findViewById(R.id.user_profile_name);
        profileEmailEdit = findViewById(R.id.emailProfile);
        profileUsernameEdit = findViewById(R.id.usernameProfile);
        updateBtn = findViewById(R.id.buttonUpProfile);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        profileUsername.setText(preferences.getString("username", ""));
        profileEmail.setText(preferences.getString("email", ""));
        profileUsernameEdit.setText(preferences.getString("username", ""));
        profileEmailEdit.setText(preferences.getString("email", ""));
        contactProfile.setText(preferences.getString("contact", ""));
        contactEdit.setText(preferences.getString("contact", ""));
        if(!preferences.getString("image", "").equals("NULL")){
            Picasso.with(this).load(preferences.getString("image", "")).into(profileImage);
            //new DownloadImage().execute();
        }/*else{
            profileImage.setImageResource(R.drawable.user);
        }*/
        profileEmail.setEnabled(false);
        updateBtn.setOnClickListener(this);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
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
    public void onClick(View v) {
        if(v==updateBtn)
            updateUser();
    }

    private void updateUser() {
        String imgUrl = getStringImage(bitmap);
        String contact = contactEdit.getText().toString().trim();
        String username = Objects.requireNonNull(profileUsernameEdit.getText()).toString().trim();
        String email = Objects.requireNonNull(profileEmailEdit.getText()).toString().trim();
        if (imgUrl!=null && username!=null && email!=null && contact!=null){
            stringRequest = new StringRequest(Request.Method.POST, Constants.IMAGE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("INFO", response);
                            if (response.equals("success")){
                                Toast.makeText(getApplicationContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                String imageUrl = "https://flyindex.000webhostapp.com/uploads/"+username+".jpg";
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("username", username);
                                editor.putString("contact", contact);
                                editor.putString("email", email);
                                editor.putString("image", imageUrl);
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
                    hashmap.put(KEY_USERNAME, username);
                    hashmap.put(KEY_EMAIL, email);
                    hashmap.put(KEY_IMAGE, imgUrl);
                    hashmap.put(KEY_CONTACT, contact);
                    return hashmap;
                }
            };
            singleton = VolleySingleton.getInstance(this);
            singleton.addToRequestQueue(stringRequest);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                profileImage.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                profileImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}