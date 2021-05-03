package com.github.xenon.entityquiz.plugin

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

class EntityQuizPlugin : JavaPlugin(), Listener {
    companion object {
        lateinit var instance: EntityQuizPlugin
    }
    override fun onEnable() {
        Bukkit.getServer().scheduler.runTaskTimer(this, EntityQuizScheduler(), 0L, 1L)
        Bukkit.getPluginManager().registerEvents(this, this)
        instance = this
        getCommand("eq").executor = EntityCommand()
        getCommand("eq").tabCompleter = EntityCommand()
    }
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        EntityQuiz.entity[event.player] = false
        Bukkit.getOnlinePlayers().forEach { p ->
            EntityQuiz.entities[p]!!.scale()
        }
    }
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        EntityQuiz.entity[event.player] = false
        if(EntityQuiz.entities[event.player] != null) {
            EntityQuiz.entities[event.player]!!.destroy()
            EntityQuiz.entities.remove(event.player)
        }
    }
}