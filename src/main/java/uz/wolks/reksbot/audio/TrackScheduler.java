package uz.wolks.reksbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public final class TrackScheduler extends AudioEventAdapter implements AudioLoadResultHandler {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        player.addListener(this);
    }
    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public void nextTrack() {
        player.startTrack(queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }

    @Override
    public void trackLoaded(final AudioTrack track) {
        queue(track);
    }

    @Override
    public void playlistLoaded(final AudioPlaylist playlist) {
        throw new UnsupportedOperationException("Playlists are not supported");
    }

    @Override
    public void noMatches() {
        throw new RuntimeException("No tracks were found");
    }

    @Override
    public void loadFailed(final FriendlyException exception) {
        throw new RuntimeException("Error when loading track");
    }
}