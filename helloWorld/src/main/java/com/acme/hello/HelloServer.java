package com.acme.hello;

import javax.xml.bind.SchemaOutputResolver;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class HelloServer {
    static HelloImpl impl;
    static Remote stub;
    static Registry registry;
    static final RMIClientSocketFactory CSF = Socket::new;
    static final RMIServerSocketFactory SSF = port -> {
        ServerSocket serverSocket = new ServerSocket(port);
        lastUsedPort = serverSocket.getLocalPort();
        return serverSocket;
    };
    static int lastUsedPort = -1;

    public static synchronized int start(int port) {
        try {
            impl = new HelloImpl();
            stub = UnicastRemoteObject.exportObject(impl, port, CSF, SSF);
            registry = LocateRegistry.createRegistry(lastUsedPort, CSF, SSF);
            Naming.rebind("//localhost:" + lastUsedPort + "/MessengerService", stub);
            System.out.println("Stub and registry Created and bound.");
        } catch (Exception e) {
            System.out.println("HelloServer err: " + e.getMessage());
            e.printStackTrace();
        }
        return lastUsedPort;
    }

    public static void stop() {
        try {
            UnicastRemoteObject.unexportObject(stub, true);
            System.out.println("Impl stopped");
            UnicastRemoteObject.unexportObject(registry, true);
            System.out.println("Registry stopped");
        } catch (Exception e) {
            System.out.println("HelloServer err: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
