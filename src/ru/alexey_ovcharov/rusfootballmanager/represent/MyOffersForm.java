package ru.alexey_ovcharov.rusfootballmanager.represent;

import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Offer;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Market;

/**
 *
 * @author Алексей
 */
public class MyOffersForm extends javax.swing.JDialog {

    private List<Offer> myOffers = Collections.EMPTY_LIST;
    private Team team;

    private static final String[] OFFER_TABLE_HEADERS = {
        "Имя/Фамилия",
        "Возраст",
        "Позиция",
        "Общее",
        "Предложение",
        "Зарплата",
        "Стоимость"
    };

    private void initLocation() {
        setLocationRelativeTo(null);
    }

    private class OffersTableModel extends DefaultTableModel {

        public OffersTableModel() {
        }

        @Override
        public int getRowCount() {
            return myOffers.size();
        }

        @Override
        public int getColumnCount() {
            return OFFER_TABLE_HEADERS.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Offer offer = myOffers.get(rowIndex);
            Player player = offer.getPlayer();
            switch (columnIndex) {
                case 0:
                    return player.getFullName();
                case 1:
                    return player.getAge();
                case 2:
                    return player.getPreferredPosition().getAbreviation();
                case 3:
                    return player.getAverage();
                case 4:
                    return offer.getStatus().getDescription();
                case 5:
                    return offer.getFare();
                case 6:
                    return offer.getSumOfTransfer();
            }
            return "";
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public String getColumnName(int column) {
            return OFFER_TABLE_HEADERS[column];
        }

    }

    public MyOffersForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initLocation();
        tableOffers.setModel(new OffersTableModel());
    }

    public void setTeam(Team team) {
        this.team = team;
        myOffers = Market.getInstance().getOffers(team);
    }

    private Offer getSelectedOffer() {
        int selectedIndex = tableOffers.getSelectedRow();
        if (selectedIndex >= 0 && selectedIndex < myOffers.size()) {
            return myOffers.get(selectedIndex);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tableOffers = new javax.swing.JTable();
        bRemoveOffer = new javax.swing.JButton();
        bChangeOffer = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Мои предложения");

        tableOffers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Игрок", "Возраст", "Позиция", "Общее", "Зарплата", "Стоимость"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableOffers);

        bRemoveOffer.setText("Отказаться от предложения");
        bRemoveOffer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRemoveOfferActionPerformed(evt);
            }
        });

        bChangeOffer.setText("Изменить предложение");
        bChangeOffer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bChangeOfferActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(440, Short.MAX_VALUE)
                        .addComponent(bRemoveOffer)
                        .addGap(18, 18, 18)
                        .addComponent(bChangeOffer))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bRemoveOffer)
                    .addComponent(bChangeOffer))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bChangeOfferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bChangeOfferActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bChangeOfferActionPerformed

    private void bRemoveOfferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRemoveOfferActionPerformed
        int result = JOptionPane.showConfirmDialog(null,
                "Отказаться?", "Подтверждение отказа", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            Offer selectedOffer = getSelectedOffer();
            Market.getInstance().removeOffer(selectedOffer);
            myOffers = Market.getInstance().getOffers(team);
            tableOffers.updateUI();
        }
    }//GEN-LAST:event_bRemoveOfferActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bChangeOffer;
    private javax.swing.JButton bRemoveOffer;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tableOffers;
    // End of variables declaration//GEN-END:variables

}
