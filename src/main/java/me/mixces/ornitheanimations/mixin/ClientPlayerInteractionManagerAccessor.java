package me.mixces.ornitheanimations.mixin;

import net.minecraft.client.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayerInteractionManager.class)
public interface ClientPlayerInteractionManagerAccessor {

	@Accessor
	BlockPos getTarget();

	@Accessor
	boolean getIsMiningBlock();

	@Accessor
	void setIsMiningBlock(boolean miningBlock);

	@Accessor
	float getMiningProgress();

	@Accessor
	void setMiningProgress(float miningProgress);
}
