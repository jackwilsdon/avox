package me.jackwilsdon.avox

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.*
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.EquipmentSlot

private val USABLE_ITEMS = arrayOf(
        Material.WRITTEN_BOOK,
        Material.LEATHER_HELMET,
        Material.LEATHER_CHESTPLATE,
        Material.LEATHER_LEGGINGS,
        Material.LEATHER_BOOTS,
        Material.CHAINMAIL_HELMET,
        Material.CHAINMAIL_CHESTPLATE,
        Material.CHAINMAIL_LEGGINGS,
        Material.CHAINMAIL_BOOTS,
        Material.IRON_HELMET,
        Material.IRON_CHESTPLATE,
        Material.IRON_LEGGINGS,
        Material.IRON_BOOTS,
        Material.DIAMOND_HELMET,
        Material.DIAMOND_CHESTPLATE,
        Material.DIAMOND_LEGGINGS,
        Material.DIAMOND_BOOTS,
        Material.GOLD_HELMET,
        Material.GOLD_CHESTPLATE,
        Material.GOLD_LEGGINGS,
        Material.GOLD_BOOTS,
        Material.ELYTRA
)

class EventListener(private val permissionProvider: PlayerEventPermissionProvider) : Listener {
    private fun checkPermissions(player: Player, event: Cancellable) {
        if (!permissionProvider.isAllowed(player, event)) {
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerArmorStandManipulateEvent(event: PlayerArmorStandManipulateEvent) = checkPermissions(event.player, event)

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerEditBook(event: PlayerEditBookEvent) = checkPermissions(event.player, event)

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerDropItem(event: PlayerDropItemEvent) = checkPermissions(event.player, event)

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) = checkPermissions(event.player, event)

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (permissionProvider.isAllowed(event.player, event)) {
            return
        }

        // Player can't interact with blocks at all.
        event.setUseInteractedBlock(Event.Result.DENY)

        // If the player can't interact with the item in their hand, stop now.
        if (event.useItemInHand() == Event.Result.DENY) {
            return
        }

        // Work out what item the player is trying to use.
        val item = when (event.hand) {
            EquipmentSlot.HAND -> event.player.inventory.itemInMainHand
            EquipmentSlot.OFF_HAND -> event.player.inventory.itemInOffHand
            else -> null
        }

        // If the player isn't trying to use a book, deny their use.
        if (item == null || !USABLE_ITEMS.contains(item.type)) {
            event.setUseItemInHand(Event.Result.DENY)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerPickupArrow(event: PlayerPickupArrowEvent) = checkPermissions(event.player, event)

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val damager = event.damager

        // Player should not be able to attack things if they don't have the right permission.
        if (damager is Player) {
            checkPermissions(damager, event)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEntityPickupItem(event: EntityPickupItemEvent) {
        val entity = event.entity

        if (entity is Player) {
            checkPermissions(entity, event)
        }
    }
}