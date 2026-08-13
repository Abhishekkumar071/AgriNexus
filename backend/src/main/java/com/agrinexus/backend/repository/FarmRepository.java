package com.agrinexus.backend.repository;

import com.agrinexus.backend.model.Farm;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FarmRepository extends MongoRepository<Farm, String> {

    List<Farm> findByOwnerId(String ownerId);
}