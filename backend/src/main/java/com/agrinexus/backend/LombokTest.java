package com.agrinexus.backend;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class LombokTest {
    private String message;

    public void test() {
        this.setMessage("Lombok is working!");
        log.info("Test message: {}", this.getMessage());
    }
}