package com.codegym.games.game2048;

import com.codegym.engine.cell.*;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped;
    private int score;

    @Override
    public void initialize(){
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }
    
    @Override
    public void onKeyPress(Key key){
        if(!canUserMove()){
            gameOver();
            if(key == Key.SPACE){
                isGameStopped = false;
                score = 0;
                setScore(score);
                createGame();
                drawScene();
                return;
            }
        }
        if(isGameStopped){
            if(key == Key.SPACE){
                isGameStopped = false;
                score = 0;
                setScore(score);
                createGame();
                drawScene();
                return;
            }
        } else {
            if(key == Key.LEFT){
                moveLeft();
            } else if (key == Key.RIGHT){
                moveRight();
            } else if (key == Key.DOWN){
                moveDown();
            } else if (key == Key.UP){
                moveUp();
            }
            
            if(key == Key.LEFT || key == Key.RIGHT || key == Key.DOWN || key == Key.UP){
                drawScene();
            }
        }
    }
    
    private void createGame(){
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }
    
    private void drawScene(){
        for(int x = 0; x < SIDE; x++){
            for(int y = 0; y < SIDE; y++){
                setCellColoredNumber(x, y, gameField[y][x]);
            }
        }
    }
    
    private void createNewNumber(){
        int randomX;
        int randomY;
        
        do {
            randomX = getRandomNumber(SIDE);
            randomY = getRandomNumber(SIDE);
        } while(gameField[randomY][randomX] != 0);
        
        int num = getRandomNumber(10);
        
        if(num == 9){
            gameField[randomY][randomX] = 4;
        } else {
            gameField[randomY][randomX] = 2;
        }
        
        if(getMaxTileValue() == 2048){
            win();
        }
    }
    
    private Color getColorByValue(int value){
        switch(value){
            case 0: return Color.WHITE;
            case 2: return Color.DARKRED;
            case 4: return Color.RED;
            case 8: return Color.ORANGE;
            case 16: return Color.YELLOW;
            case 32: return Color.GOLD;
            case 64: return Color.GREEN;
            case 128: return Color.LIGHTGREEN;
            case 256: return Color.BLUE;
            case 512: return Color.LIGHTBLUE;
            case 1024: return Color.PURPLE;
            case 2048: return Color.PINK;
            default: return Color.WHITE;
        }
    }
    private void setCellColoredNumber(int x, int y, int value){
        if(value!=0){
            setCellValueEx(x, y, getColorByValue(value), Integer.toString(value));
        }
        else{
            setCellValueEx(x, y, getColorByValue(value), "");
        }
    }
    
    private boolean compressRow(int[] row){
        boolean compressed = false;
        
        for(int i = 0; i < SIDE - 1; i++){
            if(row[i] == 0){
                for(int j = i + 1; j < SIDE; j++){
                    if(row[j] != 0){
                        compressed = true;
                        row[i] = row[j];
                        row[j] = 0;
                        j = SIDE - 1;
                    }
                }
            }
        }
        return compressed;
    }
    
    private boolean mergeRow(int[] row){
        boolean merged = false;
        for(int i = 0; i < SIDE - 1; i++){
            if(row[i] == row[i + 1] && row[i] != 0){
                merged = true;
                row[i] = 2 * row[i];
                row[i + 1] = 0;
                score += row[i];
                setScore(score);
            }
        }
        return merged;
    }
    
    private void moveLeft(){
        boolean moved = false;
        for(int[] row : gameField) {
            boolean compressed = compressRow(row);
            boolean merged = mergeRow(row);
            boolean compressedAgain = compressRow(row);
            if((compressed || merged || compressedAgain) && !moved) {
                moved = true;
                createNewNumber();
            }
        }
    }
    
    private void moveRight(){
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }
    
    private void moveUp(){
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }
    
    private void moveDown(){
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }
    
    private void rotateClockwise(){
        for (int x = 0; x < SIDE / 2; x++){
            for (int y = x; y < SIDE - x - 1; y++){
                int holder = gameField[x][y];
                gameField[x][y] = gameField[SIDE - 1 - y][x];
                gameField[SIDE - 1 - y][x] = gameField[SIDE - 1 - x][SIDE - 1 - y];
                gameField[SIDE - 1 - x][SIDE - 1 - y] = gameField[y][SIDE - 1 - x];
                gameField[y][SIDE - 1 - x] = holder;
            }
        }
    }
    
    private int getMaxTileValue(){
        int max = 0;
        for(int x = 0; x < SIDE; x++){
            for(int y = 0; y < SIDE; y++){
                if(gameField[y][x] > max){
                    max = gameField[y][x];
                }
            }
        }
        return max;
    }
    
    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "You Win!", Color.GREEN, 72);
    }
    
    private boolean canUserMove(){
        boolean possibility = false;
        for(int x = 0; x < SIDE; x++){
            for(int y = 0; y < SIDE; y++){
                if(gameField[y][x] == 0){
                    possibility = true;
                } else if(y + 1 < SIDE && gameField[y+1][x] == gameField[y][x]){
                    possibility = true;
                } else if(y - 1 > 0 && gameField[y-1][x] == gameField[y][x]){
                    possibility = true;
                } else if(x + 1 < SIDE && gameField[y][x+1] == gameField[y][x]){
                    possibility = true;
                } else if(x - 1 > 0 && gameField[y][x-1] == gameField[y][x]){
                    possibility = true;
                }
            }
        }
        return possibility;
    }
    
    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "You Lose.", Color. RED, 72);
    }
}
