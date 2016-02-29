package rusfootballmanager.forms;

import java.awt.Component;
import java.awt.HeadlessException;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import rusfootballmanager.RenderUtil;
import rusfootballmanager.entities.Condition;
import rusfootballmanager.transfers.Filter;
import rusfootballmanager.entities.GlobalPosition;
import rusfootballmanager.entities.Player;
import rusfootballmanager.entities.Team;
import rusfootballmanager.transfers.TransferFilterType;
import rusfootballmanager.transfers.TransferMarket;
import rusfootballmanager.transfers.TransferPlayer;
import rusfootballmanager.transfers.TransferStatus;

/**
 *
 * @author Алексей
 */
public class TransferForm extends javax.swing.JDialog {

    private Team team;
    private List<TransferPlayer> transferPlayers = Collections.EMPTY_LIST;
    private TransferFilterType transferFilterType;
    private TransferStatus transferStatus;
    private Filter filter = new Filter(transferFilterType, Condition.MORE, PROPERTIES);

    private static final String[] HEADERS = {
        "Имя/фамилия",
        "Возраст",
        "Амплуа",
        "Позиция",
        "Общее",
        "Статус",
        "Стоимость",
        "Команда"
    };

    private TableModel transferTableModel = new DefaultTableModel() {

        @Override
        public int getRowCount() {
            return transferPlayers.size();
        }

        @Override
        public String getColumnName(int column) {
            return HEADERS[column];
        }

        @Override
        public int getColumnCount() {
            return HEADERS.length;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Object getValueAt(int row, int column) {
            TransferPlayer transfer = transferPlayers.get(row);
            Player player = transfer.getPlayer();
            switch (column) {
                case 0:
                    return player.getFullName();
                case 1:
                    return player.getAge();
                case 2:
                    return player.getPreferredPosition().
                            getPositionOnField().getAbreviation();
                case 3:
                    return player.getPreferredPosition().getAbreviation();
                case 4:
                    return player.getAverage();
                case 5:
                    return transfer.getStatus().getDescription();
                case 6:
                    return transfer.getCost();
                case 7:
                    return transfer.getTeam().getName();
            }
            return "";
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 1:
                case 2:
                case 4:
                case 6:
                    return Integer.class;
            }
            return String.class;
        }

    };

