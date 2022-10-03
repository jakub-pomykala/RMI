package com.acme.hello;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
public class HelloImpl extends UnicastRemoteObject implements Hello
{
    public HelloImpl() throws RemoteException {}

    public String sayHello() { return "Hello world!"; }

    public static void main(String args[])
    {
        try
        {
            LocateRegistry.createRegistry(1099);
            HelloImpl obj = new HelloImpl();
            // Bind this object instance to the name "HelloServer"
            Naming.rebind("HelloServer", obj);
        }
        catch (Exception e)
        {
            System.out.println("HelloImpl err: " + e.getMessage());
            e.printStackTrace();
        }
    }
}