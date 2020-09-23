package com.example.stream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stream.Model.videoUploadDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.io.FilenameUtils;

import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Uri videoUrl;
    TextView text_video_selected;
    String videoCat,videotitle,currentuid;
    StorageReference mstorageRef;
    StorageTask mUploadTask;
    DatabaseReference referenceVideos;
    EditText video_description;
    Button upload,uploadtofirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_video_selected = findViewById(R.id.textvideoselected);
        video_description = findViewById(R.id.movie_description);
        referenceVideos = FirebaseDatabase.getInstance().getReference().child("videos");
        mstorageRef = FirebaseStorage.getInstance().getReference().child("videos");
        upload = findViewById(R.id.uploads_videos_btn);
        uploadtofirebase = findViewById(R.id.buttonUpload);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<>();

        categories.add("Action");
        categories.add("Adventure");
        categories.add("Sport");
        categories.add("Comedy");
        categories.add("Romantic");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(Intent.ACTION_GET_CONTENT);
                in.setType("video/*");
                startActivityForResult(in, 101);
            }
        });

        uploadtofirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadFileToFirebase(view);
            }
        });
    }

        @Override
        public void onItemSelected (AdapterView < ? > adapterView, View view,int i, long l){

            videoCat = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(this, "select" + videoCat, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected (AdapterView < ? > adapterView){


        }


        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 101 && resultCode == RESULT_OK && data.getData() != null) {
                videoUrl = data.getData();

                String path = null;
                Cursor cursor;
                int coloum_index_data;
                String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media._ID, MediaStore.Video.Thumbnails.DATA};

                final String orderby = MediaStore.Video.Media.DEFAULT_SORT_ORDER;
                cursor = MainActivity.this.getContentResolver().query(videoUrl, projection, null, null, orderby);
                coloum_index_data = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                while (cursor.moveToNext()) {
                    path = cursor.getString(coloum_index_data);
                    videotitle = FilenameUtils.getBaseName(path);

                }
                text_video_selected.setText(videotitle);

            }
        }

        public void uploadFileToFirebase (View v){
            if (text_video_selected.equals("no video selectd")) {
                Toast.makeText(this, "please select an video", Toast.LENGTH_SHORT).show();
            } else {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(this, "video uploads is all ready in progress..", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFiles();
                }
            }

        }

        private void uploadFiles () {
            if (videoUrl != null) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("video uploaded...");
                progressDialog.show();
                final StorageReference storageReference = mstorageRef.child(videotitle);
                mUploadTask = storageReference.putFile(videoUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String video_url = uri.toString();
                                videoUploadDetails VideoUploadDetails = new videoUploadDetails("", "", "", video_url, videotitle, video_description.getText().toString(), videoCat);
                                String uploadsid = referenceVideos.push().getKey();
                                referenceVideos.child(uploadsid).setValue(VideoUploadDetails);
                                currentuid = uploadsid;
                                progressDialog.dismiss();

                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 + taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded" + ((int) progress) + "%...");
                    }
                });

            } else {
                Toast.makeText(this, "no video selected to upload", Toast.LENGTH_SHORT).show();
            }
        }

}
