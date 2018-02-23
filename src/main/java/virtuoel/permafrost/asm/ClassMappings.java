package virtuoel.permafrost.asm;

import java.util.HashMap;

public class ClassMappings
{
	public static final HashMap<String, String> MAPPINGS = new HashMap<String, String>();
	
	static
	{
		MAPPINGS.put("net/minecraft/util/math/BlockPos", "et");
		MAPPINGS.put("net/minecraft/world/World", "ams");
	}
}
