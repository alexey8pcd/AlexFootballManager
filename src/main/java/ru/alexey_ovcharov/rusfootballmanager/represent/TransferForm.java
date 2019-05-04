package ru.alexey_ovcharov.rusfootballmanager.represent;

import ru.alexey_ovcharov.rusfootballmanager.career.User;
import ru.alexey_ovcharov.rusfootballmanager.common.MoneyHelper;
import ru.alexey_ovcharov.rusfootballmanager.common.util.RenderUtil;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.GlobalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.LocalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.*;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.*;

/**
 * @author Алексей
 */
@SuppressWarnings("ALL")
public class TransferForm extends javax.swing.JDialog {

    private static final int PAGE_SIZE = 18;
    private static final String ANY = "Не важно";
    private static final String NAME_AND_LAST_NAME = "Имя/фамилия";
    private static final String AGE = "Возраст";
    private static final String[] HEADERS = {
            NAME_AND_LAST_NAME,
            AGE,
            "Амплуа",
            "Позиция",
            "Общее",
            "Статус",
            "Стоимость",
            "Команда"
    };
    private static final String[] EMPTY = {};
    private static final int COLUMN_NAME_AND_LAST_NAME = 0;
    private static final int COLUMN_AGE = 1;
    private static final int COLUMN_GLOBAL_POSITION = 2;
    private static final int COLUMN_LOCAL_POSITION = 3;
    private static final int COLUMN_AVERAGE = 4;
    private static final int COLUMN_TRANSFER_STATUS = 5;
    private static final int COLUMN_COST = 6;
    private static final int COLUMN_TEAM_NAME = 7;

    private final transient Market market = Market.getInstance();

    private transient Team team;
    private transient List<Transfer> transferPlayers = Collections.emptyList();
    private final transient Filter filter = new Filter();
    private transient String[] localPositionCurrentData;
    private transient String[][] localPositionData;
    private transient LocalDate date;
    private transient User user;
    private transient int pageIndex;

    private static String[] namesToArray(Set<LocalPosition> positions) {
        int index = 1;
        String[] result = new String[positions.size() + 1];
        result[0] = ANY;
        for (LocalPosition position : positions) {
            result[index++] = position.getAbreviation();
        }
        return result;
    }

    TransferForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }

    private void init() {
        Set<LocalPosition> defenders = GlobalPosition.DEFENDER.getLocalPositions();
        Set<LocalPosition> midfielders = GlobalPosition.MIDFIELDER.getLocalPositions();
        Set<LocalPosition> forwards = GlobalPosition.FORWARD.getLocalPositions();
        localPositionData = new String[][]{
                namesToArray(defenders),
                namesToArray(midfielders),
                namesToArray(forwards)
        };
        localPositionCurrentData = EMPTY;
        TableModel transferTableModel = new MyDefaultTableModel();
        tableTransfers.setModel(transferTableModel);
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(transferTableModel);
        tableTransfers.setRowSorter(rowSorter);
        int width = tableTransfers.getWidth();
        float onePercentWidth = width / 100f;

        TableColumnModel columnModel = tableTransfers.getColumnModel();
        int[] columnSizeValues = {
                20, 8, 8, 8, 8, 10, 12, 26
        };
        for (int i = 0; i < columnSizeValues.length - 1; i++) {
            int sizeValue = columnSizeValues[i];
            TableColumn column = columnModel.getColumn(i);
            column.setResizable(false);
            column.setMaxWidth(Math.round(onePercentWidth * sizeValue));//20%
            column.setPreferredWidth(Math.round(onePercentWidth * sizeValue));
        }

        TableColumn columnAverage = columnModel.getColumn(COLUMN_AVERAGE);
        columnAverage.setCellRenderer(new MyTableCellRenderer());

        TableColumn columnCost = columnModel.getColumn(COLUMN_COST);
        columnCost.setCellRenderer(new MyTableCellRenderer());

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(localPositionCurrentData);
        comboLocal.setModel(model);
        comboLocal.setEnabled(false);
    }

    public void setParams(Team team, User user) {
        this.team = team;
        this.date = user.getCurrentDate();
        this.user = user;
        updateBudgetLabel();
        filter.setTransferStatus(TransferStatus.ANY);
        long budget = team.getBudget();
        spinnerPrice.setValue((int) Math.min(budget, Integer.MAX_VALUE));
        SpinnerModel model = spinnerPrice.getModel();
        if (model instanceof SpinnerNumberModel) {
            ((SpinnerNumberModel) model).setStepSize(budget / 20);
        }
        radioButtonAnyStatus.setEnabled(true);
        transferPlayers = market.getTransfers(team);
        int maxPage = transferPlayers.size() / PAGE_SIZE;
        updatePrevAndNextButtons(maxPage);
        tableTransfers.updateUI();
    }

    private void updateBudgetLabel() {
        long budget = team.getBudget();
        labelBudget.setText("Бюджет: " + MoneyHelper.formatSum(budget));
    }

    private void researchPlayersWithoutFilter() {
        if (radoButtonMyTeam.isSelected()) {
            transferPlayers = market.getTransfers(team);
        } else {
            transferPlayers = market.getTransfers();
        }
        tableTransfers.getSelectionModel().clearSelection();
        tableTransfers.getRowSorter().modelStructureChanged();
        tableTransfers.updateUI();
    }

    private void researchPlayersWithFilter() {
        filter.setName(tfName.getText());
        filter.setAgeFrom(checkBoxAgeFrom.isSelected()
                ? (int) spinnerAgeFrom.getValue() : Filter.DEFAULT_VALUE);
        filter.setAgeTo(checkBoxAgeTo.isSelected()
                ? (int) spinnerAgeTo.getValue() : Filter.DEFAULT_VALUE);
        filter.setAvgFrom(checkBoxAvgFrom.isSelected()
                ? (int) spinnerAvgFrom.getValue() : Filter.DEFAULT_VALUE);
        filter.setAvgTo(checkBoxAvgTo.isSelected()
                ? (int) spinnerAvgTo.getValue() : Filter.DEFAULT_VALUE);
        if (checkBoxPriceLow.isSelected()) {
            filter.setPriceLow((int) spinnerPrice.getValue());
        } else {
            filter.setPriceLow(Integer.MAX_VALUE);
        }
        if (radoButtonMyTeam.isSelected()) {
            transferPlayers = market.getTransfers(team, filter);
        } else {
            transferPlayers = market.getTransfers(filter);
        }
        pageIndex = 0;
        int maxPage = transferPlayers.size() / PAGE_SIZE;
        updatePrevAndNextButtons(maxPage);
        tableTransfers.getSelectionModel().clearSelection();
        tableTransfers.getRowSorter().modelStructureChanged();
        tableTransfers.updateUI();
    }

    private void makeTransferOfferToPlayer() {
        Optional<Transfer> transferPlayer = getSelectedTransferAll();
        transferPlayer.ifPresent(transfer -> {
            Player player = transfer.getPlayer();
            if (!team.containsPlayer(player)) {
                List<Offer> offers = market.getOffersBuy(team);
                boolean did = offers.stream()
                                    .map(Offer::getPlayer)
                                    .anyMatch(player1 -> player1 == player);
                if (did) {
                    JOptionPane.showMessageDialog(null, "Предложение этому игроку уже сделано!");
                } else {
                    int cost = transfer.getCost();
                    if (cost > team.getBudget()) {
                        JOptionPane.showMessageDialog(null, "В вашем бюджете недостаточно средств!");
                    } else {
                        TransferOfferForm offerForm = new TransferOfferForm(null, true);
                        offerForm.setLocationRelativeTo(this);
                        offerForm.setParams(transfer, team, TransferStatus.ON_TRANSFER, date, user, Offer.OfferType.BUY);
                        offerForm.setVisible(true);
                        updateBudgetLabel();
                    }
                }
            }
        });

    }

    private void makeRentOfferToPlayer() {
        Optional<Transfer> transferPlayer = getSelectedTransferAll();
        transferPlayer.ifPresent(transfer -> {
            Player player = transfer.getPlayer();
            if (!team.containsPlayer(player)) {
                List<Offer> myOffers = market.getOffersBuy(team);
                boolean did = myOffers.stream()
                                      .map(Offer::getPlayer)
                                      .anyMatch(player1 -> player1 == player);
                if (did) {
                    TransferOfferForm offerForm = new TransferOfferForm(null, true);
                    offerForm.setParams(transfer, team, TransferStatus.TO_RENT, date, user, Offer.OfferType.RENT);
                    offerForm.setVisible(true);
                }
            }
        });
    }

    @Nonnull
    private Optional<Transfer> getSelectedTransferAll() {
        int selectedIndex = tableTransfers.getSelectedRow();
        if (selectedIndex >= 0 && selectedIndex < transferPlayers.size()) {
            int index = tableTransfers.convertRowIndexToModel(selectedIndex);
            return Optional.ofNullable(transferPlayers.get(index));
        }
        return Optional.empty();
    }

    private Optional<Transfer> getSelectedTransfer() {
        Optional<Transfer> transferPlayerOpt = getSelectedTransferAll();
        return transferPlayerOpt.filter(transfer -> team.containsPlayer(transfer.getPlayer()));
    }

    private void onTransfer(Transfer transferPlayer) {
        if (transferPlayer != null && transferPlayer.getTransferStatus() != TransferStatus.ON_TRANSFER) {
            int result = JOptionPane.showConfirmDialog(null, "Вы хотите выставить этого "
                    + "игрока на трансфер?", "Подтверждение", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                team.onTransfer(transferPlayer.getPlayer());
                tableTransfers.updateUI();
            }
        }
    }

    private void toRent(Transfer transferPlayer) {
        if (transferPlayer != null && transferPlayer.getTransferStatus() != TransferStatus.TO_RENT) {
            int result = JOptionPane.showConfirmDialog(null, "Вы хотите отдать этого "
                    + "игрока в аренду?", "Подтверждение", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                team.onRent(transferPlayer.getPlayer());
                tableTransfers.updateUI();
            }
        }
    }

    private void onSale() {
        Optional<Transfer> selectedTransferOpt = getSelectedTransfer();
        selectedTransferOpt.ifPresent(this::onTransfer);
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
            localPositionCurrentData = EMPTY;
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
        jPanel1 = new javax.swing.JPanel();
        comboGlobal = new javax.swing.JComboBox<>();
        comboLocal = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        radioButtonAnyStatus = new javax.swing.JRadioButton();
        radioButtonForSale = new javax.swing.JRadioButton();
        radioButtonForRent = new javax.swing.JRadioButton();
        radioButtonOnSaleOrRent = new javax.swing.JRadioButton();
        radioButtonFreeAgentStatus = new javax.swing.JRadioButton();
        tfName = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        checkBoxAgeFrom = new javax.swing.JCheckBox();
        checkBoxAgeTo = new javax.swing.JCheckBox();
        checkBoxAvgFrom = new javax.swing.JCheckBox();
        checkBoxAvgTo = new javax.swing.JCheckBox();
        spinnerAgeFrom = new javax.swing.JSpinner();
        spinnerAgeTo = new javax.swing.JSpinner();
        spinnerAvgFrom = new javax.swing.JSpinner();
        spinnerAvgTo = new javax.swing.JSpinner();
        spinnerPrice = new javax.swing.JSpinner();
        checkBoxPriceLow = new javax.swing.JCheckBox();
        labelGlobalPosition = new javax.swing.JLabel();
        labelPosition = new javax.swing.JLabel();
        labelNameAndLastName = new javax.swing.JLabel();
        bApplyFilter = new javax.swing.JButton();
        bClearFilter = new javax.swing.JButton();
        radoButtonMyTeam = new javax.swing.JRadioButton();
        radioButtonAllTeams = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        bToRent = new javax.swing.JButton();
        bOnSale = new javax.swing.JButton();
        bTryGetRent = new javax.swing.JButton();
        bTryBuy = new javax.swing.JButton();
        bMyOffers = new javax.swing.JButton();
        labelBudget = new javax.swing.JLabel();
        labelPage = new javax.swing.JLabel();
        buttonNextPage = new javax.swing.JButton();
        buttonPrevPage = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Трансферный рынок");
        setResizable(false);

        tableTransfers.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null}
                },
                new String[]{
                        "Имя/фамилия", "Возраст", "Амплуа", "Позиция", "Общее", "Статус", "Стоимость", "Команда"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableTransfers);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Фильтр"));

        comboGlobal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Не важно", "Вратарь", "Защитник", "Полузащитник", "Нападающий"}));
        comboGlobal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboGlobalActionPerformed(evt);
            }
        });

        comboLocal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Не важно"}));
        comboLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboLocalActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Статус"));

        bgTransferStatus.add(radioButtonAnyStatus);
        radioButtonAnyStatus.setSelected(true);
        radioButtonAnyStatus.setText("Не важно");
        radioButtonAnyStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonAnyStatusActionPerformed(evt);
            }
        });

        bgTransferStatus.add(radioButtonForSale);
        radioButtonForSale.setText("На продажу");
        radioButtonForSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonForSaleActionPerformed(evt);
            }
        });

        bgTransferStatus.add(radioButtonForRent);
        radioButtonForRent.setText("В аренду");
        radioButtonForRent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonForRentActionPerformed(evt);
            }
        });

        bgTransferStatus.add(radioButtonOnSaleOrRent);
        radioButtonOnSaleOrRent.setText("На продажу или в аренду");
        radioButtonOnSaleOrRent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonOnSaleOrRentActionPerformed(evt);
            }
        });

        bgTransferStatus.add(radioButtonFreeAgentStatus);
        radioButtonFreeAgentStatus.setText("Свободный агент");
        radioButtonFreeAgentStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonFreeAgentStatusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                             .addGroup(jPanel3Layout.createSequentialGroup()
                                                    .addContainerGap()
                                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                           .addComponent(radioButtonAnyStatus)
                                                                           .addComponent(radioButtonForSale)
                                                                           .addComponent(radioButtonForRent)
                                                                           .addComponent(radioButtonOnSaleOrRent)
                                                                           .addComponent(radioButtonFreeAgentStatus))
                                                    .addContainerGap(9, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                             .addGroup(jPanel3Layout.createSequentialGroup()
                                                    .addComponent(radioButtonAnyStatus)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(radioButtonForSale)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(radioButtonForRent)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(radioButtonOnSaleOrRent)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(radioButtonFreeAgentStatus)
                                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        checkBoxAgeFrom.setText("Возраст от:");
        checkBoxAgeFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAgeFromActionPerformed(evt);
            }
        });

        checkBoxAgeTo.setText("Возраст до:");
        checkBoxAgeTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAgeToActionPerformed(evt);
            }
        });

        checkBoxAvgFrom.setText("Общее от:");
        checkBoxAvgFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgFromActionPerformed(evt);
            }
        });

        checkBoxAvgTo.setText("Общее до:");
        checkBoxAvgTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgToActionPerformed(evt);
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

        spinnerPrice.setModel(new javax.swing.SpinnerNumberModel(10000000, 10000, 999999999, 1000000));

        checkBoxPriceLow.setText("Цена до:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                             .addGroup(jPanel4Layout.createSequentialGroup()
                                                    .addContainerGap()
                                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                           .addComponent(checkBoxAgeFrom)
                                                                           .addComponent(checkBoxAgeTo)
                                                                           .addComponent(checkBoxAvgFrom)
                                                                           .addComponent(checkBoxAvgTo)
                                                                           .addComponent(checkBoxPriceLow))
                                                    .addGap(12, 12, 12)
                                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                           .addComponent(spinnerAvgTo)
                                                                           .addComponent(spinnerPrice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                                                           .addComponent(spinnerAgeFrom)
                                                                           .addComponent(spinnerAgeTo, javax.swing.GroupLayout.Alignment.TRAILING)
                                                                           .addComponent(spinnerAvgFrom, javax.swing.GroupLayout.Alignment.TRAILING))
                                                    .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                             .addGroup(jPanel4Layout.createSequentialGroup()
                                                    .addContainerGap()
                                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                           .addComponent(checkBoxAgeFrom)
                                                                           .addGroup(jPanel4Layout.createSequentialGroup()
                                                                                                  .addGap(1, 1, 1)
                                                                                                  .addComponent(spinnerAgeFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                    .addGap(8, 8, 8)
                                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                           .addComponent(checkBoxAgeTo)
                                                                           .addGroup(jPanel4Layout.createSequentialGroup()
                                                                                                  .addGap(1, 1, 1)
                                                                                                  .addComponent(spinnerAgeTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                    .addGap(8, 8, 8)
                                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                           .addComponent(checkBoxAvgFrom)
                                                                           .addGroup(jPanel4Layout.createSequentialGroup()
                                                                                                  .addGap(1, 1, 1)
                                                                                                  .addComponent(spinnerAvgFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                    .addGap(3, 3, 3)
                                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                           .addComponent(checkBoxAvgTo)
                                                                           .addGroup(jPanel4Layout.createSequentialGroup()
                                                                                                  .addGap(1, 1, 1)
                                                                                                  .addComponent(spinnerAvgTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                           .addComponent(spinnerPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                           .addComponent(checkBoxPriceLow))
                                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        labelGlobalPosition.setText("Амплуа");

        labelPosition.setText("Позиция");

        labelNameAndLastName.setText("Имя/фамилия");

        bApplyFilter.setText("<html>Отфильтровать");
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

        bgTeamGroup.add(radoButtonMyTeam);
        radoButtonMyTeam.setSelected(true);
        radoButtonMyTeam.setText("Моя команда");
        radoButtonMyTeam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radoButtonMyTeamActionPerformed(evt);
            }
        });

        bgTeamGroup.add(radioButtonAllTeams);
        radioButtonAllTeams.setText("Все команды");
        radioButtonAllTeams.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonAllTeamsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                             .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addContainerGap()
                                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                           .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                                  .addComponent(radoButtonMyTeam)
                                                                                                  .addGap(38, 38, 38)
                                                                                                  .addComponent(radioButtonAllTeams))
                                                                           .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                                  .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                         .addComponent(labelNameAndLastName)
                                                                                                                         .addComponent(labelPosition)
                                                                                                                         .addComponent(labelGlobalPosition))
                                                                                                  .addGap(18, 18, 18)
                                                                                                  .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                         .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                .addComponent(comboGlobal, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                .addComponent(comboLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                         .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                                                                                .addComponent(bApplyFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                                                .addComponent(bClearFilter)))))
                                                    .addGap(18, 18, 18)
                                                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                             .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                       .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                                       .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                                                                                                                                                         .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                                                                                                                .addComponent(radoButtonMyTeam)
                                                                                                                                                                                                                .addComponent(radioButtonAllTeams))
                                                                                                                                                                                         .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                                                                                         .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                                                                                                                .addComponent(comboGlobal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                                                                .addComponent(labelGlobalPosition))
                                                                                                                                                                                         .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                                                                                         .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                                                                                                                .addComponent(comboLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                                                                .addComponent(labelPosition))
                                                                                                                                                                                         .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                                                                                         .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                                                                                                                .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                                                                .addComponent(labelNameAndLastName))
                                                                                                                                                                                         .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                                                                                         .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                                                                                                                .addComponent(bApplyFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                                                                .addComponent(bClearFilter))
                                                                                                                                                                                         .addGap(0, 0, Short.MAX_VALUE))
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
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
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

        labelBudget.setText("Бюджет");

        labelPage.setText("Страница");

        buttonNextPage.setText(">");
        buttonNextPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNextPageActionPerformed(evt);
            }
        });

        buttonPrevPage.setText("<");
        buttonPrevPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrevPageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addGroup(layout.createSequentialGroup()
                                      .addContainerGap()
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                      .addComponent(jScrollPane1)
                                                      .addGroup(layout.createSequentialGroup()
                                                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                      .addComponent(labelBudget)
                                                                                      .addGroup(layout.createSequentialGroup()
                                                                                                      .addComponent(labelPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                      .addComponent(buttonPrevPage)
                                                                                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                      .addComponent(buttonNextPage)))
                                                                      .addGap(18, 18, 18)
                                                                      .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                      .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addGroup(layout.createSequentialGroup()
                                      .addContainerGap()
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                      .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                      .addComponent(buttonPrevPage)
                                                                                      .addComponent(buttonNextPage))
                                                                      .addGroup(layout.createSequentialGroup()
                                                                                      .addComponent(labelBudget)
                                                                                      .addGap(18, 18, 18)
                                                                                      .addComponent(labelPage))))
                                      .addGap(14, 14, 14)
                                      .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                      .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void radoButtonMyTeamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radoButtonMyTeamActionPerformed
        filterMyTeam();
    }//GEN-LAST:event_radoButtonMyTeamActionPerformed

    private void filterMyTeam() {
        bTryBuy.setEnabled(false);
        bTryGetRent.setEnabled(false);
    }

    private void radioButtonForSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonForSaleActionPerformed
        filter.setTransferStatus(TransferStatus.ON_TRANSFER);
    }//GEN-LAST:event_radioButtonForSaleActionPerformed

    private void radioButtonForRentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonForRentActionPerformed
        filter.setTransferStatus(TransferStatus.TO_RENT);
    }//GEN-LAST:event_radioButtonForRentActionPerformed

    private void radioButtonOnSaleOrRentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonOnSaleOrRentActionPerformed
        filter.setTransferStatus(TransferStatus.ON_TRANSFER_OR_RENT);
    }//GEN-LAST:event_radioButtonOnSaleOrRentActionPerformed

    private void radioButtonAnyStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonAnyStatusActionPerformed
        filter.setTransferStatus(TransferStatus.ANY);
    }//GEN-LAST:event_radioButtonAnyStatusActionPerformed

    private void comboGlobalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboGlobalActionPerformed
        globalPositionComboAction();
    }//GEN-LAST:event_comboGlobalActionPerformed

    private void radioButtonAllTeamsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonAllTeamsActionPerformed
        filterAllTeam();
    }//GEN-LAST:event_radioButtonAllTeamsActionPerformed

    private void filterAllTeam() {
        bTryBuy.setEnabled(true);
        bTryGetRent.setEnabled(true);
    }

    private void bTryBuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTryBuyActionPerformed
        makeTransferOfferToPlayer();
    }//GEN-LAST:event_bTryBuyActionPerformed

    private void bOnSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOnSaleActionPerformed
        onSale();
    }//GEN-LAST:event_bOnSaleActionPerformed


    private void bToRentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bToRentActionPerformed
        toRent();
    }//GEN-LAST:event_bToRentActionPerformed

    private void toRent() {
        Optional<Transfer> selectedTransferOpt = getSelectedTransfer();
        selectedTransferOpt.ifPresent(this::toRent);
    }

    private void bTryGetRentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTryGetRentActionPerformed
        makeRentOfferToPlayer();
    }//GEN-LAST:event_bTryGetRentActionPerformed

    private void bMyOffersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMyOffersActionPerformed
        myOffers();
    }//GEN-LAST:event_bMyOffersActionPerformed

    private void myOffers() {
        MyOffersForm myOffersForm = new MyOffersForm(null, true);
        myOffersForm.setLocationRelativeTo(this);
        myOffersForm.setParams(team, user);
        myOffersForm.setVisible(true);
        updateBudgetLabel();
    }

    private void radioButtonFreeAgentStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonFreeAgentStatusActionPerformed
        filter.setTransferStatus(TransferStatus.FREE_AGENT);
    }//GEN-LAST:event_radioButtonFreeAgentStatusActionPerformed

    private void comboLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboLocalActionPerformed
        filter.setLocalPosition(
                LocalPosition.getByAbreviation(
                        Objects.requireNonNull(comboLocal.getSelectedItem()).toString()));
    }//GEN-LAST:event_comboLocalActionPerformed

    private void checkBoxAgeFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAgeFromActionPerformed
        spinnerAgeFrom.setEnabled(checkBoxAgeFrom.isSelected());
    }//GEN-LAST:event_checkBoxAgeFromActionPerformed

    private void checkBoxAgeToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAgeToActionPerformed
        spinnerAgeTo.setEnabled(checkBoxAgeTo.isSelected());
    }//GEN-LAST:event_checkBoxAgeToActionPerformed

    private void checkBoxAvgFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgFromActionPerformed
        spinnerAvgFrom.setEnabled(checkBoxAvgFrom.isSelected());
    }//GEN-LAST:event_checkBoxAvgFromActionPerformed

    private void checkBoxAvgToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgToActionPerformed
        spinnerAvgTo.setEnabled(checkBoxAvgTo.isSelected());
    }//GEN-LAST:event_checkBoxAvgToActionPerformed

    private void bApplyFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bApplyFilterActionPerformed
        researchPlayersWithFilter();
    }//GEN-LAST:event_bApplyFilterActionPerformed

    private void bClearFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bClearFilterActionPerformed
        researchPlayersWithoutFilter();
    }//GEN-LAST:event_bClearFilterActionPerformed

    private void buttonPrevPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrevPageActionPerformed
        prevPage();
    }//GEN-LAST:event_buttonPrevPageActionPerformed

    private void prevPage() {
        int maxPage = transferPlayers.size() / PAGE_SIZE;
        if (pageIndex > 0) {
            --pageIndex;
            Rectangle rect = tableTransfers.getCellRect(pageIndex * PAGE_SIZE, 0, true);
            tableTransfers.scrollRectToVisible(rect);
        }
        updatePrevAndNextButtons(maxPage);
    }

    private void updatePrevAndNextButtons(int maxPage) {
        buttonNextPage.setEnabled(pageIndex != maxPage);
        buttonPrevPage.setEnabled(pageIndex != 0);
        labelPage.setText("Страница " + (pageIndex + 1) + "/" + (maxPage + 1));
    }

    private void buttonNextPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNextPageActionPerformed
        nextPage();
    }//GEN-LAST:event_buttonNextPageActionPerformed

    private void nextPage() {
        int maxPage = transferPlayers.size() / PAGE_SIZE;
        if (pageIndex < maxPage) {
            ++pageIndex;
            Rectangle rect = tableTransfers.getCellRect(pageIndex * PAGE_SIZE, 0, true);
            tableTransfers.scrollRectToVisible(rect);
        }
        updatePrevAndNextButtons(maxPage);
    }

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
    private javax.swing.JButton buttonNextPage;
    private javax.swing.JButton buttonPrevPage;
    private javax.swing.JCheckBox checkBoxAgeFrom;
    private javax.swing.JCheckBox checkBoxAgeTo;
    private javax.swing.JCheckBox checkBoxAvgFrom;
    private javax.swing.JCheckBox checkBoxAvgTo;
    private javax.swing.JCheckBox checkBoxPriceLow;
    private javax.swing.JComboBox<String> comboGlobal;
    private javax.swing.JComboBox<String> comboLocal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelBudget;
    private javax.swing.JLabel labelGlobalPosition;
    private javax.swing.JLabel labelNameAndLastName;
    private javax.swing.JLabel labelPage;
    private javax.swing.JLabel labelPosition;
    private javax.swing.JRadioButton radioButtonAllTeams;
    private javax.swing.JRadioButton radioButtonAnyStatus;
    private javax.swing.JRadioButton radioButtonForRent;
    private javax.swing.JRadioButton radioButtonForSale;
    private javax.swing.JRadioButton radioButtonFreeAgentStatus;
    private javax.swing.JRadioButton radioButtonOnSaleOrRent;
    private javax.swing.JRadioButton radoButtonMyTeam;
    private javax.swing.JSpinner spinnerAgeFrom;
    private javax.swing.JSpinner spinnerAgeTo;
    private javax.swing.JSpinner spinnerAvgFrom;
    private javax.swing.JSpinner spinnerAvgTo;
    private javax.swing.JSpinner spinnerPrice;
    private javax.swing.JTable tableTransfers;
    private javax.swing.JTextField tfName;
    // End of variables declaration//GEN-END:variables

    private static class MyTableCellRenderer extends DefaultTableCellRenderer {

        private final NumberFormat numberFormat = NumberFormat.getInstance();

        MyTableCellRenderer() {
            super();
            numberFormat.setGroupingUsed(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == COLUMN_AVERAGE) {
                Object o = table.getValueAt(row, column);
                if (o instanceof Integer) {
                    int val = (int) o;
                    setBackground(RenderUtil.getPlayerAverageColor(val));
                }
            } else if (column == COLUMN_COST) {
                String s = numberFormat.format(value);
                setValue(s);
            }
            return this;
        }
    }

    private class MyDefaultTableModel extends DefaultTableModel {

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
                    case COLUMN_NAME_AND_LAST_NAME:
                        return player.getFullName();
                    case COLUMN_AGE:
                        return player.getAge();
                    case COLUMN_GLOBAL_POSITION:
                        return player.getPreferredPosition().
                                getPositionOnField().getAbbreviation();
                    case COLUMN_LOCAL_POSITION:
                        return player.getPreferredPosition().getAbreviation();
                    case COLUMN_AVERAGE:
                        return player.getAverage();
                    case COLUMN_TRANSFER_STATUS:
                        return transfer.getTransferStatus().getDescription();
                    case COLUMN_COST:
                        return transfer.getCost();
                    case COLUMN_TEAM_NAME:
                        return transfer.getTeam().getName();
                }
            }
            return "";
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case COLUMN_AGE:
                case COLUMN_AVERAGE:
                case COLUMN_COST:
                    return Integer.class;
            }
            return String.class;
        }

    }
}
