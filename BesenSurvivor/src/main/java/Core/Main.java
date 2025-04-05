package Core;

import Game.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

public final class Main extends Application {

    // VM-Options:
    //      --module-path [FX/lib Path] --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media

    public static final double SCREEN_WIDTH = 1000;
    public static final double SCREEN_HEIGHT = 562;

    private GameAudioPlayer audioPlayer;
    private GameController gameController;

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        setup();

        stage.setWidth(SCREEN_WIDTH);
        stage.setHeight(SCREEN_HEIGHT);
        stage.setResizable(false);
        stage.setTitle("Besen Survivor");
        stage.show();

        enterMainScreen();
    }

    public void enterMainScreen() {
        primaryStage.setScene(SceneGenerator.generateMainMenu(this));
        audioPlayer.playSong(GameAudioPlayer.MusicType.menu);
    }

    public void enterGame() {
        primaryStage.setScene(SceneGenerator.generateGameScene(this));
        audioPlayer.playSong(GameAudioPlayer.MusicType.game);

        gameController.startGamePhase();
    }

    public void endGame() {
        primaryStage.close();
    }

    public GameAudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setup() {
        audioPlayer = new GameAudioPlayer();
        gameController = new GameController(this);
    }
}