    public TransferForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        tableTransfers.setModel(transferTableModel);
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(transferTableModel);
        tableTransfers.setRowSorter(rowSorter);
        tableTransfers.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 4) {
                    int val = (int) table.getValueAt(row, column);
                    setBackground(RenderUtil.getPlayerAverageColor(val));
                }
                return this;
            }
        });
    }

    public void setTeam(Team team) {
        this.team = team;
        transferStatus = TransferStatus.ANY;
        transferFilterType = TransferFilterType.BY_AGE;
        filter = new Filter(transferFilterType, Condition.MORE, null);
        rbAnyStatus.setEnabled(true);
        transferPlayers = TransferMarket.getInstance().getTransfersByTeamWithoutFilter(team);
        tableTransfers.updateUI();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgTeamGroup = new javax.swing.ButtonGroup();
        bgFilterGroup = new javax.swing.ButtonGroup();
        bgTransferStatus = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableTransfers = new javax.swing.JTable();
        rbMyTeam = new javax.swing.JRadioButton();
        rbAllTeams = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        rbByName = new javax.swing.JRadioButton();
        rbByAge = new javax.swing.JRadioButton();
        rbByAverage = new javax.swing.JRadioButton();
        rbByPosition = new javax.swing.JRadioButton();
        rbByGlobalPosition = new javax.swing.JRadioButton();
        comboBoxCondition = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        cbFilterEnable = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        tfFromEquals = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfTo = new javax.swing.JTextField();
        comboPosition = new javax.swing.JComboBox<>();
        rbForSale = new javax.swing.JRadioButton();
        rbForRent = new javax.swing.JRadioButton();
        rbOnSaleOrRent = new javax.swing.JRadioButton();
        rbAnyStatus = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        bApplyFilter = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        bToRent = new javax.swing.JButton();
        bOnSale = new javax.swing.JButton();
        bTryGetRent = new javax.swing.JButton();
        bTryBuy = new javax.swing.JButton();
        bMyOffers = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Трансферный рынок");
        setResizable(false);

        tableTransfers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Имя/фамилия", "Возраст", "Амплуа", "Позиция", "Общее", "Статус", "Стоимость", "Команда"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableTransfers);

        bgTeamGroup.add(rbMyTeam);
        rbMyTeam.setSelected(true);
        rbMyTeam.setText("Моя команда");
        rbMyTeam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbMyTeamActionPerformed(evt);
            }
        });

        bgTeamGroup.add(rbAllTeams);
        rbAllTeams.setText("Все команды");
        rbAllTeams.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAllTeamsActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Фильтр"));

        bgFilterGroup.add(rbByName);
        rbByName.setText("По имени/фамилии");
        rbByName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbByNameActionPerformed(evt);
            }
        });

        bgFilterGroup.add(rbByAge);
        rbByAge.setSelected(true);
        rbByAge.setText("По возрасту");
        rbByAge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbByAgeActionPerformed(evt);
            }
        });

        bgFilterGroup.add(rbByAverage);
        rbByAverage.setText("По общему");
        rbByAverage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbByAverageActionPerformed(evt);
            }
        });

        bgFilterGroup.add(rbByPosition);
        rbByPosition.setText("По позиции");
        rbByPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbByPositionActionPerformed(evt);
            }
        });

        bgFilterGroup.add(rbByGlobalPosition);
        rbByGlobalPosition.setText("По амплуа");
        rbByGlobalPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbByGlobalPositionActionPerformed(evt);
            }
        });

        comboBoxCondition.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Равно или содержит", "Не равно или не содержит", "Больше", "Меньше", "Больше, чем и меньше, чем" }));
        comboBoxCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxConditionActionPerformed(evt);
            }
        });

        jLabel1.setText("Условие");

        cbFilterEnable.setText("Активен");

        jLabel2.setText("От/Соответствует");

        jLabel3.setText("До");

        comboPosition.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Вратарь", "Защитник", "Полузащитник", "Нападающий" }));
        comboPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboPositionActionPerformed(evt);
            }
        });

        bgTransferStatus.add(rbForSale);
        rbForSale.setText("На продажу");
        rbForSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbForSaleActionPerformed(evt);
            }
        });

        bgTransferStatus.add(rbForRent);
        rbForRent.setText("В аренду");
        rbForRent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbForRentActionPerformed(evt);
            }
        });

        bgTransferStatus.add(rbOnSaleOrRent);
        rbOnSaleOrRent.setText("На продажу или в аренду");
        rbOnSaleOrRent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbOnSaleOrRentActionPerformed(evt);
            }
        });

        bgTransferStatus.add(rbAnyStatus);
        rbAnyStatus.setSelected(true);
        rbAnyStatus.setText("Не важно");
        rbAnyStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAnyStatusActionPerformed(evt);
            }
        });

        jLabel4.setText("Позиция");

        bApplyFilter.setText("Применить");
        bApplyFilter.setEnabled(false);
        bApplyFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bApplyFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbFilterEnable)
                    .addComponent(rbForSale)
                    .addComponent(rbForRent)
                    .addComponent(rbOnSaleOrRent)
                    .addComponent(rbAnyStatus))
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbByAverage)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rbByGlobalPosition)
                                    .addComponent(rbByPosition))
                                .addGap(44, 44, 44)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(comboBoxCondition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbByAge)
                            .addComponent(rbByName))
                        .addGap(8, 8, 8)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(comboPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bApplyFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfFromEquals)
                    .addComponent(tfTo))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(rbByPosition)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbByGlobalPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbByAverage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbByAge)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbByName)
                            .addComponent(bApplyFilter)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfFromEquals, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbFilterEnable, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(rbForSale)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(rbForRent))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(49, 49, 49)
                                                .addComponent(rbOnSaleOrRent)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(rbAnyStatus))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(21, 21, 21)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(tfTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel3)))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(4, 4, 4)
                                                .addComponent(comboBoxCondition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(comboPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4)))))
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        bToRent.setText("В аренду");
        bToRent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bToRentActionPerformed(evt);
            }
        });

        bOnSale.setText("На продажу");
        bOnSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOnSaleActionPerformed(evt);
            }
        });

        bTryGetRent.setText("Арендовать");
        bTryGetRent.setEnabled(false);
        bTryGetRent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTryGetRentActionPerformed(evt);
            }
        });

        bTryBuy.setText("Купить");
        bTryBuy.setEnabled(false);
        bTryBuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTryBuyActionPerformed(evt);
            }
        });

        bMyOffers.setText("Мои предложения");
        bMyOffers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMyOffersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bTryBuy)
                .addGap(18, 18, 18)
                .addComponent(bTryGetRent)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bMyOffers)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bOnSale)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bToRent)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bToRent)
                    .addComponent(bOnSale)
                    .addComponent(bTryGetRent)
                    .addComponent(bTryBuy)
                    .addComponent(bMyOffers))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(rbMyTeam)
                        .addGap(38, 38, 38)
                        .addComponent(rbAllTeams)
                        .addGap(38, 38, 38)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbMyTeam)
                            .addComponent(rbAllTeams)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rbMyTeamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbMyTeamActionPerformed
        researchPlayers();
        bTryBuy.setEnabled(false);
        bTryGetRent.setEnabled(false);
    }//GEN-LAST:event_rbMyTeamActionPerformed

    private void researchPlayers() {
        filter.setFirstParameter(tfFromEquals.getText());
        filter.setSecondParameter(tfTo.getText());
        if (rbMyTeam.isSelected()) {
            tableTransfers.setRowSelectionInterval(0, 0);
            if (cbFilterEnable.isSelected()) {
                transferPlayers = TransferMarket.getInstance().getTransfersByTeam(transferStatus, team, filter);
            } else {
                transferPlayers = TransferMarket.getInstance().getTransfersByTeamWithoutFilter(team);
            }
        } else if (cbFilterEnable.isSelected()) {
            transferPlayers = TransferMarket.getInstance().getTransfers(transferStatus, filter);
        } else {
            transferPlayers = TransferMarket.getInstance().getTransfersWithoutFilter();
        }
        tableTransfers.getRowSorter().modelStructureChanged();
        tableTransfers.updateUI();
    }

    private void makeTransferOfferToPlayer() {
        TransferPlayer transferPlayer = getSelectedTransferAll();
        if (transferPlayer != null) {
            if (!team.containsPlayer(transferPlayer.getPlayer())) {
                if (transferPlayer.getOffers() != null && transferPlayer.getOffers().containsKey(team)) {
                    JOptionPane.showMessageDialog(null, "Предложение этому игроку уже сделано!");
                } else {
                    TransferOfferForm offerForm = new TransferOfferForm(null, true);
                    offerForm.setParams(transferPlayer, team, TransferStatus.ON_TRANSFER);
                    offerForm.setVisible(true);
                }
            }
        }
    }

    private void makeRentOfferToPlayer() {
        TransferPlayer transferPlayer = getSelectedTransferAll();
        if (transferPlayer != null) {
            if (!team.containsPlayer(transferPlayer.getPlayer())) {
                if (transferPlayer.getOffers() != null && transferPlayer.getOffers().containsKey(team)) {
                    JOptionPane.showMessageDialog(null, "Предложение этому игроку уже сделано!");
                } else {
                    TransferOfferForm offerForm = new TransferOfferForm(null, true);
                    offerForm.setParams(transferPlayer, team, TransferStatus.TO_RENT);
                    offerForm.setVisible(true);
                }
            }
        }

    }

    private TransferPlayer getSelectedTransferAll() {
        int selectedIndex = tableTransfers.getSelectedRow();
        if (selectedIndex >= 0 && selectedIndex < transferPlayers.size()) {
            int index = tableTransfers.convertRowIndexToModel(selectedIndex);
            return transferPlayers.get(index);
        }
        return null;
    }

    private TransferPlayer getSelectedTransfer() {
        TransferPlayer transferPlayer = getSelectedTransferAll();
        if (team.containsPlayer(transferPlayer.getPlayer())) {
            return transferPlayer;
        } else {
            return null;
        }

    }

    private void onTransfer(TransferPlayer transferPlayer) throws HeadlessException {
        if (transferPlayer != null) {
            if (transferPlayer.getStatus() != TransferStatus.ON_TRANSFER) {
                int result = JOptionPane.showConfirmDialog(null, "Вы хотите выставить этого "
                        + "игрока на трансфер?", "Подтверждение", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    team.onTransfer(transferPlayer.getPlayer());
                    tableTransfers.updateUI();
                }
            }
        }
    }

    private void toRent(TransferPlayer transferPlayer) throws HeadlessException {
        if (transferPlayer != null) {
            if (transferPlayer.getStatus() != TransferStatus.TO_RENT) {
                int result = JOptionPane.showConfirmDialog(null, "Вы хотите отдать этого "
                        + "игрока в аренду?", "Подтверждение", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    team.onRent(transferPlayer.getPlayer());
                    tableTransfers.updateUI();
                }
            }
        }
    }

    private void rbForSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbForSaleActionPerformed
        transferStatus = TransferStatus.ON_TRANSFER;
    }//GEN-LAST:event_rbForSaleActionPerformed

    private void rbForRentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbForRentActionPerformed
        transferStatus = TransferStatus.TO_RENT;
    }//GEN-LAST:event_rbForRentActionPerformed

    private void rbOnSaleOrRentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbOnSaleOrRentActionPerformed
        transferStatus = TransferStatus.ON_TRANSFER_OR_RENT;
    }//GEN-LAST:event_rbOnSaleOrRentActionPerformed

    private void rbAnyStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAnyStatusActionPerformed
        transferStatus = TransferStatus.ANY;
    }//GEN-LAST:event_rbAnyStatusActionPerformed

    private void bApplyFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bApplyFilterActionPerformed
        researchPlayers();
    }//GEN-LAST:event_bApplyFilterActionPerformed

    private void rbByPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbByPositionActionPerformed
        this.transferFilterType = TransferFilterType.BY_LOCAL_POSITION;
    }//GEN-LAST:event_rbByPositionActionPerformed

    private void rbByGlobalPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbByGlobalPositionActionPerformed
        this.transferFilterType = TransferFilterType.BY_GLOBAL_POSITION;
    }//GEN-LAST:event_rbByGlobalPositionActionPerformed

    private void rbByAverageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbByAverageActionPerformed
        this.transferFilterType = TransferFilterType.BY_AVERAGE;
    }//GEN-LAST:event_rbByAverageActionPerformed

    private void rbByAgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbByAgeActionPerformed
        this.transferFilterType = TransferFilterType.BY_AGE;
    }//GEN-LAST:event_rbByAgeActionPerformed

    private void rbByNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbByNameActionPerformed
        this.transferFilterType = TransferFilterType.BY_NAME;
    }//GEN-LAST:event_rbByNameActionPerformed

    private void comboBoxConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxConditionActionPerformed
        filter.setCondition(Condition.getByIndex(comboBoxCondition.getSelectedIndex()));
    }//GEN-LAST:event_comboBoxConditionActionPerformed

    private void comboPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboPositionActionPerformed
        filter.setFirstParameter(GlobalPosition.getByIndex(comboPosition.getSelectedIndex()));
    }//GEN-LAST:event_comboPositionActionPerformed

    private void rbAllTeamsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAllTeamsActionPerformed
        bTryBuy.setEnabled(true);
        bTryGetRent.setEnabled(true);
        researchPlayers();
    }//GEN-LAST:event_rbAllTeamsActionPerformed

    private void bTryBuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTryBuyActionPerformed
        makeTransferOfferToPlayer();
    }//GEN-LAST:event_bTryBuyActionPerformed

    private void bOnSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOnSaleActionPerformed
        TransferPlayer selectedTransfer = getSelectedTransfer();
        onTransfer(selectedTransfer);
    }//GEN-LAST:event_bOnSaleActionPerformed

    private void bToRentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bToRentActionPerformed
        TransferPlayer selectedTransfer = getSelectedTransfer();
        toRent(selectedTransfer);
    }//GEN-LAST:event_bToRentActionPerformed

    private void bTryGetRentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTryGetRentActionPerformed
        makeRentOfferToPlayer();
    }//GEN-LAST:event_bTryGetRentActionPerformed

    private void bMyOffersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMyOffersActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bMyOffersActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bApplyFilter;
    private javax.swing.JButton bMyOffers;
    private javax.swing.JButton bOnSale;
    private javax.swing.JButton bToRent;
    private javax.swing.JButton bTryBuy;
    private javax.swing.JButton bTryGetRent;
    private javax.swing.ButtonGroup bgFilterGroup;
    private javax.swing.ButtonGroup bgTeamGroup;
    private javax.swing.ButtonGroup bgTransferStatus;
    private javax.swing.JCheckBox cbFilterEnable;
    private javax.swing.JComboBox<String> comboBoxCondition;
    private javax.swing.JComboBox<String> comboPosition;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rbAllTeams;
    private javax.swing.JRadioButton rbAnyStatus;
    private javax.swing.JRadioButton rbByAge;
    private javax.swing.JRadioButton rbByAverage;
    private javax.swing.JRadioButton rbByGlobalPosition;
    private javax.swing.JRadioButton rbByName;
    private javax.swing.JRadioButton rbByPosition;
    private javax.swing.JRadioButton rbForRent;
    private javax.swing.JRadioButton rbForSale;
    private javax.swing.JRadioButton rbMyTeam;
    private javax.swing.JRadioButton rbOnSaleOrRent;
    private javax.swing.JTable tableTransfers;
    private javax.swing.JTextField tfFromEquals;
    private javax.swing.JTextField tfTo;
    // End of variables declaration//GEN-END:variables

}
