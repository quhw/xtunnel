package com.chinaums.xtunnel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SlaveChannelInitializer extends ChannelInitializer<SocketChannel> {
	private Channel masterChannel;
	private TunnelConfig config;
	private SSLContext sslContext;

	public SlaveChannelInitializer(TunnelConfig config, Channel masterChannel) {
		this.config = config;
		this.masterChannel = masterChannel;
		if (config.getRemote().isEnableSSL()) {
			sslContext = SSLUtil.getSSLContext();
		}
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		if (config.getRemote().isEnableSSL()) {
			SSLEngine sslEngine = sslContext.createSSLEngine();
			sslEngine.setUseClientMode(true);
			sslEngine.setNeedClientAuth(false);
			ch.pipeline().addLast(new SslHandler(sslEngine));
		}
		ch.pipeline().addLast(new SlaveTransferHandler(masterChannel));
	}

}
