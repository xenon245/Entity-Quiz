package com.github.xenon.entityquiz.plugin

import com.github.noonmaru.tap.Tap
import com.github.noonmaru.tap.entity.TapEntity
import com.github.noonmaru.tap.entity.TapLivingEntity
import com.github.noonmaru.tap.packet.Packet
import com.github.xenon.entityquiz.plugin.EntityQuizPlugin.Companion.instance
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

class EntityCommand : CommandExecutor, TabCompleter {
    override fun onCommand(p0: CommandSender?, p1: Command?, p2: String, p3: Array<out String>): Boolean {
        if(p0 is Player) {
            when(p3[0]) {
                "summon" -> {
                        if(p3.size == 2) {
                            EntityQuiz.entity[p0] = true
                            EntityQuiz.public[p0] = false
                            EntityQuiz.entities[p0] = Entity(EntityType.valueOf(p3[1]), p0)
                        } else {
                            p0.sendMessage("/eq summon [entity]")
                        }
                }
                "destroy" -> {
                    if(p3.size == 1) {
                        EntityQuiz.entities[p0]!!.destroy()
                        EntityQuiz.entities.remove(p0)
                        EntityQuiz.entity[p0] = false
                    } else {
                        p0.sendMessage("/eq destroy")
                    }
                }
                "distance" -> {
                    if(p3.size == 2) {
                        EntityQuiz.distance[p0] = p3[1].toInt()
                    } else {
                        p0.sendMessage("/eq distance [distance]")
                    }
                }
                "scale" -> {
                    if(p3.size == 4) {
                        EntityQuiz.entities[p0]!!.scale(p3[1].toFloat(), p3[2].toFloat(), p3[3].toFloat())
                    } else {
                        p0.sendMessage("/eq scale [sizeX] [sizeY] [sizeZ]")
                    }
                }
                "public" -> {
                    if(p3.size == 1) {
                        EntityQuiz.public[p0] = true
                    } else {
                        p0.sendMessage("/eq public")
                    }
                }
                "move" -> {
                    when(EntityQuiz.move[p0]) {
                        true -> EntityQuiz.move[p0] = false
                        false -> EntityQuiz.move[p0] = true
                    }
                }
            }
        }
        return true
    }

    override fun onTabComplete(p0: CommandSender?, p1: Command?, p2: String?, p3: Array<out String>) =
        when(p3.size) {
            1 -> listOf("summon", "destroy", "distance", "scale", "public", "move").filter { it.startsWith(p3[0]) }
            2 -> {
                when(p3[0]) {
                    "summon" -> {
                        val list = arrayListOf<String>()
                        EntityType.values().toList().forEach { list += it.name }
                        list
                    }
                    else -> emptyList()
                }
            }
            else -> emptyList()
        }
}