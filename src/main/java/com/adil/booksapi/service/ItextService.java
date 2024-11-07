package com.adil.booksapi.service;

import com.adil.booksapi.task.PdfRenderTask;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class ItextService {

    @Autowired
    private GoogleDriveAPIService googleDriveAPIService;

    public JSONArray getBookByParsingPdf() throws GeneralSecurityException, IOException, InterruptedException {
        JSONArray jsonArray = new JSONArray();
        List<File> files = googleDriveAPIService.getBooksFromDrive();
        CountDownLatch countDownLatch = new CountDownLatch(files.size());
        for (File file : files) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", file.getName());
            File fileMetadata = GoogleDriveAPIService.service.files().get(file.getId()).setFields("webViewLink").execute();
            String viewLink = fileMetadata.getWebViewLink();
            jsonObject.put("viewLink", viewLink);
            InputStream inputStream = GoogleDriveAPIService.service.files().get(file.getId()).executeMediaAsInputStream();
            new PdfRenderTask(PDDocument.load(inputStream), 0, 300, jsonArray, jsonObject, countDownLatch).run();
        }
        countDownLatch.await();

        return jsonArray;
    }

}
