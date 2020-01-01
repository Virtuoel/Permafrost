package virtuoel.permafrost;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import virtuoel.permafrost.api.PermafrostConfig;
import virtuoel.permafrost.mixin.BlockAccessor;

public class Permafrost implements ModInitializer
{
	public static final String MOD_ID = "permafrost";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	@Override
	public void onInitialize()
	{
		if(!PermafrostConfig.DATA.get("snowLayerMelting").getAsBoolean())
		{
			((BlockAccessor) Blocks.SNOW).setRandomTicks(false);
		}
		
		if(!PermafrostConfig.DATA.get("iceMelting").getAsBoolean())
		{
			((BlockAccessor) Blocks.ICE).setRandomTicks(false);
		}
	}
}
