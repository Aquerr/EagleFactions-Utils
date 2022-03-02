package io.github.aquerr.eaglefactionsutils.module;

import io.github.aquerr.eaglefactionsutils.EagleFactionsUtils;
import io.github.aquerr.eaglefactionsutils.module.exception.CouldNotInitializeModule;
import io.github.aquerr.eaglefactionsutils.module.fly.FlyModule;
import io.github.aquerr.eaglefactionsutils.module.help.HelpModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager
{
    private final EagleFactionsUtils plugin;

    private final List<Module> modules = new ArrayList<>();

    public ModuleManager(EagleFactionsUtils plugin)
    {
        this.plugin = plugin;
    }

    public void init()
    {
        this.modules.add(new HelpModule(this.plugin));
        this.modules.add(new FlyModule(this.plugin));

        // Perform the initialization of modules
        for (final Module module : modules)
        {
            try
            {
                module.init();
            }
            catch (CouldNotInitializeModule e)
            {
                e.printStackTrace();
            }
        }

        this.plugin.getCommandManager().registerCommands();
    }

    public void disablePlugin()
    {
        this.modules.clear();
    }
}
