package com.senninha.tankc.map.message;

import java.util.List;

import com.senninha.tankc.map.Grid;

import cn.senninha.sserver.lang.message.BaseMessage;
import cn.senninha.sserver.lang.message.Message;
import cn.senninha.sserver.message.CmdConstant;

@Message(cmd = CmdConstant.MAP_RESOURCE_RES)
public class ResMapResourceMessage extends BaseMessage{
	private long mapId;
	private List<Grid> list;
	
	public static ResMapResourceMessage valueOf() {
		ResMapResourceMessage m = new ResMapResourceMessage();
		m.mapId = System.currentTimeMillis();
//		m.list = MapResource.generateGridRandom();
		return m;
	}
	
	public long getMapId() {
		return mapId;
	}
	@Override
	public String toString() {
		return "ResMapResourceMessage [mapId=" + mapId + ", list=" + list + "]";
	}

	public void setMapId(long mapId) {
		this.mapId = mapId;
	}
	public List<Grid> getList() {
		return list;
	}
	public void setList(List<Grid> list) {
		this.list = list;
	}
	
	
}
