package ru.alexey_ovcharov.rusfootballmanager.represent;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import ru.alexey_ovcharov.rusfootballmanager.career.User;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Offer;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Market;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.TransferResult;

/**
 * @author Алексей
 */
public class MyOffersForm extends javax.swing.JDialog {

    private static final int COLUMN_NAME_AND_LAST_NAME = 0;
    private static final int COLUMN_AGE = 1;
    private static final int COLUMN_GLOBAL_POSITION = 2;
    private static final int COLUMN_AVERAGE = 3;
    private static final int COLUMN_OFFER_TYPE = 4;
    private static final int COLUMN_FARE = 5;
    private static final int COLUMN_PRICE = 6;
    private static final int COLUMN_DEAL_PROGRESS = 7;
    private final transient Market market = Market.getInstance();
    private transient List<Offer> myOffers = Collections.emptyList();
    private transient Team team;

    private static final String[] OFFER_TABLE_HEADERS = {
            "Имя/Фамилия",
            "Возраст",
            "Позиция",
            "Общее",
            "Предложение",
            "Зарплата",
            "Стоимость",
            "Ход переговоров"
    };
    private transient User user;


    private class OffersTableModel extends DefaultTableModel {

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
                case COLUMN_NAME_AND_LAST_NAME:
                    return player.getFullName();
                case COLUMN_AGE:
                    return player.getAge();
                case COLUMN_GLOBAL_POSITION:
                    return player.getPreferredPosition().getAbreviation();
                case COLUMN_AVERAGE:
                    return player.getAverage();
                case COLUMN_OFFER_TYPE:
                    return offer.getOfferType().getDescription();
                case COLUMN_FARE:
                    return offer.getFare();
                case COLUMN_PRICE:
                    return offer.getSumOfTransfer();
                case COLUMN_DEAL_PROGRESS:
                    return offer.getTransferResult().getDescription();
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
        tableOffers.setModel(new OffersTableModel());
    }

    public void setParams(Team team, User user) {
        this.team = team;
        this.user = user;
        this.myOffers = market.getOffers(team);
    }

    private Optional<Offer> getSelectedOffer() {
        int selectedIndex = tableOffers.getSelectedRow();
        if (selectedIndex >= 0 && selectedIndex < myOffers.size()) {
            return Optional.ofNullable(myOffers.get(selectedIndex));
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tableOffers = new javax.swing.JTable();
        bRemoveOffer = new javax.swing.JButton();
        bChangeOffer = new javax.swing.JButton();
        buttonMakeTransfer = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Мои предложения");

        tableOffers.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null}
                },
                new String[]{
                        "Игрок", "Возраст", "Позиция", "Общее", "Предложение", "Зарплата", "Стоимость", "Ход переговоров"
                }
        ) {
            final Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            final boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false, false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableOffers);

        bRemoveOffer.setText("Отказаться от предложения");
        bRemoveOffer.addActionListener(this::bRemoveOfferActionPerformed);

        bChangeOffer.setText("Изменить предложение");
        bChangeOffer.addActionListener(this::bChangeOfferActionPerformed);

        buttonMakeTransfer.setText("Подтвердить");
        buttonMakeTransfer.addActionListener(this::buttonMakeTransferActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addGroup(layout.createSequentialGroup()
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                      .addGroup(layout.createSequentialGroup()
                                                                      .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                      .addComponent(buttonMakeTransfer)
                                                                      .addGap(18, 18, 18)
                                                                      .addComponent(bRemoveOffer)
                                                                      .addGap(18, 18, 18)
                                                                      .addComponent(bChangeOffer))
                                                      .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE))
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
                                                                                                  .addComponent(bChangeOffer)
                                                                                                  .addComponent(buttonMakeTransfer))
                                                                                  .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bChangeOfferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bChangeOfferActionPerformed
        changeOffer();
    }//GEN-LAST:event_bChangeOfferActionPerformed

    private void bRemoveOfferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRemoveOfferActionPerformed
        removeOffer();
    }//GEN-LAST:event_bRemoveOfferActionPerformed

    private void buttonMakeTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMakeTransferActionPerformed
        makeTransfer();
    }//GEN-LAST:event_buttonMakeTransferActionPerformed

    private void makeTransfer() {
        int selectedRow = tableOffers.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < myOffers.size()) {
            Offer offer = myOffers.get(selectedRow);
            if (offer.getTransferResult() != TransferResult.ACCEPT) {
                JOptionPane.showMessageDialog(this, "Не согласовано!");
            } else {
                market.performTransfer(offer);
                JOptionPane.showMessageDialog(this, "Сделка совершена");
                myOffers.remove(offer);
                tableOffers.updateUI();
            }
        }
    }


    private void changeOffer() {
        Optional<Offer> selectedOfferOpt = getSelectedOffer();
        selectedOfferOpt.ifPresent(offer -> {
            TransferOfferForm transferOfferForm = new TransferOfferForm(null, true);
            transferOfferForm.setParams(offer, user);
            transferOfferForm.setLocationRelativeTo(MyOffersForm.this);
            transferOfferForm.setVisible(true);
            tableOffers.updateUI();
        });
    }

    private void removeOffer() {
        int result = JOptionPane.showConfirmDialog(null,
                "Отказаться?", "Подтверждение отказа", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            Optional<Offer> selectedOfferOpt = getSelectedOffer();
            selectedOfferOpt.ifPresent(offer -> {
                market.removeOffer(offer);
                myOffers = market.getOffers(team);
                tableOffers.updateUI();
            });
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bChangeOffer;
    private javax.swing.JButton bRemoveOffer;
    private javax.swing.JButton buttonMakeTransfer;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tableOffers;
    // End of variables declaration//GEN-END:variables

}
