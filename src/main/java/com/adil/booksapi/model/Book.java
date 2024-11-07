package com.adil.booksapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {

    String title;
    String author;
    String isbn;
    String category;
    String language;
    String publicationDate;
    String publisher;
    String description;
    String thumbnail;
    String previewLink;
    String pageCount;
    String base64Image;



}
