package com.adil.booksapi.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class GoogleBookAPIService {

    private static final String API_KEY = "AIzaSyAa0zHl20vrHr4v9x2FWYxWXAwORQODz_g"; // Remplace avec ta clé API

    public JSONObject getBookDescription(String bookTitle) {
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + bookTitle + "&maxResults=1&key=" + API_KEY;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Traiter la réponse JSON
                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.getJSONArray("items").length() > 0) {
                    JSONObject volumeInfo = jsonResponse.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo");

                    JSONObject bookJson = new JSONObject();
                    bookJson.put("title", volumeInfo.optString("title"));
                    bookJson.put("author", volumeInfo.optJSONArray("authors") != null ? volumeInfo.optJSONArray("authors").join(", ") : "");
                    bookJson.put("isbn", volumeInfo.optJSONArray("industryIdentifiers") != null ? volumeInfo.optJSONArray("industryIdentifiers").getJSONObject(0).optString("identifier") : "");
                    bookJson.put("category", volumeInfo.optJSONArray("categories") != null ? volumeInfo.optJSONArray("categories").join(", ") : "");
                    bookJson.put("language", volumeInfo.optString("language"));
                    bookJson.put("publicationDate", volumeInfo.optString("publishedDate"));
                    bookJson.put("publisher", volumeInfo.optString("publisher"));
                    bookJson.put("description", volumeInfo.optString("description"));
                    bookJson.put("thumbnail", volumeInfo.optJSONObject("imageLinks") != null ? volumeInfo.optJSONObject("imageLinks").optString("thumbnail") : "");
                    bookJson.put("pageCount", volumeInfo.optInt("pageCount"));

                    return bookJson;
                } else {
                    return new JSONObject().put("error", "No description available.");
                }
            } else {
                return new JSONObject().put("error", "Error: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().put("error", "Error occurred while fetching description.");
        }
    }
}
