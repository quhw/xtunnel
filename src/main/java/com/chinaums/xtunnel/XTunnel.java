package com.chinaums.xtunnel;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.FileInputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XTunnel {
	private static Logger log = LoggerFactory.getLogger(XTunnel.class);

	static private EventLoopGroup bossGroup = new NioEventLoopGroup();
	static private EventLoopGroup workerGroup = new NioEventLoopGroup(128);
	private List<TunnelConfig> configs;

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("XTunnel <config.xml>");
			System.exit(-1);
		}

		new XTunnel().start(args[0]);
	}

	public XTunnel() {
	}

	static public EventLoopGroup getWorkerGroup() {
		return workerGroup;
	}

	public void start(String configFile) throws Exception {
		configs = new ConfigXMLParser().parse(new FileInputStream(configFile));

		for (final TunnelConfig config : configs) {
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new MasterChannelInitializer(config))
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);
			int port = config.getLocal().getPort();
			log.info("Bind port: {}", port);
			server.bind(port);
		}
	}
}
