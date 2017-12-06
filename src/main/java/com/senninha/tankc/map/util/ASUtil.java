package com.senninha.tankc.map.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.senninha.tankc.map.Grid;
import com.senninha.tankc.map.GridStatus;

public class ASUtil {

	public static void main(String[] args) {
		Map<ASNode, ASNode> test = new HashMap<>();
		Grid grid = new Grid();
		
		ASNode a0 = new ASNode(grid, 1, 2, null);
		ASNode a1 = new ASNode(grid, 1, 4, null);
		
		test.put(a0, a0);
		
		System.out.println(test.containsKey(a1));
	}
	
	/**
	 * 
	 * @param grids
	 * @param source
	 * @param target
	 * @param width
	 * @param height
	 * @return
	 */
	public static ASNode aStar(List<Grid> grids, Grid source, Grid target, int width, int height) {
		ASNode cur = new ASNode(source, 0, 0, null);
		List<ASNode> openList = new ArrayList<>();
		Map<ASNode, ASNode> closeMap = new HashMap<>();
		Map<ASNode, ASNode> openMap = new HashMap<>();
		
		while(cur.getValue() != target) {
			closeMap.put(cur, cur);	//先把cur加入closeList

			/** 1找出当前节点周围可用的节点 **/
			List<ASNode> temToOpenList = findNodeFromFourOriention(grids, source, target, cur, width, height);
			
			/** 处理这个可走路径，待加入open和closelist中**/
			for(ASNode node : temToOpenList) {
				//1.如果不在closeMap中才考虑
				if(!closeMap.containsKey(node)) {
					ASNode existInOpen = openMap.get(node);
					if(existInOpen == null) {					//不存在在openList里
						node.setParent(cur);
						//加入openList里
						openList.add(node); openMap.put(node, node);
					}else {
						if(existInOpen.getgValue() >= node.getgValue()) {//比较G值
							existInOpen.setgValue(node.getgValue());  	//把较小的G值给它
							existInOpen.setParent(cur);						//设置父结点为当前
						}
					}
				}
				
			}
			
			/** 排序，并且把**最小**的路径拿出来当作当前节点**/
			Collections.sort(openList);
			if(openList.size() == 0) {
				return null;
			}
			/** 移除openList **/
			cur = openList.remove(0);
			openMap.remove(cur);
		}
		
		return cur;
	}
	
	/**
	 * 计算某个节点周围可走的路径
	 * @param grids	地图
	 * @param source	源头
	 * @param target	目标
	 * @param cur	当前走到了哪里
	 * @return	返回可走的节点，待加入openlist
	 */
	private static List<ASNode> findNodeFromFourOriention(List<Grid> grids, Grid source, Grid target, ASNode cur, int width, int height){
		List<ASNode> lists = new ArrayList<>(4);
		Grid current = cur.getValue();
		/** 计算逻辑待完善 **/
		/** 上 **/
		if (current.getY() - 1 >= 0) { // 不越界
			Grid next = grids.get(((current.getY() - 1) * width) + current.getX());
			caculateFourOrientionRoad(lists, source, current, target, next);
		}
		/** 下 **/
		if (current.getY() + 1 < height) {
			Grid next = grids.get(((current.getY() + 1) * width) + current.getX());
			caculateFourOrientionRoad(lists, source, current, target, next);
		}
		/** 左 **/
		if (current.getX() - 1 >= 0) {
			Grid next = grids.get(((current.getY()) * width) + current.getX() - 1);
			caculateFourOrientionRoad(lists, source, current, target, next);
		}
		/** 右 **/
		if (current.getX() + 1 < width) {
			Grid next = grids.get(((current.getY()) * width) + current.getX() + 1);
			caculateFourOrientionRoad(lists, source, current, target, next);
		}

		return lists;
	}
	
	private static void caculateFourOrientionRoad(List<ASNode> lists, Grid startGrid,
			Grid current, Grid target, Grid grid) {
		if (grid.getStatus() == GridStatus.CAN_RUN.getStatus()) { // 可走动
			int hValue = Math.abs(target.getX() - grid.getX()) + Math.abs(target.getY() - grid.getY());
			int gValue = Math.abs(startGrid.getX() - grid.getX()) + Math.abs(startGrid.getY() - grid.getY());
			ASNode tem = new ASNode(grid, gValue, hValue, null);
			lists.add(tem);
		}
	}
}
