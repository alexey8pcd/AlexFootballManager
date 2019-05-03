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
    private transient List<Offer> myOffersBuy = Collections.emptyList();
    private transient List<Offer> myOffersSale = Collections.emptyList();
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


    private static class OffersTableModel extends DefaultTableModel {

        private final transient List<Offer> offers;

        OffersTableModel(List<Offer> offers) {
            this.offers = offers;
        }

        @Override
        public int getRowCount() {
            return offers == null ? 0 : offers.size();
        }

        @Override
        public int getColumnCount() {
            return OFFER_TABLE_HEADERS.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Offer offer = offers.get(rowIndex);
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
    }

    public void setParams(Team team, User user) {
        this.team = team;
        this.user = user;
        this.myOffersBuy = market.getOffersBuy(team);
        this.myOffersSale = market.getOffersSale(team);
        tableOffersBuy.setModel(new OffersTableModel(myOffersBuy));
        tableOffersSale.setModel(new OffersTableModel(myOffersSale));
    }

    private Optional<Offer> getSelectedOfferBuy() {
        int selectedIndex = tableOffersBuy.getSelectedRow();
        if (selectedIndex >= 0 && selectedIndex < myOffersBuy.size()) {
            return Optional.ofNullable(myOffersBuy.get(selectedIndex));
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableOffersBuy = new javax.swing.JTable();
        buttonApplyBuy = new javax.swing.JButton();
        buttonDeclineBuy = new javax.swing.JButton();
        buttonChangeBuyOffer = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableOffersSale = new javax.swing.JTable();
        buttonApplySale = new javax.swing.JButton();
        buttonDeclineSale = new javax.swing.JButton();
        buttonRequireMoreSumSale = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Мои предложения");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Потенциальные трансферы в мою команду"));

        tableOffersBuy.setModel(new javax.swing.table.DefaultTableModel(
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
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableOffersBuy);

        buttonApplyBuy.setText("Подтвердить");
        buttonApplyBuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonApplyBuyActionPerformed(evt);
            }
        });

        buttonDeclineBuy.setText("Отклонить");
        buttonDeclineBuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeclineBuyActionPerformed(evt);
            }
        });

        buttonChangeBuyOffer.setText("Изменить предложение");
        buttonChangeBuyOffer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonChangeBuyOfferActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                             .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                           .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 776, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                           .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                                                                                              .addComponent(buttonApplyBuy)
                                                                                                                                              .addGap(18, 18, 18)
                                                                                                                                              .addComponent(buttonDeclineBuy)
                                                                                                                                              .addGap(18, 18, 18)
                                                                                                                                              .addComponent(buttonChangeBuyOffer)))
                                                    .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                             .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addContainerGap()
                                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                           .addComponent(buttonApplyBuy)
                                                                           .addComponent(buttonDeclineBuy)
                                                                           .addComponent(buttonChangeBuyOffer))
                                                    .addGap(0, 9, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Игроки, которых хотят купить другие команды"));

        tableOffersSale.setModel(new javax.swing.table.DefaultTableModel(
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
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane3.setViewportView(tableOffersSale);

        buttonApplySale.setText("Принять предложение");
        buttonApplySale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonApplySaleActionPerformed(evt);
            }
        });

        buttonDeclineSale.setText("Отказаться от предложения");
        buttonDeclineSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeclineSaleActionPerformed(evt);
            }
        });

        buttonRequireMoreSumSale.setText("Попросить больше");
        buttonRequireMoreSumSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRequireMoreSumSaleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                             .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                           .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 776, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                           .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                                                                                                              .addComponent(buttonApplySale)
                                                                                                                                              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                                              .addComponent(buttonDeclineSale)
                                                                                                                                              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                                              .addComponent(buttonRequireMoreSumSale)))
                                                    .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                             .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addContainerGap()
                                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                           .addComponent(buttonApplySale)
                                                                           .addComponent(buttonDeclineSale)
                                                                           .addComponent(buttonRequireMoreSumSale))
                                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                  .addContainerGap()
                                                                                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                  .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                  .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                                  .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                  .addContainerGap()
                                                                                  .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                  .addGap(18, 18, 18)
                                                                                  .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                  .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonChangeBuyOfferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonChangeBuyOfferActionPerformed
        changeOfferBuy();
    }//GEN-LAST:event_buttonChangeBuyOfferActionPerformed

    private void buttonDeclineSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeclineSaleActionPerformed
        declineSale();
    }//GEN-LAST:event_buttonDeclineSaleActionPerformed

    private void buttonApplySaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonApplySaleActionPerformed
        applySale();
    }//GEN-LAST:event_buttonApplySaleActionPerformed

    private void buttonApplyBuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonApplyBuyActionPerformed
        makeTransfer();
    }//GEN-LAST:event_buttonApplyBuyActionPerformed

    private void buttonDeclineBuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeclineBuyActionPerformed
        removeOffer();
    }//GEN-LAST:event_buttonDeclineBuyActionPerformed

    private void buttonRequireMoreSumSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRequireMoreSumSaleActionPerformed
        requireMoreSumSale();
    }//GEN-LAST:event_buttonRequireMoreSumSaleActionPerformed

    private void declineSale() {

    }

    private void applySale() {

    }

    private void requireMoreSumSale() {

    }

    private void makeTransfer() {
        int selectedRow = tableOffersBuy.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < myOffersBuy.size()) {
            Offer offer = myOffersBuy.get(selectedRow);
            if (offer.getTransferResult() != TransferResult.ACCEPT) {
                JOptionPane.showMessageDialog(this, "Не согласовано!");
            } else {
                if (offer.getToTeam().getBudget() > offer.getSumOfTransfer()) {
                    market.performTransfer(offer);
                    JOptionPane.showMessageDialog(this, "Сделка совершена");
                    myOffersBuy.remove(offer);
                    tableOffersBuy.updateUI();
                } else {
                    JOptionPane.showMessageDialog(this, "В вашем бюджете не хватает средств!");
                }
            }
        }
    }


    private void changeOfferBuy() {
        Optional<Offer> selectedOfferOpt = getSelectedOfferBuy();
        selectedOfferOpt.ifPresent(offer -> {
            TransferOfferForm transferOfferForm = new TransferOfferForm(null, true);
            transferOfferForm.setParams(offer, user);
            transferOfferForm.setLocationRelativeTo(MyOffersForm.this);
            transferOfferForm.setVisible(true);
            tableOffersBuy.updateUI();
        });
    }

    private void removeOffer() {
        int result = JOptionPane.showConfirmDialog(null,
                "Отказаться?", "Подтверждение отказа", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            Optional<Offer> selectedOfferOpt = getSelectedOfferBuy();
            selectedOfferOpt.ifPresent(offer -> {
                market.removeOffer(offer);
                myOffersBuy = market.getOffersBuy(team);
                tableOffersBuy.updateUI();
            });
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonApplyBuy;
    private javax.swing.JButton buttonApplySale;
    private javax.swing.JButton buttonChangeBuyOffer;
    private javax.swing.JButton buttonDeclineBuy;
    private javax.swing.JButton buttonDeclineSale;
    private javax.swing.JButton buttonRequireMoreSumSale;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tableOffersBuy;
    private javax.swing.JTable tableOffersSale;
    // End of variables declaration//GEN-END:variables

}
