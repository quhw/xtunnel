package com.chinaums.xtunnel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TunnelConfig {
	private String name;
	private EndpointConfig local;
	private EndpointConfig remote;
}
