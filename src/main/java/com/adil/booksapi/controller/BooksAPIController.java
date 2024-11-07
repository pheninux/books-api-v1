package com.adil.booksapi.controller;

import com.adil.booksapi.model.Book;
import com.adil.booksapi.service.GoogleDriveAPIService;
import com.adil.booksapi.service.ItextService;
import com.google.api.services.drive.model.File;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BooksAPIController {


    @Autowired
    private GoogleDriveAPIService googleDriveAPIService;

    @Autowired
    private ItextService itextService;

    @RequestMapping(value = "/google/api/books", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Book>> getBooksFromGoogleAPI() {
        List<Book> books = new ArrayList<>();
        JSONArray jsonArray = googleDriveAPIService.getBookFromGoogleApiByISBN();
        bookMapperForV2(books, jsonArray);
        return ResponseEntity.ok(books);
    }

    @RequestMapping(value = "/google/drive/books", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Book>> getBooksFromDRIVE() throws GeneralSecurityException, IOException {
        List<Book> books = new ArrayList<>();
        List<File> files = googleDriveAPIService.getBooksFromDrive();
        return ResponseEntity.ok(books);
    }


    @RequestMapping(value = "/v1/books", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Book>> getBooks() throws GeneralSecurityException, IOException, InterruptedException {
        List<Book> books = new ArrayList<>();
        JSONArray jsonArray = itextService.getBookByParsingPdf();
        bookMapperForV1(books, jsonArray);
        return ResponseEntity.ok(books);
    }

    private void bookMapperForV1(List<Book> books, JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Book book = new Book();
            book.setTitle(jsonObject.getString("title"));
            book.setPreviewLink(jsonObject.getString("viewLink"));
            book.setBase64Image(jsonObject.getString("imageBase64"));


            books.add(book);
        }
    }

    private void bookMapperForV2(List<Book> books, JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Book book = new Book();
            book.setTitle(jsonObject.getString("title"));
            book.setAuthor(jsonObject.getString("author"));
            book.setDescription(jsonObject.getString("description"));
            book.setPreviewLink(jsonObject.getString("viewLink"));
            book.setThumbnail(jsonObject.getString("thumbnail"));
            book.setPageCount(jsonObject.getString("pageCount"));
            book.setIsbn(jsonObject.getString("isbn"));
            book.setLanguage(jsonObject.getString("language"));
            book.setPublicationDate(jsonObject.getString("publicationDate"));
            book.setPublisher(jsonObject.getString("publisher"));
            book.setCategory(jsonObject.getString("category"));

            books.add(book);
        }
    }

}
