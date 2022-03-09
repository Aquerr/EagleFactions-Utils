package io.github.aquerr.eaglefactionsutils.module.fly.listener;

import com.flowpowered.math.vector.Vector3i;
import io.github.aquerr.eaglefactions.api.entities.Faction;
import io.github.aquerr.eaglefactions.api.logic.FactionLogic;
import io.github.aquerr.eaglefactionsutils.module.fly.FlyModule;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Objects;

public class TFlyListener
{
    private final FlyModule flyModule;

    public TFlyListener(FlyModule flyModule)
    {
        this.flyModule = flyModule;
    }

    @Listener
    public void onPlayerMove(final MoveEntityEvent event, final @Root Player player)
    {
        final Location<World> fromLocation = event.getFromTransform().getLocation();
        final Location<World> toLocation = event.getToTransform().getLocation();

        if (isSameChunk(fromLocation.getChunkPosition(), toLocation.getChunkPosition()))
            return;

        if (hasGameModeThatEnablesFlight(player))
            return;

        if (hasEFUFlightEnabled(player))
        {
            if (!isPlayerInsideItsOwnFaction(player, toLocation))
            {
                setCanFly(player, false);
            }
            else
            {
                setCanFly(player, true);
            }
        }
    }

    @Listener(order = Order.EARLY, beforeModifications = true)
    public void onPlayerDisconnect(final ClientConnectionEvent.Disconnect event, final @Root Player player)
    {
        if (hasGameModeThatEnablesFlight(player))
            return;

        if (hasEFUFlightEnabled(player))
        {
            setCanFly(player, false);
            this.flyModule.getFlyPlayerToggles().remove(player.getUniqueId());
        }
    }

    private void setCanFly(final Player player, final boolean canFly)
    {
        player.offer(Keys.CAN_FLY, canFly);
        if (player.get(Keys.IS_FLYING).orElse(false))
        {
            player.offer(Keys.IS_FLYING, canFly);
        }
    }

    private boolean hasEFUFlightEnabled(Player player)
    {
        return this.flyModule.getFlyPlayerToggles().getOrDefault(player.getUniqueId(), false);
    }

    private boolean isPlayerInsideItsOwnFaction(Player player, Location<World> location)
    {
        FactionLogic factionLogic = this.flyModule.getPlugin().getEagleFactions().getFactionLogic();
        Faction chunkFaction = factionLogic.getFactionByChunk(location.getExtent().getUniqueId(), location.getChunkPosition()).orElse(null);
        Faction playerFaction = factionLogic.getFactionByPlayerUUID(player.getUniqueId()).orElse(null);

        return playerFaction != null && Objects.equals(chunkFaction, playerFaction);
    }

    private boolean isSameChunk(Vector3i chunkPosition1, Vector3i chunkPosition2)
    {
        return chunkPosition1.equals(chunkPosition2);
    }

    private boolean hasGameModeThatEnablesFlight(Player player)
    {
        GameMode playerGameMode = player.get(Keys.GAME_MODE).orElse(GameModes.NOT_SET);
        return playerGameMode == GameModes.CREATIVE || playerGameMode == GameModes.SPECTATOR;
    }
}
