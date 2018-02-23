package virtuoel.permafrost.asm;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class LoadingPlugin implements IFMLLoadingPlugin
{
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] { "virtuoel.permafrost.asm.ClassTransformer" };
	}
	
	@Override
	public String getModContainerClass()
	{
		return null;
	}
	
	@Override
	public String getSetupClass()
	{
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data)
	{
		ClassTransformer.runtimeDeobf = (Boolean) data.get("runtimeDeobfuscationEnabled");
	}
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}
