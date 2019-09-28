package com.bc.bookcrossing.src.requestManager.communication;

import com.bc.bookcrossing.src.requestManager.SendRequest;

import java.net.ConnectException;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

/**
 *
 * Classe che si occupa di andare a gestire la connessione e la comunicazione verso il server
 * a basso livello.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public class Communication implements SendRequest {
    public static final Communication singletonCommunication = new Communication();
    //private static final String port = "5000";
    private static final String IP = "13.59.242.64";
    private static boolean sendResult = true;

    private static final boolean SSL = System.getProperty("ssl") != null;
    public static final String HOST = System.getProperty("host", IP);
    //public static final int PORT = Integer.parseInt(System.getProperty("port", port));
    public static final int PORT = 5000;
    public EventLoopGroup group;

    private Communication() { }

    public static Communication getInstance(){
        return singletonCommunication;
    }

    @Override
    public boolean send(String data) {
        // Configure SSL.
        SslContext sslCtx = null;
        if (SSL) {
            try {
                sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            } catch (SSLException e) {
                e.printStackTrace();
            }
        } else {
            sslCtx = null;
        }

        group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).handler(new ClientInitializer(sslCtx));
            // Start the connection attempt.
            ChannelFuture ch = null;

            try {
                ch = b.connect(HOST, PORT);
                ch.addListener(f -> {
                    if (!f.isSuccess() && f.cause() instanceof ConnectException) {
                        System.out.println("[Info] The server is offline.");
                        sendResult = false;
                    } else {
                        System.out.println("[Info] The server is online.");
                        sendResult = true;
                    }
                });

                while(!ch.isDone()) {
                    System.out.println("Wait");
                }

                if(sendResult == false) {
                    return false;
                } else {
                    ch.await();
                }
            } catch (InterruptedException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    return false;
                }
            }

            ChannelFuture lastWriteFuture = null;
            lastWriteFuture = ch.channel().writeAndFlush(data + "\r\n");

            // Wait until all messages are flushed before closing the channel.
            if (lastWriteFuture != null) {
                try {
                    lastWriteFuture.sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } finally {
            return sendResult;
        }
    }
}
