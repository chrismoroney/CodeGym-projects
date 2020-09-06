package com.codegym.games.minesweeper;

import com.codegym.engine.cell.*;
import java.util.*;

public class MinesweeperGame extends Game {
    // Vary dimension of the game board
    private static final int SIDE = 15;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDCA9";
    private int countFlags;
    private boolean isGameStopped;
    private int countClosedTiles = SIDE * SIDE;
    private int score;
    
    @Override
    public void onMouseLeftClick(int x, int y){
        if (!isGameStopped){
            openTile(x, y);
        } else {
            restart();
        }
    }
    
    @Override
    public void onMouseRightClick(int x, int y){
        markTile(x, y);
    }
    
    public void initialize(){
        setScreenSize(SIDE, SIDE);
        createGame();
    }
    
    private void createGame(){
        boolean isMine;
        for(int x = 0; x < SIDE; x++){
            for(int y = 0; y < SIDE; y++){
                setCellValue(x, y, "");
                int rand = getRandomNumber(10);
                if(rand == 0){
                    isMine = true;
                    countMinesOnField++;
                } else {
                    isMine = false;
                }
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.YELLOW);
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;
    }
    
    private void countMineNeighbors(){
        for (int x = 0; x < SIDE; x++){
            for (int y = 0; y < SIDE; y++){
                if (!gameField[y][x].isMine){
                    for(GameObject obj : getNeighbors(gameField[y][x])){
                        if (obj.isMine){
                            gameField[y][x].countMineNeighbors++;
                        }
                    }
                }
            }
        }
    }

    // Slighly modified, but borrowed from another person for help
    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> returnList = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if ((y < 0 || y >= SIDE) || (x < 0 || x >= SIDE) || (gameField[y][x] == gameObject)) {
                    continue;
                } else {
                    returnList.add(gameField[y][x]);
                }
            }
        }
        return returnList;
    }
    
    private void openTile(int x, int y) {
        if (!gameField[y][x].isOpen && !isGameStopped && !gameField[y][x].isFlag) {
            
            gameField[y][x].isOpen = true;
            setCellColor(x, y, Color.WHITE);
            countClosedTiles --;
            
            if (gameField[y][x].isMine) {
                setCellValueEx(x, y, Color.RED, MINE);
                gameOver();
            } else if (countClosedTiles == countMinesOnField) {
                win();
            } else if (gameField[y][x].countMineNeighbors == 0) {
                setCellValue(x, y, "");
                setCellColor(x, y, Color.WHITE);
                for (GameObject neighbor : getNeighbors(gameField[y][x])) {
                    openTile(neighbor.x, neighbor.y);
                }
            } else {
                setCellNumber(x, y, gameField[y][x].countMineNeighbors);
            }
            
            if (!isGameStopped) {
                score += 5;
                setScore(score);
            }
        }
    }
    
    private void markTile(int x, int y){
        if((gameField[y][x].isOpen || (countFlags == 0 && !gameField[y][x].isFlag)) || isGameStopped){
            
        } else if (!gameField[y][x].isFlag){
            gameField[y][x].isFlag = true; 
            countFlags--;
            setCellValue(x, y, FLAG);
            setCellColor(x, y, Color.LIGHTBLUE);
        } else if (gameField[y][x].isFlag){
            gameField[y][x].isFlag = false; 
            countFlags++;
            setCellValue(x, y, "");
            setCellColor(x, y, Color.YELLOW);
        }
        
    }
    
    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.RED, "Game Over", Color.NONE, 72);
    }
    
    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.GREEN, "You Win!", Color.NONE, 72);
    }
    
    private void restart(){
        isGameStopped = false;
        countClosedTiles = SIDE * SIDE;
        score = 0;
        setScore(score);
        countMinesOnField = 0;
        createGame();
    }
}
