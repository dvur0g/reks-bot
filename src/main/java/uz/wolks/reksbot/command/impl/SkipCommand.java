package uz.wolks.reksbot.command.impl;

import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import uz.wolks.reksbot.audio.TrackScheduler;
import uz.wolks.reksbot.command.Command;

@Component
@RequiredArgsConstructor
public class SkipCommand implements Command {
    private final TrackScheduler scheduler;

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        return Mono.justOrEmpty(event.getMessage().getContent())
                .doOnNext(command -> scheduler.nextTrack())
                .then();
    }

    @Override
    public String getName() {
        return "skip";
    }
}
