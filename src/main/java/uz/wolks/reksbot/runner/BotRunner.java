package uz.wolks.reksbot.runner;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import uz.wolks.reksbot.command.Command;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BotRunner implements CommandLineRunner {
    @Value("${token}")
    private String token;

    private final Map<String, Command> commands;

    public BotRunner(List<Command> commands) {
        this.commands = commands.stream()
                .collect(Collectors.toMap(Command::getName, Function.identity()));
    }

    public void run(String... args) {
        var client = DiscordClientBuilder.create(token).build()
                .login()
                .block();

        if (client == null) {
            throw new NullPointerException("Couldn't create client");
        }

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .flatMap(event -> Mono.just(event.getMessage().getContent())
                        .filter(content -> content.startsWith("!"))
                        .map(content -> Arrays.asList(content.split(" ")))
                        .map(content -> commands.get(content.get(0).replaceFirst("!", "")))
                        .flatMap(command -> command.execute(event)))
                .subscribe();

        client.onDisconnect().block();
    }
}
