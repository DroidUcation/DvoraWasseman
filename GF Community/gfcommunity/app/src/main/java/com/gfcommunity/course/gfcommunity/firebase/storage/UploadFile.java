package com.gfcommunity.course.gfcommunity.firebase.storage;
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
public class UploadFile {
    private static String logTag = UploadFile.class.getName();


    public interface OnuploadCompletedListener{
        void onUrlReceived(Uri uri);
    }

    public static void uploadFile(Context context, Uri fileUri , final OnuploadCompletedListener listener, String fileCategory){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://" + context.getString(R.string.google_storage_bucket));

        // Create a child reference imagesRef now points to "images"
        StorageReference imagesRef = storageRef.child("images");
        StorageReference itemRef = storageRef.child("images/"+fileCategory+"s/"+fileCategory+"_img"+String.valueOf(System.currentTimeMillis())+fileUri.getLastPathSegment());
        UploadTask uploadTask = itemRef.putFile(fileUri);

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
