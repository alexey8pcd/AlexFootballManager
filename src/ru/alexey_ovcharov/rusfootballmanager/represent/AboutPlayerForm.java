package ru.alexey_ovcharov.rusfootballmanager.represent;

import java.util.List;
import javafx.util.Pair;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.school.PlayerCreator;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.ProgressParameters;

/**
 *
 * @author Алексей
 */
public class AboutPlayerForm extends javax.swing.JDialog {

    private Player player;

    public AboutPlayerForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initLocation();
    }

    public void setPlayer(Player player) {
        this.player = player;
        StringBuilder builder = new StringBuilder();
        builder.append("---------- Общее ----------\n");
        builder.append("Имя: ").append(player.getName()).append("\n");
        builder.append("Фамилия: ").append(player.getLastName()).append("\n");
        int age = player.getAge();
        int avg = player.getAverage();
        builder.append("Возраст: ").append(age).append("\n");
        builder.append("Общее: ").append(avg).append("\n");
        builder.append("Позиция: ").append(player.getPreferredPosition().getAbreviation()).append("\n");
        builder.append("Контракт: ").append(player.getContract()).append("\n");
        builder.append("---------- Основные навыки ---------\n");
        List<Pair<String, Integer>> primaryChars = player.getPrimaryChars();
        for (Pair<String, Integer> primaryChar : primaryChars) {
            builder.append(primaryChar.getKey()).append(": ").append(primaryChar.getValue()).append("\n");
        }
        builder.append("---------- Второстепенные навыки ---------\n");
        List<Pair<String, Integer>> secondaryChars = player.getSecondaryChars();
        for (Pair<String, Integer> primaryChar : secondaryChars) {
            builder.append(primaryChar.getKey()).append(": ").append(primaryChar.getValue()).append("\n");
        }
        if (age == Player.MAX_AGE) {
            builder.append("Играет последний сезон");
        } else {
            builder.append("---------- Ожидаемый прогресс ----------\n");
            int limit = Player.MAX_AGE - age;
            if (limit > 5) {
                limit = 5;
            }

            for (int i = age + 1, j = age - Player.MIN_AGE; i <= age + limit; ++i, ++j) {
                avg += ProgressParameters.CONSTANTS.get(player.getTalentType()).get(j);
                if (avg > 99) {
                    avg = 99;
                }
                builder.append("Возраст: ").append(i).append(", Общее: ").append(avg).append("\n");
            }
        }

        taInfo.setText(builder.toString());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        taInfo = new javax.swing.JTextArea();
        bClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Подробнее об игроке");
        setResizable(false);
        setSize(new java.awt.Dimension(400, 500));

        taInfo.setEditable(false);
        taInfo.setColumns(20);
        taInfo.setRows(5);
        jScrollPane1.setViewportView(taInfo);

        bClose.setText("Закрыть");
        bClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bClose)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(bClose)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCloseActionPerformed
        dispose();
    }//GEN-LAST:event_bCloseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bClose;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea taInfo;
    // End of variables declaration//GEN-END:variables

    private void initLocation() {
        setLocationRelativeTo(null);
    }

}
