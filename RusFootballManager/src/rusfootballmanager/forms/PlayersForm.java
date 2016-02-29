package rusfootballmanager.forms;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import rusfootballmanager.RenderUtil;
import rusfootballmanager.entities.Contract;
import rusfootballmanager.entities.Player;

/**
 *
 * @author Алексей
 */
public class PlayersForm extends javax.swing.JDialog {

    private List<Player> players;
    private final String[] tableHeaders = {
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

    private class PlayersTableCellRenderer extends DefaultTableCellRenderer {

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

    private class PlayersTableModel extends DefaultTableModel {

        public PlayersTableModel() {
        }

        @Override
        public int getRowCount() {
            return players.size();
        }

        @Override
        public int getColumnCount() {
            return 9;
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
                    return player.getNumber();
                case 4:
                    return player.getAverage();
                case 5:
                    return player.getStatusOfPlayer().getDescription();
                case 6:
                    return player.getCurrentFare();
                case 7:
                    Contract contract = player.getContract();
                    if (contract == null) {
                        return "0";
                    } else {
                        return contract.getDuration();
                    }
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
            return tableHeaders[column];
        }

    }

    public PlayersForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
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
        setResizable(false);
        setSize(new java.awt.Dimension(800, 500));

        tablePlayers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Фамилия, имя", "Возраст", "Позиция", "Номер", "Общее", "Состояние", "Зарплата", "Контракт", "Настроение"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
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

    public void setPlayers(List<Player> players) {
        this.players = players;
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
