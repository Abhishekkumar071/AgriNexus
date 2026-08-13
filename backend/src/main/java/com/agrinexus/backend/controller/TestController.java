//package com.agrinexus.backend.controller;
//
//import com.agrinexus.backend.model.TestDocument;
//import com.agrinexus.backend.repository.TestRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.MongoDatabaseFactory;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//
//@RestController
//public class TestController {
//
//    @Autowired
//    private MongoDatabaseFactory mongoDatabaseFactory;
//    private final TestRepository testRepository;
//
//    public TestController(TestRepository testRepository) {
//        this.testRepository = testRepository;
//    }
//
//    @PostMapping("/test-mongodb")
//    public TestDocument testMongoDB() {
//        System.out.println(
//                "DATABASE = " + mongoDatabaseFactory.getMongoDatabase().getName()
//        );
//
//        return testRepository.save(
//                new TestDocument(null, "MongoDB connected")
//        );
//    }
//}

package com.agrinexus.backend.controller;

import com.agrinexus.backend.model.Role;
import com.agrinexus.backend.model.User;
import com.agrinexus.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final UserRepository userRepository;

    @PostMapping("/user")
    public User createTestUser() {
        User user = User.builder()
                .name("Test Farmer")
                .email("test" + System.currentTimeMillis() + "@example.com")
                .password("dummy-not-hashed-yet")
                .role(Role.FARMER)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return userRepository.save(user);
    }

    @GetMapping("/users")
    public List<User> getAllTestUsers() {
        return userRepository.findAll();
    }
}