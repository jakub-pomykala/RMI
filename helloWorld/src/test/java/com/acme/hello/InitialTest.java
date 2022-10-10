package com.acme.hello;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testify.bus.Bus;
import testify.bus.TypeSpec;
import testify.jupiter.annotation.ConfigurePartRunner;
import testify.parts.PartRunner;

import java.rmi.Naming;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static com.acme.hello.InitialTest.ServerEvent.SERVER_STARTED;

@ConfigurePartRunner
public class InitialTest {
    static int port;
    private static String lookupURL;

    @BeforeAll
    static void setup(PartRunner runner) {
        runner.useNewJVMWhenForking();
        // Create a new part called HelloServer
        runner.fork("HelloServer",
                // Tell Testify how to start this new part
                InitialTest::startServer,
                // Tell Testify how to stop this part
                minibus -> HelloServer.stop());
        // Wait for the server to start
        port = runner.bus("HelloServer").get(SERVER_STARTED);
        lookupURL = "//localhost:" + port + "/MessengerService";
        System.out.println(lookupURL);
    }

    private static void startServer(Bus bus) {
        final ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.FINEST);
        consoleHandler.setFormatter(new SimpleFormatter());
        final Logger serial = Logger.getLogger("java.io.serialization");
        serial.setLevel(Level.FINEST);
        serial.addHandler(consoleHandler);
        int port = HelloServer.start(0);
        // Notify the test process that the server has now started on a particular port
        bus.put(SERVER_STARTED, port);
        System.out.println(port);
    }

    enum ServerEvent implements TypeSpec<Integer> { SERVER_STARTED }

    @Test
    void testHello() throws Exception {
        Hello obj = (Hello) Naming.lookup(lookupURL);         //objectname in registry
        System.out.println(obj.sayHello());
    }

    @Test
    void testSetGreeting() throws Exception {
        Hello hello = (Hello) Naming.lookup(lookupURL);
        hello.setGreeting("Good day!");
        Assertions.assertEquals("Good day!", hello.sayHello());
    }

}
