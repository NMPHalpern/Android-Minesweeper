package com.noahhalpern.sweeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

import static android.R.attr.id;
import static android.R.attr.process;
import static junit.runner.Version.id;

public class GameActivity extends AppCompatActivity {
    public static int EASY_ROW_COUNT = 8;
    public static int EASY_MINE_COUNT = 10;
    public static int HARD_ROW_COUNT = 12;
    public static int HARD_MINE_COUNT = 30;

    private int mLevel;
    private int mGameBoardCellNum;
    private int mGameBoardRowNum;
    private int mGameBoardMineNum;
    private String mGameBoardTitle;

    private ArrayList<CellView> mGameCells;
    private ArrayList<Integer> mMineIds;

    @BindView(R.id.game_title) TextView gameTitle;
    @BindView(R.id.game_board) GridView gameBoard;
    @BindView(R.id.restart_button) Button restartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        mLevel = intent.getIntExtra(MainActivity.GAME_LEVEL, 0);

        if (mLevel == MainActivity.EASY){
            mGameBoardRowNum = EASY_ROW_COUNT;
            mGameBoardCellNum = EASY_ROW_COUNT * EASY_ROW_COUNT;
            mGameBoardMineNum = EASY_MINE_COUNT;
            mGameBoardTitle = "EASY";
        } else {
            mGameBoardRowNum = HARD_ROW_COUNT;
            mGameBoardCellNum = HARD_ROW_COUNT * HARD_ROW_COUNT;
            mGameBoardMineNum = HARD_MINE_COUNT;
            mGameBoardTitle = "HARD";
        }

        gameTitle.setText(mGameBoardTitle);
        setupBoard();
    }


    @OnItemClick(R.id.game_board)
    public void selectCell(AdapterView<?> parent, CellView clickedCell, int position, long id){
        int index = ((clickedCell.row * mGameBoardRowNum) - (mGameBoardRowNum - clickedCell.column)) - 1;
        Log.i("ITEM CLICK", "ROW: " + clickedCell.row + " COLUMN: " + clickedCell.column + " INDEX: " + index);
        boolean proceed = clickedCell.select();
        if (proceed){
            handleCell(clickedCell);
        } else {
            for (CellView cell : mGameCells){
                if (!cell.isRevealed){
                    cell.select();
                }
            }
        }
    }

    @OnClick(R.id.restart_button)
    public void restartGame(Button button){
        recreate();
    }
    private void handleCell(CellView cell){
        ArrayList<CellView> adjCells = getAdjacentCells(cell);
        for (CellView adjCell : adjCells){
            int mineCount = getMineCount(adjCell);
            if (mineCount == 0 && !adjCell.isMined && !adjCell.isRevealed){
                adjCell.select();
                handleCell(adjCell);
            } else if (!adjCell.isMined && !adjCell.isRevealed){
                Log.i("HANDLE CELL", "HINT: " + String.valueOf(mineCount));
                adjCell.setHint(String.valueOf(mineCount));
                adjCell.select();
            }
        }
    }

    private ArrayList<CellView> getAdjacentCells(CellView cell){
        ArrayList<CellView> adjCells = new ArrayList<CellView>();
        CellView cell1 = getAdjacentCell(cell.row - 1, cell.column - 1);
        if (cell1 != null){
            adjCells.add(cell1);
        }
        CellView cell2 = getAdjacentCell(cell.row - 1, cell.column);
        if (cell2 != null){
            adjCells.add(cell2);
        }
        CellView cell3 = getAdjacentCell(cell.row - 1, cell.column + 1);
        if (cell3 != null){
            adjCells.add(cell3);
        }
        CellView cell4 = getAdjacentCell(cell.row, cell.column - 1);
        if (cell4 != null){
            adjCells.add(cell4);
        }
        CellView cell5 = getAdjacentCell(cell.row, cell.column + 1);
        if (cell5 != null){
            adjCells.add(cell5);
        }
        CellView cell6 = getAdjacentCell(cell.row + 1, cell.column - 1);
        if (cell6 != null){
            adjCells.add(cell6);
        }
        CellView cell7 = getAdjacentCell(cell.row + 1, cell.column);
        if (cell7 != null){
            adjCells.add(cell7);
        }
        CellView cell8 = getAdjacentCell(cell.row + 1, cell.column + 1);
        if (cell8 != null){
            adjCells.add(cell8);
        }

        return adjCells;
    }

    private CellView getAdjacentCell(int row, int column){
        // Return 0 if cell is not valid;
        if (row < 1 || row > mGameBoardRowNum || column < 1 || column > mGameBoardRowNum){
            return null;
        } else {
            int index = ((row * mGameBoardRowNum) - (mGameBoardRowNum - column)) - 1;
            return mGameCells.get(index);
        }
    }

    private int getMineCount(CellView cell){
        int count = 0;
        for(CellView adjCell : getAdjacentCells(cell)){
            if (adjCell.isMined){
                count += 1;
            }
        }
        return count;
    }

    private void setupBoard(){
        gameBoard.setNumColumns(mGameBoardRowNum);
        mGameCells = generateCells(mGameBoardCellNum);
        ArrayAdapter boardAdapter = new GameBoardAdapter(this, mGameCells);
        gameBoard.setAdapter(boardAdapter);
    }

    private ArrayList<CellView> generateCells(int size) {
        ArrayList<Integer> mineIds = generateMineIds();
        ArrayList<CellView> cells = new ArrayList<CellView>();
        for (int i = 0; i < size; ++i) {
            CellView cell = new CellView(this);
            cell.row = (i / mGameBoardRowNum) + 1;
            cell.column = (i % mGameBoardRowNum) + 1;
            if (mineIds.contains(i)){
                cell.setMine();
            }
            cells.add(cell);
        }
        return cells;
    }

    private ArrayList<Integer> generateMineIds(){
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < mGameBoardCellNum; ++i) {
            list.add(i);
        }
        Collections.shuffle(list);

        mMineIds = new ArrayList<Integer>();
        for (int i = 0; i < mGameBoardMineNum; i++) {
            mMineIds.add(list.get(i));
        }

        return mMineIds;
    }

}
