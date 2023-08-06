package uz.wolks.reksbot.configuration;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AudioConfiguration {
    @Bean
    public AudioPlayerManager audioPlayerManager() {
        var playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        return playerManager;
    }

    @Bean
    public AudioPlayer audioPlayer(AudioPlayerManager playerManager) {
        return playerManager.createPlayer();
    }
}
