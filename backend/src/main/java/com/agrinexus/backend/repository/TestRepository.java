package com.agrinexus.backend.repository;

import com.agrinexus.backend.model.TestDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestRepository extends MongoRepository<TestDocument, String> {
}