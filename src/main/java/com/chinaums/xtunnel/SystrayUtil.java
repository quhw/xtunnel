package com.chinaums.xtunnel;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SystrayUtil {
	private TrayIcon trayIcon = null;

	public void showSystray() {
		// 判断系统是否支持系统托盘
		if (!SystemTray.isSupported())
			return;

		SystemTray tray = SystemTray.getSystemTray(); // 创建系统托盘
		Image image = Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("/icon.png"));// 载入图片,这里要写你的图标路径哦

		// 创建弹出菜单
		PopupMenu popup = new PopupMenu();

		// 退出程序选项
		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		popup.add(exitItem);

		trayIcon = new TrayIcon(image, "XTunnel", popup);// 创建trayIcon
		trayIcon.setImageAutoSize(true);
		try {
			tray.add(trayIcon);
		} catch (AWTException e1) {
			log.error("", e1);
		}
	}
}
