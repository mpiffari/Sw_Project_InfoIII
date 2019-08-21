

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import user.User;

/**
 * Simplistic telnet client.
 */
public final class Client {

	static final String port = "5000";
	static final boolean SSL = System.getProperty("ssl") != null;
	static final String HOST = System.getProperty("host", "35.180.103.132");
	//static final String HOST = System.getProperty("host", "127.0.0.1");
	static final int PORT = Integer.parseInt(System.getProperty("port", port));

	public static void main(String[] args)  {
		// Configure SSL.
		SslContext sslCtx = null;
		if (SSL) {
			try {
				sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
			} catch (SSLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			sslCtx = null;
		}

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(new ClientInitializer(sslCtx));

			// Start the connection attempt.
			Channel ch = null;
			
			while(ch  == null || !ch.isOpen()) {
				try {
					ch = b.connect(HOST, PORT).sync().channel();
				} catch (InterruptedException e) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

			// Read commands from the stdin.
			ChannelFuture lastWriteFuture = null;
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			for (;;) {
				String line = null;
				try {
					line = in.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (line == null) {
					break;
				}

				// Sends the received line to the server.
				Book obj = new Book("Piccolo Principe", "Antoine de Saint-Exupèry", 1854, 1, BookType.ACTION);
				
				User user = new User("Pippo", "mario", "rossi", "1996-08-13", "aaaaaaaa", 10.5, 10.5, 5);
				
				lastWriteFuture = ch.writeAndFlush("Pippo;requestType:2;" + user.toString() + "\r\n");
				//System.out.print(obj.toString());
				// If user typed the 'bye' command, wait until the server closes
				// the connection.
				if ("bye".equals(line.toLowerCase())) {
					try {
						ch.closeFuture().sync();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}

			// Wait until all messages are flushed before closing the channel.
			if (lastWriteFuture != null) {
				try {
					lastWriteFuture.sync();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} finally {
			group.shutdownGracefully();
		}
	}
}
