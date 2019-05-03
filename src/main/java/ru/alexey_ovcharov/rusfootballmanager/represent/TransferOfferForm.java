package ru.alexey_ovcharov.rusfootballmanager.represent;

import ru.alexey_ovcharov.rusfootballmanager.career.Message;
import ru.alexey_ovcharov.rusfootballmanager.career.User;
import ru.alexey_ovcharov.rusfootballmanager.common.MoneyHelper;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.*;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;

import java.time.LocalDate;

/**
 * @author Алексей
 */
public class TransferOfferForm extends javax.swing.JDialog {

    private final transient Market market = Market.getInstance();
    private transient Transfer transferPlayer;
    private transient Team team;
    private transient TransferStatus transferStatus;
    private transient LocalDate date;
    private transient User user;
    private transient Offer offer;
    private transient Offer.OfferType offerType;

    public void setParams(Offer offer, User user) {
        this.offer = offer;
        this.transferStatus = offer.getTransferStatus();
        this.team = offer.getFromTeam();
        this.date = offer.getDate();
        this.user = user;
        ftfSum.setValue(offer.getSumOfTransfer());
        ftfPay.setValue(offer.getFare());
        spinnerContractDuration.setValue(offer.getContractDuration());
        Player player = offer.getPlayer();
        int transferCost = MoneyHelper.calculateTransferCost(player);
        labelDesiredSumValue.setText(MoneyHelper.formatSum(transferCost));
        long fare = MoneyHelper.calculatePayForMatch(player);
        labelDesiredFareValue.setText(MoneyHelper.formatSum(fare));
    }

    public void setParams(Transfer transferPlayer, Team team, TransferStatus transferStatus, LocalDate date,
                          User user, Offer.OfferType offerType) {
        this.transferPlayer = transferPlayer;
        this.team = team;
        this.transferStatus = transferStatus;
        this.date = date;
        this.user = user;
        this.offerType = offerType;
        if (transferStatus == TransferStatus.ON_TRANSFER) {
            int cost = transferPlayer.getCost();
            labelDesiredSumValue.setText(MoneyHelper.formatSum(cost));
            ftfSum.setValue(cost);
        } else {
            labelDesiredSumValue.setText("Отсутствует");
            labelOfferedSum.setVisible(false);
            ftfSum.setVisible(false);
        }
        int payForMatch = MoneyHelper.calculatePayForMatch(transferPlayer.getPlayer());
        labelDesiredFareValue.setText(MoneyHelper.formatSum(payForMatch));
        ftfPay.setValue(payForMatch);

    }

    public TransferOfferForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private void makeOffer() {
        Number sum = ftfSum.getValue() == null ? 0 : (Number) ftfSum.getValue();
        int pay = ((Number) ftfPay.getValue()).intValue();
        int contractDuration = (int) spinnerContractDuration.getValue();
        if (offer == null) {
            Team teamFrom = transferPlayer.getTeam();
            Player player = transferPlayer.getPlayer();
            offer = new Offer(teamFrom, this.team, player, transferStatus, sum.intValue(), pay,
                    contractDuration, date, offerType);
            offer.setOfferListener(offerObj -> {
                String body = "Решение: " + offerObj.getTransferResult().getDescription();
                user.addMessage(new Message("Директор команды " + teamFrom.getName(), date,
                        "Трансферное предложение по игроку " + player.getNameAbbrAndLastName(), body));
            });
            market.makeOffer(offer);
        } else {
            offer.setSumOfTransfer(sum.intValue());
            offer.setFare(pay);
            offer.setContractDuration(contractDuration);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        labelDesiredSum = new javax.swing.JLabel();
        labelDesiredSumValue = new javax.swing.JLabel();
        labelOfferedSum = new javax.swing.JLabel();
        ftfSum = new javax.swing.JFormattedTextField();
        labelDesiredFare = new javax.swing.JLabel();
        labelOfferedFare = new javax.swing.JLabel();
        ftfPay = new javax.swing.JFormattedTextField();
        labelContractDuration = new javax.swing.JLabel();
        spinnerContractDuration = new javax.swing.JSpinner();
        bOffer = new javax.swing.JButton();
        labelDesiredFareValue = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Трансферное предложение");
        setPreferredSize(new java.awt.Dimension(400, 400));
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        labelDesiredSum.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelDesiredSum.setText("Желаемая сумма:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 21, 6, 21);
        getContentPane().add(labelDesiredSum, gridBagConstraints);

        labelDesiredSumValue.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelDesiredSumValue.setText("1 000 000");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 21, 5, 21);
        getContentPane().add(labelDesiredSumValue, gridBagConstraints);

        labelOfferedSum.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelOfferedSum.setText("Предлагаемая сумма:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 21, 8, 21);
        getContentPane().add(labelOfferedSum, gridBagConstraints);

        ftfSum.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 21, 4, 21);
        getContentPane().add(ftfSum, gridBagConstraints);

        labelDesiredFare.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelDesiredFare.setText("Желаемая зарплата:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 21, 5, 21);
        getContentPane().add(labelDesiredFare, gridBagConstraints);

        labelOfferedFare.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelOfferedFare.setText("Предлагаемая зарплата:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 21, 7, 21);
        getContentPane().add(labelOfferedFare, gridBagConstraints);

        ftfPay.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 21, 4, 21);
        getContentPane().add(ftfPay, gridBagConstraints);

        labelContractDuration.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelContractDuration.setText("Длительность контракта:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 21, 6, 21);
        getContentPane().add(labelContractDuration, gridBagConstraints);

        spinnerContractDuration.setModel(new javax.swing.SpinnerNumberModel(3, 1, 5, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 21, 5, 21);
        getContentPane().add(spinnerContractDuration, gridBagConstraints);

        bOffer.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        bOffer.setText("Предложить");
        bOffer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOfferActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(8, 21, 8, 21);
        getContentPane().add(bOffer, gridBagConstraints);

        labelDesiredFareValue.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelDesiredFareValue.setText("50 000");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 21, 5, 21);
        getContentPane().add(labelDesiredFareValue, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bOfferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOfferActionPerformed
        makeOffer();
        dispose();
    }//GEN-LAST:event_bOfferActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bOffer;
    private javax.swing.JFormattedTextField ftfPay;
    private javax.swing.JFormattedTextField ftfSum;
    private javax.swing.JLabel labelContractDuration;
    private javax.swing.JLabel labelDesiredFare;
    private javax.swing.JLabel labelDesiredFareValue;
    private javax.swing.JLabel labelDesiredSum;
    private javax.swing.JLabel labelDesiredSumValue;
    private javax.swing.JLabel labelOfferedFare;
    private javax.swing.JLabel labelOfferedSum;
    private javax.swing.JSpinner spinnerContractDuration;
    // End of variables declaration//GEN-END:variables

}
