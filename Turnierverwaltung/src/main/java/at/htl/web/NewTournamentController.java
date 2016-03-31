package at.htl.web;

import at.htl.entity.Team;
import at.htl.entity.Tournament;
import at.htl.logic.TournamentFacade;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import at.htl.logic.TournamentSystems;
import org.omnifaces.util.Messages;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class NewTournamentController implements Serializable {

    //private static final Logger logger = LogManager.getLogger(NewTournamentController.class);

    @Inject
    private TournamentFacade tournamentFacade;

    @Inject
    private TournamentSystems systems;

    private DualListModel<String> types;
    private List<String> selectedTypes = new ArrayList<>();
    private int teamCount = 10;
    private int groupSize = 3;
    private int pointsWin = 3;
    private int pointsDraw = 1;
    private List<Team> teams;
    private String tournamentSystem;
    private String groupPhaseIcon;
    List<String> typesSource = new ArrayList<String>();
    List<String> typesTarget = new ArrayList<String>();



    public Tournament getLatestTournament() {
        return tournamentFacade.findLatestTournament();
    }

    public List<Tournament> getTournaments() {
        return tournamentFacade.findAllTournaments();
    }

    public List<Tournament> getClosedTournaments() {
        return tournamentFacade.findAllClosedTournaments();
    }

    /**
     * Fügt die verschiedenen Systeme in die Source-List ein
     */
    @PostConstruct
    public void setupPickList() {
        typesSource.add("Gruppenphase");
        typesSource.add("Leitersystem");
        typesSource.add("Schweizersystem");
        typesSource.add("Doppel-KO-System");
        typesSource.add("KO-System");

        types = new DualListModel<>(typesSource, typesTarget);
    }


    /**
     * Wird aufgerufen, wenn bei der Picklist ein Element auf die andere Seite geschoben
     * wird und fügt das Element zur Liste hinzu. Gibt dann eine Message zurück.
     *
     * @param event
     */
    public void onTransfer(TransferEvent event) {
        FacesMessage msg = new FacesMessage();
        StringBuilder detail = new StringBuilder();

        for (String s : (List<String>) event.getItems()) {
            detail.append(s).append("<br />");
            if (!selectedTypes.contains(s)) {
                selectedTypes.add(s);
                msg.setSummary("Items Transferred");
            } else {
                selectedTypes.remove(s);
                msg.setSummary("Items removed");
            }
        }

        msg.setSeverity(FacesMessage.SEVERITY_INFO);

        msg.setDetail(detail.toString());

        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    /*public List<Team> updateTeamList(){
        for (int i = 1; i < teamCount+1; i++) {
            teams.add(new Team("Team "+i,false));
        }
        System.out.println(getSelectedTypes().size()+"-"+getTeamCount()+"-"+getGroupSize()+"-"+getPointsDraw());
        return teams;
    }*/

    public void buttonAction(ActionEvent actionEvent) {
        Tournament tournament = new Tournament("Schulcup", LocalDate.now(), true, teams);
        systems.koSystemRound(systems.manageGroupPhase(tournament));
        /*for (long j = 1; j<3;j++) {
            teams = new ArrayList<Team>();
            for (long i = 1; i < TEAM_COUNT + 1; i++) {
                Team team = new Team("Team" + i, false, 0);
                em.persist(team);
                //team = teamFacade.save(team);
                teams.add(team);
            }
            Tournament tournament = new Tournament("Schulcup"+j, LocalDate.now().minusDays(j), true, teams);
            em.persist(tournament);
            systems.koSystemRound(systems.manageGroupPhase(tournament));
            //systems.koSystemRound(teams);
            //systems.koSystemRound(systems.manageGroupPhase(teams));

            //systems.getRankKoSystem();
            tournament.setTeams(teams);
            for(Team t : teams){
                t.setTournament(tournament);
                em.merge(t);
            }
        }*/
    }

    public void onTeamCountSlideEnd(SlideEndEvent event) {
        //logger.info("************************ " + event.getValue());
        setTeamCount(event.getValue());
        getTeams();
    }
    public void onGroupSizeSlideEnd(SlideEndEvent event) {
        //logger.info("************************ " + event.getValue());
        setGroupSize(event.getValue());
    }
    public void onPointsWinSlideEnd(SlideEndEvent event) {
        //logger.info("************************ " + event.getValue());
        setPointsWin(event.getValue());
    }
    public void onPointsDrawSlideEnd(SlideEndEvent event) {
        //logger.info("************************ " + event.getValue());
        setPointsDraw(event.getValue());
    }

    public String getGroupPhaseIcon() {
        if (selectedTypes.contains("Gruppenphase"))
            return "ui-icon-check";
        return "ui-icon-close";
    }

    public String getTournamentSystem() {
        for (String s : selectedTypes) {
            if (!s.equals("Gruppenphase")) {
                for (String types : typesSource) {
                    if (types.equals(s))
                        return s;
                }
            }
        }
        return "none";
    }

    public void setTournamentSystem(String tournamentSystem) {
        this.tournamentSystem = tournamentSystem;
    }

    public List<String> getSelectedTypes() {
        return selectedTypes;
    }

    public void setSelectedTypes(List<String> selectedTypes) {
        this.selectedTypes = selectedTypes;
    }

    public DualListModel<String> getTypes() {
        return types;
    }

    public void setTypes(DualListModel<String> types) {
        this.types = types;
    }

    public int getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public int getPointsDraw() {
        return pointsDraw;
    }

    public void setPointsDraw(int pointsDraw) {
        this.pointsDraw = pointsDraw;
    }

    public int getPointsWin() {
        return pointsWin;
    }

    public void setPointsWin(int pointsWin) {
        this.pointsWin = pointsWin;
    }

    public List<Team> getTeams() {
        if(teams==null || getTeamCount()!=teams.size()){
            List<Team> teams = new ArrayList<Team>();
            for (int i = 1; i < teamCount + 1; i++) {
                teams.add(new Team("Team " + i, false));
            }
            setTeams(teams);
            System.out.println(getSelectedTypes().size() + "-" + getTeamCount() + "-" + getGroupSize() + "-" + getPointsDraw());
        }
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
