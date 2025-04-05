package Core;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.HashMap;

public final class GameAudioPlayer {

    private static final String menuSongName = "IntroUwU.mp3";
    private static final String gameSongName = "JavaxDaniel.mp3";

    public enum MusicType {
        menu, game
    }

    private final HashMap<MusicType, MediaPlayer> playerMapByMusicType;


    public GameAudioPlayer() {
        playerMapByMusicType = new HashMap<>();
        playerMapByMusicType.put(MusicType.menu, createMediaPlayer(menuSongName));
        playerMapByMusicType.put(MusicType.game, createMediaPlayer(gameSongName));
    }

    public void playSong(MusicType type) {
        stopAllPlayers();

        MediaPlayer current = playerMapByMusicType.get(type);
        current.setStartTime(Duration.seconds(0));
        current.setCycleCount(MediaPlayer.INDEFINITE);
        current.play();
    }

    private void stopAllPlayers() {
        for(MediaPlayer m : playerMapByMusicType.values()) {
            m.stop();
        }
    }

    private MediaPlayer createMediaPlayer(String songName) {
        String songPath = getClass().getResource("/audio/" + songName).toExternalForm();
        Media songMedia = new Media(songPath);
        MediaPlayer newPlayer = new MediaPlayer(songMedia);
        newPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        return new MediaPlayer(songMedia);
    }

}
