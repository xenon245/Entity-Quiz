package com.github.xenon.entityquiz.plugin

import com.github.noonmaru.customentity.CustomEntityPacket
import com.github.noonmaru.tap.Tap
import com.github.noonmaru.tap.entity.TapEntity
import com.github.noonmaru.tap.entity.TapLivingEntity
import com.github.noonmaru.tap.packet.Packet.ENTITY
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class Entity(entityType: EntityType, val owner: Player) {
    private val tapEntity: TapLivingEntity = Tap.ENTITY.createEntity(entityType.entityClass)
    private val entity: LivingEntity?
    private lateinit var position: Location
    private var ticks = 0
    private var removed = false
    var scaleX: Float = 1.0F
    var scaleY: Float = 1.0F
    var scaleZ: Float = 1.0F

    /**
     * This boolean checks whether the projectile is dead,
     * and should be removed.
     */
    var dead = false
        private set

    /**
     * This is called when the new projectile is created.
     */
    init {
        tapEntity.apply {
            setGravity(false)
            bukkitEntity.setAI(false)
            owner.eyeLocation.let { pos ->
                setPositionAndRotation(pos.x, pos.y - 0.5, pos.z, pos.yaw + 90F, 0F)
            }
        }
        entity = tapEntity.bukkitEntity.apply {
            ENTITY.spawnMob(this).sendAll()
            ENTITY.metadata(this).sendAll()
        }
    }
    fun update(location: Location) {
        position = location
        tapEntity.isInvisible = false
        position.let { tapEntity.setPosition(it.x, it.y, it.z) }
        ENTITY.metadata(entity).sendTo(owner)
        tapEntity.let { ENTITY.teleport(entity, it.posX, it.posY, it.posZ, it.yaw, it.pitch, false).sendTo(owner) }
    }
    fun showToOthers(location: Location) {
        val players = Bukkit.getOnlinePlayers().filter { it !== owner }
        when(EntityQuiz.public[owner]) {
            true -> tapEntity.isInvisible = false
            false -> tapEntity.isInvisible = true
        }
        position = location
        position.let { tapEntity.setPosition(it.x, it.y, it.z) }
        ENTITY.metadata(entity).sendTo(players)
        tapEntity.let { ENTITY.teleport(entity, it.posX, it.posY, it.posZ, it.yaw, it.pitch, false).sendTo(players) }
    }
    fun destroy() {
        tapEntity.isInvisible = true
        ENTITY.metadata(tapEntity.bukkitEntity).sendAll()
        ENTITY.destroy(tapEntity.id)
    }
    fun scale(x: Float, y: Float, z: Float) {
        scaleX = x
        scaleY = y
        scaleZ = z
        CustomEntityPacket.register(tapEntity.id).sendAll()
        CustomEntityPacket.scale(tapEntity.id, x, y, z, 10).sendAll()
    }
    fun scale() {
        CustomEntityPacket.register(tapEntity.id).sendAll()
        CustomEntityPacket.scale(tapEntity.id, scaleX, scaleY, scaleZ, 10).sendAll()
    }
}