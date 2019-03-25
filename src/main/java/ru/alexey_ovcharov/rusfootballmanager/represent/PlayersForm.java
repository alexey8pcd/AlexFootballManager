package ru.alexey_ovcharov.rusfootballmanager.represent;

import ru.alexey_ovcharov.rusfootballmanager.common.util.RenderUtil;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Contract;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.InjureType;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Алексей
 */
public class PlayersForm extends javax.swing.JDialog {

    private static final String[] PLAYERS_TABLE_HEADERS = {
            "Имя/Фамилия",
            "Возраст",
            "Позиция",
            "Номер",
            "Общее",
            "Состояние",
            "Зарплата",
            "Контракт",
            "Настрой"
    };

    private transient Team team;
    private transient List<Player> players;

    private void initLocation() {
        setLocationRelativeTo(null);
    }

    private class PlayersTableModel extends DefaultTableModel {


        @Override
        public int getRowCount() {
            return players.size();
        }

        @Override
        public int getColumnCount() {
            return PLAYERS_TABLE_HEADERS.length;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Player player = players.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return player.getFullName();
                case 1:
                    return player.getAge();
                case 2:
                    return player.getPreferredPosition().getAbreviation();
                case 3:
                    return team.getNumberOfPlayer(player).orElseThrow(
                            () -> new IllegalStateException("Игрок " + player.getFullName() + " в команде "
                                    + team.getName() + " не имеет номера"));
                case 4:
                    return player.getAverage();
                case 5:
                    return player.getInjure()
                                 .map(InjureType::getDescription)
                                 .orElse("Здоров");
                case 6:
                    return player.getCurrentFare();
                case 7:
                    return player.getContract()
                                 .map(Contract::getDuration)
                                 .orElse(0);
                case 8:
                    return player.getMood();
            }
            return "";
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 1:
                case 3:
                case 4:
                case 6:
                case 7:
                case 8:
                    return Integer.class;
                default:
                    return String.class;
            }
        }

        @Override
        public String getColumnName(int column) {
            return PLAYERS_TABLE_HEADERS[column];
        }

    }

    private static class PlayersTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(CENTER);
            if (column == 4) {
                int val = (int) table.getValueAt(row, column);
                setBackground(RenderUtil.getPlayerAverageColor(val));
            }
            return this;
        }

    }

    public PlayersForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initLocation();
        players = new ArrayList<>();
        tablePlayers.setModel(new PlayersTableModel());
        TableRowSorter<TableModel> rowSorter
                = new TableRowSorter<>(tablePlayers.getModel());
        tablePlayers.setRowSorter(rowSorter);
        TableColumn column = tablePlayers.getColumnModel().getColumn(0);
        column.setResizable(false);
        column.setPreferredWidth(120);
        tablePlayers.getColumnModel().getColumn(4).setCellRenderer(
                new PlayersTableCellRenderer());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablePlayers = new javax.swing.JTable();
        bProlongContract = new javax.swing.JButton();
        bPlayerDescription = new javax.swing.JButton();
        bClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Текущий состав");
        setResizable(false);
        setSize(new java.awt.Dimension(800, 500));

        tablePlayers.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null}
                },
                new String[]{
                        "Фамилия, имя", "Возраст", "Позиция", "Номер", "Общее", "Состояние", "Зарплата", "Контракт", "Настроение"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablePlayers);

        bProlongContract.setText("Продлить контракт");

        bPlayerDescription.setText("Подробнее об игроке");
        bPlayerDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPlayerDescriptionActionPerformed(evt);
            }
        });

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
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                      .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                                                      .addGroup(layout.createSequentialGroup()
                                                                      .addComponent(bProlongContract)
                                                                      .addGap(18, 18, 18)
                                                                      .addComponent(bPlayerDescription)
                                                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                      .addComponent(bClose)))
                                      .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addGroup(layout.createSequentialGroup()
                                      .addContainerGap()
                                      .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                                      .addGap(18, 18, 18)
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                      .addComponent(bProlongContract)
                                                      .addComponent(bPlayerDescription)
                                                      .addComponent(bClose))
                                      .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void setTeam(Team team) {
        this.team = team;
        this.players = team.getAllPlayers();
        tablePlayers.updateUI();
    }


    private void bPlayerDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPlayerDescriptionActionPerformed
        int rowIndex = tablePlayers.getSelectedRow();
        if (rowIndex >= 0 && rowIndex < players.size()) {
            int index = tablePlayers.convertRowIndexToModel(rowIndex);
            Player player = players.get(index);
            AboutPlayerForm aboutPlayerForm = new AboutPlayerForm(null, true);
            aboutPlayerForm.setPlayer(player);
            aboutPlayerForm.setVisible(true);
        }
    }//GEN-LAST:event_bPlayerDescriptionActionPerformed

    private void bCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCloseActionPerformed
        dispose();
    }//GEN-LAST:event_bCloseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bClose;
    private javax.swing.JButton bPlayerDescription;
    private javax.swing.JButton bProlongContract;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablePlayers;
    // End of variables declaration//GEN-END:variables

}
