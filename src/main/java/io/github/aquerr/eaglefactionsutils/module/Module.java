package io.github.aquerr.eaglefactionsutils.module;

import io.github.aquerr.eaglefactionsutils.module.exception.CouldNotInitializeModule;
import io.github.aquerr.eaglefactionsutils.module.fly.ModuleType;

public interface Module
{
    ModuleType type();

    void init() throws CouldNotInitializeModule;
}
