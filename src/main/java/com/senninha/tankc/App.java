package com.senninha.tankc;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.senninha.tankc.ui.GameFrame;
import com.senninha.tankc.ui.util.DrawUtil;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		App app = new App();
		app.startUI();
		System.out.println("start ui");
	}
	
	private void startUI() {
		GameFrame gameFrame = new GameFrame();
		gameFrame.setSize(800, 700);
		gameFrame.setTitle("MyTankGame");
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setResizable(false);
		gameFrame.setVisible(true);

		// 显示器屏幕大小
		Dimension screenSizeInfo = Toolkit.getDefaultToolkit().getScreenSize();
		int leftTopX = ((int) screenSizeInfo.getWidth() - gameFrame.getWidth()) / 2;
		int leftTopY = ((int) screenSizeInfo.getHeight() - gameFrame
				.getHeight()) / 2;

		// 设置显示的位置在屏幕中间
		gameFrame.setLocation(leftTopX, leftTopY);
	}

}
