package me.mixces.ornitheanimations.shared

import net.minecraft.entity.living.LivingEntity

interface IDamageTint {
    fun `ornitheAnimations$setupOverlayColor`(entity: LivingEntity, tickDelta: Float): Boolean
}
