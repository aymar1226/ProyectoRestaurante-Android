package com.example.proyectorestaurante;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

public class ImageUploader {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_PERMISSION_STORAGE = 2;

    private Activity activity;
    private StorageReference storageReference;
    private Uri imageUri;
    private String imageName=null;

    public ImageUploader(Activity activity, StorageReference storageReference) {
        this.activity = activity;
        this.storageReference = storageReference;
    }

    public void abrirSelector() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void subirImagen(final Runnable whenDone) {
        StorageReference fileReference = storageReference.child("imagenes/" + imageName);
        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // La imagen se ha subido exitosamente a Firebase Storage
                    whenDone.run();
                })
                .addOnFailureListener(e -> {
                    // Ocurrió un error al subir la imagen a Firebase Storage
                });
    }

    public void seleccionarImagen() {
        if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            abrirSelector();
        } else {
            activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                abrirSelector();
            } else {
                Toast.makeText(activity, "Los permisos de almacenamiento son necesarios para seleccionar una imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageName = getFileName(imageUri);
            Toast.makeText(activity.getApplicationContext(), "Imagen cargada", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    public String getImageName(){
        return imageName;
    }

    public void eliminarImagen(String imagenPorEliminar){

        // Obtén una referencia al archivo que deseas eliminar
        StorageReference fileRef = storageReference.child("imagenes/"+imagenPorEliminar);

        // Elimina el archivo
        fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // El archivo se eliminó exitosamente
                Log.d("ImageUploader", "Archivo eliminado correctamente");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Ocurrió un error al eliminar el archivo
                Log.e("ImageUploader", "Error al eliminar el archivo: " + e.getMessage());
            }
        });

    }
}

