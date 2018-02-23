package virtuoel.permafrost.asm;

import java.util.HashMap;
import java.util.function.Function;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;

import net.minecraft.launchwrapper.IClassTransformer;
import virtuoel.permafrost.Permafrost;

public class ClassTransformer implements IClassTransformer
{
	public static boolean ENABLED = true;
	
	public static boolean runtimeDeobf;
	
	private static final HashMap<String, Function<ClassNode, Void>> TRANSFORMATIONS = new HashMap<String, Function<ClassNode, Void>>();
	
	static
	{
		TRANSFORMATIONS.put("net.minecraft.world.World", ClassTransformer::transformWorld);
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(ENABLED && TRANSFORMATIONS.containsKey(transformedName))
		{
			System.out.println("Transforming Class [" + transformedName + "]");
			try
			{
				ClassNode classNode = new ClassNode();
				ClassReader classReader = new ClassReader(basicClass);
				classReader.accept(classNode, 0);
				
				TRANSFORMATIONS.get(transformedName).apply(classNode);
				
				ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES)
				{
					@Override
					protected String getCommonSuperClass(String type1, String type2)
					{
						String superclass = "java/lang/Object";
						try
						{
							superclass = super.getCommonSuperClass(type1, type2);
						}
						catch(Exception e)
						{
							
						}
						return superclass;
					}
				};
				classNode.accept(classWriter);
				return classWriter.toByteArray();
			}
			catch(Exception e)
			{
				Permafrost.LOGGER.throwing(e);
			}
		}
		return basicClass;
	}
	
	private static Void transformWorld(ClassNode classNode)
	{
		MethodNode method = findMethod(classNode, new MethodInfo("canBlockFreezeBody", "(Lnet/minecraft/util/math/BlockPos;Z)Z"));
		if(method != null)
		{
			AbstractInsnNode target = null;
			for(AbstractInsnNode instruction : method.instructions.toArray())
			{
				if(instruction.getOpcode() == Opcodes.BIPUSH)
				{
					target = instruction;
					break;
				}
			}
			if(target != null)
			{
				method.instructions.set(target, new IntInsnNode(Opcodes.BIPUSH, 16));
			}
		}
		
		method = findMethod(classNode, new MethodInfo("canSnowAtBody", "(Lnet/minecraft/util/math/BlockPos;Z)Z"));
		if(method != null)
		{
			AbstractInsnNode target = null;
			for(AbstractInsnNode instruction : method.instructions.toArray())
			{
				if(instruction.getOpcode() == Opcodes.BIPUSH)
				{
					target = instruction;
					break;
				}
			}
			if(target != null)
			{
				method.instructions.set(target, new IntInsnNode(Opcodes.BIPUSH, 16));
			}
		}
		
		return null;
	}
	
	private static MethodNode findMethod(ClassNode classNode, MethodInfo target)
	{
		for(MethodNode method : classNode.methods)
		{
			if((method.name.equals(target.getMCPName()) || method.name.equals(target.getSRGName()) || method.name.equals(target.getObfName())) && (method.desc.equals(target.getSRGDesc()) || method.desc.equals(target.getObfDesc())))
			{
				return method;
			}
		}
		return null;
	}
	
	public static final class MethodInfo
	{
		final String mcpName;
		final String srgName;
		final String obfName;
		final String srgDesc;
		final String obfDesc;
		
		public MethodInfo(String mcpName, String srgName, String obfName, String srgDesc)
		{
			this.mcpName = mcpName;
			this.srgName = srgName;
			this.obfName = obfName;
			this.srgDesc = srgDesc;
			this.obfDesc = obfuscate(srgDesc);
		}
		
		public MethodInfo(String name, String srgDesc)
		{
			this(name, name, name, srgDesc);
		}
		
		public String getMCPName()
		{
			return mcpName;
		}
		
		public String getSRGName()
		{
			return srgName;
		}
		
		public String getObfName()
		{
			return obfName;
		}
		
		public String getSRGDesc()
		{
			return srgDesc;
		}
		
		public String getObfDesc()
		{
			return obfDesc;
		}
		
		public static String obfuscate(String mcpDesc)
		{
			for(String s : ClassMappings.MAPPINGS.keySet())
			{
				if(mcpDesc.contains(s))
					mcpDesc = mcpDesc.replaceAll(s, ClassMappings.MAPPINGS.get(s));
			}
			return mcpDesc;
		}
	};
}
