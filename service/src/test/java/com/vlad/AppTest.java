package com.vlad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void testApp() {
        assertTrue(true, "This test should always pass");
    }

    @Test
    void testGreeting() {
        App app = new App();
        String greeting = app.getGreeting();
        assertEquals("Hello World!", greeting, "Greeting should be 'Hello World!'");
    }

    @Test
    void testGreetingStartsWithHello() {
        App app = new App();
        String greeting = app.getGreeting();
        assertTrue(greeting.startsWith("Hello"), "Greeting should start with 'Hello'");
    }
}
