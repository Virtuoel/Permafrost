package virtuoel.permafrost.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.fabricmc.fabric.api.block.BlockSettingsExtensions;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import virtuoel.permafrost.api.PermafrostConfig;

@Mixin(Blocks.class)
public abstract class BlocksMixin
{
	@Redirect(method = "<clinit>", at = @At(value = "INVOKE", ordinal = 20, target = "Lnet/minecraft/block/Block$Settings;ticksRandomly()Lnet/minecraft/block/Block$Settings;"))
	private static Block.Settings onSnowTickRandomlyProxy(Block.Settings obj)
	{
		if(PermafrostConfig.DATA.get("snowLayerMelting").getAsBoolean())
		{
			BlockSettingsExtensions.ticksRandomly(obj);
		}
		return obj;
	}
	
	@Redirect(method = "<clinit>", at = @At(value = "INVOKE", ordinal = 21, target = "Lnet/minecraft/block/Block$Settings;ticksRandomly()Lnet/minecraft/block/Block$Settings;"))
	private static Block.Settings onIceTickRandomlyProxy(Block.Settings obj)
	{
		if(PermafrostConfig.DATA.get("iceMelting").getAsBoolean())
		{
			BlockSettingsExtensions.ticksRandomly(obj);
		}
		return obj;
	}
}
