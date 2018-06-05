package it.polito.tdp.seriea.model;

import java.util.HashMap;
import java.util.Map;

public class SeasonIDMap {
	private Map<Integer, Season> map;

	public SeasonIDMap() {
		super();
		map=new HashMap<>();
	}
	public Season get(Season t) {
		Season old = map.get(t.getSeason());
		if(old==null) {
			map.put(t.getSeason(), t);
			return t;
		}else {
			return old;
		}
	}
	
	public void put(Season t) {
		map.put(t.getSeason(), t);
		
	}
	
}
