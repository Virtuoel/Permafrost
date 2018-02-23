package virtuoel.permafrost;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = Permafrost.MOD_ID, name = Permafrost.MOD_NAME, version = Permafrost.VERSION, acceptableRemoteVersions = "*", certificateFingerprint = "@FINGERPRINT@")
public class Permafrost
{
	public static final String MOD_ID = "permafrost";
	public static final String MOD_NAME = "Permafrost";
	public static final String VERSION = MinecraftForge.MC_VERSION + "-@VERSION@";
	
	@Mod.Instance(MOD_ID)
	public static Permafrost instance;
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static boolean isDev = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
	
	@Mod.EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event)
	{
		LOGGER.warn("Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		Blocks.ICE.setTickRandomly(false);
		Blocks.SNOW_LAYER.setTickRandomly(false);
	}
}
