package com.senninha.tankc.map.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.senninha.tankc.map.Direction;
import com.senninha.tankc.map.GridStatus;
import com.senninha.tankc.map.MapHelper;
import com.senninha.tankc.map.message.ResBattleResultMessage;
import com.senninha.tankc.map.message.ReqRunMessage;
import com.senninha.tankc.map.message.ResBulletMessage;
import com.senninha.tankc.map.message.ResHitMessage;
import com.senninha.tankc.map.message.ResMapResourceMessage;
import com.senninha.tankc.map.message.ResRunResultMessage;
import com.senninha.tankc.ui.GameData;
import com.senninha.tankc.ui.GamePanel;

import cn.senninha.sserver.client.ClientSession;
import cn.senninha.sserver.lang.dispatch.MessageHandler;
import cn.senninha.sserver.lang.dispatch.MessageInvoke;
import cn.senninha.sserver.lang.message.BaseMessage;
import cn.senninha.sserver.message.CmdConstant;

@MessageHandler
public class MapHandler {
	private Logger logger = LoggerFactory.getLogger(MapHandler.class);
	
	@MessageInvoke(cmd = CmdConstant.MAP_RESOURCE_RES)
	public void receiveMap(int sessionId, BaseMessage message) {
		logger.error("收到地图阻挡信息");
		ResMapResourceMessage res = (ResMapResourceMessage) message;
		GameData.getInstance().setMap(res.getList());
		//更新地图
		GameData.getInstance().updateMap();
		GameData.getInstance().setInGame(true);
		
		/**
		 * 这个是为了矫正显示的
		 */
		ReqRunMessage req = new ReqRunMessage();
		req.setDirection(Direction.EAST.getDirection());
		req.setGridStep((byte)1);
		ClientSession.getInstance().pushMessage(req);
		logger.error("推送行走数据成功");
	}
	
	@MessageInvoke(cmd = CmdConstant.RUN_RES)
	public void receiveRun(int sessionId, BaseMessage message){
		ResRunResultMessage res = (ResRunResultMessage) message;
		GridStatus g = MapHelper.getStatus(sessionId, res, res.getDirection());
		GameData.getInstance().updateMap(res.getX(), res.getY(), g, res.getDirection(), res.getSessionId());
		//刷新UI
		GameData.getInstance().updateMap();
		logger.error("更新坦克{},{}坐标完毕", res.getX(), res.getY());
//		move();
	}
	
	@MessageInvoke(cmd = CmdConstant.RES_BULLET)
	public void receiveBullet(int sessionId, BaseMessage message) {
		ResBulletMessage res = (ResBulletMessage) message;
		GridStatus g = MapHelper.getBulletStatus(res.getId(), res);
		GameData.getInstance().updateMapOfBullet(res.getX(), res.getY(), g, 0, res.getId());
		//刷新UI
		GameData.getInstance().updateMap();
		logger.error("更新子弹{},{}坐标完毕", res.getX(), res.getY());
	}
	
	@MessageInvoke(cmd = CmdConstant.RES_HIT_RESULT)
	public void receiveHitMessage(int sessionId, BaseMessage message) {
		ResHitMessage res = (ResHitMessage) message;
		logger.debug("{}击中了{}", res.getHitFrom(), res.getHitTo());
		
		String info = "";
		if(sessionId == res.getHitFrom()) {
			info = "您击中了对方,对方只剩下:" + res.getRemainHp() + "血了！			";
		}else {
			info = "您被击中了，只剩下:" + res.getRemainHp() + "血了!			";
			
			
			/** 构造一个移动来更新ui，智障。。。**/
			byte direction = (byte) GameData.getInstance().getTankContainer().get(sessionId).getDirection();
			ReqRunMessage req = new ReqRunMessage();
			req.setDirection(direction);
			req.setGridStep((byte)1);
			ClientSession.getInstance().pushMessage(req);
			/** 迫不得已的智障做法 **/
			
			
		}
		GameData.getInstance().updateInfo(info);
	}
	
	@MessageInvoke(cmd = CmdConstant.RES_BATTLE_RESULT)
	public void receiveBattleResultMessage(int sessionId, BaseMessage message) {
		ResBattleResultMessage res = (ResBattleResultMessage) message;
		logger.debug("{}杀死了{}", res.getFromSessionId(), res.getDieSessionId());
		GameData.getInstance().clearMap();	//清理战斗
		
		String info = "";
		if(sessionId == res.getFromSessionId()) {
			info = "你击败了:" + res.getName() + "						";
		}else {
			info = "你已经gg了								";
		}
		GameData.getInstance().updateInfo(info);
	}
//	/**
//	 * 向下移动，测试用
//	 */
//	private void upmove() {
//		// 尝试移动
//		ReqRunMessage req = new ReqRunMessage();
//		req.setDirection(Direction.SOUTH.getDirection());
//		req.setGridStep((byte) 10);
//
//		ClientSession.getInstance().pushMessage(req);
//	}
}
