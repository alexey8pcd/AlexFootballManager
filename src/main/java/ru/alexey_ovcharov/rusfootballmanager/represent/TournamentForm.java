package ru.alexey_ovcharov.rusfootballmanager.represent;

import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.PlaceInfo;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.Table;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.Tournament;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.util.Collections;
import java.util.List;

/**
 * @author Алексей
 */
public class TournamentForm extends javax.swing.JDialog {
    private static final int PLACE = 0;
    private static final int TEAM = 1;
    private static final int GAMES = 2;
    private static final int WINS = 3;
    private static final int DRAWS = 4;
    private static final int LOSES = 5;
    private static final int GOALS_SCORED = 6;
    private static final int GOALS_CONCEDED = 7;
    private static final int POINS = 8;
    private Tournament tournament;

    public TournamentForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableResults = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Турнирная таблица");
        setSize(new java.awt.Dimension(800, 500));

        tableResults.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null}
                },
                new String[]{
                        "Место", "Команда", "Игр", "Побед", "Вничью", "Поражений", "Забито", "Пропущено", "Очки"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableResults);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableResults;
// End of variables declaration//GEN-END:variables

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
        Table tournamentTable = tournament.getTournamentTable();
        List<PlaceInfo> placeInfoList;
        if (tournamentTable != null) {
            placeInfoList = tournamentTable.getPlaceInfoList();
        } else {
            placeInfoList = Collections.emptyList();
        }
        tableResults.setModel(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return placeInfoList.size();
            }

            @Override
            public int getColumnCount() {
                //"Место", "Команда", "Игр", "Побед", "Вничью", "Поражений", "Забито", "Пропущено", "Очки"
                return 9;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                PlaceInfo placeInfo = placeInfoList.get(rowIndex);
                switch (columnIndex) {
                    case PLACE:
                        return rowIndex + 1;
                    case TEAM:
                        return placeInfo.getTeam();
                    case GAMES:
                        return placeInfo.getGamesCount();
                    case WINS:
                        return placeInfo.getWinsCount();
                    case DRAWS:
                        return placeInfo.getDrawsCount();
                    case LOSES:
                        return placeInfo.getLosesCount();
                    case GOALS_SCORED:
                        return placeInfo.getGoalsScored();
                    case GOALS_CONCEDED:
                        return placeInfo.getGoalsConceded();
                    case POINS:
                        return placeInfo.getPointsCount();
                }
                return "";
            }
        });
        TableColumnModel tableColumnModel = tableResults.getTableHeader().getColumnModel();
        tableColumnModel.getColumn(0).setHeaderValue("Место");
        tableColumnModel.getColumn(1).setHeaderValue("Команда");
        tableColumnModel.getColumn(2).setHeaderValue("Игр");
        tableColumnModel.getColumn(3).setHeaderValue("Побед");
        tableColumnModel.getColumn(4).setHeaderValue("Вничью");
        tableColumnModel.getColumn(5).setHeaderValue("Поражений");
        tableColumnModel.getColumn(6).setHeaderValue("Забито");
        tableColumnModel.getColumn(7).setHeaderValue("Пропущено");
        tableColumnModel.getColumn(8).setHeaderValue("Очки");
        tableResults.updateUI();
    }


}
