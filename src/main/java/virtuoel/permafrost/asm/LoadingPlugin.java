package virtuoel.permafrost.asm;

import java.security.cert.Certificate;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.CertificateHelper;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import virtuoel.permafrost.Permafrost;

@IFMLLoadingPlugin.Name(LoadingPlugin.CORE_MOD_ID)
@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions("virtuoel." + Permafrost.MOD_ID + ".asm")
@IFMLLoadingPlugin.MCVersion(MinecraftForge.MC_VERSION)
public class LoadingPlugin implements IFMLLoadingPlugin
{
	public static final String CORE_MOD_ID = Permafrost.MOD_ID + "-core";
	public static final String CORE_MOD_NAME = Permafrost.MOD_NAME + "Core";
	private static final String CERTIFICATE_FINGERPRINT = "@FINGERPRINT@";
	
	public static final Logger LOGGER = LogManager.getLogger(CORE_MOD_ID);
	
	public LoadingPlugin()
	{
		Certificate[] certificates = LoadingPlugin.class.getProtectionDomain().getCodeSource().getCertificates();
		ImmutableList<String> certList = CertificateHelper.getFingerprints(certificates);
		if(!certList.contains(CERTIFICATE_FINGERPRINT))
		{
			LOGGER.error("Expecting signature {}, however there is no signature matching that description", CERTIFICATE_FINGERPRINT);
		}
	}
	
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] { "virtuoel.permafrost.asm.ClassTransformer" };
	}
	
	@Override
	public String getModContainerClass()
	{
		return "virtuoel.permafrost.Permafrost";
	}
	
	@Override
	public String getSetupClass()
	{
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data)
	{
		
	}
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}
