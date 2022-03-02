package io.github.aquerr.eaglefactionsutils.module.help;

import io.github.aquerr.eaglefactionsutils.EFUCommandManager;
import io.github.aquerr.eaglefactionsutils.EagleFactionsUtils;
import io.github.aquerr.eaglefactionsutils.PluginPermissions;
import io.github.aquerr.eaglefactionsutils.module.Module;
import io.github.aquerr.eaglefactionsutils.module.exception.CouldNotInitializeModule;
import io.github.aquerr.eaglefactionsutils.module.fly.ModuleType;
import io.github.aquerr.eaglefactionsutils.module.help.command.HelpCommand;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.Collections;

public class HelpModule implements Module
{
    private final EagleFactionsUtils plugin;

    public HelpModule(EagleFactionsUtils plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public ModuleType type()
    {
        return ModuleType.HELP;
    }

    @Override
    public void init() throws CouldNotInitializeModule
    {
        try
        {
            EFUCommandManager commandManager = this.plugin.getCommandManager();
            commandManager.addCommand(Collections.singletonList("help"), CommandSpec.builder()
                    .description(Text.of("Displays all EagleFactions-Utils commands"))
                    .permission(PluginPermissions.HELP_COMMAND)
                    .executor(new HelpCommand(this))
                    .build());
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
}
