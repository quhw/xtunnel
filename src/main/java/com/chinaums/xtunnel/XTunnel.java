package com.chinaums.xtunnel;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.awt.GraphicsEnvironment;
import java.io.FileInputStream;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XTunnel {
	private static Logger log = LoggerFactory.getLogger(XTunnel.class);

	static private EventLoopGroup bossGroup = new NioEventLoopGroup();
	static private EventLoopGroup workerGroup = new NioEventLoopGroup(128);
	private List<TunnelConfig> configs;
	static private boolean isHeadless = GraphicsEnvironment.isHeadless();

	public static void main(String[] args) throws Exception {
		if (!isHeadless) {
			String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (Exception e) {
			}
			new SystrayUtil().showSystray();
		}

		if (args.length != 1) {
			System.out.println("XTunnel <config.xml>");
			if (!isHeadless) {
				JOptionPane.showMessageDialog(null,
						"未指定配置文件。\nXTunnel <config.xml>");
			}
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
			ChannelFuture cf = server.bind(port).await();
			if (!cf.isSuccess()) {
				log.error("Cannot bind port: {}", port);
				if (!isHeadless) {
					JOptionPane.showMessageDialog(null, "Cannot bind port: "
							+ port);
				}
				System.exit(-1);
			}
		}
	}
}
