package virtuoel.permafrost.reference;

import net.minecraftforge.common.config.Config;
import virtuoel.permafrost.Permafrost;

@Config(modid = Permafrost.MOD_ID)
public class PermafrostConfig
{
	@Config.Comment("False to disable Snow Layer melting.")
	public static boolean snowLayerMelting = false;
	
	@Config.Comment("True to ignore light for Snow Layer forming. Effectively false if replaceProvider is false.")
	public static boolean snowLayerFormingInLight = true;
	
	@Config.Comment("False to disable Ice melting.")
	public static boolean iceMelting = false;
	
	@Config.Comment("True to ignore light for Ice forming. Effectively false if replaceProvider is false.")
	public static boolean iceFormingInLight = true;
	
	@Config.Comment("False to disable replacement of world providers that use the Overworld DimensionType.")
	@Config.RequiresWorldRestart
	public static boolean replaceProvider = true;
}
