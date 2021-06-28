package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private Map<Integer, Team> idMap;
	private Map<Team, Integer> classifica;
	
	public Model() {
		dao = new PremierLeagueDAO();
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		idMap = new HashMap<>();
		classifica = new HashMap<>();
		dao.listAllTeams(idMap);
		Graphs.addAllVertices(this.grafo, idMap.values());
		for(Team t : idMap.values()) {
			classifica.put(t, 0);
			dao.casa(classifica, idMap, t);
			dao.trasferta(classifica, idMap, t);
		}
		for(Team t : classifica.keySet()) {
			for(Team t2 : classifica.keySet()) {
				if(classifica.get(t) - classifica.get(t2) > 0) {
					Graphs.addEdgeWithVertices(this.grafo, t, t2, (classifica.get(t) - classifica.get(t2)));
				}
			}
		}
	}
	
	public Set<Team> getVertici(){
		return grafo.vertexSet();
	}
	
	public Set<DefaultWeightedEdge> getArchi(){
		return grafo.edgeSet();
	}
	
	public List<TeamDiff> getMigliori(Team t){
		List<TeamDiff> list = new ArrayList<>();
		for(Team team : classifica.keySet()) {
			if(classifica.get(team) > classifica.get(t)) {
				list.add(new TeamDiff(team, classifica.get(team) - classifica.get(t)));
			}
		}
		Collections.sort(list);
		return list;
	}
	
	public List<TeamDiff> getPeggiori(Team t){
		List<TeamDiff> list = new ArrayList<>();
		for(Team team : classifica.keySet()) {
			if(classifica.get(team) < classifica.get(t)) {
				list.add(new TeamDiff(team, classifica.get(t) - classifica.get(team)));
			}
		}
		Collections.sort(list);
		return list;
	}
}
