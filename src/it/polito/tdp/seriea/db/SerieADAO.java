package it.polito.tdp.seriea.db;

import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.seriea.model.Punteggio;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.SeasonIDMap;
import it.polito.tdp.seriea.model.Team;
import it.polito.tdp.seriea.model.TeamIDMap;

public class SerieADAO {

	public List<Season> listAllSeasons() {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams(TeamIDMap mapteam) {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Team team = new Team(res.getString("team"));
				result.add(mapteam.get(team));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Punteggio> getPunteggio(Team t,SeasonIDMap map) {
		
		List punteggio = new ArrayList<>();
		
		String sql1 = "SELECT s.season,s.description, (punteggioVittorie.punteggio+punteggioPareggio.punteggio) as totPunteggio\n" + 
				"FROM (SELECT s.season,s.description,3*COUNT(match_id) as punteggio\n" + 
				"FROM matches as m, seasons as s\n" + 
				"WHERE  ((m.HomeTeam=? AND m.FTR=\"H\") OR (m.AwayTeam=? AND m.FTR=\"A\")) AND m.Season=s.season\n" + 
				"GROUP BY Season) as punteggioVittorie, (SELECT s.season,s.description,COUNT(match_id) as punteggio\n" + 
				"FROM matches as m, seasons as s\n" + 
				"WHERE  ((m.HomeTeam=? AND m.FTR=\"D\") OR (m.AwayTeam=? AND m.FTR=\"D\")) AND m.Season=s.season\n" + 
				"GROUP BY Season) as punteggioPareggio, seasons s\n" + 
				"WHERE punteggioVittorie.season=punteggioPareggio.season AND s.season=punteggioVittorie.season";
		Connection conn = DBConnect.getConnection();
		
		
		try {
			PreparedStatement st = conn.prepareStatement(sql1);
			st.setString(1, t.getTeam());
			st.setString(2, t.getTeam());
			st.setString(3, t.getTeam());
			st.setString(4, t.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Season s = new Season(res.getInt("season"),res.getString("description"));
				Punteggio p = new Punteggio(t, map.get(s), res.getInt("totPunteggio"));
				punteggio.add(p);
				
				
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return punteggio;
	}
	
	public int getPunteggio(Team t,Season s) {
		
		int result = -1;
		
		String sql1 = "SELECT p.Pareggi+v.Vincite as result\n" + 
				"FROM (SELECT COUNT(match_id) as Pareggi\n" + 
				"FROM matches\n" + 
				"WHERE ((HomeTeam=? AND FTR=\"D\") OR (AwayTeam=? AND FTR=\"D\")) AND season=?) as p,\n" + 
				"(SELECT 3*COUNT(match_id) as Vincite\n" + 
				"FROM matches\n" + 
				"WHERE ((HomeTeam=? AND FTR=\"H\") OR (AwayTeam=? AND FTR=\"A\")) AND season=?) as v";
		Connection conn = DBConnect.getConnection();
		
		
		try {
			PreparedStatement st = conn.prepareStatement(sql1);
		st.setString(1, t.getTeam());
		st.setString(2, t.getTeam());
		st.setInt(3, s.getSeason());
		st.setString(4, t.getTeam());
		st.setString(5, t.getTeam());
		st.setInt(6, s.getSeason());
		ResultSet res = st.executeQuery();

			while (res.next()) {
				
				 result = res.getInt("result");
				 System.out.println(result);

			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		return result;
	}
	
	

}
