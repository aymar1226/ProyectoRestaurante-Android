package com.example.proyectorestaurante;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;


public class GeneradorPDF {
    public static void generarCartillaPDF(Context context) {
        Document document = new Document();
        String imageName="background_pdf";
        String imageExtension=".pdf";

        try {
            // Obtener conexión a la base de datos
            Connection connection = ConexionDB.obtenerConexion();
            Statement statement = connection.createStatement();

            // Consultar los platos desde la tabla "platos"
            ResultSet resultSet = statement.executeQuery("SELECT * FROM platos");

            // Obtener la ruta de la carpeta de Descargas
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            String nombreArchivo = "cartilla.pdf";
            String rutaCompleta = path + "/" + nombreArchivo;

            // Crear el archivo PDF
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(rutaCompleta));
            document.open();


            //Agregar titulo y imagen


            // Agregar los datos de los platos al PDF
            while (resultSet.next()) {

                //Definir fondo
                Image background=subirImagen(context,imageName,imageExtension);
                background.setAbsolutePosition(0, 0);
                background.scaleToFit(document.getPageSize());

                // Agregar la imagen de fondo al documento PDF
                PdfContentByte canvas = writer.getDirectContentUnder();
                canvas.addImage(background);

                String nombrePlato = resultSet.getString("nombre_plato");
                double precioPlato = resultSet.getDouble("precio");

                String platoExt = resultSet.getString(("imagen_plato"));
                String imagenPlato = platoExt.substring(0, platoExt.lastIndexOf('.'));
                // Agregar los datos al documento PDF
                Image image=subirImagen(context,imagenPlato,platoExt);
                image.scaleToFit(100, 100); // Ajusta el tamaño de la imagen


                document.add(image);

                document.add(new Paragraph("" + nombrePlato));
                document.add(new Paragraph("Precio: " + precioPlato));
                document.add(new Paragraph("")); // Agregar una línea en blanco
            }

            document.close();

            // Cerrar la conexión a la base de datos
            resultSet.close();
            statement.close();
            connection.close();

            System.out.println("PDF generado exitosamente en la carpeta de Descargas.");
        } catch (SQLException | DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Image subirImagen(Context context, String imageName,String imageExtension) throws BadElementException, IOException {

            // Cargar la imagen de fondo desde la carpeta drawable
            int imageResource = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageResource);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //Control de extension de imagen
            String fileExtension = imageExtension.substring(imageExtension.lastIndexOf(".") + 1).toLowerCase();
            Bitmap.CompressFormat compressFormat;
                if (fileExtension.equals("png")) {
                    compressFormat = Bitmap.CompressFormat.PNG;
                } else if (fileExtension.equals("webp")) {
                    compressFormat = Bitmap.CompressFormat.WEBP;
                } else {
                    compressFormat = Bitmap.CompressFormat.JPEG;
                }
                bitmap.compress(compressFormat, 50, stream);

            Image imagepdf = Image.getInstance(stream.toByteArray());
        return imagepdf;
    }

}

