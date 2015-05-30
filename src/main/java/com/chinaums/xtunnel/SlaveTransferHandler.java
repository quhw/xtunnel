package com.chinaums.xtunnel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SlaveTransferHandler extends ChannelInboundHandlerAdapter {
	private Channel relatedChannel;

	public SlaveTransferHandler(Channel relatedChannel) {
		this.relatedChannel = relatedChannel;
	}

	public SlaveTransferHandler() {
	}

	public Channel getRelatedChannel() {
		return relatedChannel;
	}

	public void setRelatedChannel(Channel relatedChannel) {
		this.relatedChannel = relatedChannel;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("Connection established: {}", ctx.channel().remoteAddress());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("Connection closed: {}", ctx.channel().remoteAddress());
		if (relatedChannel != null)
			relatedChannel.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (relatedChannel != null && relatedChannel.isWritable()) {
			relatedChannel.writeAndFlush(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		log.info("Exception occurred: {} : {}", cause.getMessage(), ctx
				.channel().remoteAddress());
		ctx.channel().close();
		if (relatedChannel != null)
			relatedChannel.close();
	}

}
