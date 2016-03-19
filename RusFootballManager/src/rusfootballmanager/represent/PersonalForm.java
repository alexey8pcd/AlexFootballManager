package rusfootballmanager.represent;

import javax.swing.JOptionPane;
import rusfootballmanager.entities.team.Personal;
import rusfootballmanager.entities.team.Team;

/**
 *
 * @author Алексей
 */
public class PersonalForm extends javax.swing.JDialog {

    private Team team;
    private static final String TOOLTIP_TEXT
            = "<html>1->2:  10 000<br>"
            + "2->3:  25 000<br>"
            + "3->4:  50 000<br>"
            + "4->5:  100 000<br>"
            + "5->6:  250 000<br>"
            + "6->7:  500 000<br>"
            + "7->8:  1 000 000<br>"
            + "8->9:  2 500 000<br>"
            + "9->10:  5 000 000";

    public PersonalForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void setTeam(Team team) {
        this.team = team;
        updateInterface(team);
        bDoctorInc.setToolTipText(TOOLTIP_TEXT);
        bForwTrInc.setToolTipText(TOOLTIP_TEXT);
        bGkTrInc.setToolTipText(TOOLTIP_TEXT);
        bJunTrInc.setToolTipText(TOOLTIP_TEXT);
        bPsyInc.setToolTipText(TOOLTIP_TEXT);
        bStadManInc.setToolTipText(TOOLTIP_TEXT);
        bTrDefInc.setToolTipText(TOOLTIP_TEXT);
        bTrMidInc.setToolTipText(TOOLTIP_TEXT);
    }

    private void updateInterface(Team team) {
        lBudget.setText("Бюджет: " + team.getBudget());
        Personal personal = team.getPersonal();

        pbDoctor.setValue(personal.getDoctor() + 1);
        pbDoctor.setString(String.valueOf(pbDoctor.getValue()));

        pbJuniorsTrainer.setValue(personal.getJuniorsTrainer() + 1);
        pbJuniorsTrainer.setString(String.valueOf(pbJuniorsTrainer.getValue()));

        pbPsyholigist.setValue(personal.getPsychologist() + 1);
        pbPsyholigist.setString(String.valueOf(pbPsyholigist.getValue()));

        pbStadiumManager.setValue(personal.getStadiumManager() + 1);
        pbStadiumManager.setString(String.valueOf(pbStadiumManager.getValue()));

        pbTrainerDefenders.setValue(personal.getDefendersTrainer() + 1);
        pbTrainerDefenders.setString(String.valueOf(pbTrainerDefenders.getValue()));

        pbTrainerForwards.setValue(personal.getForwardsTrainer() + 1);
        pbTrainerForwards.setString(String.valueOf(pbTrainerForwards.getValue()));

        pbTrainerGoalkeepers.setValue(personal.getGoalkeepersTrainer() + 1);
        pbTrainerGoalkeepers.setString(String.valueOf(pbTrainerGoalkeepers.getValue()));

        pbTrainerMidfielders.setValue(personal.getMidfieldersTrainer() + 1);
        pbTrainerMidfielders.setString(String.valueOf(pbTrainerMidfielders.getValue()));
    }

