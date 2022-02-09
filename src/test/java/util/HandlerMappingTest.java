package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HandlerMappingTest {

    HandlerMapping handlerMapping = HandlerMapping.getInstance();

    @Test
    void getController() {
        assertTrue(handlerMapping.getController("/user/create").isPresent());
        assertFalse(handlerMapping.getController("/user").isPresent());
    }
}