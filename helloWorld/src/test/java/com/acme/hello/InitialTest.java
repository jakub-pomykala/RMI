package com.acme.hello;

import com.acme.hello.Hello;
import com.acme.hello.HelloImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testify.jupiter.annotation.ConfigurePartRunner;
import testify.parts.PartRunner;

import java.rmi.Naming;

@ConfigurePartRunner
public class InitialTest {

    @BeforeAll
    static void setup(PartRunner runner) {

        runner.useNewJVMWhenForking();
        runner.forkMain(HelloImpl.class);
    }

    @Test
    void testPrint() throws Exception {

            Hello obj = (Hello) Naming.lookup( "//" +
                    "localhost:1099" +
                    "/HelloServer");         //objectname in registry
            System.out.println(obj.sayHello());
    }
}
