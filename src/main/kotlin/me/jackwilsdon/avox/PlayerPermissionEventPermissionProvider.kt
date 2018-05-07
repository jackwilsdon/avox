package me.jackwilsdon.avox

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import java.util.logging.Logger

class PlayerPermissionEventPermissionProvider : PlayerEventPermissionProvider {
    override fun isAllowed(player: Player, event: Cancellable): Boolean {
        return !player.hasPermission("avox.is-avox")
    }
}