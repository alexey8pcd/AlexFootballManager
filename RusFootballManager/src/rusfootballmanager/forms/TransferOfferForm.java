package rusfootballmanager.forms;

import rusfootballmanager.common.CostCalculator;
import rusfootballmanager.entities.Offer;
import rusfootballmanager.entities.Team;
import rusfootballmanager.transfers.TransferPlayer;
import rusfootballmanager.transfers.TransferStatus;

/**
 *
 * @author Алексей
 */
public class TransferOfferForm extends javax.swing.JDialog {

    private TransferPlayer transferPlayer;
    private Team team;
    private TransferStatus transferStatus;

    public void setParams(TransferPlayer transferPlayer, Team team,
            TransferStatus transferStatus) {
        this.transferPlayer = transferPlayer;
        this.team = team;
        if (transferStatus == TransferStatus.ON_TRANSFER) {
            int cost = transferPlayer.getCost();
            lDesiredSum.setText(String.valueOf(cost));
            ftfSum.setValue(cost / 10 * 11);
        } else {
            lDesiredSum.setText("Отсутствует");
            lOfferedSum.setVisible(false);
            ftfSum.setVisible(false);
        }
        int payForMatch = CostCalculator.calculatePayForMatch(
                transferPlayer.getPlayer());
        lDesiredPay.setText(String.valueOf(payForMatch));
        ftfPay.setValue(payForMatch / 10 * 11);

    }

    public TransferOfferForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private void makeOffer() {
        int sum = ftfSum.getValue() == null ? 0 : (int) ftfSum.getValue();
        int pay = (int) ftfPay.getValue();
        int contract = (int) spinnerContractDuration.getValue();
        transferPlayer.addOffer(team, new Offer(transferStatus, sum, pay, contract));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel2 = new javax.swing.JLabel();
        lDesiredSum = new javax.swing.JLabel();
        lOfferedSum = new javax.swing.JLabel();
        ftfSum = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ftfPay = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        spinnerContractDuration = new javax.swing.JSpinner();
        bOffer = new javax.swing.JButton();
        lDesiredPay = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Желаемая сумма:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 21, 6, 21);
        getContentPane().add(jLabel2, gridBagConstraints);

        lDesiredSum.setText("1 000 000");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 21, 5, 21);
        getContentPane().add(lDesiredSum, gridBagConstraints);

        lOfferedSum.setText("Предлагаемая сумма:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 21, 8, 21);
        getContentPane().add(lOfferedSum, gridBagConstraints);

        ftfSum.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 21, 4, 21);
        getContentPane().add(ftfSum, gridBagConstraints);

        jLabel3.setText("Желаемая зарплата:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 21, 5, 21);
        getContentPane().add(jLabel3, gridBagConstraints);

        jLabel4.setText("Предлагаемая зарплата:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 21, 7, 21);
        getContentPane().add(jLabel4, gridBagConstraints);

        ftfPay.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 21, 4, 21);
        getContentPane().add(ftfPay, gridBagConstraints);

        jLabel5.setText("Длительность контракта:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 21, 6, 21);
        getContentPane().add(jLabel5, gridBagConstraints);

        spinnerContractDuration.setModel(new javax.swing.SpinnerNumberModel(3, 1, 5, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 21, 5, 21);
        getContentPane().add(spinnerContractDuration, gridBagConstraints);

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

        lDesiredPay.setText("50 000");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 21, 5, 21);
        getContentPane().add(lDesiredPay, gridBagConstraints);

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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lDesiredPay;
    private javax.swing.JLabel lDesiredSum;
    private javax.swing.JLabel lOfferedSum;
    private javax.swing.JSpinner spinnerContractDuration;
    // End of variables declaration//GEN-END:variables

}
