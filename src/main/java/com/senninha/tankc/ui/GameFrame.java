package com.senninha.tankc.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.senninha.tankc.map.MapHelper;
import com.senninha.tankc.map.handler.MapHandler;
import com.senninha.tankc.ui.util.DrawUtil;

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
	 * 菜单条
	 */
	private JMenuBar jMenuBar;
	
	/**
	 * 开始双人对战
	 */
	private JMenu twoPeople;
	private JMenuItem startTwoPeople;

	/**
	 * 游戏面板
	 */
	private GamePanel tankGamePanel;
	
	private JLabel label;

	/**
	 * 构造函数，初始化相关信息
	 */
	public GameFrame() {

		tankGamePanel = new GamePanel();

		// 菜单相关
		jMenuBar = new JMenuBar();
		
		twoPeople = new JMenu("双人对战");
		startTwoPeople = new JMenu("开始双人对战");
		startTwoPeople.setActionCommand(ActionCommand.START_TWO_PEOPLE);
		startTwoPeople.addActionListener(tankGamePanel);
		
		label = new JLabel("");				//显示战况的

		this.addKeyListener(tankGamePanel); // 游戏面板来作为按键侦听器

//		 菜单条
		this.setJMenuBar(jMenuBar);
		
		/** 设置背景颜色 **/
		tankGamePanel.setBackground(Color.BLACK);

		// 将panel添加到Frame
		this.add(tankGamePanel);
		tankGamePanel.add(label);
		// 加入菜单
		this.setJMenuBar(jMenuBar);
		

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