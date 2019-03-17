package ru.alexey_ovcharov.rusfootballmanager.represent;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.League;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;

/**
 *
 * @author Алексей
 */
public class StartCareerForm extends javax.swing.JDialog {

    private List<League> leagues;
    private List<Team> teamsInLeague;
    private League choosenLeague;
    private Team choosenTeam;
    

    public StartCareerForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        teamsInLeague = new ArrayList<>();
        listTeams.setModel(new AbstractListModel<String>() {
            @Override
            public int getSize() {
                return teamsInLeague.size();
            }

            @Override
            public String getElementAt(int index) {
                return teamsInLeague.get(index).getName();
            }
        });
        initLocation();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        listLeagues = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listTeams = new javax.swing.JList<>();
        progressBarTeamLevel = new javax.swing.JProgressBar();
        jLabel3 = new javax.swing.JLabel();
        lBudget = new javax.swing.JLabel();
        bNext = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Выбор команды");
        setResizable(false);

        listLeagues.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listLeaguesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listLeagues);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Лига");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Команда");

        listTeams.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listTeamsValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(listTeams);

        progressBarTeamLevel.setString("0");
        progressBarTeamLevel.setStringPainted(true);

        jLabel3.setText("Уровень игры команды");

        lBudget.setText("Бюджет: 1 000 000");

        bNext.setText("Далее");
        bNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bNextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bNext))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(363, 363, 363)
                        .addComponent(progressBarTeamLevel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(158, 158, 158))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(318, 318, 318)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lBudget)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progressBarTeamLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bNext)
                    .addComponent(lBudget))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void listLeaguesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listLeaguesValueChanged
        int index = listLeagues.getSelectedIndex();
        if (index >= 0 && index < leagues.size()) {
            League league = leagues.get(index);
            choosenLeague = league;
            teamsInLeague = league.getTeams();
            listTeams.updateUI();
            listTeams.setSelectedIndex(0);
            listTeamsValueChanged(null);
        }
    }//GEN-LAST:event_listLeaguesValueChanged

    private void bNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNextActionPerformed
        dispose();
    }//GEN-LAST:event_bNextActionPerformed

    private void listTeamsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listTeamsValueChanged
        int index = listTeams.getSelectedIndex();
        if (index >= 0 && index < teamsInLeague.size()) {
            Team team = teamsInLeague.get(index);
            choosenTeam = team;
            int avg = team.getAverage();
            progressBarTeamLevel.setValue(avg);
            progressBarTeamLevel.setString(String.valueOf(avg));
            lBudget.setText("Бюджет: " + team.getBudget());
        }
    }//GEN-LAST:event_listTeamsValueChanged

    public void setData(List<League> leagues) {
        this.leagues = leagues;
        listLeagues.setModel(new AbstractListModel<String>() {
            @Override
            public int getSize() {
                return leagues.size();
            }

            @Override
            public String getElementAt(int index) {
                return leagues.get(index).getName();
            }
        });
        listLeagues.updateUI();
        listLeagues.setSelectedIndex(0);
    }

    public Team getChoosenTeam() {
        return choosenTeam;
    }

    public League getChoosenLeague() {
        return choosenLeague;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bNext;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lBudget;
    private javax.swing.JList<String> listLeagues;
    private javax.swing.JList<String> listTeams;
    private javax.swing.JProgressBar progressBarTeamLevel;
    // End of variables declaration//GEN-END:variables

    private void initLocation() {
        setLocationRelativeTo(null);
    }

}
