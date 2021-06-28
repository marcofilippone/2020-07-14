package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.Team;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void listAllTeams(Map<Integer, Team> idMap){
		String sql = "SELECT * FROM Teams";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("TeamID"))) {
					Team team = new Team(res.getInt("TeamID"), res.getString("Name"));
					idMap.put(res.getInt("TeamID"), team);
				}
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Match> listAllMatches(){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				
				result.add(match);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void casa(Map<Team, Integer> classifica, Map<Integer, Team> idMap, Team t) {
		String sql = "SELECT m.TeamHomeID, m.ResultOfTeamHome "
				+ "FROM teams t, matches m "
				+ "WHERE t.TeamID = m.TeamHomeID AND t.TeamID = ?";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, t.getTeamID());
			ResultSet res = st.executeQuery();
			int punti = 0;
			int ris = 0;
			int id = 0;
			while (res.next()) {
				if(idMap.containsKey(res.getInt("m.TeamHomeID"))) {
					ris = res.getInt("m.ResultOfTeamHome");
					if(ris == 1)
						punti += 3;
					else if(ris == 0)
						punti += 1;
				}
				id = res.getInt("m.TeamHomeID");
			}
			classifica.put(idMap.get(id), classifica.get(idMap.get(id)) + punti);
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void trasferta(Map<Team, Integer> classifica, Map<Integer, Team> idMap, Team t) {
		String sql = "SELECT m.TeamAwayID, m.ResultOfTeamHome "
				+ "FROM teams t, matches m "
				+ "WHERE t.TeamID = m.TeamAwayID AND t.TeamID = ?";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, t.getTeamID());
			ResultSet res = st.executeQuery();
			int punti = 0;
			int ris = 0;
			int id = 0;
			while (res.next()) {
				if(idMap.containsKey(res.getInt("m.TeamAwayID"))) {
					ris = res.getInt("m.ResultOfTeamHome");
					if(ris == -1)
						punti += 3;
					else if(ris == 0)
						punti += 1;
				}
				id = res.getInt("m.TeamAwayID");
			}
			classifica.put(idMap.get(id), classifica.get(idMap.get(id)) + punti);
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
