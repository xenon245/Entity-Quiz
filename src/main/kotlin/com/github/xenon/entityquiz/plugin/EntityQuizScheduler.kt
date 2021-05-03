package com.github.xenon.entityquiz.plugin

import com.github.noonmaru.tap.packet.Packet
import org.bukkit.Bukkit

class EntityQuizScheduler : Runnable {
    override fun run() {
        Bukkit.getOnlinePlayers().forEach { p ->
            if(EntityQuiz.distance[p] == null) {
                EntityQuiz.distance[p] = 3
            }
            if(EntityQuiz.move[p] == null) {
                EntityQuiz.move[p] = true
            }
        }
        EntityQuiz.entities.forEach { (p, e) ->
            if(EntityQuiz.move[p] == true) {
                e.update(p.eyeLocation.apply {
                    add(direction.multiply(EntityQuiz.distance[p]!!))
                })
                e.showToOthers(p.eyeLocation.apply {
                    add(direction.multiply(EntityQuiz.distance[p]!!))
                })
            }
        }
    }
}