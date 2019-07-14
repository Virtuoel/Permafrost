package virtuoel.permafrost;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import virtuoel.permafrost.api.PermafrostConfig;

public class Permafrost implements ModInitializer
{
	public static final String MOD_ID = "permafrost";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public Permafrost()
	{
		PermafrostConfig.DATA.getClass();
	}
	
	@Override
	public void onInitialize()
	{
		
	}
	
	public static Identifier id(String name)
	{
		return new Identifier(MOD_ID, name);
	}
}
