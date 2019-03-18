/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.alexey_ovcharov.rusfootballmanager.represent;

import ru.alexey_ovcharov.rusfootballmanager.entities.match.Event;
import ru.alexey_ovcharov.rusfootballmanager.entities.match.Match;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.*;
import ru.alexey_ovcharov.rusfootballmanager.simulation.Simulator;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Admin
 */
public class StartMatchForm extends javax.swing.JDialog {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCalculateResult;
    private javax.swing.JButton buttonExit;
    private javax.swing.JButton buttonSimulate;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelGuestTeam;
    private javax.swing.JLabel labelGuestTeamBestPlayer;
    private javax.swing.JLabel labelGuestTeamForm;
    private javax.swing.JLabel labelGuestTeamName;
    private javax.swing.JLabel labelGuestTeamTournamentPlace;
    private javax.swing.JLabel labelHostTeam;
    private javax.swing.JLabel labelHostTeamBestPlayer;
    private javax.swing.JLabel labelHostTeamForm;
    private javax.swing.JLabel labelHostTeamName;
    private javax.swing.JLabel labelHostTeamTournamentPlace;
    private javax.swing.JLabel labelMatchDay;
    private javax.swing.JLabel labelMatchResult;
    private javax.swing.JList<String> listMatchEvents;
    private javax.swing.JProgressBar progressBarGuestTeamLevel;
    private javax.swing.JProgressBar progressBarHostTeamLevel;
    // End of variables declaration//GEN-END:variables
    private Opponents opponents;
    private LocalDate matchDay;
    private Match match;

    /**
     * Creates new form StartMatchForm
     */
    public StartMatchForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void setData(Opponents opponents, LocalDate matchDay, Tournament tournament) {
        init(opponents, matchDay, tournament);
    }

