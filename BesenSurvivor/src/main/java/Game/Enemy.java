package Game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

public class Enemy {

    public static final double ENEMY_SPEED = 100.0;

    private final ImageView imageView;
    private final ImageView player;

    public Enemy(ImageView enemyView, ImageView player) {
        this.imageView = enemyView;
        this.player = player;
    }

    public void tick(double deltaTime) {
        double[] dire = getNormalizedVector(
                imageView.getX(), imageView.getY(),
                player.getX(), player.getY()
        );

        double moveAmountX = dire[0] * ENEMY_SPEED * deltaTime;
        double moveAmountY = dire[1] * ENEMY_SPEED * deltaTime;

        imageView.setX(imageView.getX() + moveAmountX);
        imageView.setY(imageView.getY() + moveAmountY);
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

    public static String getRandomEnemySpriteName() {
        String[] names = {"enemy1.png", "enemy2.png", "enemy3.png", "enemy4.png", "enemy5.png"};
        Random random = new Random();
        return names[random.nextInt(0, names.length)];
    }

    public ImageView getImageView() {
        return imageView;
    }

}
