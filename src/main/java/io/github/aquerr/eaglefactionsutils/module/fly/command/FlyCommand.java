package io.github.aquerr.eaglefactionsutils.module.fly.command;

import io.github.aquerr.eaglefactions.api.entities.Faction;
import io.github.aquerr.eaglefactions.api.logic.FactionLogic;
import io.github.aquerr.eaglefactionsutils.module.fly.FlyModule;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Objects;

public class FlyCommand implements CommandExecutor
{
    private final FlyModule flyModule;

    public FlyCommand(FlyModule flyModule)
    {
        this.flyModule = flyModule;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        if (!isPlayer(src))
            throw new CommandException(Text.of("Only in-game players can use this command!"));

        Player player = (Player)src;
        boolean isFlightEnabled = this.flyModule.getFlyPlayerToggles().getOrDefault(player.getUniqueId(), false);

        if (isFlightEnabled)
        {
            this.flyModule.getFlyPlayerToggles().remove(player.getUniqueId());
        }
        else
        {
            this.flyModule.getFlyPlayerToggles().put(player.getUniqueId(), true);
        }

        if (!hasGameModeThatEnablesFlight(player) && isPlayerInsideItsOwnFaction(player))
        {
            player.offer(Keys.CAN_FLY, !isFlightEnabled);
            if (isFlightEnabled && player.get(Keys.IS_FLYING).orElse(false))
            {
                player.offer(Keys.IS_FLYING, false);
            }
        }

        src.sendMessage(Text.of(TextColors.GREEN, "Flight has been " + (isFlightEnabled ? "disabled" : "enabled")));

        return CommandResult.success();
    }

    private boolean isPlayer(CommandSource source)
    {
        return source instanceof Player;
    }

    private boolean hasGameModeThatEnablesFlight(Player player)
    {
        GameMode playerGameMode = player.get(Keys.GAME_MODE).orElse(GameModes.NOT_SET);
        return playerGameMode == GameModes.CREATIVE || playerGameMode == GameModes.SPECTATOR;
    }

    private boolean isPlayerInsideItsOwnFaction(Player player)
    {
        FactionLogic factionLogic = this.flyModule.getPlugin().getEagleFactions().getFactionLogic();
        Faction chunkFaction = factionLogic.getFactionByChunk(player.getWorld().getUniqueId(), player.getLocation().getChunkPosition()).orElse(null);
        Faction playerFaction = factionLogic.getFactionByPlayerUUID(player.getUniqueId()).orElse(null);

        return Objects.equals(chunkFaction, playerFaction);
    }
}
