package virtuoel.permafrost;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class Permafrost extends DummyModContainer
{
	public static final String MOD_ID = "permafrost";
	public static final String MOD_NAME = "Permafrost";
	public static final String VERSION = "@VERSION@";
	
	public Permafrost()
	{
		super(new ModMetadata());
		ModMetadata metadata = getMetadata();
		metadata.modId = MOD_ID;
		metadata.name = MOD_NAME;
		metadata.version = VERSION;
		metadata.authorList.add("Virtuoel");
		metadata.description = "Prevents light from melting Ice and Snow Layers";
		metadata.screenshots = new String[0];
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller)
	{
		bus.register(this);
		return true;
	}
	
	@Subscribe
	public void postInit(FMLPostInitializationEvent event)
	{
		Blocks.ICE.setTickRandomly(false);
		Blocks.SNOW_LAYER.setTickRandomly(false);
	}
}
