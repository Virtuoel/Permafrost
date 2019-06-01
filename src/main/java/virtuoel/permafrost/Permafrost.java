package virtuoel.permafrost;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import virtuoel.permafrost.util.ConfigHandler;
import virtuoel.permafrost.util.JsonConfigHandler;

public class Permafrost implements ModInitializer
{
	public static final String MOD_ID = "permafrost";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static final ConfigHandler<JsonObject> CONFIG_HANDLER = new JsonConfigHandler(MOD_ID, MOD_ID + "/config", Permafrost::createDefaultConfig);
	public static final JsonObject CONFIG = CONFIG_HANDLER.get();
	
	public Permafrost()
	{
		
	}
	
	@Override
	public void onInitialize()
	{
		
	}
	
	public static Identifier id(String name)
	{
		return new Identifier(MOD_ID, name);
	}
	
	private static JsonObject createDefaultConfig()
	{
		JsonObject config = new JsonObject();
		config.addProperty("snowLayerMelting", false);
		config.addProperty("snowLayerFormingInLight", true);
		config.addProperty("iceMelting", false);
		config.addProperty("iceFormingInLight", true);
		return config;
	}
}
