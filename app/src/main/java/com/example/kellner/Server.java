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

import at.orderlibrary.*;

public class Server {

private String ipAddress;
private int port;
private ReentrantLock lock;

private Socket socket;
private BufferedReader reader;
private PrintWriter writer;

    public Server(String ipAddress,  int port){
        this.ipAddress=ipAddress;
        this.port=port;
        lock=new ReentrantLock();
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

    }
}
