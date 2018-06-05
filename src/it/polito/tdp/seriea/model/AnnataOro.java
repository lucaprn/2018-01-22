package it.polito.tdp.seriea.model;

public class AnnataOro {
	
	private Season season; 
	private int differenza;
	public AnnataOro(Season season, int differenza) {
		super();
		this.season = season;
		this.differenza = differenza;
	}
	public Season getSeason() {
		return season;
	}
	public void setSeason(Season season) {
		this.season = season;
	}
	public int getDifferenza() {
		return differenza;
	}
	public void setDifferenza(int differenza) {
		this.differenza = differenza;
	}
	@Override
	public String toString() {
		return "AnnataOro [season=" + season + ", differenza=" + differenza + "]";
	}
	
	

}
