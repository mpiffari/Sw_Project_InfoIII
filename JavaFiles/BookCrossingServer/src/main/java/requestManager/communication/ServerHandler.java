package requestManager.communication;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import requestManager.ComputeRequest;


class ServerHandler extends SimpleChannelInboundHandler<String> {

	ComputeRequest computeRequest;
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// Send greeting for a new connection.
		computeRequest = ComputeRequest.COMPUTE_REQUEST_SINGLETON;
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