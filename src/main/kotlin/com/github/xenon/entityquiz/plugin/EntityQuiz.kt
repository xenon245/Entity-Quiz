package com.github.xenon.entityquiz.plugin

import com.github.noonmaru.tap.entity.TapEntity
import com.github.noonmaru.tap.entity.TapLivingEntity
import org.bukkit.entity.Player

object EntityQuiz {
    val entity = HashMap<Player, Boolean>()
    val entities = HashMap<Player, Entity>()
    val distance = HashMap<Player, Int>()
    val public = HashMap<Player, Boolean>()
    val move = HashMap<Player, Boolean>()
}