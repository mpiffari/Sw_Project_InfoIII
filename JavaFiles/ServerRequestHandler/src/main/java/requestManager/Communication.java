package requestManager;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetAddress;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.SSLException;

import dataManager.DBConnector;

class ServerInitializer extends ChannelInitializer<SocketChannel> {

	private static final StringDecoder DECODER = new StringDecoder();
	private static final StringEncoder ENCODER = new StringEncoder();

	private static final ServerHandler SERVER_HANDLER = new ServerHandler();

	private final SslContext sslCtx;

	public ServerInitializer(SslContext sslCtx) {
		this.sslCtx = sslCtx;
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		if (sslCtx != null) {
			pipeline.addLast(sslCtx.newHandler(ch.alloc()));
		}

		// Add the text line codec combination first,
		pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		// the encoder and decoder are static as these are sharable
		pipeline.addLast(DECODER);
		pipeline.addLast(ENCODER);

		// and then business logic.
		pipeline.addLast(SERVER_HANDLER);
	}
}

@Sharable
class ServerHandler extends SimpleChannelInboundHandler<String> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// Send greeting for a new connection.
		ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
		ctx.write("It is " + new Date() + " now.\r\n");
		ctx.flush();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
		// Communication.chc = ctx;

		int j = request.indexOf(";");
		String username = request.substring(0, j);
		Communication.chcMap.put(username, ctx);

		ComputeRequest computeRequest = new ComputeRequest();
		computeRequest.process(request.substring(j + 1), username);

		// Generate and write a response.
		/*
		 * String response = request + "\r\n"; String perPrint = ""; boolean close =
		 * false;
		 * 
		 * Book bookReceived = new Book(response);
		 * 
		 * perPrint = "Title: " + bookReceived.getTitle() + " Author: " +
		 * bookReceived.getAuthor() + " Year: " + bookReceived.getYearOfPubblication() +
		 * " Edition: " + bookReceived.getEditionNumber() + " Type: " +
		 * bookReceived.getType() + "\r\n";
		 * 
		 * System.out.print(perPrint); ChannelFuture future = ctx.write(perPrint);
		 * 
		 * // Close the connection after sending 'Have a good day!' // if the client has
		 * sent 'bye'. if (close) { future.addListener(ChannelFutureListener.CLOSE); }
		 */
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}

public final class Communication implements SendAnswer {

	static final String portValue = "5000";
	static final boolean SSL = System.getProperty("ssl") != null;
	static final int PORT = Integer.parseInt(System.getProperty("port", portValue));
	// static ChannelHandlerContext chc;

	static HashMap<String, ChannelHandlerContext> chcMap = new HashMap<String, ChannelHandlerContext>();

	private static Communication instance;

	public static Communication getInstance() {
		if (instance == null) {
			try {
				instance = new Communication();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return instance;
		}
		return instance;

	}

	private Communication() throws Exception {

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
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SSLException e) {
						// TODO Auto-generated catch block
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

					// ch = b.bind(PORT).sync().channel();
					// Thread.sleep(20000);

					// ch.writeAndFlush("hahaha" + "\r\n");

					// this.send("Ciao");

					b.bind(PORT).sync().channel().closeFuture().sync();

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					bossGroup.shutdownGracefully();
					workerGroup.shutdownGracefully();
				}

			}
		});
		t.start();

	}

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("unused")
		Communication c = new Communication();
		// Thread.sleep(30000);
		// c.send("Ciao");
		// c.ch.closeFuture().sync();
	}

	public void send(final String username, final String msg) {
		System.out.println("invio: " + msg);

		ChannelFuture oo = chcMap.get(username).writeAndFlush(msg + "\r\n");
		oo.addListener(new ChannelFutureListener() {

			public void operationComplete(ChannelFuture future) throws Exception {
				while (!future.isSuccess()) {
					chcMap.get(username).writeAndFlush(msg + "\r\n");
					System.out.println("FAIL!");
				}
				future.addListener(ChannelFutureListener.CLOSE);
				// rimuvo il ChannelHandlerContext
				chcMap.remove(username);
			}
		});

	}
}
