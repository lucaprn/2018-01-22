package it.polito.tdp.seriea.model;

import java.util.HashMap;
import java.util.Map;

public class TeamIDMap {
	private Map<String, Team> map;

	public TeamIDMap() {
		super();
		map=new HashMap<>();
	}
	public Team get(Team t) {
		Team old = map.get(t.getTeam());
		if(old==null) {
			map.put(t.getTeam(), t);
			return t;
		}else {
			return old;
		}
	}
	
	public void put(Team t) {
		map.put(t.getTeam(), t);
		
	}
	
	
}
