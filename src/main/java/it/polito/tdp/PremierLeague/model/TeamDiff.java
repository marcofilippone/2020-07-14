package it.polito.tdp.PremierLeague.model;

public class TeamDiff implements Comparable<TeamDiff>{
	private Team team;
	private Integer diff;
	public TeamDiff(Team team, Integer diff) {
		super();
		this.team = team;
		this.diff = diff;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public Integer getDiff() {
		return diff;
	}
	public void setDiff(Integer diff) {
		this.diff = diff;
	}
	@Override
	public int compareTo(TeamDiff other) {
		return this.getDiff().compareTo(other.getDiff());
	}
	@Override
	public String toString() {
		return this.getTeam().getName()+"("+this.getDiff()+")";
	}
	
	

}
