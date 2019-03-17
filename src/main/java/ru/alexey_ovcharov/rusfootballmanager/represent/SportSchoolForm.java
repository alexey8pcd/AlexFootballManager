package ru.alexey_ovcharov.rusfootballmanager.represent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.ProgressParameters;

/**
 *
 * @author Алексей
 */
public class SportSchoolForm extends javax.swing.JDialog {

    private Team team;
    private List<Player> juniors = Collections.EMPTY_LIST;
    private Map<Player, List<Integer>> playerProgressValues = new HashMap<>();
    private String[] HEADERS = {
        "Имя/Фамилия",
        "Возраст",
        "Позиция",
        "Общее",
        "+1",
        "+2",
        "+3",
        "+4",
        "+5",
        "+7",
        "+10"
    };

    private TableModel model = new DefaultTableModel() {
        @Override
        public String getColumnName(int column) {
            return HEADERS[column];
        }

        @Override
        public int getColumnCount() {
            return HEADERS.length;
        }

        @Override
        public int getRowCount() {
            return juniors.size();
        }

        @Override
        public Object getValueAt(int row, int column) {
            Player player = juniors.get(row);
            List<Integer> progressValues = playerProgressValues.get(player);
            switch (column) {
                case 0:
                    return player.getFullName();
                case 1:
                    return player.getAge();
                case 2:
                    return player.getPreferredPosition().getAbreviation();
                case 3:
                    return player.getAverage(player.getPreferredPosition());
                default:
                    int index = column - 4;
                    if (progressValues != null
                            && index >= 0
                            && index < progressValues.size()) {
                        return progressValues.get(index);
                    }
            }
            return "";
        }

    };

    public SportSchoolForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initLocation();
    }

    public void setTeam(Team team) {
        this.team = team;
        juniors = team.getJuniors();
        final int limit = 10;
        juniors.forEach(p -> {
            List<Integer> values = new ArrayList<>(7);
            int age = p.getAge();
            int avg = p.getAverage(p.getPreferredPosition());
            for (int i = age + 1, j = age - Player.MIN_AGE, k = 0;
                    i <= age + limit; ++i, ++j, ++k) {
                avg += ProgressParameters.CONSTANTS.get(p.getTalentType()).get(j);
                if (avg > 99) {
                    avg = 99;
                }
                if (k < 5 || k == 6 || k == 9) {
                    values.add(avg);
                }
            }
            playerProgressValues.put(p, values);
        });
        tableYoungPlayers.setModel(model);
        tableYoungPlayers.getColumnModel().getColumn(0).setPreferredWidth(150);
        tableYoungPlayers.updateUI();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bContract = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableYoungPlayers = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Спортивная школа");

        bContract.setText("Заключить контракт");
        bContract.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bContractActionPerformed(evt);
            }
        });

        tableYoungPlayers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tableYoungPlayers);

        jLabel1.setText("Молодые игроки, готовые присоединиться к команде");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 639, Short.MAX_VALUE)
                        .addComponent(bContract))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(bContract)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bContractActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bContractActionPerformed
        contract();
    }//GEN-LAST:event_bContractActionPerformed

    private void contract() {
        int index = tableYoungPlayers.getSelectedRow();
        if (index >= 0 && index < juniors.size()) {
            Player player = juniors.get(index);
            boolean added = team.addPlayerFromSportSchool(player);
            if (added) {
                juniors = team.getJuniors();
                playerProgressValues.remove(player);                
            }
            String text = added 
                    ? "Контракт заключен" 
                    : "В вашей команде слишком много игроков";
            JOptionPane.showMessageDialog(null, text, "Результат", 
                    JOptionPane.INFORMATION_MESSAGE);
            tableYoungPlayers.updateUI();
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bContract;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableYoungPlayers;
    // End of variables declaration//GEN-END:variables

    private void initLocation() {
        setLocationRelativeTo(null);
    }

}
