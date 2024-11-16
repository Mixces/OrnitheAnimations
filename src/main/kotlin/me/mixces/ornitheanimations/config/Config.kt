package me.mixces.ornitheanimations.config

import net.ornithemc.osl.config.api.ConfigScope
import net.ornithemc.osl.config.api.LoadingPhase
import net.ornithemc.osl.config.api.config.BaseConfig
import net.ornithemc.osl.config.api.config.option.BooleanOption
import net.ornithemc.osl.config.api.serdes.FileSerializerType
import net.ornithemc.osl.config.api.serdes.SerializerTypes

object Config : BaseConfig() {

    val GROUP_NAME: String = "OrnitheAnimations"

    /* Mechanics */
    val BLOCK_HITTING: BooleanOption = BooleanOption("blockHitting", null, true)
    val SMOOTH_SNEAKING: BooleanOption = BooleanOption("smoothSneaking", null, true)
    val FULL_REEQUIP_LOGIC: BooleanOption = BooleanOption("fullReequipLogic", null, true)
    val HIDE_MISS_PENALTY: BooleanOption = BooleanOption("hideMissPenalty", null, true)

    /* Render */
    val OLD_ITEM_POSITIONS: BooleanOption = BooleanOption("oldItemPositions", null, true)
    val BETTER_GLINT: BooleanOption = BooleanOption("betterGlint", null, true)
    val MIRRORED_PROJECTILES: BooleanOption = BooleanOption("mirroredProjectiles", null, true)
    val ALTERNATIVE_DAMAGE_TINT: BooleanOption = BooleanOption("alternativeDamageTint", null, true)
    val BETTER_ITEM_PICKUP: BooleanOption = BooleanOption("betterItemPickup", null, true)
    val OLD_XP_ORB_RENDERING: BooleanOption = BooleanOption("oldXpOrbRendering", null, true)
    val FAST_ITEMS: BooleanOption = BooleanOption("fastItems", null, true)
    val REPLACE_CAST_ROD: BooleanOption = BooleanOption("replaceCastRod", null, true)
    val SIMPLE_SKIN_RENDERING: BooleanOption = BooleanOption("simpleSkinRendering", null, true)

    /* GUI */
    val REMOVE_HEART_FLASHING: BooleanOption = BooleanOption("removeHeartFlashing", null, true)
    val SIMPLE_PLAYER_LIST: BooleanOption = BooleanOption("simplePlayerList", null, true)
    val OLD_DEBUG_MENU: BooleanOption = BooleanOption("oldDebugMenu", null, true)
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
            FULL_REEQUIP_LOGIC,
            HIDE_MISS_PENALTY,
            OLD_ITEM_POSITIONS,
            BETTER_GLINT,
            MIRRORED_PROJECTILES,
            ALTERNATIVE_DAMAGE_TINT,
            BETTER_ITEM_PICKUP,
            OLD_XP_ORB_RENDERING,
            FAST_ITEMS,
            REPLACE_CAST_ROD,
            SIMPLE_SKIN_RENDERING,
            REMOVE_HEART_FLASHING,
            SIMPLE_PLAYER_LIST,
            OLD_DEBUG_MENU,
            CENTER_GUI_SELECTION
        )
    }
}
