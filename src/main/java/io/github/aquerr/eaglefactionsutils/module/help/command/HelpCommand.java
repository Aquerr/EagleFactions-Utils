package io.github.aquerr.eaglefactionsutils.module.help.command;

import com.google.common.collect.Lists;
import io.github.aquerr.eaglefactionsutils.module.help.HelpModule;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.Map;

public class HelpCommand implements CommandExecutor
{
    private final HelpModule helpModule;

    public HelpCommand(HelpModule helpModule)
    {
        this.helpModule = helpModule;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException
    {
        final int pageNumber = args.<Integer>getOne(Text.of("page")).orElse(1);
        final Map<List<String>, CommandSpec> commands = this.helpModule.getPlugin().getCommandManager().getSubcommands();
        final List<Text> helpList = Lists.newArrayList();

        for (final List<String> aliases: commands.keySet())
        {
            CommandSpec commandSpec = commands.get(aliases);

            if(source instanceof Player)
            {
                Player player = (Player)source;

                if(!commandSpec.testPermission(player))
                {
                    continue;
                }
            }

            final Text commandHelp = Text.builder()
                    .append(Text.builder()
                            .append(Text.of(TextColors.AQUA, "/fu " + aliases.toString().replace("[","").replace("]","")))
                            .build())
                    .append(Text.builder()
                            .append(Text.of(TextColors.WHITE, " - " + commandSpec.getShortDescription(source).get().toPlain() + "\n"))
                            .build())
                    .append(Text.builder()
                            .append(Text.of(TextColors.GRAY, "Usage: /fu " + aliases.toString().replace("[","").replace("]","") + " " + commandSpec.getUsage(source).toPlain()))
                            .build())
                    .build();
            helpList.add(commandHelp);
        }

        //Sort commands alphabetically.
        helpList.sort(Text::compareTo);

        PaginationList.Builder paginationBuilder = PaginationList.builder()
                .title(Text.of(TextColors.GREEN, "Commands List"))
                .padding(Text.of("-"))
                .contents(helpList)
                .linesPerPage(11);
        paginationBuilder.build().sendTo(source, pageNumber);
        return CommandResult.success();
    }
}
