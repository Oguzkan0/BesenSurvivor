package Core;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import javax.swing.*;

public final class SceneGenerator {

    public static Scene generateMainMenu(Main main) {
        VBox mainPane = new VBox();
        Scene newScene = new Scene(mainPane);

        Label titleText = new Label();
        titleText.setText("BesenSurvivor");
        titleText.setFont(new Font(80));
        titleText.setTextFill(Color.WHITE);
        titleText.setBackground(new Background(new BackgroundFill(Color.MEDIUMPURPLE.darker(), null, null)));
        titleText.setPrefWidth(Main.SCREEN_WIDTH);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setAlignment(Pos.CENTER);
        VBox.setMargin(titleText, new Insets(0, 0, 35, 0)); // 20 Pixel Abstand nach unten

        mainPane.getChildren().add(titleText);

        mainPane.getChildren().addAll(
                createMainButton("Starte Spiel", main::enterGame),
                createMainButton("Beende Spiel", main::endGame)
        );

        mainPane.setBackground(new Background(new BackgroundFill(Color.MEDIUMPURPLE, null, null)));
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setSpacing(10);

        return newScene;
    }

    public static Scene generateGameScene(Main main) {
        AnchorPane mainPane = new AnchorPane();
        Scene newScene = new Scene(mainPane);

        String backgroundImagePath = main.getClass().getResource("/sprites/map.gif").toExternalForm();
        mainPane.setBackground(new Background(new BackgroundImage(new Image(backgroundImagePath),
                null, null, null, new BackgroundSize(
                        Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT, true , true, true, true
        ))));

        Label scoreText = createTopLabel("scoreText", "Score - 0");

        AnchorPane.setTopAnchor(scoreText, 10.0);
        AnchorPane.setLeftAnchor(scoreText, 10.0);

        mainPane.getChildren().add(scoreText);


        Label timeText = createTopLabel("timeText", "Zeit - 0.00");

        AnchorPane.setTopAnchor(timeText, 10.0);
        AnchorPane.setRightAnchor(timeText, 10.0);

        mainPane.getChildren().add(timeText);


        Pane gamePane = new Pane();
        gamePane.setId("gamePane");

        AnchorPane.setTopAnchor(gamePane, 0.0);
        AnchorPane.setLeftAnchor(gamePane, 0.0);

        gamePane.setPrefWidth(Main.SCREEN_WIDTH);
        gamePane.setPrefHeight(Main.SCREEN_HEIGHT);

        mainPane.getChildren().add(gamePane);


        return newScene;
    }

    private static Button createMainButton(String textContent, Runnable pressAction) {
        Button newButton = new Button();
        newButton.setText(textContent);

        newButton.setPrefWidth(200);
        newButton.setPrefHeight(50);

        newButton.setOnMouseClicked(e -> {pressAction.run();});

        return newButton;
    }

    private static Label createTopLabel(String id, String startText) {
        Label scoreText = new Label();
        scoreText.setText(startText);
        scoreText.setTextFill(Color.WHITE);
        scoreText.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.4), new CornerRadii(10), null)));
        scoreText.setPadding(new Insets(5, 5, 5, 5));
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        scoreText.setId(id);

        return scoreText;
    }

}
