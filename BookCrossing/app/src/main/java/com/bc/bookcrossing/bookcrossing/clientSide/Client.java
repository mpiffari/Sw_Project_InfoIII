package com.bc.bookcrossing.bookcrossing.clientSide;

import android.util.Log;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public final class Client {

	static final String port = "5000";
	static final boolean SSL = System.getProperty("ssl") != null;
	static final String HOST = System.getProperty("host", "35.180.103.132");
	static final int PORT = Integer.parseInt(System.getProperty("port", port));

	public static void main(String[] args) throws Exception {}

	public static void send(String newBook) throws Exception {
		// Configure SSL.
		final SslContext sslCtx;
		if (SSL) {
			sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		} else {
			sslCtx = null;
		}

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(new ClientInitializer(sslCtx));

			// Start the connection attempt.
			Channel ch = b.connect(HOST, PORT).sync().channel();

			ChannelFuture lastWriteFuture = null;
			lastWriteFuture = ch.writeAndFlush(newBook + "\r\n");
			Log.d("!!! Book inserted: !!!", newBook);

			// Wait until all messages are flushed before closing the channel.
			if (lastWriteFuture != null) {
				lastWriteFuture.sync();
			}
		} finally {
			group.shutdownGracefully();
		}

	}
}
