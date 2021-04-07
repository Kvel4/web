package ru.itmo.wp.web.page;

import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class TicTacToePage {
    public void action(HttpServletRequest request, Map<String, Object> view) {
        HttpSession session = request.getSession();
        State state = (State) session.getAttribute("state");
        if (state == null) {
            newGame(request, view);
        } else {
            view.put("state", state);
        }
    }

    public void onMove(HttpServletRequest request, Map<String, Object> view) {
        HttpSession session = request.getSession();
        State state = (State) session.getAttribute("state");

        Cell cell = getCell(request);
        state.makeMove(cell);

        view.put("state", state);
        throw new RedirectException("/ticTacToe");
    }

    public void newGame(HttpServletRequest request, Map<String, Object> view) {
        HttpSession session = request.getSession();
        State state = new State(3);
        session.setAttribute("state", state);
        view.put("state", state);
        throw new RedirectException("/ticTacToe");
    }

    private Cell getCell(HttpServletRequest request) {
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            String[] tokens = entry.getKey().split("_");
            if (tokens[0].equals("cell")) {
                int row = Integer.parseInt(String.valueOf(tokens[1].charAt(0)));
                int col = Integer.parseInt(String.valueOf(tokens[1].charAt(1)));
                return new Cell(row, col);
            }
        }
        return null;
    }

    private enum CellValue {
        X("X"), O("O"), E(" ");
        private String value;

        CellValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    private enum Phase {
        RUNNING("RUNNING"), X_WON("WON_X"), O_WON("WON_O"), DRAW("DRAW");
        private String value;

        Phase(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public static class State {
        CellValue turn = CellValue.X;
        private Cell[][] cells;
        private Phase phase = Phase.RUNNING;
        private int freePositionCnt;

        public State(int size) {
            cells = new Cell[size][size];
            freePositionCnt = size * size;
        }

        public void makeMove(Cell cell) {
            if (!isValid(cell)) {
                return;
            }
            cell = new Cell(cell.getRow(), cell.getCol(), turn);

            cells[cell.getRow()][cell.getCol()] = cell;
            freePositionCnt--;
            phase = changePhase(cell);
            if (phase != Phase.RUNNING) {
                fillFreeSpace();
            }
            turn = turn == CellValue.X ? CellValue.O : CellValue.X;
        }

        private void fillFreeSpace() {
            for (int row = 0; row < cells.length; row++) {
                for (int col = 0; col < cells.length; col++) {
                    if (cells[row][col] == null) {
                        cells[row][col] = new Cell(row, col, CellValue.E);
                    }
                }
            }
        }

        private Phase changePhase(Cell cell) {
            int[][] directions = {{0, 1}, {1, 1}, {1, 0}, {1, -1}};

            for (int[] direction : directions) {
                int row, col, cnt = -1;

                row = cell.getRow();
                col = cell.getCol();
                while (isValid(row, col) && cells[row][col] != null && cells[row][col].getVal() == turn) {
                    row += direction[0];
                    col += direction[1];
                    cnt += 1;
                }

                row = cell.getRow();
                col = cell.getCol();
                while (isValid(row, col) && cells[row][col] != null && cells[row][col].val == turn) {
                    row -= direction[0];
                    col -= direction[1];
                    cnt += 1;
                }

                if (cnt == cells.length) {
                    return turn == CellValue.X ? Phase.X_WON : Phase.O_WON;
                } else if (freePositionCnt == 0) {
                    return Phase.DRAW;
                }
            }
            return Phase.RUNNING;
        }

        public boolean isValid(Cell cell) {
            return isValid(cell.getRow(), cell.getCol()) &&
                    cells[cell.getRow()][cell.getCol()] == null;
        }

        private boolean isValid(int row, int col) {
            return row >= 0 && row < cells.length &&
                    col >= 0 && col < cells.length;
        }

        public int getSize() {
            return cells.length;
        }

        public Cell[][] getCells() {
            return cells;
        }

        public boolean isCrossesMove() {
            return turn == CellValue.X;
        }

        public String getPhase() {
            return phase.getValue();
        }
    }

    public static class Cell {
        private int row, col;
        private CellValue val;

        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public Cell(int row, int col, CellValue val) {
            this.row = row;
            this.col = col;
            this.val = val;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public CellValue getVal() {
            return val;
        }

        @Override
        public String toString() {
            return val != null ? val.getValue() : " ";
        }
    }
}
