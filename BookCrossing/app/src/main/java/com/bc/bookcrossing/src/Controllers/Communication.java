package com.bc.bookcrossing.src.Controllers;

import java.net.ConnectException;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

/**
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

@ChannelHandler.Sharable
class ClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Processing.getInstance().processAnswer(msg);
        Communication.getInstance().group.shutdownGracefully();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private static final StringDecoder DECODER = new StringDecoder();
    private static final StringEncoder ENCODER = new StringEncoder();

    private static final ClientHandler CLIENT_HANDLER = new ClientHandler();

    private final SslContext sslCtx;

    public ClientInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc(), Communication.HOST, Communication.PORT));
        }

        // Add the text line codec combination first,
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(DECODER);
        pipeline.addLast(ENCODER);

        // and then business logic.
        pipeline.addLast(CLIENT_HANDLER);
    }
}

public class Communication implements SendRequests  {
    public static final Communication singletonCommunication = new Communication();
    private static final String port = "5000";
    private static final String ip = "35.180.103.132";
    private static boolean sendResult = true;

    private static final boolean SSL = System.getProperty("ssl") != null;
    public static final String HOST = System.getProperty("host", ip);
    public static final int PORT = Integer.parseInt(System.getProperty("port", port));

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
                    // TODO Auto-generated catch block
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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return false;
                }
            }
        } finally {
            //group.shutdownGracefully();
            return sendResult;
        }
    }
}
