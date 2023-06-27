package com.example.proyectorestaurante;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;

public class FirebaseManager {
    private static final String TAG = "FirebaseManager";
    private static final String STORAGE_PATH = "pdfs/"; // Carpeta en la que se guardarán los PDFs

    private Context context;
    private FirebaseStorage storage;

    public FirebaseManager(Context context) {
        this.context = context;
        this.storage = FirebaseStorage.getInstance();
    }

    public void uploadPdfToFirebase(File pdfFile) {
        StorageReference storageRef = storage.getReference();

        // Obtener el nombre del archivo
        String fileName = pdfFile.getName();

        // Crear una referencia al archivo en Firebase Storage
        StorageReference fileRef = storageRef.child(STORAGE_PATH + fileName);

        // Obtener la URI del archivo local
        Uri fileUri = Uri.fromFile(pdfFile);

        // Subir el archivo a Firebase Storage
        fileRef.putFile(fileUri)
                .addOnSuccessListener((OnSuccessListener) o -> {
                    // El archivo se ha subido exitosamente
                    Log.d(TAG, "PDF subido a Firebase Storage");
                    // Aquí puedes realizar otras acciones después de subir el archivo, si es necesario

                })
                .addOnFailureListener(e -> {
                    // Se produjo un error al subir el archivo
                    Log.e(TAG, "Error al subir el PDF a Firebase Storage", e);
                    // Aquí puedes manejar el error de manera apropiada
                });
    }
}

