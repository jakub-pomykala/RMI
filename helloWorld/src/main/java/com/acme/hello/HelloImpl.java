package com.acme.hello;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;



public class HelloImpl implements Hello
{
    public HelloImpl() throws RemoteException {
    }

    public String sayHello() { return "Hello world!"; }

}