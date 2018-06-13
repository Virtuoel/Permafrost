package virtuoel.permafrost;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Permafrost.MOD_ID, version = "@VERSION@", acceptableRemoteVersions = "*", certificateFingerprint = "@FINGERPRINT@")
public class Permafrost
{
	public static final String MOD_ID = "permafrost";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	@Mod.EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event)
	{
		LOGGER.error("Expecting signature {}, however there is no signature matching that description", event.getExpectedFingerprint());
	}
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Blocks.ICE.setTickRandomly(false);
		Blocks.SNOW_LAYER.setTickRandomly(false);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
		if(event.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD)
		{
			WorldProvider provider = new WorldProviderSurface()
			{
				@Override
				public boolean canBlockFreeze(BlockPos pos, boolean noWaterAdj)
				{
					if(world.getBiome(pos).getTemperature(pos) >= 0.15F)
					{
						return false;
					}
					else if(pos.getY() >= 0 && pos.getY() < 256)
					{
						IBlockState iblockstate1 = world.getBlockState(pos);
						Block block = iblockstate1.getBlock();
						
						if((block == Blocks.WATER || block == Blocks.FLOWING_WATER) && ((Integer) iblockstate1.getValue(BlockLiquid.LEVEL)).intValue() == 0)
						{
							if(!noWaterAdj)
							{
								return true;
							}
							
							if(world.getBlockState(pos.north()).getMaterial() != Material.WATER ||
								world.getBlockState(pos.south()).getMaterial() != Material.WATER ||
								world.getBlockState(pos.east()).getMaterial() != Material.WATER ||
								world.getBlockState(pos.west()).getMaterial() != Material.WATER
							)
							{
								return true;
							}
						}
					}
					return false;
				}
				
				@Override
				public boolean canSnowAt(BlockPos pos, boolean checkLight)
				{
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
						IBlockState iblockstate1 = world.getBlockState(pos);
						
						if(iblockstate1.getBlock().isAir(iblockstate1, world, pos) && Blocks.SNOW_LAYER.canPlaceBlockAt(world, pos))
						{
							return true;
						}
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
}
