package com.example.labtestapplication.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.labtestapplication.AddUserPopupAct;
import com.example.labtestapplication.ProfileActivity;
import com.example.labtestapplication.R;
import com.example.labtestapplication.VolleySingleton;
import com.example.labtestapplication.adapter.MyReportsAdapter;
import com.example.labtestapplication.model.Bookings;
import com.example.labtestapplication.util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Profile extends Fragment implements View.OnClickListener {

    private static final int RESULT_OK = 1;
    private ImageView profileImage, edit_profile;
    private TextView profileEmail, profileUsername1, profileContact;
    private LinearLayout addUserLayout;
    private AppCompatTextView lastName, lastMembers, lastDate, lastTime;
    private EditText profileEmailEdit, profileUsernameEdit;
    private AppCompatButton updateBtn;
    private StringRequest stringRequest;
    private VolleySingleton singleton;
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE = "image";
    private static final int PICK_IMAGE_REQUEST = 1;
    private SharedPreferences preferences;
    //private SharedPreferences.Editor editor;
    private Bitmap bitmap;
    View view;


    public Profile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle("Your Profile");
        preferences = (SharedPreferences) this.getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        profileImage = (ImageView) view.findViewById(R.id.user_profile_photo_frag);
        addUserLayout = view.findViewById(R.id.addUserLayout);
        profileContact = view.findViewById(R.id.user_profile_contact_frag);
        profileEmail = view.findViewById(R.id.user_profile_emil_frag);
        profileUsername1 = (TextView) view.findViewById(R.id.user_profile_name_frag);
        edit_profile = view.findViewById(R.id.editProfile);
        lastName = view.findViewById(R.id.LastTestname);
        lastMembers = view.findViewById(R.id.LastUsername);
        lastDate = view.findViewById(R.id.LastDate);
        lastTime = view.findViewById(R.id.LastTime);

        SharedPreferences preferences = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        profileUsername1.setText(preferences.getString("username", ""));
        profileEmail.setText(preferences.getString("email", ""));
        profileContact.setText(preferences.getString("contact", ""));
        if(preferences.getString("image", "")!=null && preferences.getString("image", "")!=""){
            Picasso.with(getActivity()).load(preferences.getString("image", "")).into(profileImage);
        }else{
            Log.i("INFO", "no img");
        }
        lastUser();
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ProfileActivity.class);
                startActivity(i);
            }
        });
        addUserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopBox();
            }
        });
        return view;
    }

    private void showPopBox() {
        startActivity(new Intent(getActivity(), AddUserPopupAct.class));
    }

    public void pickImage() {
        startGallery();
    }

    @SuppressLint({"IntentReset", "QueryPermissionsNeeded"})
    private void startGallery() {
        //Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void updateUser() {
        String imgUrl = getStringImage(bitmap);
        String username = Objects.requireNonNull(profileUsernameEdit.getText()).toString().trim();
        String email = Objects.requireNonNull(profileEmailEdit.getText()).toString().trim();
        if (imgUrl != null && username != null && email != null) {
            stringRequest = new StringRequest(Request.Method.POST, Constants.IMAGE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("INFO", response);
                            if (response.equals("success")) {
                                Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                String imageUrl = "https://flyindex.000webhostapp.com/uploads/" + username + ".jpg";
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("username", username);
                                editor.putString("email", email);
                                editor.putString("image", imageUrl);
                                editor.apply();
                                editor.commit();
                            } else {
                                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERROR", error.toString());
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashmap = new HashMap<>();
                    hashmap.put(KEY_USERNAME, username);
                    hashmap.put(KEY_EMAIL, email);
                    hashmap.put(KEY_IMAGE, imgUrl);
                    return hashmap;
                }
            };
            singleton = VolleySingleton.getInstance(getActivity());
            singleton.addToRequestQueue(stringRequest);
        }
    }


    private void lastUser() {
            stringRequest = new StringRequest(Request.Method.POST, Constants.USER_LAST_BOOKING,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("INFO", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Bookings bookings = null;
                                for (int i=0; i<jsonArray.length(); i++) {
                                    JSONObject jObj = jsonArray.getJSONObject(i);
                                    lastName.setText(jObj.getString("testname"));
                                    lastMembers.setText(jObj.getString("members"));
                                    lastDate.setText(jObj.getString("date"));
                                    lastTime.setText(jObj.getString("time"));
                                }
                            }catch (JSONException e){
                                Log.e("ERRORPatho", e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERROR", error.toString());
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashmap = new HashMap<>();
                    hashmap.put(KEY_EMAIL, preferences.getString("email", ""));
                    return hashmap;
                }
            };
            singleton = VolleySingleton.getInstance(getActivity());
            singleton.addToRequestQueue(stringRequest);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Uri returnUri = data.getData();
                Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                profileImage.setImageBitmap(bitmapImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onClick(View v) {
    }

}