package requestManager.communication;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import java.security.cert.CertificateException;
import java.util.HashMap;

import javax.net.ssl.SSLException;


/**
 * 
 * Classe contenente i metodi necessari per gestire la comunicazione lato server
 * basandosi sul framework Netty
 * 
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 *
 */


public final class Communication implements SendAnswer {

	static final String PORTVALUE = "5000";
	static final boolean SSL = System.getProperty("ssl") != null;
	static final int PORT = Integer.parseInt(System.getProperty("port", PORTVALUE));
	// HashMap of Users and the associated channel
	static HashMap<String, ChannelHandlerContext> chcMap = new HashMap<String, ChannelHandlerContext>();

	private static Communication instance;

	public static Communication getInstance() {
		if (instance == null)
			instance = new Communication();
		
		return instance;
	}

	private Communication() {

		Thread t = new Thread(new Runnable() {

			public void run() {
				// Configure SSL.
				SslContext sslCtx = null;
				if (SSL) {
					SelfSignedCertificate ssc;
					try {
						ssc = new SelfSignedCertificate();
						sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
					} catch (CertificateException e) {
						e.printStackTrace();
					} catch (SSLException e) {
						e.printStackTrace();
					}
				} else {
					sslCtx = null;
				}

				EventLoopGroup bossGroup = new NioEventLoopGroup(1);
				EventLoopGroup workerGroup = new NioEventLoopGroup();
				try {
					ServerBootstrap b = new ServerBootstrap();
					b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ServerInitializer(sslCtx));
					b.bind(PORT).sync().channel().closeFuture().sync();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					bossGroup.shutdownGracefully();
					workerGroup.shutdownGracefully();
				}

			}
		});
		t.start();
	}

	
	public void send(final String username, final String msg) {
		System.out.println("Risposta da server verso client:\r\n" + msg);

		//sending the message to the correct ChannelHandlerContext
		ChannelFuture oo = chcMap.get(username).writeAndFlush(msg + "\r\n");
		//adding a listener to check if the message is sent correctly
		oo.addListener(new ChannelFutureListener() {

			public void operationComplete(ChannelFuture future) throws Exception {
				int count = 0;
				boolean isError = false;
				
				//if there was an error, retry sending the message up to 3 times
				while(!future.isSuccess()) {
					chcMap.get(username).writeAndFlush(msg + "\r\n");
					System.out.println("Retry send response!");
					if(count == 3) {
						isError = true;
						break;
					} else {
						count ++;
					}
				}

				if(isError == true) {
					//chcMap.get(username).writeAndFlush("Error" + "\r\n");
					System.out.println("Error!");
				} else {
					//chcMap.get(username).writeAndFlush("All ok" + "\r\n");
					System.out.println("All ok!");	
				}

				future.addListener(ChannelFutureListener.CLOSE);
				// rimuovo il ChannelHandlerContext
				chcMap.remove(username);
			}
		});
	}
	
	
	

}
