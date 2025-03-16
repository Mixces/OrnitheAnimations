package me.mixces.ornitheanimations.config

import net.ornithemc.osl.config.api.ConfigScope
import net.ornithemc.osl.config.api.LoadingPhase
import net.ornithemc.osl.config.api.config.BaseConfig
import net.ornithemc.osl.config.api.config.option.BooleanOption
import net.ornithemc.osl.config.api.serdes.FileSerializerType
import net.ornithemc.osl.config.api.serdes.SerializerTypes

object Config : BaseConfig() {

    const val GROUP_NAME: String = "OrnitheAnimations"

    /* Mechanics */
    val BLOCK_HITTING: BooleanOption = BooleanOption("blockHitting", null, true)
    val SMOOTH_SNEAKING: BooleanOption = BooleanOption("smoothSneaking", null, true)
    val OLD_EQUIP_LOGIC: BooleanOption = BooleanOption("oldEquipLogic", null, true)
    val FIX_ARM_ITEM_ROTATION: BooleanOption = BooleanOption("fixArmItemRotation", null, true)
    val OLD_MISS_PENALTY: BooleanOption = BooleanOption("oldMissPenalty", null, true)
    val OLD_RENDER_TICK_DELAY: BooleanOption = BooleanOption("oldRenderTickDelay", null, true)

    /* Render */
    val OLD_ITEM_POSITIONS: BooleanOption = BooleanOption("oldItemPositions", null, true)
    val OLD_GLINT: BooleanOption = BooleanOption("oldGlint", null, true)
    val OLD_LAYER_GLINT: BooleanOption = BooleanOption("oldLayerGlint", null, true)
    val MIRRORED_PROJECTILES: BooleanOption = BooleanOption("mirroredProjectiles", null, true)
    val OLD_DAMAGE_TINT: BooleanOption = BooleanOption("oldDamageTint", null, true)
    val OLD_ITEM_PICKUP: BooleanOption = BooleanOption("oldItemPickup", null, true)
    val OLD_XP_ORB_RENDERING: BooleanOption = BooleanOption("oldXpOrbRendering", null, true)
    val FAST_ITEMS: BooleanOption = BooleanOption("fastItems", null, true)
    val REPLACE_CAST_ROD: BooleanOption = BooleanOption("replaceCastRod", null, true)
    val OLD_SKIN_RENDERING: BooleanOption = BooleanOption("oldSkinRendering", null, true)
    val OLD_FLAME_OFFSET: BooleanOption = BooleanOption("oldFlameOffset", null, true)
    val OLD_SKULL_MODEL: BooleanOption = BooleanOption("oldSkullModel", null, true)

    /* GUI */
    val REMOVE_HEART_FLASHING: BooleanOption = BooleanOption("removeHeartFlashing", null, true)
    val OLD_PLAYER_LIST: BooleanOption = BooleanOption("oldPlayerList", null, true)
    val OLD_DEBUG_MENU: BooleanOption = BooleanOption("oldDebugMenu", null, true)
    val REMOVE_TITLES: BooleanOption = BooleanOption("removeTitles", null, true)
    val CENTER_GUI_SELECTION: BooleanOption = BooleanOption("centerGuiSelection", null, true)

    override fun getNamespace(): String {
        return ""
    }

    override fun getName(): String {
        return "OrnitheAnimations"
    }

    override fun getSaveName(): String {
        return "ornitheanimations.json"
    }

    override fun getScope(): ConfigScope {
        return ConfigScope.GLOBAL
    }

    override fun getLoadingPhase(): LoadingPhase {
        return LoadingPhase.READY
    }

    override fun getType(): FileSerializerType<*> {
        return SerializerTypes.JSON
    }

    override fun getVersion(): Int {
        return 0
    }

    override fun init() {
        registerOptions(
            GROUP_NAME,
            BLOCK_HITTING,
            SMOOTH_SNEAKING,
            OLD_EQUIP_LOGIC,
            FIX_ARM_ITEM_ROTATION,
            OLD_MISS_PENALTY,
            OLD_RENDER_TICK_DELAY,
            OLD_ITEM_POSITIONS,
            OLD_GLINT,
            OLD_LAYER_GLINT,
            MIRRORED_PROJECTILES,
            OLD_DAMAGE_TINT,
            OLD_ITEM_PICKUP,
            OLD_XP_ORB_RENDERING,
            FAST_ITEMS,
            REPLACE_CAST_ROD,
            OLD_SKIN_RENDERING,
            OLD_FLAME_OFFSET,
            OLD_SKULL_MODEL,
            REMOVE_HEART_FLASHING,
            OLD_PLAYER_LIST,
            OLD_DEBUG_MENU,
            REMOVE_TITLES,
            CENTER_GUI_SELECTION
        )
    }
}