    private boolean increasePersonal(int currentLevel) {
        if (currentLevel < Personal.MAX_LEVEL) {
            int sum = Personal.NEXT_LEVEL_SUMS[currentLevel];
            if (team.getBudget() > sum) {
                int result = JOptionPane.showConfirmDialog(null, "Улучшение будет стоить "
                        + sum + ". Произвести улучшение?",
                        "Подробности операции", JOptionPane.YES_NO_OPTION);
                return result == JOptionPane.YES_OPTION;
            } else {
                JOptionPane.showMessageDialog(null,
                        "В вашем бюджете недостаточно средств",
                        "Подробности операции",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        pbDoctor = new javax.swing.JProgressBar();
        bDoctorInc = new javax.swing.JButton();
        bTrDefInc = new javax.swing.JButton();
        pbTrainerDefenders = new javax.swing.JProgressBar();
        jLabel2 = new javax.swing.JLabel();
        pbTrainerMidfielders = new javax.swing.JProgressBar();
        bTrMidInc = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        bForwTrInc = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        pbTrainerForwards = new javax.swing.JProgressBar();
        bGkTrInc = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        pbTrainerGoalkeepers = new javax.swing.JProgressBar();
        jLabel6 = new javax.swing.JLabel();
        bPsyInc = new javax.swing.JButton();
        pbPsyholigist = new javax.swing.JProgressBar();
        bJunTrInc = new javax.swing.JButton();
        pbJuniorsTrainer = new javax.swing.JProgressBar();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        pbStadiumManager = new javax.swing.JProgressBar();
        bStadManInc = new javax.swing.JButton();
        lBudget = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Управление персоналом");

        jLabel1.setText("Врач");

        pbDoctor.setMaximum(10);
        pbDoctor.setString("0");
        pbDoctor.setStringPainted(true);

        bDoctorInc.setText("Повысить");
        bDoctorInc.setToolTipText("");
        bDoctorInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDoctorIncActionPerformed(evt);
            }
        });

        bTrDefInc.setText("Повысить");
        bTrDefInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTrDefIncActionPerformed(evt);
            }
        });

        pbTrainerDefenders.setMaximum(10);
        pbTrainerDefenders.setString("0");
        pbTrainerDefenders.setStringPainted(true);

        jLabel2.setText("Тренер защитников");

        pbTrainerMidfielders.setMaximum(10);
        pbTrainerMidfielders.setString("0");
        pbTrainerMidfielders.setStringPainted(true);

        bTrMidInc.setText("Повысить");
        bTrMidInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTrMidIncActionPerformed(evt);
            }
        });

        jLabel3.setText("Тренер полузащитников");

        bForwTrInc.setText("Повысить");
        bForwTrInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bForwTrIncActionPerformed(evt);
            }
        });

        jLabel4.setText("Тренер вратарей");

        pbTrainerForwards.setMaximum(10);
        pbTrainerForwards.setString("0");
        pbTrainerForwards.setStringPainted(true);

        bGkTrInc.setText("Повысить");
        bGkTrInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGkTrIncActionPerformed(evt);
            }
        });

        jLabel5.setText("Тренер нападающих");

        pbTrainerGoalkeepers.setMaximum(10);
        pbTrainerGoalkeepers.setString("0");
        pbTrainerGoalkeepers.setStringPainted(true);

        jLabel6.setText("Психолог");

        bPsyInc.setText("Повысить");
        bPsyInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPsyIncActionPerformed(evt);
            }
        });

        pbPsyholigist.setMaximum(10);
        pbPsyholigist.setString("0");
        pbPsyholigist.setStringPainted(true);

        bJunTrInc.setText("Повысить");
        bJunTrInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bJunTrIncActionPerformed(evt);
            }
        });

        pbJuniorsTrainer.setMaximum(10);
        pbJuniorsTrainer.setString("0");
        pbJuniorsTrainer.setStringPainted(true);

        jLabel7.setText("Тренер молодежного состава");

        jLabel8.setText("Менеджер стадиона");

        pbStadiumManager.setMaximum(10);
        pbStadiumManager.setString("0");
        pbStadiumManager.setStringPainted(true);

        bStadManInc.setText("Повысить");
        bStadManInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bStadManIncActionPerformed(evt);
            }
        });

        lBudget.setText("Бюджет");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lBudget, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pbPsyholigist, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(pbTrainerGoalkeepers, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(pbTrainerForwards, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(pbTrainerMidfielders, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(pbTrainerDefenders, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(pbDoctor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(pbJuniorsTrainer, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pbStadiumManager, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bDoctorInc, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bTrDefInc, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bTrMidInc, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bForwTrInc, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bGkTrInc, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bPsyInc, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bJunTrInc, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bStadManInc, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bDoctorInc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pbDoctor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bTrDefInc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pbTrainerDefenders, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bTrMidInc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pbTrainerMidfielders, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(bForwTrInc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pbTrainerForwards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bGkTrInc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pbTrainerGoalkeepers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bPsyInc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pbPsyholigist, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bJunTrInc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pbJuniorsTrainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bStadManInc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pbStadiumManager, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(lBudget, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bDoctorIncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDoctorIncActionPerformed
        docInc();
    }//GEN-LAST:event_bDoctorIncActionPerformed

    private void bTrDefIncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTrDefIncActionPerformed
        defInc();
    }//GEN-LAST:event_bTrDefIncActionPerformed

    private void bTrMidIncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTrMidIncActionPerformed
        midInc();
    }//GEN-LAST:event_bTrMidIncActionPerformed

    private void bForwTrIncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bForwTrIncActionPerformed
        frwInc();
    }//GEN-LAST:event_bForwTrIncActionPerformed

    private void bGkTrIncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGkTrIncActionPerformed
        gkInc();
    }//GEN-LAST:event_bGkTrIncActionPerformed

    private void bPsyIncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPsyIncActionPerformed
        psyInc();
    }//GEN-LAST:event_bPsyIncActionPerformed
    
    private void bJunTrIncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bJunTrIncActionPerformed
        junInc();
    }//GEN-LAST:event_bJunTrIncActionPerformed
  
    private void bStadManIncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bStadManIncActionPerformed
        stadInc();
    }//GEN-LAST:event_bStadManIncActionPerformed

    private void docInc() {
        int level = team.getPersonal().getDoctor();
        boolean confirm = increasePersonal(level);
        if (confirm) {
            team.budgetOperation(-Personal.NEXT_LEVEL_SUMS[level]);
            team.getPersonal().addLevelDoctor();
            if (level == 8) {
                bDoctorInc.setEnabled(false);
                bDoctorInc.setToolTipText(null);
            }
            updateInterface(team);
        }
    }
    
    private void defInc() {
        int level = team.getPersonal().getDefendersTrainer();
        boolean confirm = increasePersonal(level);
        if (confirm) {
            team.budgetOperation(-Personal.NEXT_LEVEL_SUMS[level]);
            team.getPersonal().addLevelDefendersTrainer();
            if (level == 8) {
                bTrDefInc.setEnabled(false);
                bTrDefInc.setToolTipText(null);
            }
            updateInterface(team);
        }
    }
    
    private void midInc() {
        int level = team.getPersonal().getMidfieldersTrainer();
        boolean confirm = increasePersonal(level);
        if (confirm) {
            team.budgetOperation(-Personal.NEXT_LEVEL_SUMS[level]);
            team.getPersonal().addLevelMidfieldersTrainer();
            if (level == 8) {
                bTrMidInc.setEnabled(false);
                bTrMidInc.setToolTipText(null);
            }
            updateInterface(team);
        }
    }
    
    private void frwInc() {
        int level = team.getPersonal().getForwardsTrainer();
        boolean confirm = increasePersonal(level);
        if (confirm) {
            team.budgetOperation(-Personal.NEXT_LEVEL_SUMS[level]);
            team.getPersonal().addLevelForwardsTrainer();
            if (level == 8) {
                bForwTrInc.setEnabled(false);
                bForwTrInc.setToolTipText(null);
            }
            updateInterface(team);
        }
    }
    
    private void gkInc() {
        int level = team.getPersonal().getGoalkeepersTrainer();
        boolean confirm = increasePersonal(level);
        if (confirm) {
            team.budgetOperation(-Personal.NEXT_LEVEL_SUMS[level]);
            team.getPersonal().addLevelGoalkeepersTrainer();
            if (level == 8) {
                bGkTrInc.setEnabled(false);
                bGkTrInc.setToolTipText(null);
            }
            updateInterface(team);
        }
    }
    
    private void psyInc() {
        int level = team.getPersonal().getPsychologist();
        boolean confirm = increasePersonal(level);
        if (confirm) {
            team.budgetOperation(-Personal.NEXT_LEVEL_SUMS[level]);
            team.getPersonal().addLevelPsychologist();
            if (level == 8) {
                bPsyInc.setEnabled(false);
                bPsyInc.setToolTipText(null);
            }
            updateInterface(team);
        }
    }
    
    private void junInc() {
        int level = team.getPersonal().getJuniorsTrainer();
        boolean confirm = increasePersonal(level);
        if (confirm) {
            team.budgetOperation(-Personal.NEXT_LEVEL_SUMS[level]);
            team.getPersonal().addLevelJuniorsTrainer();
            if (level == 8) {
                bJunTrInc.setEnabled(false);
                bJunTrInc.setToolTipText(null);
            }
            updateInterface(team);
        }
    }
    
    private void stadInc() {
        int level = team.getPersonal().getStadiumManager();
        boolean confirm = increasePersonal(level);
        if (confirm) {
            team.budgetOperation(-Personal.NEXT_LEVEL_SUMS[level]);
            team.getPersonal().addLevelStadiumManager();
            if (level == 8) {
                bStadManInc.setEnabled(false);
                bStadManInc.setToolTipText(null);
            }
            updateInterface(team);
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bDoctorInc;
    private javax.swing.JButton bForwTrInc;
    private javax.swing.JButton bGkTrInc;
    private javax.swing.JButton bJunTrInc;
    private javax.swing.JButton bPsyInc;
    private javax.swing.JButton bStadManInc;
    private javax.swing.JButton bTrDefInc;
    private javax.swing.JButton bTrMidInc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel lBudget;
    private javax.swing.JProgressBar pbDoctor;
    private javax.swing.JProgressBar pbJuniorsTrainer;
    private javax.swing.JProgressBar pbPsyholigist;
    private javax.swing.JProgressBar pbStadiumManager;
    private javax.swing.JProgressBar pbTrainerDefenders;
    private javax.swing.JProgressBar pbTrainerForwards;
    private javax.swing.JProgressBar pbTrainerGoalkeepers;
    private javax.swing.JProgressBar pbTrainerMidfielders;
    // End of variables declaration//GEN-END:variables

}
