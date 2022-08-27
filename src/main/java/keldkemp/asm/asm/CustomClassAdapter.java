package keldkemp.asm.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.Map;

import static org.objectweb.asm.Opcodes.ASM9;

public class CustomClassAdapter extends ClassVisitor {

    private final Map<String, Integer> map;

    public CustomClassAdapter(ClassVisitor classVisitor, Map<String, Integer> map) {
        super(ASM9, classVisitor);
        this.map = map;

    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new CustomMethodAdapter(this.api, methodVisitor, access, name, descriptor, map);
    }


}
