package com.example.proyectorestaurante;


import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.proyectorestaurante.recycler.Platos;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class GeneradorPDF {
    Platos platos;
    Context mContext;
    ImageData fondo;
    Image imagenFire;
    ImageView mQrImageView;
    List<Paragraph> listaPlatos = new ArrayList<>();
    List<String> listaNombreImagenes= new ArrayList<>();
    ProgressBar mProgressBar;

    public GeneradorPDF(Context context,ProgressBar bar,ImageView qrImageView) {
        mContext = context;
        mProgressBar=bar;
        mQrImageView=qrImageView;
    }

    public void obtenerDatos() {

        listaPlatos.clear();
        listaNombreImagenes.clear();

        Connection connection = ConexionDB.obtenerConexion();
        String query = "SELECT id_plato, plato.nombre, precio,imagen  FROM plato";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            int a=0;
            while (resultSet.next()) {
                a++;
                int idPlato = resultSet.getInt("id_plato");
                String nombrePlato = resultSet.getString("nombre");
                double precio = resultSet.getDouble("precio");
                String imagen = resultSet.getString("imagen");
                String plato;
                if(a%2==0) {
                    plato = nombrePlato + "\n\n"+precio+" - - - - - - - - - - ";
                }else {
                    plato = nombrePlato + "\n\n - - - - - - - - - - " + precio;
                }
                Paragraph platoInfo = new Paragraph(plato);

                listaPlatos.add(platoInfo);
                listaNombreImagenes.add(imagen);
            }
            statement.close();
            resultSet.close();
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public CompletableFuture<List<Image>> obtenerImgFirebase() {
        List<CompletableFuture<Image>> futures = new ArrayList<>();

        int imagenesTotales = listaNombreImagenes.size();
        for (int i = 0; i < imagenesTotales; i++) {
            String imagen_nombre = listaNombreImagenes.get(i);
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("imagenes/" + imagen_nombre);
            CompletableFuture<Image> future = new CompletableFuture<>();
            futures.add(future);
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                try {
                    String imageUrl = uri.toString();
                    ImageData imageData = ImageDataFactory.create(imageUrl);
                    Image image = new Image(imageData);
                    future.complete(image);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }).addOnFailureListener(exception -> {
                future.completeExceptionally(exception);
            });
        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        return allFutures.thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList()));
    }

    public void cargarFondo(){
        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("imagenes/background_pdf.jpeg");
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            try {
                String imageUrl = uri.toString();
                 fondo = ImageDataFactory.create(imageUrl);

            } catch (Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(exception -> {

        });
    }

    public void cargarImagen(String imagePath){
        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("imagenes/"+imagePath);
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            try {
                String imageUrl = uri.toString();
                ImageData imageData = ImageDataFactory.create(imageUrl);
                imagenFire = new Image(imageData);

            } catch (Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(exception -> {

        });

    }


    public void generarPdf() {
        obtenerDatos();

        //Metodos asincronicos
        cargarFondo();
        cargarImagen("icon.png");

        CompletableFuture<List<Image>> obtenerImagenesFuture = obtenerImgFirebase();

        obtenerImagenesFuture.thenAccept(listaImagenes -> {
            // Cargar la fuente personalizada desde la carpeta 'assets'
            Context context = mContext;
            AssetManager assetManager = context.getAssets();

            //Cargar la fuente personalizada
            PdfFont customFont = null;
            try {
                InputStream fontStream = assetManager.open("fuente.ttf");

                // Copiar el archivo de fuente en el almacenamiento interno temporal del dispositivo
                File tempFile = File.createTempFile("temp", ".ttf");
                FileOutputStream out = new FileOutputStream(tempFile);
                byte[] buffer = new byte[fontStream.available()];
                fontStream.read(buffer);
                out.write(buffer);
                out.close();

                customFont = PdfFontFactory.createFont(tempFile.getAbsolutePath(), PdfEncodings.IDENTITY_H, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Crear la tabla para el menú
            Table table = new Table(2); // 2 columnas (imagen y texto)
            table.setMarginBottom(20);
            table.setMarginLeft(40);
            table.setBorder(Border.NO_BORDER);

            // Crear la tabla inversa para el menú
            Table inverseTable = new Table(2); // 2 columnas (texto Y imagen)
            inverseTable.setMarginBottom(30);
            inverseTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            inverseTable.setMarginRight(40);
            inverseTable.setBorder(Border.NO_BORDER);


            //Crear tabla para el Titulo
            Table titleTable = new Table(2); // 2 columnas (imagen y texto)
            titleTable.setMarginLeft(70);
            titleTable.setMarginTop(50);
            titleTable.setMarginBottom(50);
            titleTable.setBorder(Border.NO_BORDER);

            //Color del texto
            Color color = new DeviceRgb(0x71, 0x23, 0x23); // Código de color hexadecimal #712323 (rojo)

            //Definir el estilo del texto
            Style textStyle = new Style();
            textStyle.setFontSize(27);
            textStyle.setFontColor(color);

            //Definir el estilo del titulo
            Style titleStyle = new Style();
            titleStyle.setFontSize(40);
            titleStyle.setFontColor(color);
            titleStyle.setMarginLeft(10);
            titleStyle.setVerticalAlignment(VerticalAlignment.MIDDLE);
            titleStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);

            //Definir el estilo de las celdas
            Style cellStyle = new Style();
            cellStyle.setBorder(Border.NO_BORDER);

            //Creacion del pdf
            try {
                String filePath = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/cartilla.pdf";

                PdfDocument pdf = new PdfDocument(new PdfWriter(filePath));
                Document document = new Document(pdf, PageSize.A4);

                // Agregar el manejador de eventos de imagen de fondo
                BackgroundImageEventHandler eventHandler = new BackgroundImageEventHandler(fondo);
                pdf.addEventHandler(PdfDocumentEvent.END_PAGE, eventHandler);

                //Definir y agregar titulo
                String titleText = "HINOJO RESTAURANTE";
                Paragraph title = new Paragraph(titleText);
                title.addStyle(titleStyle);
                title.setFont(customFont);

                //Definir y agregar logo
                Image titleImage = imagenFire;
                titleImage.setWidth(175);
                titleImage.setHeight(165);
                titleImage.setMarginRight(10);
                titleImage.setMarginTop(0);
                titleImage.setHorizontalAlignment(HorizontalAlignment.RIGHT);

                Cell titleCell = new Cell();
                titleCell.addStyle(cellStyle);
                Cell logoCell = new Cell();
                logoCell.addStyle(cellStyle);

                logoCell.add(titleImage);
                titleCell.add(title);

                titleTable.addCell(logoCell);
                titleTable.addCell(titleCell);

                document.add(titleTable);

                int totaldatos = listaImagenes.size();

                for (int i = 0; i < totaldatos; i++) {

                    Paragraph platoInfo = listaPlatos.get(i).setFont(customFont);
                    platoInfo.addStyle(textStyle);

                    //Editar Imagen
                    Image imagenPlato = listaImagenes.get(i);
                    imagenPlato.setWidth(180);
                    imagenPlato.setHeight(110);


                    // Crear celdas para la imagen y la información del plato
                    Cell imageCell = new Cell();
                    imageCell.addStyle(cellStyle);
                    Cell infoCell = new Cell();
                    infoCell.addStyle(cellStyle);

                    if(i%2!=0) {
                        //Agregar el contenido a las celdas
                        infoCell.add(platoInfo);
                        imageCell.add(imagenPlato);

                        // Agregar las celdas a la tabla
                        inverseTable.addCell(infoCell);
                        inverseTable.addCell(imageCell);

                        document.add(inverseTable);
                    }else{
                        //Agregar el contenido a las celdas
                        imageCell.add(imagenPlato);
                        infoCell.add(platoInfo);

                        // Agregar las celdas a la tabla
                        table.addCell(imageCell);
                        table.addCell(infoCell);

                        document.add(table);

                    }
                }

                document.close();
                subirPdfFirebase(filePath);
                pdf.close();
            } catch (FileNotFoundException e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).exceptionally(e -> {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        });
    }


    public void subirPdfFirebase(String filePath){
        // Obtén una instancia de FirebaseStorage
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Obtén una referencia a la ubicación del archivo en Firebase Storage
        StorageReference storageRef = storage.getReference().child("pdfs/cartilla.pdf");

        // Crea una referencia al archivo local
        Uri file = Uri.fromFile(new File(filePath));

        // Sube el archivo a Firebase Storage
        storageRef.putFile(file)
                .addOnSuccessListener(taskSnapshot -> {
                    // El archivo se ha subido exitosamente
                    // Aquí puedes realizar cualquier acción adicional después de la subida del archivo
                    mProgressBar.setVisibility(View.GONE);
                    mQrImageView.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, "Pdf generado exitosamente", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(exception -> {
                    // Ocurrió un error al subir el archivo
                    // Aquí puedes manejar el error o mostrar un mensaje de error al usuario
                    Toast.makeText(mContext, "El pdf no se pudo generar", Toast.LENGTH_SHORT).show();

                });
    }
}

