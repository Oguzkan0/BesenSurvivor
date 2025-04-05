package Game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public final class Besen {

    public static final double BESEN_SPEED = 600.0;
    public static final double BESEN_ROTATE_SPEED = 360*2;

    private final ImageView imageView;

    private double throwDireX;
    private double throwDireY;

    private GameWorldHandler worldHandler;

    public Besen(ImageView imageView, ImageView player, MouseEvent mouseEvent, GameWorldHandler worldHandler) {
        this.imageView = imageView;
        this.worldHandler = worldHandler;
        calculateThrowDire(player, mouseEvent);

        destroyBesenIn(3000);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void calculateThrowDire(ImageView playerPos, MouseEvent mouseEvent) {
        double[] values = getNormalizedVector(
                playerPos.getX(), playerPos.getY(),
                mouseEvent.getSceneX(), mouseEvent.getSceneY()
        );

        this.throwDireX = values[0];
        this.throwDireY = values[1];
    }

    private static double[] getNormalizedVector(double posXA, double posYA, double posXB, double posYB) {
        // Schritt 1: Richtungsvektor berechnen
        double dx = posXB - posXA;
        double dy = posYB - posYA;

        // Schritt 2: Betrag berechnen
        double length = Math.sqrt(dx * dx + dy * dy);

        // Schritt 3: Normalisieren (wenn Länge > 0)
        if (length != 0) {
            return new double[] { dx / length, dy / length };
        } else {
            // A und B sind gleich – kein definierter Richtungsvektor
            return new double[] { 0, 0 };
        }
    }

    private void destroyBesenIn(int millis) {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(millis),
                e -> worldHandler.destroyBesen(this)
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void tick(double deltaTime) {
        double moveAmountX = throwDireX * BESEN_SPEED * deltaTime;
        double moveAmountY = throwDireY * BESEN_SPEED * deltaTime;

        imageView.setX(imageView.getX() + moveAmountX);
        imageView.setY(imageView.getY() + moveAmountY);

        spinAnimation(deltaTime);

        checkIfCollidingWithEnemy();
    }

    private void spinAnimation(double deltaTime) {
        imageView.setRotate(imageView.getRotate() + BESEN_ROTATE_SPEED * deltaTime);
    }

    private void checkIfCollidingWithEnemy() {
        for(Enemy e : worldHandler.getEnemyList()) {
            if(GameWorldHandler.checkCollision(imageView, e.getImageView())) {
                worldHandler.destroyEnemy(e, this);
                break;
            }
        }
    }

}
