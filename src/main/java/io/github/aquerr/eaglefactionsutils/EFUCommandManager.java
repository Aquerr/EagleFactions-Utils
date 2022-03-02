package io.github.aquerr.eaglefactionsutils;

import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EFUCommandManager
{
    private final Map<List<String>, CommandSpec> subcommands = new HashMap<>();

    private final EagleFactionsUtils plugin;
    private final CommandManager commandManager;

    public EFUCommandManager(EagleFactionsUtils plugin, CommandManager commandManager)
    {
        this.plugin = plugin;
        this.commandManager = commandManager;
    }

    public void addCommand(List<String> aliases, CommandSpec commandSpec)
    {
        subcommands.put(aliases, commandSpec);
    }

    // Should be fired only once
    public void registerCommands()
    {
        CommandSpec mainCommand = CommandSpec.builder()
                .description(Text.of("All EagleFactions-Utils Commands"))
                .children(this.subcommands)
                .build();

        this.commandManager.register(this.plugin, mainCommand, "fu", "futil", "futils");
    }

    public Map<List<String>, CommandSpec> getSubcommands()
    {
        return Collections.unmodifiableMap(subcommands);
    }
}
