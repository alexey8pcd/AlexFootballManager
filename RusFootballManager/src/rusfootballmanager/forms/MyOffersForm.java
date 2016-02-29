package rusfootballmanager.forms;

import java.util.Collections;
import java.util.List;
import javafx.util.Pair;
import javax.swing.table.DefaultTableModel;
import rusfootballmanager.entities.Offer;
import rusfootballmanager.entities.Player;
import rusfootballmanager.entities.Team;
import rusfootballmanager.transfers.TransferMarket;

/**
 *
 * @author Алексей
 */
public class MyOffersForm extends javax.swing.JDialog {

    private List<Pair<Player, Offer>> myOffers = Collections.EMPTY_LIST;

    private static final String[] OFFER_TABLE_HEADERS = {
        "Имя/Фамилия",
        "Возраст",
        "Позиция",
        "Общее",
        "Зарплата",
        "Стоимость"
    };
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
            Pair<Player, Offer> pair = myOffers.get(rowIndex);
            Offer offer = pair.getValue();
            /*switch (columnIndex) {
            case 0:
            return offer.getFullName();
            case 1:
            return offer.getAge();
            case 2:
            return offer.getPreferredPosition().getAbreviation();
            case 3:
            return offer.getNumber();
            case 4:
            return offer.getAverage();
            case 5:
            return offer.getStatusOfPlayer().getDescription();
            case 6:
            return offer.getCurrentFare();
            case 7:
            Contract contract = offer.getContract();
            if (contract == null) {
            return "0";
            } else {
            return contract.getDuration();
            }
            case 8:
            return offer.getMood();
            }*/
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
            return OFFER_TABLE_HEADERS[column];
        }

    }
    
    public void init(Team team) {
        myOffers = TransferMarket.getInstance().getDesiredPlayers(team);
    }

    public MyOffersForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tableOffers = new javax.swing.JTable();
        bRemoveOffer = new javax.swing.JButton();
        bChangeOffer = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        bChangeOffer.setText("Изменить предложение");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bRemoveOffer)
                        .addGap(18, 18, 18)
                        .addComponent(bChangeOffer)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bRemoveOffer)
                    .addComponent(bChangeOffer))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bChangeOffer;
    private javax.swing.JButton bRemoveOffer;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tableOffers;
    // End of variables declaration//GEN-END:variables

}
