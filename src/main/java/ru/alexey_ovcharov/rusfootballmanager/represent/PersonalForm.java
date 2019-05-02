package ru.alexey_ovcharov.rusfootballmanager.represent;

import javax.swing.JOptionPane;

import ru.alexey_ovcharov.rusfootballmanager.common.MoneyHelper;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Personal;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;

import java.time.LocalDate;

/**
 * @author Алексей
 */
public class PersonalForm extends javax.swing.JDialog {

    private static final String TOOLTIP_TEXT;

    static {
        int[] nextLevelSums = Personal.NEXT_LEVEL_SUMS;
        StringBuilder builder = new StringBuilder("<html>");
        for (int i = 0; i < nextLevelSums.length; i++) {
            int i1 = i + 1;
            builder.append(i1)
                   .append("->")
                   .append(i1 + 1)
                   .append(": ")
                   .append(MoneyHelper.formatSum(nextLevelSums[i]))
                   .append("<br>");
        }
        TOOLTIP_TEXT = builder.toString();
    }

    private static final String INFO = "Повышение квалификации персонала";
    private transient Team team;
    private transient LocalDate localDate;

    public PersonalForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initLocation();
    }

    public void init(Team team, LocalDate localDate) {
        this.team = team;
        this.localDate = localDate;
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
        lBudget.setText("Бюджет: " + MoneyHelper.formatSum(team.getBudget()));
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
            if (team.getBudget() > sum && team.getBudget() >= Team.STABLE_BUDGET_VALUE) {
                int result = JOptionPane.showConfirmDialog(null, "Улучшение будет стоить "
                                + sum + ". Произвести улучшение?",
                        "Подробности операции", JOptionPane.YES_NO_OPTION);
                return result == JOptionPane.YES_OPTION;
            } else {
                JOptionPane.showMessageDialog(null,
                        "В вашем бюджете недостаточно средств выполнения улучшения или для развития команды",
                        "Подробности операции",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelDoctor = new javax.swing.JLabel();
        pbDoctor = new javax.swing.JProgressBar();
        bDoctorInc = new javax.swing.JButton();
        bTrDefInc = new javax.swing.JButton();
        pbTrainerDefenders = new javax.swing.JProgressBar();
        labelDefendersTrainer = new javax.swing.JLabel();
        pbTrainerMidfielders = new javax.swing.JProgressBar();
        bTrMidInc = new javax.swing.JButton();
        labelMidfieldersTrainer = new javax.swing.JLabel();
        bForwTrInc = new javax.swing.JButton();
        labelGoalkeepersTrainer = new javax.swing.JLabel();
        pbTrainerForwards = new javax.swing.JProgressBar();
        bGkTrInc = new javax.swing.JButton();
        labelForwardsTrainer = new javax.swing.JLabel();
        pbTrainerGoalkeepers = new javax.swing.JProgressBar();
        labelPsyhologist = new javax.swing.JLabel();
        bPsyInc = new javax.swing.JButton();
        pbPsyholigist = new javax.swing.JProgressBar();
        bJunTrInc = new javax.swing.JButton();
        pbJuniorsTrainer = new javax.swing.JProgressBar();
        labelJuniorsTrainer = new javax.swing.JLabel();
        labelStadiumManager = new javax.swing.JLabel();
        pbStadiumManager = new javax.swing.JProgressBar();
        bStadManInc = new javax.swing.JButton();
        lBudget = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Управление персоналом");

        labelDoctor.setText("Врач");

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

        labelDefendersTrainer.setText("Тренер защитников");

        pbTrainerMidfielders.setMaximum(10);
        pbTrainerMidfielders.setString("0");
        pbTrainerMidfielders.setStringPainted(true);

        bTrMidInc.setText("Повысить");
        bTrMidInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTrMidIncActionPerformed(evt);
            }
        });

        labelMidfieldersTrainer.setText("Тренер полузащитников");

        bForwTrInc.setText("Повысить");
        bForwTrInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bForwTrIncActionPerformed(evt);
            }
        });

        labelGoalkeepersTrainer.setText("Тренер вратарей");

        pbTrainerForwards.setMaximum(10);
        pbTrainerForwards.setString("0");
        pbTrainerForwards.setStringPainted(true);

        bGkTrInc.setText("Повысить");
        bGkTrInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGkTrIncActionPerformed(evt);
            }
        });

        labelForwardsTrainer.setText("Тренер нападающих");

        pbTrainerGoalkeepers.setMaximum(10);
        pbTrainerGoalkeepers.setString("0");
        pbTrainerGoalkeepers.setStringPainted(true);

        labelPsyhologist.setText("Психолог");

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

        labelJuniorsTrainer.setText("Тренер молодежного состава");

        labelStadiumManager.setText("Менеджер стадиона");

        pbStadiumManager.setMaximum(10);
        pbStadiumManager.setString("0");
        pbStadiumManager.setStringPainted(true);

        bStadManInc.setText("Повысить");
        bStadManInc.addActionListener(this::bStadManIncActionPerformed);

        lBudget.setText("Бюджет");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                  .addContainerGap()
                                                                                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                  .addComponent(lBudget, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                  .addComponent(labelDefendersTrainer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                  .addComponent(labelMidfieldersTrainer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                  .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                                                                                                             .addComponent(labelPsyhologist, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                             .addGap(0, 0, Short.MAX_VALUE))
                                                                                                  .addComponent(labelGoalkeepersTrainer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                  .addComponent(labelForwardsTrainer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                  .addComponent(labelDoctor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                  .addComponent(labelJuniorsTrainer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                  .addComponent(labelStadiumManager, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE))
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
                                                      .addComponent(labelDoctor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                      .addComponent(bDoctorInc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                      .addComponent(pbDoctor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                      .addComponent(labelDefendersTrainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                      .addComponent(bTrDefInc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                      .addComponent(pbTrainerDefenders, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                      .addComponent(labelMidfieldersTrainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                      .addComponent(bTrMidInc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                      .addComponent(pbTrainerMidfielders, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                      .addComponent(bForwTrInc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                      .addComponent(pbTrainerForwards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                      .addComponent(labelForwardsTrainer, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                      .addComponent(bGkTrInc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                      .addComponent(pbTrainerGoalkeepers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                      .addComponent(labelGoalkeepersTrainer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                      .addComponent(labelPsyhologist, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                      .addComponent(bPsyInc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                      .addComponent(pbPsyholigist, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                      .addComponent(labelJuniorsTrainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                      .addComponent(bJunTrInc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                      .addComponent(pbJuniorsTrainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                      .addComponent(labelStadiumManager, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                      .addComponent(bStadManInc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                      .addComponent(pbStadiumManager, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                      .addGap(18, 18, 18)
                                      .addComponent(lBudget, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
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
        psychologistInc();
    }//GEN-LAST:event_bPsyIncActionPerformed

    private void bJunTrIncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bJunTrIncActionPerformed
        juniorTrainerInc();
    }//GEN-LAST:event_bJunTrIncActionPerformed

    private void bStadManIncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bStadManIncActionPerformed
        stadiumManagerInc();
    }//GEN-LAST:event_bStadManIncActionPerformed

    private void docInc() {
        int level = team.getPersonal().getDoctor();
        boolean confirm = increasePersonal(level);
        if (confirm) {
            team.budgetOperation(-Personal.NEXT_LEVEL_SUMS[level], localDate, INFO);
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
            team.budgetOperation(-Personal.NEXT_LEVEL_SUMS[level], localDate, INFO);
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
            team.budgetOperation(-Personal.NEXT_LEVEL_SUMS[level], localDate, INFO);
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
            team.budgetOperation(-Personal.NEXT_LEVEL_SUMS[level], localDate, INFO);
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
            team.budgetOperation(-Personal.NEXT_LEVEL_SUMS[level], localDate, INFO);
            team.getPersonal().addLevelGoalkeepersTrainer();
            if (level == 8) {
                bGkTrInc.setEnabled(false);
                bGkTrInc.setToolTipText(null);
            }
            updateInterface(team);
        }
    }

    private void psychologistInc() {
        int level = team.getPersonal().getPsychologist();
        boolean confirm = increasePersonal(level);
        if (confirm) {
            team.budgetOperation(-Personal.NEXT_LEVEL_SUMS[level], localDate, INFO);
            team.getPersonal().addLevelPsychologist();
            if (level == 8) {
                bPsyInc.setEnabled(false);
                bPsyInc.setToolTipText(null);
            }
            updateInterface(team);
        }
    }

    private void juniorTrainerInc() {
        int level = team.getPersonal().getJuniorsTrainer();
        boolean confirm = increasePersonal(level);
        if (confirm) {
            team.budgetOperation(-Personal.NEXT_LEVEL_SUMS[level], localDate, INFO);
            team.getPersonal().addLevelJuniorsTrainer();
            if (level == 8) {
                bJunTrInc.setEnabled(false);
                bJunTrInc.setToolTipText(null);
            }
            updateInterface(team);
        }
    }

    private void stadiumManagerInc() {
        int level = team.getPersonal().getStadiumManager();
        boolean confirm = increasePersonal(level);
        if (confirm) {
            team.budgetOperation(-Personal.NEXT_LEVEL_SUMS[level], localDate, INFO);
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
    private javax.swing.JLabel labelDoctor;
    private javax.swing.JLabel labelDefendersTrainer;
    private javax.swing.JLabel labelMidfieldersTrainer;
    private javax.swing.JLabel labelGoalkeepersTrainer;
    private javax.swing.JLabel labelForwardsTrainer;
    private javax.swing.JLabel labelPsyhologist;
    private javax.swing.JLabel labelJuniorsTrainer;
    private javax.swing.JLabel labelStadiumManager;
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

    private void initLocation() {
        setLocationRelativeTo(null);
    }

}
