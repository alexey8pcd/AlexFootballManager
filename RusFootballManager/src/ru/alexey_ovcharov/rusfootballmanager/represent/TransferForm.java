package ru.alexey_ovcharov.rusfootballmanager.represent;

import java.awt.Component;
import java.awt.HeadlessException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import ru.alexey_ovcharov.rusfootballmanager.common.util.RenderUtil;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Filter;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.GlobalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.LocalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Offer;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Market;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Transfer;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Status;

/**
 *
 * @author Алексей
 */
public class TransferForm extends javax.swing.JDialog {

    private Team team;
    private List<Transfer> transferPlayers = Collections.EMPTY_LIST;
    private Filter filter = new Filter();
    private boolean filtered = false;

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

    private String[] localPositionCurrentData;
    private String[][] localPositionData;
    private String[] empty = {};

    {
        EnumSet<LocalPosition> defenders = GlobalPosition.DEFENDER.getLocalPositions();
        EnumSet<LocalPosition> midfielders = GlobalPosition.MIDFIELDER.getLocalPositions();
        EnumSet<LocalPosition> forwards = GlobalPosition.FORWARD.getLocalPositions();
        localPositionData = new String[][]{
            namesToArray(defenders),
            namesToArray(midfielders),
            namesToArray(forwards)
        };
        localPositionCurrentData = empty;
    }

    private String[] namesToArray(EnumSet<LocalPosition> positions) {
        int index = 1;
        String[] result = new String[positions.size() + 1];
        result[0] = "Не важно";
        for (LocalPosition position : positions) {
            result[index++] = position.getAbreviation();
        }
        return result;
    }

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
            Transfer transfer = transferPlayers.get(row);
            if (transfer != null) {
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
        initLocation();
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
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(localPositionCurrentData);
        comboLocal.setModel(model);
        comboLocal.setEnabled(false);
    }

    public void setTeam(Team team) {
        this.team = team;
        filter.setTransferStatus(Status.ANY);
        rbAnyStatus.setEnabled(true);
        transferPlayers = Market.getInstance().getTransfers(team);
        tableTransfers.updateUI();
    }

    private void researchPlayersWithoutFilter() {
        if (rbMyTeam.isSelected()) {
            transferPlayers = Market.getInstance().getTransfers(team);
        } else {
            transferPlayers = Market.getInstance().getTransfers();
        }
        tableTransfers.getSelectionModel().clearSelection();
        tableTransfers.getRowSorter().modelStructureChanged();
        tableTransfers.updateUI();
    }

    private void researchPlayersWithFilter() {
        filter.setName(tfName.getText());
        filter.setAgeFrom(cbAgeFrom.isSelected()
                ? (int) spinnerAgeFrom.getValue() : Filter.DEFAULT_VALUE);
        filter.setAgeTo(cbAgeTo.isSelected()
                ? (int) spinnerAgeTo.getValue() : Filter.DEFAULT_VALUE);
        filter.setAvgFrom(cbAvgFrom.isSelected()
                ? (int) spinnerAvgFrom.getValue() : Filter.DEFAULT_VALUE);
        filter.setAvgTo(cbAvgTo.isSelected()
                ? (int) spinnerAvgTo.getValue() : Filter.DEFAULT_VALUE);
        if (rbMyTeam.isSelected()) {
            transferPlayers = Market.getInstance().getTransfers(team, filter);
        } else {
            transferPlayers = Market.getInstance().getTransfers(filter);
        }
        tableTransfers.getSelectionModel().clearSelection();
        tableTransfers.getRowSorter().modelStructureChanged();
        tableTransfers.updateUI();
    }

    private void makeTransferOfferToPlayer() {
        Transfer transferPlayer = getSelectedTransferAll();
        if (transferPlayer != null && !team.containsPlayer(transferPlayer.getPlayer())) {
            List<Offer> offers = Market.getInstance().getOffers(team);
            boolean did = false;
            for (Offer offer : offers) {
                if (offer.getPlayer() == transferPlayer.getPlayer()) {
                    did = true;
                    break;
                }
            }
            if (did) {
                JOptionPane.showMessageDialog(null, "Предложение этому игроку уже сделано!");
            } else {
                TransferOfferForm offerForm = new TransferOfferForm(null, true);
                offerForm.setParams(transferPlayer, team, Status.ON_TRANSFER);
                offerForm.setVisible(true);
            }
        }
    }

