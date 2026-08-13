package com.agrinexus.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "test")
public class TestDocument {

    @Id
    private String id;

    private String message;

    public TestDocument() {
    }

    public TestDocument(String id, String message) {
        this.id = id;
        this.message = message;
    }

    // getters and setters
}
