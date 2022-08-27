package keldkemp.asm.groovy;

import keldkemp.asm.asm.CustomClassAdapter;
import keldkemp.asm.services.Debuger;
import org.codehaus.groovy.control.BytecodeProcessor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.util.HashMap;

public class CustomBytecodeProcessor implements BytecodeProcessor {

    private final Debuger debuger;

    public CustomBytecodeProcessor(Debuger debuger) {
        this.debuger = debuger;
    }

    @Override
    public byte[] processBytecode(String name, byte[] bytes) {
        ClassReader reader = new ClassReader(bytes);
        ClassWriter writer = new ClassWriter(reader,
                ClassWriter.COMPUTE_MAXS |
                        ClassWriter.COMPUTE_FRAMES);

        HashMap<String, Integer> map = new HashMap<>();
        CustomClassAdapter ca = new CustomClassAdapter(writer, map);
        reader.accept(ca, ClassReader.EXPAND_FRAMES);

        debuger.regMap(map);
        return writer.toByteArray();
    }
}
