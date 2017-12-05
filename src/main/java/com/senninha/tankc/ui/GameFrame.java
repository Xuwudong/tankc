package com.senninha.tankc.ui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * 游戏Frame
 * 
 * @author 1052067939
 * @version 1.0
 *
 */
public class GameFrame extends JFrame {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = -1176914786963603304L;

	/**
	 * 游戏面板
	 */
	private GamePanel tankGamePanel;
	
	private JMenuBar menuBar;
	/** 菜单1 **/
	private JMenu startMenu;
	/** 开始二人游戏 **/
	private JMenuItem twoPeopleMenuItem;
	
	private JLabel label;

	/**
	 * 构造函数，初始化相关信息
	 */
	public GameFrame() {
		/** 初始化一系列的东西 **/
		tankGamePanel = new GamePanel();
		menuBar = new JMenuBar();
		startMenu = new JMenu("开始游戏");
		twoPeopleMenuItem = new JMenuItem("开始二人游戏");
		twoPeopleMenuItem.setActionCommand(ActionCommand.START_TWO_PEOPLE);
		
		label = new JLabel("");				//显示战况的

		
		/** 监听 **/
		this.addKeyListener(tankGamePanel); // 游戏面板来作为按键侦听器
		twoPeopleMenuItem.addActionListener(tankGamePanel);
		
		/** 设置背景颜色 **/
		tankGamePanel.setBackground(Color.BLACK);

		// 将panel添加到Frame
		this.add(tankGamePanel);
		tankGamePanel.add(label);
		
		/** 添加菜单类 **/
		this.setJMenuBar(menuBar);
		menuBar.add(startMenu);
		
		startMenu.add(twoPeopleMenuItem);
		
		

	}
	
	/**
	 * 重新绘制战斗场景
	 */
	public void repaint(){
		tankGamePanel.repaint();
	}
	
	/**
	 * 打印战况
	 * @param text
	 */
	public void printInfo(String text) {
		label.setText(text);
		label.setForeground(Color.RED);
	}

}