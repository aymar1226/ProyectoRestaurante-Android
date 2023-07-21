package com.example.proyectorestaurante;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

public class BackgroundImageEventHandler implements IEventHandler {
    protected ImageData img;

    public BackgroundImageEventHandler(ImageData img) {
        this.img = img;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfPage page = docEvent.getPage();
        PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), docEvent.getDocument());

        // Ajusta la imagen al tamaño de la página
        Rectangle area = page.getPageSize();
        pdfCanvas.addImage(img, area, false);
    }
}

