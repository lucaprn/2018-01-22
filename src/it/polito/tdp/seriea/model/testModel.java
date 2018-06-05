package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.List;

import javax.management.modelmbean.ModelMBeanConstructorInfo;

public class testModel {

	public static void main(String[] args) {
		Model model = new Model();
		Team t = new Team("Inter");
		model.getPunteggio(t);
		model.creaGrafo();
		System.out.println("Il grafo ha : "+model.getGrafo().vertexSet().size()+" vertici");
		System.out.println("Il grafo ha : "+model.getGrafo().edgeSet().size()+" archi");
		AnnataOro annataOro = model.getAnnataOro(t);
		System.out.println("\nAnnata d'oro è : "+annataOro.getSeason().toString()+"  differenza : "+annataOro.getDifferenza()+"\n");
//		List<Season> parziale = new ArrayList<>();
//		for(Season season : model.getGrafo().vertexSet()) {
//			parziale.add(season);
//		}
		model.camminoVirtuoso();

		List<Season> result = model.getSequenzaOttima();
		for(Season s : result) {
			System.out.println(s);
		}
		
		List<Punteggio> punt = model.getPunteggioOttimo(t);
		for(Punteggio p : punt) {
			System.out.println(p);
		}
		

	}
	
}
