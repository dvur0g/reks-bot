package uz.wolks.reksbot.command.impl;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import uz.wolks.reksbot.command.Command;

@Component
@RequiredArgsConstructor
public class ResumeCommand implements Command {
    private final AudioPlayer player;

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        return Mono.justOrEmpty(event.getMessage().getContent())
                .doOnNext(command -> player.setPaused(false))
                .then();
    }

    @Override
    public String getName() {
        return "resume";
    }
}
