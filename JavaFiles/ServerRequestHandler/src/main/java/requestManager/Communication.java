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

import java.security.cert.CertificateException;
import java.util.HashMap;

import javax.net.ssl.SSLException;


/**
 * 
 * Classe contenente i metodi necessari per gestire la comunicazione verso server
 * basandosi sul framework Netty
 * 
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 *
 */
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
		// The encoder and decoder are static as these are sharable
		pipeline.addLast(DECODER);
		pipeline.addLast(ENCODER);
		// and then business logic.
		pipeline.addLast(SERVER_HANDLER);
	}
}

@Sharable
class ServerHandler extends SimpleChannelInboundHandler<String> {

	ComputeRequest computeRequest;
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// Send greeting for a new connection.
		computeRequest = ComputeRequest.computeRequestSingleton;
		System.out.println("WELCOME TO A NEW CONNECTION");
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
		int j = request.indexOf(";");
		String username = request.substring(0, j);
		Communication.chcMap.put(username, ctx);

		System.out.println("Process request: " + request);
		computeRequest.process(request.substring(j + 1), username);
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
	// HashMap of Users and the associated channel
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

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("unused")
		Communication c = new Communication();
	}

	public void send(final String username, final String msg) {
		System.out.println("Risposta da server verso client:\r\n" + msg);

		ChannelFuture oo = chcMap.get(username).writeAndFlush(msg + "\r\n");
		oo.addListener(new ChannelFutureListener() {

			public void operationComplete(ChannelFuture future) throws Exception {
				int count = 0;
				boolean isError = false;

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
				// rimuvo il ChannelHandlerContext
				chcMap.remove(username);
			}
		});
	}
}
