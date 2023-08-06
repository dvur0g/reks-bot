package uz.wolks.reksbot.command.impl;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.AudioChannelJoinSpec;
import discord4j.voice.AudioProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import uz.wolks.reksbot.audio.TrackScheduler;
import uz.wolks.reksbot.command.Command;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class PlayCommand implements Command {
    private final AudioProvider provider;
    private final AudioPlayerManager playerManager;
    private final TrackScheduler scheduler;

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        return Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .flatMap(channel -> channel.join(AudioChannelJoinSpec.builder()
                        .provider(provider)
                        .build()))
                .then(Mono.justOrEmpty(event.getMessage().getContent())
                        .map(content -> Arrays.asList(content.split(" ")))
                        .map(commands -> commands.get(1))
                        .map(url -> url.startsWith("https") ? url.replaceFirst("https", "http") : url)
                        .doOnNext(url -> playerManager.loadItem(url, scheduler))
                        .then());
    }

    @Override
    public String getName() {
        return "play";
    }
}
