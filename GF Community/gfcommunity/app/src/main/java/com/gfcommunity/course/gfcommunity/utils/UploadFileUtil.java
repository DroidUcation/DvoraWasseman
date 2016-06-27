package com.gfcommunity.course.gfcommunity.utils;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.gfcommunity.course.gfcommunity.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/**
 * Upload file to firebase storage
 */
public class UploadFileUtil {
    private static String logTag = UploadFileUtil.class.getName();


    public interface OnuploadCompletedListener{
        void onUrlReceived(Uri uri);
    }

    public static void uploadFile(Context context, String urlPath , final OnuploadCompletedListener listener){
        //final Uri downloadUrlPath;
        Uri file = Uri.fromFile(new File(urlPath));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://" + context.getString(R.string.google_storage_bucket));

        // Create a child refere''nce imagesRef now points to "images"
        StorageReference imagesRef = storageRef.child("images");
        StorageReference productRef = storageRef.child("images/"+"product_img"+String.valueOf(System.currentTimeMillis())+file.getLastPathSegment());
        UploadTask uploadTask = productRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(logTag,"Failed to upload file to firebase storage");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                final Uri downloadUri = taskSnapshot.getDownloadUrl();
                Log.i(logTag, "Upload file to firebase storage successfully");

                listener.onUrlReceived(downloadUri);
            }
        });
    }
}
