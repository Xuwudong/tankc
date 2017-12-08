package com.senninha.tankc.ui.util;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import com.senninha.tankc.map.Grid;
import com.senninha.tankc.map.GridStatus;
import com.senninha.tankc.map.MapHelper;

/**
 * 画出游戏界面
 * @author senninha
 *
 */
public class DrawUtil {
	public static final int PER_PXIEL = 40;	//每个格子的像素
	public static final int WIDTH_GRIDS = 20; // x方向的格子数
	public static final int HEIGHT_GRIDS = 15;
	public static final int TOTAL_GRIDS = WIDTH_GRIDS * HEIGHT_GRIDS;
	private static Random r = new Random();

	/**
	 * 绘制出地图
	 * @param graphics
	 * @param jpanel
	 * @param list
	 */
	public static void drawMap(Graphics graphics, JPanel jpanel, List<Grid> list){
		if(list == null){
			return;
		}
		for(Grid grid : list){
			if(grid.getStatus() == GridStatus.BOOM0.getStatus()){
				//炸弹爆炸后还原成正常的图像,待优化此过程
				graphics.drawImage(TankGameImages.stuffImg[grid.getStatus()], grid.getPixelX() - MapHelper.BOOM_GRID_PIXEL / 2,
						grid.getPixelY() - MapHelper.BOOM_GRID_PIXEL / 2, MapHelper.BOOM_GRID_PIXEL, MapHelper.BOOM_GRID_PIXEL, jpanel);
//				grid.setStatus((byte)GridStatus.CAN_RUN.getStatus());
			}
			if (grid.getStatus() == GridStatus.CAN_NOT_SHOT.getStatus()) {//阻挡的绘制方法
				graphics.drawImage(TankGameImages.stuffImg[grid.getStatus()], grid.getPixelX(),
						grid.getPixelY(), grid.getWidth(), grid.getHeight(), jpanel);
			}else if(grid.getStatus() == GridStatus.SHELLS.getStatus()){//画出子弹
				graphics.drawImage(TankGameImages.stuffImg[grid.getStatus()], grid.getPixelX() - MapHelper.BULLET_GRID_PIXEL / 2,
						grid.getPixelY() - MapHelper.BULLET_GRID_PIXEL / 2, MapHelper.BULLET_GRID_PIXEL, MapHelper.BULLET_GRID_PIXEL, jpanel);
			}else if(grid.getStatus() >= 4 && grid.getStatus() <= 11) {//坦克的绘制方法
				graphics.drawImage(TankGameImages.stuffImg[grid.getStatus()], grid.getPixelX() - PER_PXIEL / 2,
						grid.getPixelY() - PER_PXIEL / 2, PER_PXIEL, PER_PXIEL, jpanel);
			}else if(grid.getStatus() >= GridStatus.AI_UP.getStatus() && grid.getStatus() <= GridStatus.AI_LEFT.getStatus()) {
				//AI坦克
				graphics.drawImage(TankGameImages.stuffImg[grid.getStatus()], grid.getPixelX() - PER_PXIEL / 2,
						grid.getPixelY() - PER_PXIEL / 2, PER_PXIEL, PER_PXIEL, jpanel);
			}
		}
		drawRect(graphics, jpanel, list);
	}
	
	private static void drawRect(Graphics graphics, JPanel jpanel, List<Grid> list) {
		graphics.setColor(Color.ORANGE);
		graphics.drawRect(0, 0, MapHelper.WIDTH_GRIDS * MapHelper.PER_GRID_PIXEL, MapHelper.HEIGHT_GRIDS * MapHelper.PER_GRID_PIXEL);
	}
	
	
	public static List<Grid> generateGridRandom() {
		List<Grid> list = new ArrayList<Grid>(TOTAL_GRIDS);
		for (int i = 0; i < TOTAL_GRIDS; i++) {
			list.add(new Grid((byte) (i % WIDTH_GRIDS), (byte) (i / WIDTH_GRIDS), (byte) (r.nextInt(3))));
		}
		return list;
	}
}
