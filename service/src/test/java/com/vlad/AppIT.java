package com.vlad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppIT {

    @Test
    void testAppIntegration() {
        App app = new App();
        String greeting = app.getGreeting();

        assertNotNull(greeting, "Greeting should not be null");
        assertTrue(greeting.contains("World"), "Greeting should contain 'World'");
    }
}

