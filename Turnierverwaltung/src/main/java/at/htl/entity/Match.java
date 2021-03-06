package at.htl.entity;

import at.htl.logic.DateAdapter;
import at.htl.logic.TimeAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by Laurenz on 15.10.2015 .
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "match.findAll",
        query = "select m from Match m"),
    @NamedQuery(name = "match.findByTeamId",
        query = "select m from Match m where m.team1.id = :id or m.team2.id = :id"),
    @NamedQuery(name = "match.findByTournamentId",
            query = "select m from Match m where m.tournament.id = :id")
})
@Table(name = "tv_match")
public class Match {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "m_id")
    private long id;
    @Column(name = "m_isactive")
    private boolean isActive;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "t_id1")
    private Team team1;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "t_id2")
    private Team team2;
    //@Column(name = "m_StartTime")
    //private LocalTime localTime;
    @Column(name = "m_result")
    private Result result;
    @Column(name = "m_court")
    private String court;
    @Column(name = "m_startTime")
    private LocalTime startTime;

    public Match() {
    }
    public Match(boolean isActive, Team team1, Team team2, Result result) {
        this.isActive = isActive;
        this.team1 = team1;
        this.team2 = team2;
        this.result = result;
    }

    public String getResult() {
        return result.toString();
    }

    public void setResult(String res) {
        this.result = new Result(res);
    }

    public void setResultObject(Result result) {
        this.result = result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @XmlTransient
    public Result getResultObject(){
        return result;
    }


    public boolean isActive() {
        return isActive;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String place) {
        this.court = place;
    }

    public boolean matchAlreadySet(Match match){
        if((this.getTeam1().getId() == match.getTeam1().getId() && this.getTeam2().getId() == match.getTeam2().getId()) || (this.getTeam1().getId() == match.getTeam2().getId() && this.getTeam2().getId() == match.getTeam1().getId()) ){
            return true;
        }
        return false;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "r_id")
    private Round round;

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }
    @XmlTransient
    @ManyToOne(optional = true)
    @JoinColumn(name = "to_id")
    private Tournament tournament;
    @XmlTransient
    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
    @XmlJavaTypeAdapter(TimeAdapter.class)
    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
}
