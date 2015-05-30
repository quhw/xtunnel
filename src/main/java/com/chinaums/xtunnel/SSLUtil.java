package com.chinaums.xtunnel;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SSLUtil {
	public static SSLContext getSSLContext() {
		try {
			KeyStore keyStore = KeyStore.getInstance("jks");
			InputStream in = SSLUtil.class.getResourceAsStream("/xtunnel.jks");
			keyStore.load(in, "123456".toCharArray());
			in.close();

			// 初始化key manager factory
			KeyManagerFactory kmf = KeyManagerFactory
					.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(keyStore, "123456".toCharArray());

			TrustManagerFactory tmf = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(keyStore);

			// 初始化ssl context
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(kmf.getKeyManagers(), tmf.getTrustManagers(),
					new SecureRandom());

			return context;
		} catch (Exception e) {
			log.error("", e);
			return null;
		}

	}
}
