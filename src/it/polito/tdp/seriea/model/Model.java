package it.polito.tdp.seriea.model;



import java.awt.geom.Ellipse2D;
import java.awt.image.FilteredImageSource;
import java.io.DataOutput;
import java.security.interfaces.RSAKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveAction;

import javax.lang.model.element.Parameterizable;
import javax.swing.text.DefaultEditorKit.DefaultKeyTypedAction;
import javax.swing.text.StyledEditorKit.ItalicAction;
import javax.xml.stream.EventFilter;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;
import org.jgrapht.util.ArrayUnenforcedSet;
import org.omg.PortableServer.AdapterActivator;
import org.omg.PortableServer.ID_ASSIGNMENT_POLICY_ID;

import com.mysql.jdbc.interceptors.SessionAssociationInterceptor;

import it.polito.tdp.seriea.db.SerieADAO;



public class Model {
	
	private List<Team> squadre;
	private SerieADAO adao;
	private TeamIDMap mapteam;
	private SeasonIDMap mapseason;
	private List<Punteggio> punteggio;
	private Graph<Season, DefaultWeightedEdge> grafo;
	private List<Season> sequenzaOttima;
	private List<Season> seasonFiltered;
	private int lunghezzaMax;
	
	
	
	public Model() {
		this.mapteam=new TeamIDMap();
		this.mapseason=new SeasonIDMap();
		this.adao = new SerieADAO();
		this.squadre= new LinkedList<>(adao.listTeams(mapteam));
		this.grafo=new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
	}

	public List<Team> getAllTeams() {
		
		return squadre;
	}
	
	

	public List<Punteggio> getPunteggio(Team t) {
		this.punteggio=adao.getPunteggio(t, mapseason);
		return this.punteggio;
	}
	
	public void creaGrafo() {
		List<Season> vertex = new ArrayList<>();
		for(Punteggio p : this.punteggio) {
			grafo.addVertex(p.getSeason());
		}
		addAllEdges();
	}

	private void addAllEdges() {
		Collections.sort(punteggio);
		for(int i = 0; i<punteggio.size()-1; i++) {
			for(int j = i+1 ; j<punteggio.size(); j++) {
				int peso = punteggio.get(i).getPunteggio()-punteggio.get(j).getPunteggio();
				Graphs.addEdge(grafo, punteggio.get(j).getSeason(), punteggio.get(i).getSeason(), peso);
//				System.out.println("Da : "+punteggio.get(j)+" a : "+punteggio.get(i));
//				System.out.print(peso+"\n");
			}
		}
		
	}

	public AnnataOro getAnnataOro(Team t) {
		int max=-1;
		Season maxSeason=null;
		for(Season s : grafo.vertexSet()) {
		int pesoEntrate = 0;
		int pesoUscita = 0;
		int differenza;
		AnnataOro annataOro;

			for(DefaultWeightedEdge edge : grafo.incomingEdgesOf(s)) {
				pesoEntrate+=grafo.getEdgeWeight(edge);
			}
			for(DefaultWeightedEdge edge : grafo.outgoingEdgesOf(s)) {
				pesoUscita+=grafo.getEdgeWeight(edge);
			}
			differenza=pesoUscita-pesoEntrate;
			if(differenza>max) {
				max=differenza;
				maxSeason=s;
			}
		
		}
		AnnataOro a = new AnnataOro(maxSeason, max);
		return a;
	}
	
	public void camminoVirtuoso() {
		sequenzaOttima=new ArrayList<>();
		lunghezzaMax=Integer.MIN_VALUE;
		seasonFiltered = new ArrayList<>(grafo.vertexSet());
		Collections.sort(seasonFiltered);
		
		recursive(new ArrayList<Season>(),0);
		
	}

	private void recursive(ArrayList<Season> parziale, int livello) {
		
		System.out.println(livello);
		
		if(parziale!=null && parziale.size()>=lunghezzaMax) {
			lunghezzaMax=parziale.size();
			sequenzaOttima = new ArrayList<>(parziale);
		}
		
		for(int i = 0; i<seasonFiltered.size(); i++) {
			Season s = seasonFiltered.get(i);
			if(!parziale.contains(s)) {
				parziale.add(s);
				if(isCorretta(parziale) && isSuccessiva(parziale, s)) {
					
					recursive(parziale, livello+1);
					
					}
				
				parziale.remove(s);
				}
			
		
		}
		
		
	}

	private boolean isSuccessiva(List<Season> parziale, Season s) {
		Season tmp =null;
		if(parziale.size()>=2) {
			for(int i =0 ; i<seasonFiltered.size()-1; i++) {
				if(parziale.get(parziale.size()-2).equals(seasonFiltered.get(i))) {
					tmp=seasonFiltered.get(i+1);
				}
			}
			if(tmp==null) {
				return false;
			}
			if(!s.equals(tmp)) {
				return false;
			}
		}
		
		return true;
	}

	private boolean isCorretta(ArrayList<Season> parziale) {
		if(parziale.size()==1) {
			return true;
		}
		if(parziale.size()==2) {
			Season s1 = parziale.get(0);
			Season s2 = parziale.get(1);
			DefaultWeightedEdge e = grafo.getEdge(s1,s2);
			if(e==null) {
				return false;
			}else {
				return true;
			}
		}
		if(parziale.size()>=3){
			Season s1 = parziale.get(parziale.size()-3);
			Season s2 = parziale.get(parziale.size()-2);
			Season s3 = parziale.get(parziale.size()-1);
			DefaultWeightedEdge e1 = grafo.getEdge(s1, s2);
			DefaultWeightedEdge e2 = grafo.getEdge(s2, s3);
			if(e2==null) {
				return false;
			}
			int peso1 =(int) grafo.getEdgeWeight(e1);
			int peso2 =(int) grafo.getEdgeWeight(e2);
			if(peso2<=peso1) {
				return false;
			}else {
				return true;
			}
			
		}	

		return false;
	}
	
	
	public List<Punteggio> getPunteggioOttimo(Team t){
		List<Punteggio> result = new ArrayList<>();
		for(Season s : sequenzaOttima) {
			result.add(new Punteggio(t, s, adao.getPunteggio(t, s)));
		}
		return result;
	}


	public Graph<Season, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public List<Punteggio> getPunteggio() {
		return punteggio;
	}

	public List<Season> getSequenzaOttima() {
		return sequenzaOttima;
	}
	
	


	
	


	
	
	
	
	

}
