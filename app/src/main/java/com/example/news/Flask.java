package com.example.news;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;


public class Flask extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;

    String cameraPermission[];
    String storagePermission[];
    Uri image_uri;

    ImageView mPreviewIv;
    Button addImage1, addImage2, addImage3;
    Button btnSubmit,backflask;
    EditText etAuthor, etTitle, etText;

    boolean click1 = false;
    boolean click2 = false;
    boolean click3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flask);

        btnSubmit = findViewById(R.id.btnSubmit);
        etAuthor = findViewById(R.id.etAuthor);
        etTitle = findViewById(R.id.etTitle);
        etText = findViewById(R.id.etText);
        final TextView textView = findViewById(R.id.text2);

        addImage1 = findViewById(R.id.addImage1);
        addImage2 = findViewById(R.id.addImage2);
        addImage3 = findViewById(R.id.addImage3);
        mPreviewIv = findViewById(R.id.imageIv1);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        addImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click1 = true;

                showImageImportDialog();

            }
        });
        addImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click2 = true;

                showImageImportDialog();


            }
        });
        addImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click3 = true;

                showImageImportDialog();


            }
        });


            final RequestQueue queue = Volley.newRequestQueue(this);
        final String url ="http://192.168.43.127:5000";
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        /*
        String author = "Divine Revelation Of Heaven & Hell";
        String title = "Death row inmate eats 1200 pages of the Bible as his last meal.";
        String text = "A condemned murderer, Jeremy Morris from Atmore Alabama has reportedly eaten 1200 pages of the Holy bible he requested from a prison guard.The 33-year-old and murderer of two catholic nuns requested the bible as his last meal.According to Worldnews, the prison guards of the William C. Holman Correctional Facility, thought he wanted to pray and repent, so they gave him an old copy of the King James Bible.But surprisingly, the condemned murderer took hours to tear the 1200-page holy book including its cover into small pieces before he ate them.The prison guard, Walter Henri, who handed him the old and worn out Bible, says the inmate seemed to “savor every bite.”“He kept tearing pieces from his Bible and eating them like they were potato chips!”";
        */
                if (etTitle.getText().toString().isEmpty()) {
                    etTitle.setError("Please enter Title");
                    etTitle.requestFocus();
                } else {
                    JSONObject postparams = new JSONObject();
                    try {

                        postparams.put("author", etAuthor.getText());
                        postparams.put("title", etTitle.getText());
                        postparams.put("text", etText.getText());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, postparams,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                                    String data = null;
                                    try {
                                        data = response.getString("data");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    textView.setVisibility(View.VISIBLE);
                                    textView.setText("This News is "+data);
                                    textView.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            textView.setVisibility(View.INVISIBLE);
                                        }
                                    },10000);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Toast.makeText(getApplicationContext(), "That didn't Work", Toast.LENGTH_LONG).show();
                                    textView.setText("That didn't work!");
                                }
                            });

                    queue.add(jsonObjReq);
                }
            }
        });

        backflask = findViewById(R.id.backflask);
        backflask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


            }

    private void showImageImportDialog() {
        String[] items = {"Camera", "Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickCamera();
                    }

                }
                if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickGallery();
                    }

                }

            }
        });
        dialog.create().show();
    }

    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to text");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        pickCamera();
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        pickGallery();
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(this);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON).start(this);
            }

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mPreviewIv.setImageURI(resultUri);

                BitmapDrawable bitmapDrawable = (BitmapDrawable) mPreviewIv.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "Error,Text Not Readable", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());
                        sb.append("\n");//new line in last line
                    }
                    if(click1) {
                        etAuthor.setText(sb.toString());
                        click1 = false;
                    } else if(click2){
                        etTitle.setText(sb.toString());
                        click2 = false;
                    } else{
                        etText.setText(sb.toString());
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, " " + error, Toast.LENGTH_SHORT).show();


            }
        }


    }



    }

