package flavor.pie.testplugin;

import com.google.common.collect.ImmutableList;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.persistence.DataTranslators;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.monster.Creeper;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.stream.Collectors;

@Plugin(id = "testplugin")
public class SpongeTestPlugin {
    @Inject
    Game game;
    @Listener
    public void init(GameInitializationEvent e) {
        CommandSpec entityWithClass = CommandSpec.builder()
                .executor(entityTest)
                .arguments(GenericArguments.entity(Text.of("entity"), Creeper.class))
                .build();
        CommandSpec entityOrTarget = CommandSpec.builder()
                .executor(entityTest)
                .arguments(GenericArguments.entityOrTarget(Text.of("entity")))
                .build();
        CommandSpec entityOrTargetWithClass = CommandSpec.builder()
                .executor(entityTest)
                .arguments(GenericArguments.entityOrTarget(Text.of("entity"), Creeper.class))
                .build();
        CommandSpec url = CommandSpec.builder()
                .executor(urlTest)
                .arguments(GenericArguments.url(Text.of("url")))
                .build();
        CommandSpec ip = CommandSpec.builder()
                .executor(ipTest)
                .arguments(GenericArguments.ip(Text.of("ip")))
                .build();
        CommandSpec ipOrSource = CommandSpec.builder()
                .executor(ipTest)
                .arguments(GenericArguments.ipOrSource(Text.of("ip")))
                .build();
        CommandSpec bigDecimal = CommandSpec.builder()
                .executor(bigDecimalTest)
                .arguments(GenericArguments.bigDecimal(Text.of("num")))
                .build();
        CommandSpec bigInteger = CommandSpec.builder()
                .executor(bigIntegerTest)
                .arguments(GenericArguments.bigInteger(Text.of("num")))
                .build();
        CommandSpec dataContainer = CommandSpec.builder()
                .executor(dataContainerTest)
                .arguments(GenericArguments.dataContainer(Text.of("data")))
                .build();
        CommandSpec uuid = CommandSpec.builder()
                .executor(uuidTest)
                .arguments(GenericArguments.uuid(Text.of("uuid")))
                .build();
        CommandSpec textSimple = CommandSpec.builder()
                .executor(textTest)
                .arguments(GenericArguments.text(Text.of("text"), false, true))
                .build();
        CommandSpec textComplex = CommandSpec.builder()
                .executor(textTest)
                .arguments(GenericArguments.text(Text.of("text"), true, true))
                .build();
        CommandSpec textSingle = CommandSpec.builder()
                .executor(textTest)
                .arguments(GenericArguments.text(Text.of("text"), false, false))
                .build();
        CommandSpec dateTime = CommandSpec.builder()
                .executor(dateTimeTest)
                .arguments(GenericArguments.dateTime(Text.of("time")))
                .build();
        CommandSpec dateTimeOrNow = CommandSpec.builder()
                .executor(dateTimeTest)
                .arguments(GenericArguments.dateTimeOrNow(Text.of("time")))
                .build();
        CommandSpec duration = CommandSpec.builder()
                .executor(durationTest)
                .arguments(GenericArguments.duration(Text.of("duration")))
                .build();
        CommandSpec withSuggestions = CommandSpec.builder()
                .executor(stringTest)
                .arguments(GenericArguments.withSuggestions(GenericArguments.string(Text.of("elem")), ImmutableList.of("a", "list", "of", "suggestions")))
                .build();
        CommandSpec withLooseSuggestions = CommandSpec.builder()
                .executor(stringTest)
                .arguments(GenericArguments.withSuggestions(GenericArguments.string(Text.of("elem")), ImmutableList.of("a", "list", "of", "suggestions"), false))
                .build();
        CommandSpec withDynamicSuggestions = CommandSpec.builder()
                .executor(stringTest)
                .arguments(GenericArguments.withSuggestions(GenericArguments.string(Text.of("elem")), src -> game.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList())))
                .build();
        CommandSpec withLooseDynamicSuggestions = CommandSpec.builder()
                .executor(stringTest)
                .arguments(GenericArguments.withSuggestions(GenericArguments.string(Text.of("elem")), src -> game.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), false))
                .build();
        CommandSpec withConstrainedSuggestions = CommandSpec.builder()
                .executor(stringTest)
                .arguments(GenericArguments.withConstrainedSuggestions(GenericArguments.catalogedElement(Text.of("elem"), CatalogTypes.BLOCK_TYPE), t -> t.startsWith("a")))
                .build();
        CommandSpec test = CommandSpec.builder()
                .child(entityWithClass, "entityWithClass")
                .child(entityOrTarget, "entityOrTarget")
                .child(entityOrTargetWithClass, "entityOrTargetWithClass")
                .child(url, "url")
                .child(ip, "ip")
                .child(ipOrSource, "ipOrSource")
                .child(bigDecimal, "bigDecimal")
                .child(bigInteger, "bigInteger")
                .child(dataContainer, "dataContainer")
                .child(uuid, "uuid")
                .child(textSimple, "textSimple")
                .child(textComplex, "textComplex")
                .child(textSingle, "textSingle")
                .child(dateTime, "dateTime")
                .child(dateTimeOrNow, "dateTimeOrNow")
                .child(duration, "duration")
                .child(withSuggestions, "withSuggestions")
                .child(withLooseSuggestions, "withLooseSuggestions")
                .child(withDynamicSuggestions, "withDynamicSuggestions")
                .child(withLooseDynamicSuggestions, "withLooseDynamicSuggestions")
                .child(withConstrainedSuggestions, "withConstrainedSuggestions")
                .build();
        game.getCommandManager().register(this, test, "test");
    }

    CommandExecutor entityTest = (src, args) -> {
        src.sendMessage(Text.of(args.<Entity>getOne("entity").get().getUniqueId()));
        return CommandResult.success();
    };
    CommandExecutor urlTest = (src, args) -> {
        src.sendMessage(Text.of(args.<URL>getOne("url").get().toString()));
        return CommandResult.success();
    };
    CommandExecutor ipTest = (src, args) -> {
        src.sendMessage(Text.of(args.<InetAddress>getOne("ip").get().toString()));
        return CommandResult.success();
    };
    CommandExecutor bigDecimalTest = (src, args) -> {
        src.sendMessage(Text.of(args.<BigDecimal>getOne("num").get().toString()));
        return CommandResult.success();
    };
    CommandExecutor bigIntegerTest = (src, args) -> {
        src.sendMessage(Text.of(args.<BigInteger>getOne("num").get().toString()));
        return CommandResult.success();
    };
    CommandExecutor dataContainerTest = (src, args) -> {
        StringWriter writer = new StringWriter();
        try {
            GsonConfigurationLoader.builder().setSink(() -> new BufferedWriter(writer)).build().save(DataTranslators.CONFIGURATION_NODE.translate(args.<DataContainer>getOne("data").get()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        src.sendMessage(Text.of(writer.toString()));
        return CommandResult.success();
    };
    CommandExecutor uuidTest = (src, args) -> {
        src.sendMessage(Text.of(args.<UUID>getOne("uuid").get().toString()));
        return CommandResult.success();
    };
    CommandExecutor textTest = (src, args) -> {
        src.sendMessage(args.<Text>getOne("text").get());
        return CommandResult.success();
    };
    CommandExecutor dateTimeTest = (src, args) -> {
        src.sendMessage(Text.of(DateTimeFormatter.RFC_1123_DATE_TIME.format(args.<LocalDateTime>getOne("time").get())));
        return CommandResult.success();
    };
    CommandExecutor durationTest = (src, args) -> {
        Duration d = args.<Duration>getOne("duration").get();
        src.sendMessage(Text.of(d.toString() + "(" + d.toMillis() + ")"));
        return CommandResult.success();
    };
    CommandExecutor stringTest = (src, args) -> {
        src.sendMessage(Text.of(args.getOne("elem").get().toString()));
        return CommandResult.success();
    };

}
