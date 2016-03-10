package at.htl.web;

import at.htl.entity.Match;
import at.htl.entity.Team;
import at.htl.entity.Tournament;
import at.htl.logic.MatchFacade;
import at.htl.logic.TeamFacade;
import at.htl.logic.TournamentFacade;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Lokal on 21.01.2016.
 */
@Named
@RequestScoped
public class TeamsController implements Serializable {
    @Inject
    TeamFacade teamFacade;
    @Inject
    TournamentFacade tournamentFacade;
    @Inject
    MatchFacade matchFacade;

    public List<Team> getTeams(){
        return teamFacade.findTeamsFromLatestTournament();
    }
    public List<Team> getTeamsByRank(){
        return tournamentFacade.getTeamsOrderedByRank(tournamentFacade
                        .findLatestTournament());
    }
    public Match getFinal(){
        List<Match> matches= matchFacade.findMatchesByTeam(getTeamsByRank().get(0));
        return matches.get(matches.size());
    }



}