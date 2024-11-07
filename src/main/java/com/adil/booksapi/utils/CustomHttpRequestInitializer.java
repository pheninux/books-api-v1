package com.adil.booksapi.utils;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.drive.Drive;

import java.io.IOException;

public class CustomHttpRequestInitializer implements HttpRequestInitializer {
    private final HttpRequestInitializer requestInitializer;

    public CustomHttpRequestInitializer(HttpRequestInitializer requestInitializer) {
        this.requestInitializer = requestInitializer;
    }

    @Override
    public void initialize(HttpRequest httpRequest) throws IOException {
        requestInitializer.initialize(httpRequest);
        httpRequest.setConnectTimeout(3 * 60000); // 3 minutes connect timeout
        httpRequest.setReadTimeout(3 * 60000); // 3 minutes read timeout
    }

    public static Drive getDriveService(HttpTransport httpTransport, JsonFactory jsonFactory, HttpRequestInitializer requestInitializer) throws IOException {
        return new Drive.Builder(httpTransport, jsonFactory, new CustomHttpRequestInitializer(requestInitializer))
                .setApplicationName("PDFManager2")
                .build();
    }
}
