package serverSide;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.util.Date;

/**
 * Handles a server-side channel.
 */
@Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// Send greeting for a new connection.
		ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
		ctx.write("It is " + new Date() + " now.\r\n");
		ctx.flush();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
		// Generate and write a response.
		String response = request + "\r\n";
		String perPrint = "";
		boolean close = false;

		Book bookReceived = new Book(response);

		perPrint = "Title: " + bookReceived.getTitle() + 
					" Author: " + bookReceived.getAuthor() +
					" Year: " + bookReceived.getYearOfPubblication() + 
					" Edition: " + bookReceived.getEditionNumber() + 
					" Type: " + bookReceived.getType() + "\r\n";
		
		System.out.print(perPrint);
		ChannelFuture future = ctx.write(perPrint);

		// Close the connection after sending 'Have a good day!'
		// if the client has sent 'bye'.
		if (close) {
			future.addListener(ChannelFutureListener.CLOSE);
		}
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
