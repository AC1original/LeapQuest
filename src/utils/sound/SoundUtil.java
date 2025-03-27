package utils.sound;

import org.jetbrains.annotations.Nullable;
import utils.Logger;
import utils.caching.Cache;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SoundUtil {
    private static final Cache<AudioInputStream> sounds = new Cache.CacheBuilder<AudioInputStream>()
            .objectsExpires(true)
            .objectsOnlyExpiresWhenUnused(true)
            .objectsExpiresAfter(15, TimeUnit.MINUTES)
            .deleteObjectsWhenExpired(true)
            .build();

    public static void play(String path) {
        play(path, path);
    }

    public static void play(String path, String name) {
        AudioInputStream audioStream = null;
        if (sounds.contains(name)) {
            audioStream = sounds.get(name);
        } else {
            audioStream = load(path);
        }

        if (audioStream == null) {
            return;
        }

        sounds.add(name, audioStream);
        Logger.info(SoundUtil.class, "Cached sound '" + name + "'.");

        try {
            var clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
        } catch (LineUnavailableException | IOException e) {
            Logger.warn(SoundUtil.class, "Failed to play sound at '" + path + "': " + e);
        }
    }

    @Nullable
    public static AudioInputStream load(String path) {
        AudioInputStream audioInput = null;
        try {
            audioInput = AudioSystem.getAudioInputStream(new File(path));
            Logger.info(SoundUtil.class, "Loaded sound at: " + path + ".");
            return audioInput;
        } catch (UnsupportedAudioFileException | IOException e) {
            Logger.warn(SoundUtil.class, "Could not load sound at '" + path + "': " + e);
        }
        return null;
    }

}
