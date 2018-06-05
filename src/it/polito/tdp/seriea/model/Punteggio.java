package it.polito.tdp.seriea.model;

import java.util.HashMap;
import java.util.Map;

public class Punteggio implements Comparable<Punteggio>{
	
	private Team team;
	private Season season; 
	private int punteggio;
	
	
	public Punteggio(Team t,Season s,int n) {
		this.season=s;
		this.team=t;
		this.punteggio=n;
		
	}


	public Team getTeam() {
		return team;
	}


	public void setTeam(Team team) {
		this.team = team;
	}


	public int getPunteggio() {
		return punteggio;
	}


	public void setPunteggio(int punteggio) {
		this.punteggio = punteggio;
	}


	public Season getSeason() {
		return season;
	}


	public void setSeason(Season season) {
		this.season = season;
	}


	@Override
	public int compareTo(Punteggio o) {
		// TODO Auto-generated method stub
		return -(punteggio-o.getPunteggio());
	}


	@Override
	public String toString() {
		return season.toString()+"  :  "+punteggio;
	}
	
	



	
	
	

}