    private void init(Opponents opponents, LocalDate matchDay, Tournament tournament) {
        this.matchDay = matchDay;
        this.opponents = opponents;
        jScrollPane1.setVisible(false);
        listMatchEvents.setVisible(false);

        League league = tournament.getLeague();
        labelMatchDay.setText(league.getName() + " " + matchDay.format(DateTimeFormatter.ISO_DATE));
        Team host = opponents.getHost();
        Team guest = opponents.getGuest();

        labelHostTeamName.setText(host.getName());
        labelGuestTeamName.setText(guest.getName());
        Player hostBestStartPlayer = host.getBestStartPlayer();
        labelHostTeamBestPlayer.setText("Лучший игрок: " + hostBestStartPlayer.getNameAbbrAndLastName()
                + "[" + hostBestStartPlayer.getAverage() + "]");
        Player guestBestStartPlayer = guest.getBestStartPlayer();
        labelGuestTeamBestPlayer.setText("Лучший игрок: " + guestBestStartPlayer.getNameAbbrAndLastName()
                + "[" + guestBestStartPlayer.getAverage() + "]");

        progressBarHostTeamLevel.setValue(host.getAverage());
        progressBarHostTeamLevel.setString(String.valueOf(host.getAverage()));

        progressBarGuestTeamLevel.setValue(guest.getAverage());
        progressBarGuestTeamLevel.setString(String.valueOf(guest.getAverage()));

        Table tournamentTable = tournament.getTournamentTable();

        List<GameResult> hostResultList = tournamentTable.getLastResults(host, 5);
        labelHostTeamForm.setText("Форма: " + hostResultList.stream()
                                                            .map(GameResult::getShortName)
                                                            .collect(Collectors.joining(" ")));

        List<GameResult> guestResultList = tournamentTable.getLastResults(guest, 5);
        labelGuestTeamForm.setText("Форма: " + guestResultList.stream()
                                                              .map(GameResult::getShortName)
                                                              .collect(Collectors.joining(" ")));

        PlaceInfo placeInfoOfHostTeam = tournamentTable.getPlaceInfoOfTeam(host);
        int placeNumberOfHostTeam = tournamentTable.getPlaceNumberOfTeam(host);
        int hostTeamPointsCount = placeInfoOfHostTeam.getPointsCount();
        labelHostTeamTournamentPlace.setText(
                "Место: " + placeNumberOfHostTeam + " (" + hostTeamPointsCount + " очков)");

        PlaceInfo placeInfoOfGuestTeam = tournamentTable.getPlaceInfoOfTeam(guest);
        int placeNumberOfGuestTeam = tournamentTable.getPlaceNumberOfTeam(guest);
        int guestTeamPointsCount = placeInfoOfGuestTeam.getPointsCount();
        labelGuestTeamTournamentPlace.setText(
                "Место: " + placeNumberOfGuestTeam + " (" + guestTeamPointsCount + " очков)");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelMatchDay = new javax.swing.JLabel();
        labelHostTeam = new javax.swing.JLabel();
        labelHostTeamName = new javax.swing.JLabel();
        progressBarHostTeamLevel = new javax.swing.JProgressBar();
        labelGuestTeam = new javax.swing.JLabel();
        labelGuestTeamName = new javax.swing.JLabel();
        progressBarGuestTeamLevel = new javax.swing.JProgressBar();
        labelHostTeamForm = new javax.swing.JLabel();
        labelGuestTeamForm = new javax.swing.JLabel();
        labelHostTeamTournamentPlace = new javax.swing.JLabel();
        labelGuestTeamTournamentPlace = new javax.swing.JLabel();
        labelHostTeamBestPlayer = new javax.swing.JLabel();
        labelGuestTeamBestPlayer = new javax.swing.JLabel();
        buttonSimulate = new javax.swing.JButton();
        buttonCalculateResult = new javax.swing.JButton();
        buttonExit = new javax.swing.JButton();
        labelMatchResult = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listMatchEvents = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Начать матч");

        labelMatchDay.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelMatchDay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMatchDay.setText("Низшая лига, 01.08.2016");

        labelHostTeam.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelHostTeam.setText("Хозяева");

        labelHostTeamName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelHostTeamName.setText("Мир(Москва)");

        progressBarHostTeamLevel.setString("0");
        progressBarHostTeamLevel.setStringPainted(true);

        labelGuestTeam.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelGuestTeam.setText("Гости");

        labelGuestTeamName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelGuestTeamName.setText("Витязь(Томск)");

        progressBarGuestTeamLevel.setString("0");
        progressBarGuestTeamLevel.setStringPainted(true);

        labelHostTeamForm.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelHostTeamForm.setText("<html>Форма: <font color = \"green\">В В В</font>");

        labelGuestTeamForm.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelGuestTeamForm.setText("<html>Форма: <font color = \"green\">В В В</font>");

        labelHostTeamTournamentPlace.setText("Место: 3 (25 очков, 13 игр)");

        labelGuestTeamTournamentPlace.setText("Место: 7 (19 очков, 13 игр)");

        labelHostTeamBestPlayer.setText("Лучший игрок: В. Веселов");

        labelGuestTeamBestPlayer.setText("Лучший игрок: А. Станицкий");

        buttonSimulate.setText("Симуляция");
        buttonSimulate.setEnabled(false);
        buttonSimulate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSimulateActionPerformed(evt);
            }
        });

        buttonCalculateResult.setText("Расчет итогов");
        buttonCalculateResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCalculateResultActionPerformed(evt);
            }
        });

        buttonExit.setText("Выйти");
        buttonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExitActionPerformed(evt);
            }
        });

        labelMatchResult.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMatchResult.setText("Результат");

        jScrollPane1.setViewportView(listMatchEvents);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addGroup(layout.createSequentialGroup()
                                      .addContainerGap()
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                      .addComponent(labelMatchDay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                      .addGroup(layout.createSequentialGroup()
                                                                      .addComponent(labelHostTeam)
                                                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                      .addComponent(labelGuestTeam))
                                                      .addGroup(layout.createSequentialGroup()
                                                                      .addComponent(labelHostTeamName)
                                                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                      .addComponent(labelGuestTeamName))
                                                      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                                                  .addComponent(buttonCalculateResult)
                                                                                                                  .addGap(18, 18, 18)
                                                                                                                  .addComponent(buttonSimulate)
                                                                                                                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                                  .addComponent(buttonExit))
                                                      .addGroup(layout.createSequentialGroup()
                                                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                      .addComponent(progressBarHostTeamLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                      .addComponent(labelHostTeamForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                      .addComponent(labelHostTeamTournamentPlace)
                                                                                      .addComponent(labelHostTeamBestPlayer))
                                                                      .addGap(18, 18, 18)
                                                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                      .addGroup(layout.createSequentialGroup()
                                                                                                      .addComponent(labelMatchResult, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                      .addGap(18, 18, 18)
                                                                                                      .addComponent(progressBarGuestTeamLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                                                                                  .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                                                                                                                                                  .addGap(18, 18, 18)
                                                                                                                                                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                                  .addComponent(labelGuestTeamTournamentPlace, javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                                                                  .addComponent(labelGuestTeamBestPlayer, javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                                                                  .addComponent(labelGuestTeamForm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                      .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addGroup(layout.createSequentialGroup()
                                      .addContainerGap()
                                      .addComponent(labelMatchDay)
                                      .addGap(18, 18, 18)
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                      .addComponent(labelHostTeam)
                                                      .addComponent(labelGuestTeam))
                                      .addGap(18, 18, 18)
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                      .addComponent(labelHostTeamName)
                                                      .addComponent(labelGuestTeamName))
                                      .addGap(18, 18, 18)
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                      .addComponent(progressBarHostTeamLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                      .addComponent(progressBarGuestTeamLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                      .addComponent(labelMatchResult))
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                      .addGroup(layout.createSequentialGroup()
                                                                      .addGap(209, 209, 209)
                                                                      .addComponent(labelHostTeamForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                      .addGap(18, 18, 18)
                                                                      .addComponent(labelHostTeamTournamentPlace)
                                                                      .addGap(18, 18, 18)
                                                                      .addComponent(labelHostTeamBestPlayer))
                                                      .addGroup(layout.createSequentialGroup()
                                                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                      .addGroup(layout.createSequentialGroup()
                                                                                                      .addComponent(labelGuestTeamForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                      .addGap(18, 18, 18)
                                                                                                      .addComponent(labelGuestTeamTournamentPlace))
                                                                                      .addGroup(layout.createSequentialGroup()
                                                                                                      .addGap(18, 18, 18)
                                                                                                      .addComponent(jScrollPane1)))
                                                                      .addGap(18, 18, 18)
                                                                      .addComponent(labelGuestTeamBestPlayer)))
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                      .addComponent(buttonExit)
                                                      .addComponent(buttonCalculateResult)
                                                      .addComponent(buttonSimulate))
                                      .addContainerGap())
        );

        labelGuestTeam.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonSimulateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSimulateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonSimulateActionPerformed

    private void buttonCalculateResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCalculateResultActionPerformed
        calculateResult();

    }//GEN-LAST:event_buttonCalculateResultActionPerformed

    private void calculateResult() {
        Team host = opponents.getHost();
        Team guest = opponents.getGuest();
        match = Simulator.simulate(host, guest, matchDay);
        String result = match.getResult();
        List<Event> events = match.getEvents();
        labelMatchResult.setText(result);
        listMatchEvents.setModel(new AbstractListModel<String>() {
            @Override
            public int getSize() {
                return events.size();
            }

            @Override
            public String getElementAt(int index) {
                return events.get(index).toString();
            }
        });
        jScrollPane1.setVisible(true);
        listMatchEvents.setVisible(true);
        listMatchEvents.updateUI();
        buttonCalculateResult.setEnabled(false);
    }

    public Match getMatchResult() {
        return match;
    }

    private void buttonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExitActionPerformed
        dispose();
    }//GEN-LAST:event_buttonExitActionPerformed

}
