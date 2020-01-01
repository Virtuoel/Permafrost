package virtuoel.permafrost.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.Block;

@Mixin(Block.class)
public interface BlockAccessor
{
	@Accessor
	void setRandomTicks(boolean randomTicks);
}
