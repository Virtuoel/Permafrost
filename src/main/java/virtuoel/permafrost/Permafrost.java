package virtuoel.permafrost;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import virtuoel.permafrost.reference.PermafrostConfig;

@Mod.EventBusSubscriber(modid = Permafrost.MOD_ID)
@Mod(modid = Permafrost.MOD_ID, version = "@VERSION@", acceptableRemoteVersions = "*", certificateFingerprint = "@FINGERPRINT@")
public class Permafrost
{
	public static final String MOD_ID = "permafrost";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	@Mod.EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event)
	{
		LOGGER.error("Expecting signature {}, however there is no signature matching that description. The file {} may have been tampered with. This version will NOT be supported by the author!", event.getExpectedFingerprint(), event.getSource().getName());
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		Blocks.ICE.setTickRandomly(PermafrostConfig.iceMelting);
		Blocks.SNOW_LAYER.setTickRandomly(PermafrostConfig.snowLayerMelting);
	}
	
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event)
	{
		if(PermafrostConfig.replaceProvider && event.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD)
		{
			WorldProvider provider = new WorldProviderSurface()
			{
				@Override
				public boolean canBlockFreeze(BlockPos pos, boolean noWaterAdj)
				{
					if(!PermafrostConfig.iceFormingInLight)
					{
						return super.canBlockFreeze(pos, noWaterAdj);
					}
					else if(world.getBiome(pos).getTemperature(pos) < 0.15F && pos.getY() >= 0 && pos.getY() < 256)
					{
						IBlockState state = world.getBlockState(pos);
						
						if(state.getBlock() == Blocks.WATER && state.getValue(BlockLiquid.LEVEL) == 0)
						{
							return !noWaterAdj ||
							       world.getBlockState(pos.north()).getMaterial() != Material.WATER ||
							       world.getBlockState(pos.south()).getMaterial() != Material.WATER ||
							       world.getBlockState(pos.east()).getMaterial() != Material.WATER ||
							       world.getBlockState(pos.west()).getMaterial() != Material.WATER;
						}
					}
					return false;
				}
				
				@Override
				public boolean canSnowAt(BlockPos pos, boolean checkLight)
				{
					if(!PermafrostConfig.snowLayerFormingInLight)
					{
						return super.canSnowAt(pos, checkLight);
					}
					if(world.getBiome(pos).getTemperature(pos) >= 0.15F)
					{
						return false;
					}
					else if(!checkLight)
					{
						return true;
					}
					else if(pos.getY() >= 0 && pos.getY() < 256)
					{
						IBlockState state = world.getBlockState(pos);
						
						return state.getBlock().isAir(state, world, pos) && Blocks.SNOW_LAYER.canPlaceBlockAt(world, pos);
					}
					
					return false;
				}
			};
			
			World world = event.getWorld();
			
			provider.setWorld(world);
			provider.setDimension(world.provider.getDimension());
			world.provider = provider;
		}
	}
	
	@SubscribeEvent
	public static void onConfigChanged(OnConfigChangedEvent event)
	{
		if(event.getModID().equals(Permafrost.MOD_ID))
		{
			ConfigManager.sync(Permafrost.MOD_ID, Config.Type.INSTANCE);
			
			Blocks.ICE.setTickRandomly(PermafrostConfig.iceMelting);
			Blocks.SNOW_LAYER.setTickRandomly(PermafrostConfig.snowLayerMelting);
		}
	}
}
