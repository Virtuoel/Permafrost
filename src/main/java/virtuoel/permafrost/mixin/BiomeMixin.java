package virtuoel.permafrost.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import virtuoel.permafrost.api.PermafrostConfig;

@Mixin(Biome.class)
public abstract class BiomeMixin
{
	@Redirect(method = "canSetIce(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldView;getLightLevel(Lnet/minecraft/world/LightType;Lnet/minecraft/util/math/BlockPos;)I"))
	private int canSetIceGetLightLevelProxy(WorldView obj, LightType var1, BlockPos var2)
	{
		return PermafrostConfig.DATA.get("iceFormingInLight").getAsBoolean() ? 0 : obj.getLightLevel(var1, var2);
	}
	
	@Redirect(method = "canSetSnow(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Z)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldView;getLightLevel(Lnet/minecraft/world/LightType;Lnet/minecraft/util/math/BlockPos;)I"))
	private int canSetSnowGetLightLevelProxy(WorldView obj, LightType var1, BlockPos var2)
	{
		return PermafrostConfig.DATA.get("snowLayerFormingInLight").getAsBoolean() ? 0 : obj.getLightLevel(var1, var2);
	}
}
