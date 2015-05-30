package com.chinaums.xtunnel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MasterTransferHandler extends SlaveTransferHandler {
	private TunnelConfig config;

	public MasterTransferHandler(TunnelConfig config) {
		this.config = config;
	}

	public MasterTransferHandler() {
	}

	public TunnelConfig getConfig() {
		return config;
	}

	public void setConfig(TunnelConfig config) {
		this.config = config;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("Connection accepted at port {} : {}", config.getLocal()
				.getPort(), ctx.channel().remoteAddress());
		log.info("Connecting to {}:{}", config.getRemote().getHost(), config
				.getRemote().getPort());

		Bootstrap b = new Bootstrap();
		b.group(XTunnel.getWorkerGroup());
		b.channel(NioSocketChannel.class);
		b.option(ChannelOption.SO_KEEPALIVE, true);
		b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
		b.handler(new SlaveChannelInitializer(config, ctx.channel()));

		// 注意，这里是同步等待，如果网络连接时间过长，会卡住所有线程。
		ChannelFuture f = b.connect(config.getRemote().getHost(),
				config.getRemote().getPort()).await();
		if (f.isSuccess()) {
			setRelatedChannel(f.channel());
		} else {
			log.info("Cannot connect to {}", config.getRemote().getHost(),
					config.getRemote().getPort());
			ctx.channel().close();
		}
	}
}
