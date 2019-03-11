package ru.alexey_ovcharov.rusfootballmanager.represent;

import ru.alexey_ovcharov.rusfootballmanager.data.Strategy;
import ru.alexey_ovcharov.rusfootballmanager.data.Tactics;
import ru.alexey_ovcharov.rusfootballmanager.data.Trick;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.GlobalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.LocalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * @author Алексей
 */
public class TeamSettingsForm extends javax.swing.JDialog {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCancel;
    private javax.swing.JButton bMoveUpPlayerFromStart;
    private javax.swing.JButton bSwapReserveAndSub;
    private javax.swing.JButton bSwapStartAndSub;
    private javax.swing.JButton bMoveUpPlayerFromSub;
    private javax.swing.JButton bSaveAndExit;
    private javax.swing.JComboBox<String> cbFreeKick;
    private javax.swing.JComboBox<String> cbLeftCorner;
    private javax.swing.JComboBox<String> cbPenalty;
    private javax.swing.JComboBox<String> cbRightCorner;
    private javax.swing.JComboBox<String> cbStrategy;
    private javax.swing.JComboBox<String> cbTactic;
    private javax.swing.JCheckBox chBreakthroughAndFeed;
    private javax.swing.JCheckBox chEarlySubstitutions;
    private javax.swing.JCheckBox chMinorFault;
    private javax.swing.JCheckBox chPowerSaving;
    private javax.swing.JCheckBox chSafety;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lFreeKick;
    private javax.swing.JLabel lLeftCorner;
    private javax.swing.JLabel lStartPlayers;
    private javax.swing.JLabel lPenalty;
    private javax.swing.JLabel lReservePlayers;
    private javax.swing.JLabel lRightCorner;
    private javax.swing.JLabel lStrategy;
    private javax.swing.JLabel lSubstitutes;
    private javax.swing.JLabel lTactic;
    private javax.swing.JList<String> listStartPlayers;
    private javax.swing.JList<String> listReservePlayers;
    private javax.swing.JList<String> listSubstitutes;
    // End of variables declaration//GEN-END:variables

    private Tactics tactics;
    private List<Player> startPlayers;
    private List<Player> substitutes;
    private List<Player> reservePlayers;
    private Team team;
    private Map<Trick, JCheckBox> trickComponents = new HashMap<>();

