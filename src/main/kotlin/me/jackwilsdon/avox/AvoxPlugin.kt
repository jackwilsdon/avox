package me.jackwilsdon.avox

import org.bukkit.plugin.java.JavaPlugin

class AvoxPlugin : JavaPlugin() {

    override fun onEnable() {
        server.pluginManager.registerEvents(EventListener(PlayerPermissionEventPermissionProvider()), this)

        logger.info("Avox started")
    }

    override fun onDisable() {
        logger.info("Avox stopped")
    }
}
