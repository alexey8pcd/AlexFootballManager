package rusfootballmanager.transfers;

import java.util.HashMap;
import java.util.Map;
import rusfootballmanager.common.CostCalculator;
import rusfootballmanager.entities.Offer;
import rusfootballmanager.entities.Player;
import rusfootballmanager.entities.Team;

/**
@author Alexey
*/
public class TransferPlayer {
    private Player player;
    private Team team;
    private int sum;
    private TransferStatus status;
    private Map<Team, Offer> offers;

    public TransferPlayer(Player player, Team team, TransferStatus status) {
        this.player = player;
        this.team = team;
        this.status = status;
        this.sum = CostCalculator.calculateTransferCost(player.getAge(), 
                player.getAverage());
    }
    
    public void addOffer(Team team, Offer offer){
        if(offers == null){
            offers = new HashMap<>();
        }
        offers.put(team, offer);
    }

    public Map<Team, Offer> getOffers() {
        return offers;
    }   
    

    public Player getPlayer() {
        return player;
    }

    public Team getTeam() {
        return team;
    }

    public int getSum() {
        return sum;
    }

    public TransferStatus getStatus() {
        return status;
    }
    
    
}
