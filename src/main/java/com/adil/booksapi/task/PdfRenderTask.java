package com.adil.booksapi.task;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;
import java.util.concurrent.CountDownLatch;

public class PdfRenderTask implements Runnable {
    private CountDownLatch countDownLatch;
    private final PDDocument document;
    private final int pageNumber;
    private final int dpi;
    private final JSONArray jsonArray;

    private final JSONObject jsonObject;

    public PdfRenderTask(PDDocument document, int pageNumber, int dpi, JSONArray jsonArray, JSONObject jsonObject, CountDownLatch countDownLatch) {
        this.document = document;
        this.pageNumber = pageNumber;
        this.dpi = dpi;
        this.jsonArray = jsonArray;
        this.jsonObject = jsonObject;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                BufferedImage bim = pdfRenderer.renderImageWithDPI(pageNumber, dpi);
                document.close();

                // Convert BufferedImage to Base64 string
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bim, "jpeg", baos);
                byte[] imageBytes = baos.toByteArray();
                String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

                // Add imageBase64 to JSON object
                jsonObject.put("imageBase64", imageBase64);
                jsonArray.put(jsonObject);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        countDownLatch.countDown();
    }
}
