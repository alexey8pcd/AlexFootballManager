/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.alexey_ovcharov.rusfootballmanager.represent;

/**
 *
 * @author Admin
 */
public class TrainingForm extends javax.swing.JDialog {

    /**
     * Creates new form TrainingForm
     */
    public TrainingForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelAvailableExercises = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listAvailableExercises = new javax.swing.JList<>();
        buttonSelectExercises = new javax.swing.JButton();
        buttonDiscardExercises = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        listSelectedExercises = new javax.swing.JList<>();
        labelSelectedExercises = new javax.swing.JLabel();
        labelWarning = new javax.swing.JLabel();
        buttonApply = new javax.swing.JButton();
        labelExit = new javax.swing.JButton();
        labelAvailableHours = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Провести тренировку");

        labelAvailableExercises.setText("Доступные упражнения");

        jScrollPane1.setViewportView(listAvailableExercises);

        buttonSelectExercises.setText(">");
        buttonSelectExercises.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSelectExercisesActionPerformed(evt);
            }
        });

        buttonDiscardExercises.setText("<");
        buttonDiscardExercises.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDiscardExercisesActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(listSelectedExercises);

        labelSelectedExercises.setText("Выбранные упражнения");

        labelWarning.setText("Внимание! Тренировка может причинить травмы игрокам и снизить запас сил");

        buttonApply.setText("Выполнить");
        buttonApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonApplyActionPerformed(evt);
            }
        });

        labelExit.setText("Выйти");
        labelExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labelExitActionPerformed(evt);
            }
        });

        labelAvailableHours.setText("Доступно часов: 10");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(buttonSelectExercises)
                                    .addComponent(buttonDiscardExercises)))
                            .addComponent(labelAvailableExercises))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelSelectedExercises)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(labelWarning)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonApply)
                        .addGap(18, 18, 18)
                        .addComponent(labelExit))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelAvailableHours)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelAvailableExercises)
                    .addComponent(labelSelectedExercises))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(buttonSelectExercises)
                        .addGap(20, 20, 20)
                        .addComponent(buttonDiscardExercises)
                        .addGap(0, 214, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2)))
                .addGap(18, 18, 18)
                .addComponent(labelAvailableHours)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonApply)
                    .addComponent(labelExit)
                    .addComponent(labelWarning))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonSelectExercisesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSelectExercisesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonSelectExercisesActionPerformed

    private void buttonDiscardExercisesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDiscardExercisesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonDiscardExercisesActionPerformed

    private void buttonApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonApplyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonApplyActionPerformed

    private void labelExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labelExitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_labelExitActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonApply;
    private javax.swing.JButton buttonDiscardExercises;
    private javax.swing.JButton buttonSelectExercises;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelAvailableExercises;
    private javax.swing.JLabel labelAvailableHours;
    private javax.swing.JButton labelExit;
    private javax.swing.JLabel labelSelectedExercises;
    private javax.swing.JLabel labelWarning;
    private javax.swing.JList<String> listAvailableExercises;
    private javax.swing.JList<String> listSelectedExercises;
    // End of variables declaration//GEN-END:variables
}
