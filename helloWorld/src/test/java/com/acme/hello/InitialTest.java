package com.acme.hello;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testify.bus.Bus;
import testify.bus.TypeSpec;
import testify.jupiter.annotation.ConfigurePartRunner;
import testify.parts.PartRunner;

import java.rmi.Naming;

import static com.acme.hello.InitialTest.ServerEvent.SERVER_STARTED;

@ConfigurePartRunner
public class InitialTest {
    static int port;

    @BeforeAll
    static void setup(PartRunner runner, Bus bus) {

        runner.useNewJVMWhenForking();
        // Create a new part called HelloServer
        runner.fork("HelloServer",
                // Tell Testify how to start this new part
                InitialTest::startServer,
                // Tell Testify how to stop this part
                minibus -> HelloServer.stop());
        // Wait for the server to start
        port = bus.get(SERVER_STARTED);
    }

    private static void startServer(Bus bus) {
        int port = HelloServer.start(0);
        // Notify the test process that the server has now started on a particular port
        bus.put(SERVER_STARTED, port);
    }

    enum ServerEvent implements TypeSpec<Integer> { SERVER_STARTED }

    @Test
    void testPrint() throws Exception {

            Hello obj = (Hello) Naming.lookup( "//" +
                    "localhost:" + port +
                    "/MessengerService");         //objectname in registry
            System.out.println(obj.sayHello());
            System.out.println(port);

    }
}
