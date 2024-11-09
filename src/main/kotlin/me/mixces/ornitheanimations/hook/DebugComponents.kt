@file:JvmName("DebugComponents")

package me.mixces.ornitheanimations.hook

import me.mixces.ornitheanimations.dsl.mc
import net.minecraft.client.Minecraft
import net.minecraft.client.render.world.RenderChunk
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.LightType

fun getLeft(): List<String> {
    return buildList {
        add("Minecraft 1.8.9 (${Minecraft.getCurrentFps()} fps, ${RenderChunk.updateCounter} chunk updates)")
        add(mc.worldRenderer.chunkDebugInfo) /* this will return different data than in 1.7 unfortunately */
        add(mc.worldRenderer.entityDebugInfo)
        add("P: ${mc.particleManager.particlesDebugInfo}. T: ${mc.world.entitiesDebugInfo}")
        add(mc.world.chunkSourceDebugInfo)
        add("")
    }
}

fun getLeftBottom(): List<String> {
    val blockpos = BlockPos(
        mc.camera.x,
        mc.camera.shape.minY,
        mc.camera.z
    )
    val entity = mc.camera
    val enumfacing = entity.horizontalFacing
    val chunk = mc.world.getChunk(blockpos)

    return buildList {
        val playerPosX = mc.player.x
        val playerPosY = mc.player.y
        val playerPosZ = mc.player.z

        add("x: %.5f (%d) // c: %d (%d)".format(
            playerPosX,
            MathHelper.floor(playerPosX),
            MathHelper.floor(playerPosX) shr 4,
            MathHelper.floor(playerPosX) and 15
        ))
        add("y: %.3f (feet pos, %.3f eyes pos)".format(
            mc.player.shape.minY,
            playerPosY + mc.player.getEyeHeight()
        ))
        add("z: %.5f (%d) // c: %d (%d)".format(
            playerPosZ,
            MathHelper.floor(playerPosZ),
            MathHelper.floor(playerPosZ) shr 4,
            MathHelper.floor(playerPosZ) and 15
        ))

        add("f: ${MathHelper.floor(
            (mc.player.yaw * 4.0f / 360.0f) + 0.5
        )
                and 3} (${enumfacing.toString().uppercase()}) / ${MathHelper.wrapDegrees(mc.player.yaw)}"
        )

        val light = chunk.getLight(blockpos, 0)
        val biomeName = chunk.getBiome(blockpos, mc.world.biomeSource).name
        val blockLight = chunk.getLight(LightType.BLOCK, blockpos)
        val skyLight = chunk.getLight(LightType.SKY, blockpos)

        add("lc: $light b: $biomeName bl: $blockLight sl: $skyLight rl: $light")

        add("ws: %.3f, fs: %.3f, g: %b, fl: %.0f".format(
            mc.player.abilities.walkSpeed,
            mc.player.abilities.flySpeed,
            mc.player.onGround,
            playerPosY
        ))

        mc.gameRenderer?.takeIf { it.hasShader() }?.let {
            add("shader: ${it.shader.name}")
        }
    }
}

fun getRight(): List<String> {
    val runtime = Runtime.getRuntime()
    val maxMemory = runtime.maxMemory()
    val totalMemory = runtime.totalMemory()
    val freeMemory = runtime.freeMemory()
    val usedMemory = totalMemory - freeMemory

    val usedMemoryMb = bytesToMb(usedMemory)
    val maxMemoryMb = bytesToMb(maxMemory)
    val totalMemoryMb = bytesToMb(totalMemory)

    return buildList {
        add("Used memory: ${usedMemory * 100L / maxMemory}% (${usedMemoryMb}MB) of ${maxMemoryMb}MB")
        add("Allocated memory: ${totalMemory * 100L / maxMemory}% (${totalMemoryMb}MB)")
        add("")
    }
}

private fun bytesToMb(bytes: Long): Long {
    return bytes / 1024L / 1024L
}
