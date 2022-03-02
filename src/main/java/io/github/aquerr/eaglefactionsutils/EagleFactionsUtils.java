package io.github.aquerr.eaglefactionsutils;

import com.google.inject.Inject;
import io.github.aquerr.eaglefactions.api.EagleFactions;
import io.github.aquerr.eaglefactionsutils.module.ModuleManager;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;

@Plugin(
        id = "eaglefactionsutils",
        name = "Eaglefactionsutils",
        url = "https://github.com/Aquerr/EagleFactions-Utils",
        authors = {
                "Aquerr"
        },
        dependencies = {
                @Dependency(id = "eaglefactions")
        }
)
public class EagleFactionsUtils
{
    private final Logger logger;
    private final ModuleManager moduleManager;
    private final EFUCommandManager commandManager;
    private final EventManager eventManager;
    private final Path configDir;

    private EagleFactions eagleFactions;

    @Inject
    public EagleFactionsUtils(final Logger logger,
                              final CommandManager commandManager,
                              final EventManager eventManager,
                              final @ConfigDir(sharedRoot = false) Path configDir)
    {
        this.logger = logger;
        this.moduleManager = new ModuleManager(this);
        this.commandManager = new EFUCommandManager(this, commandManager);
        this.eventManager = eventManager;
        this.configDir = configDir;
    }

    @Listener
    public void onPostInitialize(GamePostInitializationEvent event)
    {
        //Hook into EagleFactions
        EagleFactions eagleFactions = Sponge.getPluginManager().getPlugin("eaglefactions")
                .map(PluginContainer::getInstance)
                .flatMap(instance -> instance.map(EagleFactions.class::cast))
                .orElse(null);
        if (eagleFactions == null)
        {
            this.logger.error("Could not establish connection with EagleFactions");
            disablePlugin();
        }
        else
        {
            this.eagleFactions = eagleFactions;
        }
    }

    @Listener
    public void onInitialization(final GameInitializationEvent event)
    {
        logger.info("Initializing plugin...");
        this.moduleManager.init();
        logger.info("Plugin initialization completed!");
    }

    public EFUCommandManager getCommandManager()
    {
        return commandManager;
    }

    public EventManager getEventManager()
    {
        return eventManager;
    }

    public Path getConfigDir()
    {
        return configDir;
    }

    public EagleFactions getEagleFactions()
    {
        return eagleFactions;
    }

    private void disablePlugin()
    {
        Sponge.getCommandManager().getOwnedBy(this).forEach(Sponge.getCommandManager()::removeMapping);
        Sponge.getEventManager().unregisterPluginListeners(this);
        this.moduleManager.disablePlugin();
    }
}
