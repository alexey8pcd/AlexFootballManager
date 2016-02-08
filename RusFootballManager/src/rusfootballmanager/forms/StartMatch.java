package rusfootballmanager.forms;

import java.util.Random;
import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;
import rusfootballmanager.simulation.match.MatchResult;
import rusfootballmanager.entities.Player;
import rusfootballmanager.entities.Position;
import rusfootballmanager.entities.PositionType;
import rusfootballmanager.simulation.Simulator;
import rusfootballmanager.entities.Team;

/**
 * @author Алексей
 */
public class StartMatch extends javax.swing.JFrame {

    private final String[] names = {
        "Александр",
        "Алексей",
        "Анатолий",
        "Андрей",
        "Антон",
        "Артем",
        "Валентин",
        "Валерий",
        "Василий",
        "Виктор",
        "Виталий",
        "Владимир",
        "Владислав",
        "Всеволод",
        "Вячеслав"
    };

    private final String[] lastNames = {
        "Иванов",
        "Васильев",
        "Петров",
        "Смирнов",
        "Михайлов",
        "Фёдоров",
        "Соколов",
        "Яковлев",
        "Попов",
        "Андреев",
        "Богданов",
        "Николаев",
        "Дмитриев",
        "Егоров",
        "Волков",
        "Кузнецов",
        "Никитин",
        "Соловьёв",
        "Тимофеев",
        "Орлов",
        "Афанасьев",
        "Филиппов",
        "Сергеев",
        "Захаров",
        "Матвеев",
        "Виноградов",
        "Кузьмин",
        "Максимов",
        "Козлов",
        "Ильин"
    };

    private Random random = new Random();
    private Team homeTeam;
    private Team guestTeam;

    public StartMatch() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lHomeTeamName = new javax.swing.JLabel();
        lGuestTeamName = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listHomeTeamPlayers = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        listGuestTeamPlayers = new javax.swing.JList();
        bStart = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        listEvents = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lHomeTeamName.setText("название команды хозяев");

        lGuestTeamName.setText("название команды гостей");

        jScrollPane1.setViewportView(listHomeTeamPlayers);

        jScrollPane2.setViewportView(listGuestTeamPlayers);

        bStart.setText("Расчет итогов");
        bStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bStartActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportView(listEvents);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(94, 94, 94)
                                .addComponent(bStart)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lHomeTeamName, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 272, Short.MAX_VALUE)
                        .addComponent(lGuestTeamName, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lGuestTeamName, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lHomeTeamName, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bStart)
                        .addGap(7, 7, 7)
                        .addComponent(jScrollPane3)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bStartActionPerformed
        MatchResult matchResult = Simulator.simulate(homeTeam, guestTeam);
        listEvents.setModel(new AbstractListModel<String>() {

            @Override
            public int getSize() {
                return matchResult.getEvents().size();
            }

            @Override
            public String getElementAt(int index) {
                return matchResult.getEvents().get(index).toString();
            }
        });
        String result = matchResult.getResult(homeTeam);
        JOptionPane.showMessageDialog(null, result);
    }//GEN-LAST:event_bStartActionPerformed

    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StartMatch.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StartMatch.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StartMatch.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StartMatch.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StartMatch().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bStart;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lGuestTeamName;
    private javax.swing.JLabel lHomeTeamName;
    private javax.swing.JList listEvents;
    private javax.swing.JList listGuestTeamPlayers;
    private javax.swing.JList listHomeTeamPlayers;
    // End of variables declaration//GEN-END:variables

}
