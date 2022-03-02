package io.github.aquerr.eaglefactionsutils.module.exception;

public class CouldNotInitializeModule extends Exception
{
    public CouldNotInitializeModule(String message, Exception exception)
    {
        super(message, exception);
    }
}
