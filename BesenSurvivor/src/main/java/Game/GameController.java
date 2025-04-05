package Game;

import Core.Main;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

public final class GameController {

    private final Main main;

    private AnimationTimer gameLoopTimer;
    private GameWorldHandler gameWorldHandler;

    private long score;
    private Label currentScoreText;
    private Label currentTimeText;

    private long startTimeMillis;
    private long lastTick;

    public GameController(Main main) {
        this.main = main;
        this.score = 0;
        setupGameLoopTimer();
        this.gameWorldHandler = new GameWorldHandler(this);
    }

    public void startGamePhase() {
        this.score = 0;
        this.currentScoreText = (Label) main.getPrimaryStage().getScene().lookup("#scoreText");
        this.currentTimeText = (Label) main.getPrimaryStage().getScene().lookup("#timeText");
        this.startTimeMillis = System.currentTimeMillis();
        this.lastTick = 0L;

        startGameLoop();
    }

    public void endGamePhase() {
        stopGameLoop();
        main.enterMainScreen();
    }

    public Main getMain() {
        return main;
    }

    public void addScore(long amount) {
        this.score += amount;
        updateScoreText();
    }

    private void updateScoreText() {
         currentScoreText.setText("Score - " + Long.toString(score));
    }

    private void updateTimeText() {
        currentTimeText.setText("Zeit - " + String.format("%.2f", (System.currentTimeMillis() - startTimeMillis) / 1000.0));
    }

    private void startGameLoop() {
        gameWorldHandler.resetAndSetupNewGame();
        gameLoopTimer.start();
    }

    private void stopGameLoop() {
        gameLoopTimer.stop();
    }

    private void setupGameLoopTimer() {
        gameLoopTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(lastTick == 0L) {
                    lastTick = l;
                    return;
                }

                long delta = l - lastTick;
                double deltaTimeSeconds = (double)delta / 1_000_000_000.0;

                lastTick = l;

                callTick(deltaTimeSeconds);
            }
        };
    }

    private void callTick(double deltaTime) {
        gameWorldHandler.tick(deltaTime);
        updateTimeText();
    }

    public void triggerGameOver() {
        stopGameLoop();
        gameWorldHandler.clearEverything();
    }

}
