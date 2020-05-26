package com.example.kellner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import javax.security.auth.callback.Callback;

import at.orderlibrary.*;

public class Server {
    private static Server server;
    private String ipAddress;
    private int port;
    private ReentrantLock lock;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private ArrayList<Consumer> callbacks;

    private Server(){
        lock=new ReentrantLock();
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void sendOrderToServer(Order order){
        new Thread((()->{
            String json=new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create()
                    .toJson(order);
            writer.print(json+"\r\n");
            writer.flush();
        })).start();
    }

    public void readOffersFromServer(Consumer<List<Offer>> callback){
        new Thread(()->{
            try {
                String json=reader.readLine();
                TypeToken<List<Offer>> token=new TypeToken<List<Offer>>(){};
                ArrayList<Offer> offers=new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create()
                        .fromJson(json,token.getType());
                callback.accept(offers);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public boolean connect(){
        try {
            Thread t=new Thread(() -> {
                try {
                    lock.lock();
                    socket=new Socket();
                    socket.connect(new InetSocketAddress(ipAddress,port),5*1000);
                    reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));


                    TypeRequest r=new TypeRequest();
                    r.type= Type.WAITER;

                    Gson gson=new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create();
                    String json=gson.toJson(r);
                    writer.print(json+"\n");
                    writer.flush();

                    lock.unlock();
                } catch (IOException e) {
                    e.printStackTrace();
                    lock.unlock();
                }

            });
            t.start();
            Thread.sleep(100);
            lock.lock();
            if(socket!=null&&writer!=null&&reader!=null) {
                lock.unlock();
                for (Consumer callback:callbacks) {
                    callback.accept(null);
                }
                return true;
            }
            lock.unlock();

        } catch (Exception e) {
            e.printStackTrace();
            lock.unlock();
        }

        return false;
    }
    public void close() {
        try {
            socket.close();
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket=null;
        writer=null;
        reader=null;

    }
    public boolean isOpen(){
        return reader!=null&&writer!=null;
    }
    public synchronized static Server getInstance(){
        if(server==null){
            server=new Server();
        }
        return server;
    }
    public synchronized void onOpen(Consumer c){
        if(!isOpen()){
            if(callbacks==null){
                callbacks=new ArrayList<>();
            }
            callbacks.add(c);
        }else{
            c.accept(null);
        }

    }
}














