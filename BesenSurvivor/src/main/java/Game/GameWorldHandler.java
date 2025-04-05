package Game;

import Core.Main;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Random;

public final class GameWorldHandler {

    public static final int PLAYER_GROUND_PIXEL_Y = 395;
    public static final int ENEMYS_AT_START = 5;
    public static final int ENEMY_SPAWN_Y = -50;

    private GameController gameController;

    private double worldProgress;
    private int currentMaxEnemys;

    private Pane currentGamePane;

    private ArrayList<Enemy> enemyList;
    private ArrayList<Besen> besenList;

    private ArrayList<Enemy> enemyDestroyQueue;
    private ArrayList<Besen> besenDestroyQueue;

    private ImageView player;
    private PlayerController playerController;

    public GameWorldHandler(GameController gameController) {
        this.gameController = gameController;
    }

    public void resetAndSetupNewGame() {
        currentGamePane = (Pane) gameController.getMain().getPrimaryStage().getScene().lookup("#gamePane");

        worldProgress = 0.0;
        currentMaxEnemys = ENEMYS_AT_START;

        enemyList = new ArrayList<>();
        besenList = new ArrayList<>();

        enemyDestroyQueue = new ArrayList<>();
        besenDestroyQueue = new ArrayList<>();

        player = createImageView(loadImage("character.png"), 50, 100);
        player.setX(200);
        player.setY(PLAYER_GROUND_PIXEL_Y);
        currentGamePane.getChildren().add(player);
        playerController = new PlayerController(player, gameController.getMain(), gameController, this);
    }

    public void tick(double deltaTime) {
        if(playerController != null) playerController.tick(deltaTime);
        if(enemyList != null) {
            generateEnemysIfNeeded();
            for(Enemy e : enemyList) e.tick(deltaTime);
        }
        if(besenList != null) for(Besen b : besenList) b.tick(deltaTime);

        // Process destroy-queues
        enemyList.removeAll(enemyDestroyQueue);
        besenList.removeAll(besenDestroyQueue);

        enemyDestroyQueue.clear();
        besenDestroyQueue.clear();
    }

    public ArrayList<Enemy> getEnemyList() {
        return enemyList;
    }

    public ArrayList<Besen> getBesenList() {
        return besenList;
    }

    private ImageView createImageView(Image image, int sizeX, int sizeY) {
        ImageView newView = new ImageView();
        newView.setImage(image);
        newView.setFitWidth(sizeX);
        newView.setFitHeight(sizeY);

        return newView;
    }

    private Image loadImage(String imageName) {
        String path = getClass().getResource("/sprites/" + imageName).toExternalForm();
        return new Image(path);
    }

    private void generateEnemysIfNeeded() {
        while(enemyList.size() < currentMaxEnemys) {
            createNewEnemy();
        }
    }

    private void createNewEnemy() {
        // Create the Enemy
        String imagePath = getClass().getResource("/sprites/enemys/" + Enemy.getRandomEnemySpriteName()).toExternalForm();
        Image enemySprite = new Image(imagePath);
        ImageView enemyView = createImageView(enemySprite, 50, 50);

        // Calculate random Position
        enemyView.setY(ENEMY_SPAWN_Y);
        Random random = new Random();
        enemyView.setX(random.nextInt(10, (int)Main.SCREEN_WIDTH - 11));

        // Add the enemy
        Enemy newEnemy = new Enemy(enemyView, player);
        currentGamePane.getChildren().add(newEnemy.getImageView());
        enemyList.add(newEnemy);
    }

    public void trowBesen(MouseEvent mouseEvent) {
        Image besenImage = loadImage("besen.png");
        ImageView besenView = createImageView(besenImage, 50, 50);
        Besen newBesen = new Besen(besenView, player, mouseEvent, this);

        newBesen.getImageView().setX(player.getX());
        newBesen.getImageView().setY(player.getY());

        currentGamePane.getChildren().add(newBesen.getImageView());
        besenList.add(newBesen);
    }

    public static boolean checkCollision(ImageView view1, ImageView view2) {
        return view1.getBoundsInParent().intersects(view2.getBoundsInParent());
    }

    public void destroyEnemy(Enemy enemy, Besen besen) {
        if(besen != null) destroyBesen(besen);

        currentGamePane.getChildren().remove(enemy.getImageView());
        enemyDestroyQueue.add(enemy);

        gameController.addScore(100L);
    }

    public void destroyBesen(Besen besen) {
        if(besen == null) return;
        if(besenList == null) return;
        if(!besenList.contains(besen)) return;

        currentGamePane.getChildren().remove(besen.getImageView());
        besenDestroyQueue.add(besen);
    }

    public void clearEverything() {
        for(Enemy e : enemyList) {
            destroyEnemy(e, null);
        }
        for(Besen b : besenList) {
            destroyBesen(b);
        }

        currentGamePane.getChildren().remove(player);
    }
}
