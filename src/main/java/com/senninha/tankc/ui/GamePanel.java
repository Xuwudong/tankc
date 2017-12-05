package com.senninha.tankc.ui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.senninha.tankc.map.Direction;
import com.senninha.tankc.ui.util.DrawUtil;
import com.senninha.tankc.user.message.ReqMatchMessage;

import cn.senninha.sserver.client.ClientSession;

/**
 * 游戏面板，继承自JPanel，实现KeyListener,ActionListener接口
 * <p>
 * 游戏主要类，坦克游戏就在该面板上进行
 * 
 * @author 1052067939
 *
 */
public class GamePanel extends JPanel implements KeyListener, ActionListener {
	private Logger logger = LoggerFactory.getLogger(GamePanel.class);
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
			case ActionCommand.START_TWO_PEOPLE:
				ClientSession.getInstance().pushMessage(new ReqMatchMessage());
				logger.error("匹配比赛:{}" + ClientSession.getInstance().getSessionId());
				break;			
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!GameData.getInstance().isInGame()) {
			return;
		}
		byte direction = -1;
		if(e.getKeyCode() == KeyEvent.VK_UP){
			direction = Direction.NORTH.getDirection();
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN){
			direction = Direction.SOUTH.getDirection();
		}else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			direction = Direction.EAST.getDirection();
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT){
			direction = Direction.WEST.getDirection();
		}else if(e.getKeyCode() == KeyEvent.VK_A){//开火
			GameData.getInstance().setFire();
		}
		
		if (direction != -1) {
			GameData.getInstance().setDirection(direction);
		}
		logger.error("键盘事件编码：{}" + direction);
//		MapHandler.move();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		DrawUtil.drawMap(g, this, GameData.getInstance().getMap());
	}

}
