package com.example.activityplanner.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.activityplanner.BuildConfig;
import com.example.activityplanner.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AddPicturesActivity extends AppCompatActivity {

    private List<String> pictureUris;
    private String currentPhotoPath;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pictures);

        pictureUris = (List<String>) getIntent().getSerializableExtra("pictures");
        loadCarousel();

        findViewById(R.id.urlBtn).setOnClickListener(this::addPictureFromUrl);
        findViewById(R.id.uploadBtn).setOnClickListener(this::addPictureFromDevice);
        findViewById(R.id.cameraBtn).setOnClickListener(this::addPictureFromCamera);
        findViewById(R.id.finishBtn).setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("pictures", (Serializable) pictureUris.stream().distinct().collect(Collectors.toList()));
            setResult(RESULT_OK, intent);
            finish();
        });
        findViewById(R.id.leftBtn).setOnClickListener(view -> {
            currentIndex = currentIndex == 0 ? pictureUris.size() - 1 : currentIndex - 1;
            loadCarousel();
        });
        findViewById(R.id.rightBtn).setOnClickListener(view -> {
            currentIndex = currentIndex == pictureUris.size() - 1 ? 0 : currentIndex + 1;
            loadCarousel();
        });
        findViewById(R.id.deleteBtn).setOnClickListener(view -> {
            if (pictureUris.size() > 0) {
                pictureUris.remove(currentIndex);
                currentIndex = 0;
                loadCarousel();
            }
        });
    }

    //TODO: check Internet connection
    private void addPictureFromUrl(View view) {
        EditText urlET = findViewById(R.id.urlET);
        String uri = String.valueOf(urlET.getText());
        if (!uri.isEmpty()) {
            Picasso.get().load(uri);
            pictureUris.add(uri);
            ((EditText) findViewById(R.id.urlET)).setText("");
            Toast.makeText(this, R.string.picture_uploaded, Toast.LENGTH_SHORT).show();
            loadCarousel();
        }
    }

    private void addPictureFromCamera(View view) {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startCamera.launch(takePictureIntent);
                }
            }

        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private final ActivityResultLauncher<Intent> startCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && currentPhotoPath != null) {
                    pictureUris.add("file://" + currentPhotoPath);
                    currentPhotoPath = null;
                    Toast.makeText(this, R.string.picture_uploaded, Toast.LENGTH_SHORT).show();
                    loadCarousel();
                }
            });

    private void addPictureFromDevice(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startGallery.launch(intent);
    }

    private final ActivityResultLauncher<Intent> startGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getClipData() != null) {
                        ClipData clipData = data.getClipData();
                        int count = clipData.getItemCount();
                        for (int i = 0; i < count; i++) {
                            Uri uri = clipData.getItemAt(i).getUri();
                            String savedPath = saveImage(uri);
                            if (savedPath != null) {
                                String path = "file://" + saveImage(uri);
                                pictureUris.add(path);
                            }
                        }
                        Toast.makeText(this, R.string.picture_uploaded, Toast.LENGTH_SHORT).show();
                        loadCarousel();
                    } else if (data != null && data.getData() != null) {
                        Uri uri = data.getData();
                        String path = "file://" +  saveImage(uri);
                        pictureUris.add(path);
                        Toast.makeText(this, R.string.picture_uploaded, Toast.LENGTH_SHORT).show();
                        loadCarousel();
                    }
                }
            });

    private String saveImage(Uri uri) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSS").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_.jpg";
        try (OutputStream outputStream = new FileOutputStream(new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageFileName));
             InputStream inputStream = getContentResolver().openInputStream(uri)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageFileName).getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadCarousel() {
        if (pictureUris.size() == 0) {
            findViewById(R.id.carousel).setVisibility(View.GONE);
        }
        else {
            findViewById(R.id.carousel).setVisibility(View.VISIBLE);
            ImageView imageView = findViewById(R.id.imageView);
            Picasso.get().load(pictureUris.get(currentIndex)).into(imageView);
        }
    }

}