package me.jackwilsdon.avox

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable

interface PlayerEventPermissionProvider {
    fun isAllowed(player: Player, event: Cancellable): Boolean
}