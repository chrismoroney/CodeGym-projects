package com.codegym.games.moonlander;

import com.codegym.engine.cell.*;

public class MoonLanderGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private Rocket rocket;
    private GameObject landscape;
    private boolean isUpPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private GameObject platform;
    private boolean isGameStopped;
    private int score;
    
    @Override 
    public void initialize(){
        showGrid(false);
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }
    
    @Override
    public void onTurn(int num){
        if(score > 0){
            score--;
        }
        rocket.move(isUpPressed, isLeftPressed, isRightPressed);
        check();
        setScore(score);
        drawScene();
    }
    
    @Override
    public void setCellColor(int x, int y, Color color){
        if((x < 0 || x >= WIDTH) || (y < 0 || y >= HEIGHT)){
            return;
        } else {
            super.setCellColor(x, y, color);
        }
    }
    
    @Override
    public void onKeyPress(Key key){
        if(key == Key.UP){
            isUpPressed = true;
        } else if (key == Key.LEFT){
            isLeftPressed = true;
            isRightPressed = false;
        } else if (key == Key.RIGHT){
            isRightPressed = true;
            isLeftPressed = false;
        } else if (key == Key.SPACE && isGameStopped){
            createGame();
        }
    }
    
    @Override 
    public void onKeyReleased(Key key){
        if(key == Key.UP){
            isUpPressed = false;
        } else if (key == Key.LEFT){
            isLeftPressed = false;
        } else if (key == Key.RIGHT){
            isRightPressed = false;
        }
    }
    private void createGame(){
        isUpPressed = false;
        isLeftPressed = false;
        isRightPressed = false;
        isGameStopped = false;
        score = 1000;
        createGameObjects();
        setTurnTimer(50);
        drawScene();
    }
    
    private void drawScene(){
        for(int x = 0; x < WIDTH; x++){
            for(int y = 0; y < HEIGHT; y++){
                setCellColor(x, y, Color.BLACK);
            }
        }
        rocket.draw(this);
        landscape.draw(this);
    }
    
    private void createGameObjects(){
        rocket = new Rocket(WIDTH / 2, 0);
        landscape = new GameObject(0, 25, ShapeMatrix.LANDSCAPE);
        platform = new GameObject(23, MoonLanderGame.HEIGHT - 1, ShapeMatrix.PLATFORM);
    }
    
    private void check(){
        if(rocket.isCollision(landscape) && !(rocket.isCollision(platform) && rocket.isStopped())){
            gameOver();
        }
        if(rocket.isCollision(platform) && rocket.isStopped()){
            win();
        }
        
    }
    
    private void win(){
        rocket.land();
        isGameStopped = true;
        showMessageDialog(Color.NONE, "YOU WIN", Color.GREEN, 48);
        stopTurnTimer();
    }
    
    private void gameOver(){
        rocket.crash();
        isGameStopped = true;
        showMessageDialog(Color.NONE, "CRASH! You lose.", Color.RED, 48);
        stopTurnTimer();
        score = 0; 
        setScore(score);
    }
}