    private void makeRentOfferToPlayer() {
        Transfer transferPlayer = getSelectedTransferAll();
        if (transferPlayer != null) {
            if (!team.containsPlayer(transferPlayer.getPlayer())) {
                List<Offer> myOffers = Market.getInstance().getOffers(team);
                boolean did = false;
                for (Offer myOffer : myOffers) {
                    if (myOffer.getPlayer() == transferPlayer.getPlayer()) {
                        did = true;
                        break;
                    }
                }
                if (did) {
                    TransferOfferForm offerForm = new TransferOfferForm(null, true);
                    offerForm.setParams(transferPlayer, team, Status.TO_RENT);
                    offerForm.setVisible(true);
                }
            }
        }

    }

    private Transfer getSelectedTransferAll() {
        int selectedIndex = tableTransfers.getSelectedRow();
        if (selectedIndex >= 0 && selectedIndex < transferPlayers.size()) {
            int index = tableTransfers.convertRowIndexToModel(selectedIndex);
            return transferPlayers.get(index);
        }
        return null;
    }

    private Transfer getSelectedTransfer() {
        Transfer transferPlayer = getSelectedTransferAll();
        if (team.containsPlayer(transferPlayer.getPlayer())) {
            return transferPlayer;
        } else {
            return null;
        }

    }

    private void onTransfer(Transfer transferPlayer) throws HeadlessException {
        if (transferPlayer != null) {
            if (transferPlayer.getStatus() != Status.ON_TRANSFER) {
                int result = JOptionPane.showConfirmDialog(null, "Вы хотите выставить этого "
                        + "игрока на трансфер?", "Подтверждение", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    team.onTransfer(transferPlayer.getPlayer());
                    tableTransfers.updateUI();
                }
            }
        }
    }

    private void toRent(Transfer transferPlayer) throws HeadlessException {
        if (transferPlayer != null) {
            if (transferPlayer.getStatus() != Status.TO_RENT) {
                int result = JOptionPane.showConfirmDialog(null, "Вы хотите отдать этого "
                        + "игрока в аренду?", "Подтверждение", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    team.onRent(transferPlayer.getPlayer());
                    tableTransfers.updateUI();
                }
            }
        }
    }

