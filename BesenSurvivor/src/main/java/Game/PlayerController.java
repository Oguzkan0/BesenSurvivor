package Game;

import Core.Main;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public final class PlayerController {

    private static final double PLAYER_SPEED = 350.0;
    private static final double GRAVITY = 1700;
    private static final double JUMP_FORCE = -600.0;

    private final ImageView currentPlayer;
    private final GameController gameController;
    private final GameWorldHandler worldHandler;
    private final Main main;

    private double horizontalInputAxis;
    private boolean isJumping;

    private double currentGravityAmount;

    public PlayerController(ImageView currentPlayer, Main main, GameController gameController, GameWorldHandler worldHandler) {
        this.currentPlayer = currentPlayer;
        this.main = main;
        this.gameController = gameController;
        this.worldHandler = worldHandler;
        this.horizontalInputAxis = 0.0;
        this.currentGravityAmount = 0.0;

        // Add Key event listeners
        main.getPrimaryStage().getScene().setOnKeyPressed(this::event_KeyDown);
        main.getPrimaryStage().getScene().setOnKeyReleased(this::event_KeyUp);

        main.getPrimaryStage().getScene().setOnMouseClicked(this::event_MousePressed);
    }

    public void tick(double deltaTime) {
        double playerMoveAmount = PLAYER_SPEED * horizontalInputAxis * deltaTime;
        currentPlayer.setX(currentPlayer.getX() + playerMoveAmount);

        currentPlayer.setX(Math.min(Main.SCREEN_WIDTH - currentPlayer.getFitWidth(), Math.max(0, currentPlayer.getX())));

        jumpUpdate(deltaTime);

        checkCollision();
    }

    private void event_KeyDown(KeyEvent event) {
        switch (event.getCode()) {
            case A: { // Left
                horizontalInputAxis = -1;
                break;
            }
            case D: { // Right
                horizontalInputAxis = 1;
                break;
            }
            case SPACE: { // Jump
                if(isJumping) break;
                startJump();
                break;
            }
            case ESCAPE: {
                gameController.endGamePhase();
                break;
            }
        }
    }

    private void event_KeyUp(KeyEvent event) {
        switch (event.getCode()) {
            case A: { // Left
                if(horizontalInputAxis == 1) break;
                horizontalInputAxis = 0;
                break;
            }
            case D: { // Right
                if(horizontalInputAxis == -1) break;
                horizontalInputAxis = 0;
                break;
            }
        }
    }

    private void event_MousePressed(MouseEvent event) {
        worldHandler.trowBesen(event);
    }

    private void startJump() {
        currentGravityAmount = JUMP_FORCE;
        isJumping = true;
    }

    private void jumpUpdate(double deltaTime) {
        if(!isJumping) return;

        // Move
        currentPlayer.setY(currentPlayer.getY() + currentGravityAmount * deltaTime);
        this.currentGravityAmount += GRAVITY * deltaTime;

        // Check if ground is reached
        if(currentPlayer.getY() >= GameWorldHandler.PLAYER_GROUND_PIXEL_Y) {
            currentPlayer.setY(GameWorldHandler.PLAYER_GROUND_PIXEL_Y);
            isJumping = false;
        }

    }

    private void checkCollision() {
        for(Enemy e : worldHandler.getEnemyList()) {
            if(GameWorldHandler.checkCollision(currentPlayer, e.getImageView())) {
                gameController.triggerGameOver();
                break;
            }
        }
    }
}
