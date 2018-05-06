package virtuoel.permafrost.asm;

import java.util.HashMap;
import java.util.function.Consumer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer
{
	private static final HashMap<String, Consumer<ClassNode>> TRANSFORMATIONS = new HashMap<String, Consumer<ClassNode>>();
	
	static
	{
		TRANSFORMATIONS.put("net.minecraft.world.World", ClassTransformer::transformWorld);
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(TRANSFORMATIONS.containsKey(transformedName))
		{
			LoadingPlugin.LOGGER.info("Transforming Class [" + transformedName + "]");
			try
			{
				ClassNode classNode = new ClassNode();
				ClassReader classReader = new ClassReader(basicClass);
				classReader.accept(classNode, 0);
				
				TRANSFORMATIONS.get(transformedName).accept(classNode);
				
				ClassWriter classWriter = new SafeClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
				classNode.accept(classWriter);
				return classWriter.toByteArray();
			}
			catch(Exception e)
			{
				LoadingPlugin.LOGGER.catching(e);
			}
		}
		return basicClass;
	}
	
	private static void transformWorld(ClassNode classNode)
	{
		MethodNode method = findMethod(classNode, "canBlockFreezeBody", "(Lnet/minecraft/util/math/BlockPos;Z)Z");
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
		
		method = findMethod(classNode, "canSnowAtBody", "(Lnet/minecraft/util/math/BlockPos;Z)Z");
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
	}
	
	private static MethodNode findMethod(ClassNode classNode, String targetName, String targetDesc)
	{
		for(MethodNode method : classNode.methods)
		{
			if(method.name.equals(targetName) && method.desc.equals(targetDesc))
			{
				return method;
			}
		}
		return null;
	}
}
