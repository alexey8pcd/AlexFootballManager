package ru.alexey_ovcharov.rusfootballmanager.entities.transfer;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Contract;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;

/**
 * Трансферный рынок
 *
 * @author Alexey
 */
public class Market {

    private static final Logger LOGGER = LoggerContext.getContext().getLogger(Market.class.getName());
    private static final Market MARKET = new Market();
    private final List<Transfer> transfers = new ArrayList<>();
    private final Set<Offer> offers = new HashSet<>();
    private final Map<Team, List<Transfer>> cache = new HashMap<>();
    private MarketListener listener;

    private Market() {
    }

    public static Market getInstance() {
        return MARKET;
    }

    public void clear() {
        transfers.clear();
    }

    public void addPlayer(Player player, Team team, TransferStatus transferStatus) {
        transfers.add(new Transfer(player, team, transferStatus));
    }

    public void removePlayer(Player player) {
        transfers.stream()
                 .filter(transferPlayer -> transferPlayer.getPlayer() == player)
                 .findFirst()
                 .ifPresent(transfers::remove);
    }

    public List<Transfer> getTransfers() {
        return transfers;
    }

    public List<Transfer> getTransfers(Filter filter) {
        if (filter != null) {
            return transfers.stream()
                            .filter(filter::accept)
                            .collect(Collectors.toList());
        }
        return transfers;
    }

    public List<Transfer> getTransfers(TransferStatus transferStatus) {
        return transfers.stream()
                        .filter(tr -> tr.getTransferStatus() == transferStatus)
                        .collect(Collectors.toList());
    }

    public List<Transfer> getTransfers(Team team) {
        List<Transfer> playersFromTeam;
        if (cache.containsKey(team)) {
            playersFromTeam = cache.get(team);
        } else {
            playersFromTeam = transfers.stream()
                                       .filter(Objects::nonNull)
                                       .filter(transfer -> {
                                           Optional<Team> teamOpt = transfer.getTeam();
                                           return teamOpt.isPresent() && teamOpt.get() == team;
                                       })
                                       .collect(Collectors.toList());
            cache.put(team, playersFromTeam);
        }
        return playersFromTeam;
    }

    public List<Transfer> getTransfers(Team team, Filter filter) {
        List<Transfer> playersFromTeam = getTransfers(team);
        if (filter != null) {
            return playersFromTeam.stream()
                                  .filter(filter::accept)
                                  .collect(Collectors.toList());
        } else {
            return playersFromTeam;
        }
    }

    public List<Offer> getOffersBuy(Team team) {
        return offers.stream()
                     .filter(offer -> offer.getToTeam() == team)
                     .collect(Collectors.toList());
    }

    public List<Offer> getOffersSale(Team team) {
        return offers.stream()
                     .filter(offer -> offer.getFromTeam() == team)
                     .collect(Collectors.toList());
    }

    public void makeOffer(Offer offer) {
        LOGGER.debug(() -> "makeOffer: " + offer);
        this.offers.add(offer);
        notifyListener(offer);
    }

    public void setMarketListener(MarketListener listener) {
        this.listener = listener;
    }

    private void notifyListener(Offer offer) {
        if (listener != null) {
            listener.onOfferAdd(offer);
        }
    }

    public void removeOffer(Offer selectedOffer) {
        this.offers.remove(selectedOffer);
    }

    public void processOffers(LocalDate date, Team myTeam) {
        LOGGER.info("Start processOffers");
        Set<Player> accepted = new HashSet<>();
        for (Iterator<Offer> iterator = offers.iterator(); iterator.hasNext(); ) {
            Offer offer = iterator.next();
            if (offer.getFromTeam() != myTeam) {
                Player player = offer.getPlayer();
                if (accepted.contains(player)) {
                    offer.decline();
                    iterator.remove();
                } else {
                    TransferResult transferResult = offer.process(date);
                    if (transferResult == TransferResult.ACCEPT) {
                        accepted.add(player);
                        if (offer.getToTeam() != myTeam) {
                            performTransfer(offer);
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }

    public void performTransfer(Offer offer) {
        if (offer.getTransferStatus() == TransferStatus.ON_TRANSFER
                || offer.getTransferStatus() == TransferStatus.ON_TRANSFER_OR_RENT
                || offer.getTransferStatus() == TransferStatus.ON_CONTRACT) {
            performTransferBuy(offer);

        } else if (offer.getTransferStatus() == TransferStatus.FREE_AGENT) {
            performTransferFreeAgent(offer);
        }

    }

    private void performTransferFreeAgent(Offer offer) {
        //свободный агент
        Player player = offer.getPlayer();
        Team toTeam = offer.getToTeam();
        toTeam.addPlayer(player);
        player.setContract(new Contract(offer.getContractDuration(), offer.getFare()));
        addPlayer(player, toTeam, TransferStatus.ON_CONTRACT);
        removeOffer(offer);
    }

    private void performTransferBuy(Offer offer) {
        Player player = offer.getPlayer();
        Team fromTeam = offer.getFromTeam();
        fromTeam.removePlayer(player);
        fromTeam.budgetOperation(offer.getSumOfTransfer(), offer.getDate(),
                "Продажа игрока " + player.getNameAbbrAndLastName());

        Team toTeam = offer.getToTeam();
        toTeam.addPlayer(player);
        toTeam.budgetOperation(-offer.getSumOfTransfer(), offer.getDate(),
                "Покупка игрока " + player.getNameAbbrAndLastName());
        player.setContract(new Contract(offer.getContractDuration(), offer.getFare()));
        removePlayer(player);
        addPlayer(player, toTeam, TransferStatus.ON_CONTRACT);
        LOGGER.info(() -> "Команда " + toTeam.getName() + " " +
                offer.getOfferType().getDescription().toLowerCase().replace("ть", "ла"
                        + " игрока " + player.getNameAbbrAndLastName() + " у команды " + fromTeam.getName()));
    }

    public void removeOldPlayers() {
        transfers.removeIf(transfer -> transfer.getPlayer().getAge() > Player.MAX_AGE);
    }

    public void addFreeAgents(List<Player> freeAgents) {
        freeAgents.forEach(player -> addPlayer(player, null, TransferStatus.FREE_AGENT));
    }

    public void addFreeAgent(Player freeAgent) {
        addPlayer(freeAgent, null, TransferStatus.FREE_AGENT);
    }
}
