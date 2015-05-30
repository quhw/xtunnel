package com.chinaums.xtunnel;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MasterChannelInitializer extends ChannelInitializer<SocketChannel> {
	private TunnelConfig config;
	private SSLContext sslContext;

	public MasterChannelInitializer(TunnelConfig config) {
		this.config = config;
		if (config.getLocal().isEnableSSL()) {
			sslContext = SSLUtil.getSSLContext();
		}
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		if (config.getLocal().isEnableSSL()) {
			SSLEngine sslEngine = sslContext.createSSLEngine();
			sslEngine.setUseClientMode(false);
			sslEngine.setNeedClientAuth(true);
			ch.pipeline().addLast(new SslHandler(sslEngine));
		}
		ch.pipeline().addLast(new MasterTransferHandler(config));
	}

}
