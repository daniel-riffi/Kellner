package com.example.kellner;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import at.orderlibrary.*;

public class Server {

private String ipAddress;
private int port;
private ReentrantLock lock;

private Socket socket;
private ObjectInputStream inputStream;
private ObjectOutputStream outputStream;

    public Server(String ipAddress,  int port){
        this.ipAddress=ipAddress;
        this.port=port;
        lock=new ReentrantLock();
    }

    public void sendOrderToServer(Order order){
        new Thread((()->{
            try {
                outputStream.writeObject(order);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        })).start();
    }

    public void readOffersFromServer(Consumer<List<Offer>> callback){
        new Thread(()->{
            try {
                ArrayList<Offer> offers=(ArrayList<Offer>) inputStream.readObject();
                callback.accept(offers);

            } catch (IOException e) {
                e.printStackTrace();
            }catch (ClassNotFoundException e){
                readOffersFromServer(callback);
            }


        }).start();
    }

    public boolean connect(){
        try {
            Thread t=new Thread(() -> {
                try {
                    lock.lock();
                    socket=new Socket(ipAddress,port);
                    System.out.println("");
                    outputStream = new ObjectOutputStream(socket.getOutputStream());
                    inputStream = new ObjectInputStream(socket.getInputStream());
                    lock.unlock();
                    System.out.println();
                } catch (IOException e) {
                    e.printStackTrace();
                    lock.unlock();
                }

            });
            t.start();
            Thread.sleep(100);
            lock.lock();
            if(socket!=null&&inputStream!=null&&outputStream!=null) {
                lock.unlock();
                return true;
            }
            lock.unlock();

        } catch (Exception e) {
            e.printStackTrace();
            lock.unlock();
        }

        return false;
    }
}
