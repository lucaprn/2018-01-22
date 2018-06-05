package it.polito.tdp.seriea.db;

import java.util.List;

import it.polito.tdp.seriea.model.Punteggio;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.SeasonIDMap;
import it.polito.tdp.seriea.model.Team;
import it.polito.tdp.seriea.model.TeamIDMap;

public class TestSerieADAO {

	public static void main(String[] args) {
		SerieADAO dao = new SerieADAO();
		
		TeamIDMap mapt = new TeamIDMap();
		SeasonIDMap seasonmap = new SeasonIDMap();

		List<Season> seasons = dao.listAllSeasons();
		System.out.println(seasons);
		System.out.println("Seasons # rows: " + seasons.size());

		List<Team> teams = dao.listTeams(mapt);
		System.out.println(teams);
		System.out.println("Teams # rows: " + teams.size());
		
		List<Punteggio> punteggio = dao.getPunteggio(new Team("Roma"), seasonmap);
		for(Punteggio p : punteggio) {
			System.out.format("%-10s  %10d\n", p.getSeason().getDescription(), p.getPunteggio());
		}
		
		
	}

}
