# XTunnel

XTunnel是一个TCP隧道工具，通过部署中间节点使两个无法直接联通的节点可以正常通讯，类似SSH的TUNNEL功能。

## 特性
* 支持SSL加密功能，可以在两个XTunnel之间采用SSL连接，突破防火墙的报文内容侦测。
* 同时支持多组转发配置，采用Netty异步框架，有很高的性能。

## XTunnel + OpenVPN
![openvpn](https://github.com/quhw/xtunnel/raw/master/openvpn.png)

## 配置文件

```xml
<?xml version="1.0" encoding="utf-8"?>
<config>
	<tunnel name="http://mvnrepository.com/">
		<local port="222" ssl="true" />
		<remote host="mvnrepository.com" port="80" />
	</tunnel>
	<tunnel name="http://mvnrepository.com/">
		<local port="333" />
		<remote host="127.0.0.1" port="222" ssl="true" />
	</tunnel>
</config>
```
