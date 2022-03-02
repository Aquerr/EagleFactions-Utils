package io.github.aquerr.eaglefactionsutils.module.fly;

import io.github.aquerr.eaglefactionsutils.EFUCommandManager;
import io.github.aquerr.eaglefactionsutils.EagleFactionsUtils;
import io.github.aquerr.eaglefactionsutils.PluginPermissions;
import io.github.aquerr.eaglefactionsutils.module.Module;
import io.github.aquerr.eaglefactionsutils.module.exception.CouldNotInitializeModule;
import io.github.aquerr.eaglefactionsutils.module.fly.command.FlyCommand;
import io.github.aquerr.eaglefactionsutils.module.fly.listener.TFlyListener;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.text.Text;

import java.util.*;

public class FlyModule implements Module
{
    private final Map<UUID, Boolean> flyPlayerToggles = new HashMap<>();
    private final EagleFactionsUtils plugin;

    public FlyModule(EagleFactionsUtils plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public ModuleType type()
    {
        return ModuleType.FLY;
    }

    public void init() throws CouldNotInitializeModule
    {
        try
        {
            EFUCommandManager commandManager = this.plugin.getCommandManager();
            commandManager.addCommand(Arrays.asList("tfly", "territoryfly"), CommandSpec.builder()
                    .description(Text.of("Lets you fly in factions territory"))
                    .permission(PluginPermissions.TFLY_COMMAND)
                    .executor(new FlyCommand(this))
                    .build());

            EventManager eventManager = this.plugin.getEventManager();
            eventManager.registerListeners(this.plugin, new TFlyListener(this));
        }
        catch (Exception exception)
        {
            throw new CouldNotInitializeModule("Could not initialize module: " + ModuleType.FLY, exception);
        }
    }

    public EagleFactionsUtils getPlugin()
    {
        return plugin;
    }

    public Map<UUID, Boolean> getFlyPlayerToggles()
    {
        return flyPlayerToggles;
    }
}