    private void globalPositionComboAction() {
        int index = comboGlobal.getSelectedIndex();
        if (index > 1 && index < 5) {
            localPositionCurrentData = localPositionData[index - 2];
            comboLocal.setEnabled(true);
            switch (index) {
                case 2:
                    filter.setGlobalPosition(GlobalPosition.DEFENDER);
                    break;
                case 3:
                    filter.setGlobalPosition(GlobalPosition.MIDFIELDER);
                    break;
                case 4:
                    filter.setGlobalPosition(GlobalPosition.FORWARD);
            }
            filter.setLocalPosition(null);
        } else {
            localPositionCurrentData = empty;
            if (index == 0) {
                filter.setGlobalPosition(null);
            } else {
                filter.setGlobalPosition(GlobalPosition.GOALKEEPER);
            }
            comboLocal.setEnabled(false);
        }
        DefaultComboBoxModel<String> model
                = new DefaultComboBoxModel<>(localPositionCurrentData);
        comboLocal.setModel(model);
        comboLocal.updateUI();
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
        comboGlobal = new javax.swing.JComboBox<>();
        comboLocal = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        rbAnyStatus = new javax.swing.JRadioButton();
        rbForSale = new javax.swing.JRadioButton();
        rbForRent = new javax.swing.JRadioButton();
        rbOnSaleOrRent = new javax.swing.JRadioButton();
        rbFreeAgentStatus = new javax.swing.JRadioButton();
        tfName = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        cbAgeFrom = new javax.swing.JCheckBox();
        cbAgeTo = new javax.swing.JCheckBox();
        cbAvgFrom = new javax.swing.JCheckBox();
        cbAvgTo = new javax.swing.JCheckBox();
        spinnerAgeFrom = new javax.swing.JSpinner();
        spinnerAgeTo = new javax.swing.JSpinner();
        spinnerAvgFrom = new javax.swing.JSpinner();
        spinnerAvgTo = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        bApplyFilter = new javax.swing.JButton();
        bClearFilter = new javax.swing.JButton();
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

        comboGlobal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Не важно", "Вратарь", "Защитник", "Полузащитник", "Нападающий" }));
        comboGlobal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboGlobalActionPerformed(evt);
            }
        });

        comboLocal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Не важно" }));
        comboLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboLocalActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Статус"));

        bgTransferStatus.add(rbAnyStatus);
        rbAnyStatus.setSelected(true);
        rbAnyStatus.setText("Не важно");
        rbAnyStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAnyStatusActionPerformed(evt);
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

        bgTransferStatus.add(rbFreeAgentStatus);
        rbFreeAgentStatus.setText("Свободный агент");
        rbFreeAgentStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbFreeAgentStatusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbAnyStatus)
                    .addComponent(rbForSale)
                    .addComponent(rbForRent)
                    .addComponent(rbOnSaleOrRent)
                    .addComponent(rbFreeAgentStatus))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(rbAnyStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbForSale)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbForRent)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbOnSaleOrRent)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbFreeAgentStatus)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        cbAgeFrom.setText("Возраст от:");
        cbAgeFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAgeFromActionPerformed(evt);
            }
        });

        cbAgeTo.setText("Возраст до:");
        cbAgeTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAgeToActionPerformed(evt);
            }
        });

        cbAvgFrom.setText("Общее от:");
        cbAvgFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAvgFromActionPerformed(evt);
            }
        });

        cbAvgTo.setText("Общее до:");
        cbAvgTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAvgToActionPerformed(evt);
            }
        });

        spinnerAgeFrom.setModel(new javax.swing.SpinnerNumberModel(16, 16, 36, 1));
        spinnerAgeFrom.setEnabled(false);

        spinnerAgeTo.setModel(new javax.swing.SpinnerNumberModel(36, 16, 36, 1));
        spinnerAgeTo.setEnabled(false);

        spinnerAvgFrom.setModel(new javax.swing.SpinnerNumberModel(50, 1, 90, 5));
        spinnerAvgFrom.setEnabled(false);

        spinnerAvgTo.setModel(new javax.swing.SpinnerNumberModel(50, 25, 99, 5));
        spinnerAvgTo.setEnabled(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbAgeFrom)
                    .addComponent(cbAgeTo)
                    .addComponent(cbAvgFrom)
                    .addComponent(cbAvgTo))
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spinnerAgeFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerAgeTo, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerAvgFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerAvgTo, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbAgeFrom)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(spinnerAgeFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbAgeTo)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(spinnerAgeTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbAvgFrom)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(spinnerAvgFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(3, 3, 3)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbAvgTo)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(spinnerAvgTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText("Амплуа");

        jLabel2.setText("Позиция");

        jLabel3.setText("Имя/фамилия");

        bApplyFilter.setText("Отфильтровать");
        bApplyFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bApplyFilterActionPerformed(evt);
            }
        });

        bClearFilter.setText("Сбросить");
        bClearFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bClearFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(48, 48, 48)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboGlobal, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bApplyFilter)
                        .addGap(18, 18, 18)
                        .addComponent(bClearFilter)
                        .addGap(67, 67, 67)))
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboGlobal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(comboLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bApplyFilter)
                            .addComponent(bClearFilter)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        bTryBuy.setEnabled(false);
        bTryGetRent.setEnabled(false);
        if (filtered) {
            researchPlayersWithFilter();
        } else {
            researchPlayersWithoutFilter();
        }
    }//GEN-LAST:event_rbMyTeamActionPerformed

    private void rbForSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbForSaleActionPerformed
        filter.setTransferStatus(Status.ON_TRANSFER);
    }//GEN-LAST:event_rbForSaleActionPerformed

    private void rbForRentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbForRentActionPerformed
        filter.setTransferStatus(Status.TO_RENT);
    }//GEN-LAST:event_rbForRentActionPerformed

    private void rbOnSaleOrRentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbOnSaleOrRentActionPerformed
        filter.setTransferStatus(Status.ON_TRANSFER_OR_RENT);
    }//GEN-LAST:event_rbOnSaleOrRentActionPerformed

    private void rbAnyStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAnyStatusActionPerformed
        filter.setTransferStatus(Status.ANY);
    }//GEN-LAST:event_rbAnyStatusActionPerformed

    private void comboGlobalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboGlobalActionPerformed
        globalPositionComboAction();
    }//GEN-LAST:event_comboGlobalActionPerformed

    private void rbAllTeamsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAllTeamsActionPerformed
        bTryBuy.setEnabled(true);
        bTryGetRent.setEnabled(true);
        if (filtered) {
            researchPlayersWithFilter();
        } else {
            researchPlayersWithoutFilter();
        }
    }//GEN-LAST:event_rbAllTeamsActionPerformed

    private void bTryBuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTryBuyActionPerformed
        makeTransferOfferToPlayer();
    }//GEN-LAST:event_bTryBuyActionPerformed

    private void bOnSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOnSaleActionPerformed
        Transfer selectedTransfer = getSelectedTransfer();
        onTransfer(selectedTransfer);
    }//GEN-LAST:event_bOnSaleActionPerformed

    private void bToRentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bToRentActionPerformed
        Transfer selectedTransfer = getSelectedTransfer();
        toRent(selectedTransfer);
    }//GEN-LAST:event_bToRentActionPerformed

    private void bTryGetRentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTryGetRentActionPerformed
        makeRentOfferToPlayer();
    }//GEN-LAST:event_bTryGetRentActionPerformed

    private void bMyOffersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMyOffersActionPerformed
        MyOffersForm myOffersForm = new MyOffersForm(null, true);
        myOffersForm.setTeam(team);
        myOffersForm.setVisible(true);
    }//GEN-LAST:event_bMyOffersActionPerformed

    private void rbFreeAgentStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbFreeAgentStatusActionPerformed
        filter.setTransferStatus(Status.FREE_AGENT);
    }//GEN-LAST:event_rbFreeAgentStatusActionPerformed

    private void comboLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboLocalActionPerformed
        filter.setLocalPosition(LocalPosition.getByAbreviation(
                comboLocal.getSelectedItem().toString()));
    }//GEN-LAST:event_comboLocalActionPerformed

    private void cbAgeFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAgeFromActionPerformed
        spinnerAgeFrom.setEnabled(cbAgeFrom.isSelected());
    }//GEN-LAST:event_cbAgeFromActionPerformed

    private void cbAgeToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAgeToActionPerformed
        spinnerAgeTo.setEnabled(cbAgeTo.isSelected());
    }//GEN-LAST:event_cbAgeToActionPerformed

    private void cbAvgFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAvgFromActionPerformed
        spinnerAvgFrom.setEnabled(cbAvgFrom.isSelected());
    }//GEN-LAST:event_cbAvgFromActionPerformed

    private void cbAvgToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAvgToActionPerformed
        spinnerAvgTo.setEnabled(cbAvgTo.isSelected());
    }//GEN-LAST:event_cbAvgToActionPerformed

    private void bApplyFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bApplyFilterActionPerformed
        filtered = true;
        researchPlayersWithFilter();
    }//GEN-LAST:event_bApplyFilterActionPerformed

    private void bClearFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bClearFilterActionPerformed
        filtered = false;
        researchPlayersWithoutFilter();
    }//GEN-LAST:event_bClearFilterActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bApplyFilter;
    private javax.swing.JButton bClearFilter;
    private javax.swing.JButton bMyOffers;
    private javax.swing.JButton bOnSale;
    private javax.swing.JButton bToRent;
    private javax.swing.JButton bTryBuy;
    private javax.swing.JButton bTryGetRent;
    private javax.swing.ButtonGroup bgFilterGroup;
    private javax.swing.ButtonGroup bgTeamGroup;
    private javax.swing.ButtonGroup bgTransferStatus;
    private javax.swing.JCheckBox cbAgeFrom;
    private javax.swing.JCheckBox cbAgeTo;
    private javax.swing.JCheckBox cbAvgFrom;
    private javax.swing.JCheckBox cbAvgTo;
    private javax.swing.JComboBox<String> comboGlobal;
    private javax.swing.JComboBox<String> comboLocal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rbAllTeams;
    private javax.swing.JRadioButton rbAnyStatus;
    private javax.swing.JRadioButton rbForRent;
    private javax.swing.JRadioButton rbForSale;
    private javax.swing.JRadioButton rbFreeAgentStatus;
    private javax.swing.JRadioButton rbMyTeam;
    private javax.swing.JRadioButton rbOnSaleOrRent;
    private javax.swing.JSpinner spinnerAgeFrom;
    private javax.swing.JSpinner spinnerAgeTo;
    private javax.swing.JSpinner spinnerAvgFrom;
    private javax.swing.JSpinner spinnerAvgTo;
    private javax.swing.JTable tableTransfers;
    private javax.swing.JTextField tfName;
    // End of variables declaration//GEN-END:variables

    private void initLocation() {
        setLocationRelativeTo(null);
    }

}