    /**
     * Creates new form TeamSettingsForm
     */
    public TeamSettingsForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        trickComponents.put(Trick.BREAKTHROUGH_AND_FEED, chBreakthroughAndFeed);
        trickComponents.put(Trick.EARLY_SUBSTITUTIONS, chEarlySubstitutions);
        trickComponents.put(Trick.MINOR_FAULT, chMinorFault);
        trickComponents.put(Trick.POWER_SAVING, chPowerSaving);
        trickComponents.put(Trick.SAFETY, chSafety);
    }

    public void setTeam(Team team) {
        this.team = team;
        cbTactic.setModel(new DefaultComboBoxModel<>(Arrays.stream(Tactics.values())
                                                           .map(Tactics::getShortName)
                                                           .toArray(String[]::new)));
        cbTactic.setSelectedIndex(team.getTactics().ordinal());

        cbStrategy.setModel(new DefaultComboBoxModel<>(Arrays.stream(Strategy.values())
                                                             .map(Strategy::getDescription)
                                                             .toArray(String[]::new)));
        cbStrategy.setSelectedIndex(team.getGameStrategy().ordinal());

        Set<Trick> tricks = team.getTricks();
        tricks.stream()
              .map(trick -> trickComponents.get(trick))
              .filter(Objects::nonNull)
              .forEach(checkBox -> checkBox.setSelected(true));

        startPlayers = new ArrayList<>(team.getStartPlayers());
        listStartPlayers.setModel(new PlayersListModel(startPlayers) {
            @Override
            public String getElementAt(int index) {
                LocalPosition localPosition = tactics.getPositions().get(index);
                String localPositionAbreviation = localPosition.getAbreviation();
                Player player = this.players.get(index);
                LocalPosition preferredPosition = player.getPreferredPosition();
                String preferredPositionAbreviation = preferredPosition.getAbreviation();
                return player.getNameAbbrAndLastName() + " " + localPositionAbreviation
                        + " [" + player.getAverageOnPosition(localPosition) + "]" +
                        " / " + preferredPositionAbreviation
                        + " [" + player.getAverage() + "]"
                        + "  " + getStrength(player.getStrengthReserve());
            }
        });
        listStartPlayers.setCellRenderer(new PlayerPositionColorRenderer(startPlayers));

        substitutes = new ArrayList<>(team.getSubstitutes());
        listSubstitutes.setModel(new PlayersListModel(substitutes));
        listSubstitutes.setCellRenderer(new PlayerPositionColorRenderer(substitutes));

        reservePlayers = new ArrayList<>(team.getReserve());
        listReservePlayers.setModel(new PlayersListModel(reservePlayers));
        listReservePlayers.setCellRenderer(new PlayerPositionColorRenderer(reservePlayers));

        updateComboBoxes();
        cbPenalty.setSelectedIndex(startPlayers.indexOf(team.getPlayerPenaltyScore()));
        cbFreeKick.setSelectedIndex(startPlayers.indexOf(team.getPlayerFreeKickScore()));
        cbLeftCorner.setSelectedIndex(startPlayers.indexOf(team.getPlayerLeftCorner()));
        cbRightCorner.setSelectedIndex(startPlayers.indexOf(team.getPlayerRightCorner()));

        updateTactics();
        updateLists();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        listStartPlayers = new javax.swing.JList<>();
        lStartPlayers = new javax.swing.JLabel();
        lSubstitutes = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listSubstitutes = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        listReservePlayers = new javax.swing.JList<>();
        lReservePlayers = new javax.swing.JLabel();
        cbTactic = new javax.swing.JComboBox<>();
        lTactic = new javax.swing.JLabel();
        lStrategy = new javax.swing.JLabel();
        cbStrategy = new javax.swing.JComboBox<>();
        chMinorFault = new javax.swing.JCheckBox();
        chSafety = new javax.swing.JCheckBox();
        chPowerSaving = new javax.swing.JCheckBox();
        chEarlySubstitutions = new javax.swing.JCheckBox();
        chBreakthroughAndFeed = new javax.swing.JCheckBox();
        cbPenalty = new javax.swing.JComboBox<>();
        lPenalty = new javax.swing.JLabel();
        lFreeKick = new javax.swing.JLabel();
        cbFreeKick = new javax.swing.JComboBox<>();
        lRightCorner = new javax.swing.JLabel();
        cbRightCorner = new javax.swing.JComboBox<>();
        lLeftCorner = new javax.swing.JLabel();
        cbLeftCorner = new javax.swing.JComboBox<>();
        bSaveAndExit = new javax.swing.JButton();
        bSwapStartAndSub = new javax.swing.JButton();
        bMoveUpPlayerFromSub = new javax.swing.JButton();
        bMoveUpPlayerFromStart = new javax.swing.JButton();
        bSwapReserveAndSub = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();
        setTitle("Руководство командой");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        listStartPlayers.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listStartPlayers.setToolTipText("Нажмите дважды для просмотра информации об игроке");
        listStartPlayers.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                listStartPlayersMousePressed(e);
            }
        });
        jScrollPane1.setViewportView(listStartPlayers);
        lStartPlayers.setText("Основной состав");
        lSubstitutes.setText("Запасные");

        listSubstitutes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listSubstitutes.setToolTipText("Нажмите дважды для просмотра информации об игроке");
        listSubstitutes.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                listSubstitutesMousePressed(e);
            }
        });
        jScrollPane2.setViewportView(listSubstitutes);

        listReservePlayers.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listReservePlayers.setToolTipText("Нажмите дважды для просмотра информации об игроке");
        listReservePlayers.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                listReservePlayersMousePressed(e);
            }
        });
        jScrollPane3.setViewportView(listReservePlayers);

        lReservePlayers.setText("Резерв");

        cbTactic.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
                "4-4-2", "4-2-2-2", "4-1-4-1", "4-1-2-1-2", "4-3-3", "4-5-1", "3-5-2", "3-4-3", "3-2-3-2", "5-4-1",
                "5-3-2", "5-1-3-1"}));
        cbTactic.addActionListener(this::cbTacticActionPerformed);

        lTactic.setText("Тактика");

        lStrategy.setText("Стратегия");

        cbStrategy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
                "Плотная оборона", "Оборона", "Баланс", "Атака", "Рискованная атака"}));

        chMinorFault.setText("Мелкий фол");
        chMinorFault.setToolTipText("Мелкий фол может помочь против техничных команд");

        chSafety.setText("Подстраховка");
        chSafety.setToolTipText("Подстраховка снижает шанс пропустить, но увеличивает расход сил");

        chPowerSaving.setText("Экономия сил");
        chPowerSaving.setToolTipText("Экономит силы за счет меньшего подключения к атакам защитников, " +
                "меньше шанс забить гол");

        chEarlySubstitutions.setText("Ранние замены");
        chEarlySubstitutions.setToolTipText("Позволяют балансировать силы, но могут снизить сыгранность и " +
                "затрудняют маневры");

        chBreakthroughAndFeed.setText("Прорыв и подача");
        chBreakthroughAndFeed.setToolTipText("Заставляет крайних защитников и полузащитников предпочитать навес " +
                "попыткам прорыва");

        lPenalty.setText("Бьет пенальти");

        lFreeKick.setText("Бьет штрафной удар");

        lRightCorner.setText("Правый угловой");

        lLeftCorner.setText("Левый угловой");

        bSaveAndExit.setText("Сохранить и выйти");
        bSaveAndExit.addActionListener(this::bSaveAndExitActionPerformed);

        bMoveUpPlayerFromStart.setText("^");
        bMoveUpPlayerFromStart.addActionListener(this::bMoveUpPlayerFromStartActionPerformed);

        bSwapStartAndSub.setText("<>");
        bSwapStartAndSub.addActionListener(this::bSwapStartAndSubActionPerformed);

        bMoveUpPlayerFromSub.setText("^");
        bMoveUpPlayerFromSub.addActionListener(this::bMoveUpPlayerFromSubActionPerformed);

        bSwapReserveAndSub.setText("<>");
        bSwapReserveAndSub.addActionListener(this::bSwapReserveAndSubActionPerformed);


        bCancel.setText("Отмена");
        bCancel.addActionListener(this::bCancelActionPerformed);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                      .add(layout.createSequentialGroup()
                                 .addContainerGap()
                                 .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(layout.createSequentialGroup()
                                                       .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                  .add(lStartPlayers)
                                                                  .add(lTactic)
                                                                  .add(lStrategy)
                                                                  .add(layout.createSequentialGroup()
                                                                             .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, cbStrategy, 0, 230, Short.MAX_VALUE)
                                                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, cbTactic, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                                                             .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                                             .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                                        .add(bMoveUpPlayerFromStart)
                                                                                        .add(bSwapStartAndSub))))
                                                       .add(18, 18, 18)
                                                       .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                  .add(layout.createSequentialGroup()
                                                                             .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                                                                        .add(layout.createSequentialGroup()
                                                                                                   .add(lRightCorner)
                                                                                                   .add(142, 142, 142))
                                                                                        .add(cbPenalty, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 225, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                        .add(cbRightCorner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 225, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, lSubstitutes)
                                                                                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 225, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                                             .add(10, 10, 10)
                                                                             .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                                        .add(bMoveUpPlayerFromSub)
                                                                                        .add(bSwapReserveAndSub)))
                                                                  .add(lPenalty))
                                                       .add(18, 18, 18)
                                                       .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                  .add(lReservePlayers)
                                                                  .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 225, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                  .add(cbFreeKick, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 225, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                  .add(lFreeKick)
                                                                  .add(lLeftCorner)
                                                                  .add(cbLeftCorner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 225, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                  .add(layout.createSequentialGroup()
                                                                             .add(bCancel)
                                                                             .add(18, 18, 18)
                                                                             .add(bSaveAndExit))))
                                            .add(layout.createSequentialGroup()
                                                       .add(chMinorFault)
                                                       .add(18, 18, 18)
                                                       .add(chSafety)
                                                       .add(18, 18, 18)
                                                       .add(chPowerSaving)
                                                       .add(18, 18, 18)
                                                       .add(chEarlySubstitutions)
                                                       .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                       .add(chBreakthroughAndFeed)))
                                 .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                      .add(layout.createSequentialGroup()
                                 .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(layout.createSequentialGroup()
                                                       .add(15, 15, 15)
                                                       .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                                                  .add(lStartPlayers)
                                                                  .add(lSubstitutes)))
                                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                                                                                 .addContainerGap()
                                                                                                 .add(lReservePlayers)))
                                 .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(layout.createSequentialGroup()
                                                       .add(18, 18, 18)
                                                       .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 325, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                            .add(layout.createSequentialGroup()
                                                       .add(68, 68, 68)
                                                       .add(bMoveUpPlayerFromSub)
                                                       .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                       .add(bSwapReserveAndSub))
                                            .add(layout.createSequentialGroup()
                                                       .add(66, 66, 66)
                                                       .add(bMoveUpPlayerFromStart)
                                                       .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                       .add(bSwapStartAndSub))
                                            .add(layout.createSequentialGroup()
                                                       .add(18, 18, 18)
                                                       .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                  .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 325, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                  .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 325, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                                 .add(11, 11, 11)
                                 .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(lTactic)
                                            .add(lPenalty)
                                            .add(lFreeKick))
                                 .add(8, 8, 8)
                                 .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(cbTactic, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(cbPenalty, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(cbFreeKick, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                 .add(18, 18, 18)
                                 .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(lStrategy)
                                            .add(lRightCorner)
                                            .add(lLeftCorner))
                                 .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                 .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(cbStrategy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(cbRightCorner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(cbLeftCorner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                 .add(26, 26, 26)
                                 .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                            .add(chMinorFault)
                                            .add(chSafety)
                                            .add(chPowerSaving)
                                            .add(chEarlySubstitutions)
                                            .add(chBreakthroughAndFeed)
                                            .add(bCancel)
                                            .add(bSaveAndExit))
                                 .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void listSubstitutesMousePressed(MouseEvent e) {
        aboutPlayer(e, listSubstitutes, substitutes);
    }

    private void listStartPlayersMousePressed(MouseEvent e) {
        aboutPlayer(e, listStartPlayers, startPlayers);
    }

    private void listReservePlayersMousePressed(MouseEvent e) {
        aboutPlayer(e, listReservePlayers, reservePlayers);
    }

    private void aboutPlayer(MouseEvent e, JList<String> list, List<Player> players) {
        if (e.getClickCount() == 2) {
            int selectedIndex = list.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < players.size()) {
                Player player = players.get(selectedIndex);
                AboutPlayerForm aboutPlayerForm = new AboutPlayerForm(null, true);
                aboutPlayerForm.setPlayer(player);
                aboutPlayerForm.setVisible(true);
            }
        }
    }

    private void updateLists() {
        listStartPlayers.updateUI();
        listSubstitutes.updateUI();
        listReservePlayers.updateUI();
    }

    private void cbTacticActionPerformed(ActionEvent evt) {
        updateTactics();
    }

    private void updateTactics() {
        int selectedIndex = cbTactic.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < Tactics.values().length) {
            this.tactics = Tactics.values()[selectedIndex];
            updateAverage();
            listStartPlayers.updateUI();
        }
    }

    private void bSwapStartAndSubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFromSubToMainActionPerformed
        swapPlayerFromSubToStart();
    }//GEN-LAST:event_bFromSubToMainActionPerformed

    private void bMoveUpPlayerFromStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFromSubToReserveActionPerformed
        moveUpPlayerFromStart();
    }//GEN-LAST:event_bFromSubToReserveActionPerformed

    private void moveUpPlayerFromStart() {
        int startPlayerIndex = listStartPlayers.getSelectedIndex();
        if (startPlayerIndex > 0 && startPlayerIndex < startPlayers.size()) {
            Player player = startPlayers.remove(startPlayerIndex);
            startPlayers.add(startPlayerIndex - 1, player);
            listStartPlayers.setSelectedIndex(startPlayerIndex - 1);
            listStartPlayers.updateUI();
            updateAverage();
        }
    }

    private void swapPlayerFromSubToStart() {
        int startPlayerIndex = listStartPlayers.getSelectedIndex();
        if (startPlayerIndex >= 0 && startPlayerIndex < startPlayers.size()) {
            int subPlayerIndex = listSubstitutes.getSelectedIndex();
            if (subPlayerIndex >= 0 && subPlayerIndex < substitutes.size()) {
                Player fromStart = startPlayers.remove(startPlayerIndex);
                Player fromSub = substitutes.remove(subPlayerIndex);
                startPlayers.add(startPlayerIndex, fromSub);
                substitutes.add(subPlayerIndex, fromStart);
                listStartPlayers.updateUI();
                listSubstitutes.updateUI();
                updateComboBoxes();
                updateAverage();
            }
        }
    }

    private void updateComboBoxes() {
        String[] startPlayerNames = startPlayers.stream()
                                                .map(Player::getNameAbbrAndLastName)
                                                .toArray(String[]::new);
        cbPenalty.setModel(new DefaultComboBoxModel<>(startPlayerNames));
        cbFreeKick.setModel(new DefaultComboBoxModel<>(startPlayerNames));
        cbLeftCorner.setModel(new DefaultComboBoxModel<>(startPlayerNames));
        cbRightCorner.setModel(new DefaultComboBoxModel<>(startPlayerNames));

        cbPenalty.updateUI();
        cbFreeKick.updateUI();
        cbLeftCorner.updateUI();
        cbRightCorner.updateUI();
    }

    private void updateAverage() {
        if (startPlayers != null) {
            List<LocalPosition> positions = tactics.getPositions();
            int sum = 0;
            for (int i = 0; i < positions.size(); i++) {
                LocalPosition localPosition = positions.get(i);
                Player player = startPlayers.get(i);
                int averageOnPosition = player.getAverageOnPosition(localPosition);
                sum += averageOnPosition;
            }
            int average = Math.round((float) sum / positions.size());
            lStartPlayers.setText("Основной состав, среднее: " + average);
        }
    }

    private void bMoveUpPlayerFromSubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFromMainToSubActionPerformed
        moveUpPlayerFromSub();
    }//GEN-LAST:event_bFromMainToSubActionPerformed

    private void moveUpPlayerFromSub() {
        int subPlayerIndex = listSubstitutes.getSelectedIndex();
        if (subPlayerIndex > 0 && subPlayerIndex < substitutes.size()) {
            Player player = substitutes.remove(subPlayerIndex);
            substitutes.add(subPlayerIndex - 1, player);
            listSubstitutes.setSelectedIndex(subPlayerIndex - 1);
            listSubstitutes.updateUI();
        }
    }

    private void bSwapReserveAndSubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFromReserveToSubActionPerformed
        swapPlayerFromSubToReserve();
    }//GEN-LAST:event_bFromReserveToSubActionPerformed

    private void swapPlayerFromSubToReserve() {
        int reservePlayerIndex = listReservePlayers.getSelectedIndex();
        if (reservePlayerIndex >= 0 && reservePlayerIndex < reservePlayers.size()) {
            int subPlayerIndex = listSubstitutes.getSelectedIndex();
            if (subPlayerIndex >= 0 && subPlayerIndex < substitutes.size()) {
                Player fromStart = reservePlayers.remove(reservePlayerIndex);
                Player fromSub = substitutes.remove(subPlayerIndex);
                reservePlayers.add(reservePlayerIndex, fromSub);
                substitutes.add(subPlayerIndex, fromStart);
                listReservePlayers.updateUI();
                listSubstitutes.updateUI();
            }
        }
    }

    private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelActionPerformed
        dispose();
    }//GEN-LAST:event_bCancelActionPerformed

    private void bSaveAndExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSaveAndExitActionPerformed
        save();
        dispose();
    }//GEN-LAST:event_bSaveAndExitActionPerformed

    private void save() {
        team.setStartPlayers(startPlayers);
        team.setSubstitutes(substitutes);
        team.setReserve(reservePlayers);

        team.setPlayerPenaltyScore(startPlayers.get(cbPenalty.getSelectedIndex()));
        team.setPlayerFreeKickScore(startPlayers.get(cbFreeKick.getSelectedIndex()));
        team.setPlayerLeftCorner(startPlayers.get(cbLeftCorner.getSelectedIndex()));
        team.setPlayerRightCorner(startPlayers.get(cbRightCorner.getSelectedIndex()));

        Strategy strategy = Strategy.values()[cbStrategy.getSelectedIndex()];
        team.setGameStrategy(strategy);

        Tactics tactics = Tactics.values()[cbTactic.getSelectedIndex()];
        team.setTactics(tactics);

        Set<Trick> tricks = EnumSet.noneOf(Trick.class);
        for (Map.Entry<Trick, JCheckBox> entry : trickComponents.entrySet()) {
            JCheckBox checkBox = entry.getValue();
            if (checkBox.isSelected()) {
                tricks.add(entry.getKey());
            }
        }
        team.setTricks(tricks);
    }

    private static String getStrength(int strengthReserve) {
        int value = strengthReserve / 20 + 1;
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        while (index++ < value) {
            stringBuilder.append('+');
        }
        while (index++ < 6) {
            stringBuilder.append('-');
        }
        return stringBuilder.toString();
    }

    private static class PlayersListModel extends AbstractListModel<String> {
        final transient List<Player> players;

        PlayersListModel(List<Player> players) {
            this.players = players;
        }

        @Override
        public int getSize() {
            return players.size();
        }

        @Override
        public String getElementAt(int index) {
            Player player = players.get(index);
            String preferredPositionAbreviation = player.getPreferredPosition().getAbreviation();
            return player.getNameAbbrAndLastName() + " " + preferredPositionAbreviation
                    + " [" + player.getAverage() + "]  " + getStrength(player.getStrengthReserve());
        }
    }

    private static class PlayerPositionColorRenderer extends DefaultListCellRenderer {
        private final List<Player> players;

        PlayerPositionColorRenderer(List<Player> players) {
            this.players = players;
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            if (index >= 0 && index < players.size()) {
                Player player = players.get(index);
                LocalPosition preferredPosition = player.getPreferredPosition();
                GlobalPosition positionOnField = preferredPosition.getPositionOnField();
                Color color = positionOnField.getColor();
                component.setBackground(color);
            }
            if (isSelected) {
                component.setBackground(Color.BLUE);
                component.setForeground(Color.WHITE);
            }
            return component;
        }
    }
}
