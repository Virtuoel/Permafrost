package virtuoel.permafrost.api;

import java.util.function.Supplier;

import com.google.gson.JsonObject;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Lazy;

public class PermafrostConfig
{
	public static final String NAMESPACE = "permafrost";
	
	public static final Supplier<JsonObject> HANDLER =
		!FabricLoader.getInstance().isModLoaded(NAMESPACE) ?
		new Lazy<JsonObject>(JsonObject::new)::get :
		((Supplier<Supplier<Supplier<JsonObject>>>)() ->
		(() -> new virtuoel.permafrost.util.JsonConfigHandler(
			NAMESPACE,
			NAMESPACE + "/config.json",
			PermafrostConfig::createDefaultConfig
		))).get().get();
	
	public static final JsonObject DATA = HANDLER.get();
	
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
