package com.chinaums.xtunnel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigXMLParser {
	public List<TunnelConfig> parse(InputStream is) throws Exception {
		log.info("Parse config file.");
		List<TunnelConfig> list = new ArrayList<TunnelConfig>();

		SAXReader reader = new SAXReader();
		Document doc = reader.read(is);

		List<Node> tunnelNodes = doc.selectNodes("/config/tunnel");

		for (Node tunnel : tunnelNodes) {
			TunnelConfig tunnelConfig = new TunnelConfig();
			tunnelConfig.setName(tunnel.selectSingleNode("@name")
					.getStringValue());
			tunnelConfig.setLocal(parseEndpoint(tunnel
					.selectSingleNode("local")));
			tunnelConfig.setRemote(parseEndpoint(tunnel
					.selectSingleNode("remote")));
			list.add(tunnelConfig);
		}

		return list;
	}

	private EndpointConfig parseEndpoint(Node node) throws Exception {
		EndpointConfig config = new EndpointConfig();
		config.setHost(getString(node, "@host", true));
		config.setPort(Integer.parseInt(getString(node, "@port", false)));
		config.setEnableSSL(Boolean.parseBoolean(getString(node, "@ssl", true)));
		return config;
	}

	private String getString(Node node, String path, boolean ignore)
			throws Exception {
		Node n = node.selectSingleNode(path);
		if (n == null) {
			if (ignore)
				return null;
			else
				throw new Exception("No config node：" + path);
		}

		return n.getStringValue();
	}
}
