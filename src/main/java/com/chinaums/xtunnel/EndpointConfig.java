package com.chinaums.xtunnel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EndpointConfig {
	private String host;
	private int port;
	private boolean enableSSL = false;
}
