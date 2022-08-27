package keldkemp.asm.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.Map;


public class CustomMethodAdapter extends AdviceAdapter {

    private final Map<String, Integer> map;

    public CustomMethodAdapter(int api, MethodVisitor methodVisitor, int access, String name, String descriptor, Map<String, Integer> map) {
        super(api, methodVisitor, access, name, descriptor);
        this.map = map;
    }

    @Override
    protected void onMethodEnter() {
        trace("enter method");
    }

    @Override
    protected void onMethodExit(int opcode) {
        trace("exit Method");
    }

    /**
     * Для методов
     */
    private void trace(String action) {
        mv.visitCode();
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("------" + action + " " + this.getName() + "-----");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        mv.visitEnd();
    }

    /**
     * Тут как раз вызывается метод, который регистрирует значение переменной в данный момент времени.
     */
    @Override
    public void visitVarInsn(int opcode, int var) {
        super.visitVarInsn(opcode, var);
        //Если кладем объект в стор, то надо зарегать значение
        if (opcode == ASTORE && this.getName().equals("run")  && var != 0) {
            visitTraceA(var);
        }
    }

    /**
     * На каждую линию добавляю свой метод для отладки
     */
    //TODO: Последняя строчка пропадает. Т.к. сначала новая линия, а потом уже какие то операции
    @Override
    public void visitLineNumber(int line, Label start) {
        super.visitLineNumber(line, start);
        if (this.getName().equals("run")) {
            //addMethod();
        }
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
        if (this.getName().equals("run") && index != 0) {
            registrationVariable(index, name);
        }
    }

    /**
     * Регистрирую имя переменной и индекс
     * @param index индекс переменной
     * @param name имя переменной
     */
    private void registrationVariable(int index, String name) {
        map.put(name, index);
    }

    /**
     * Здесь я просто добавляю вызов метода
     */
    private void addMethod() {
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, "keldkemp/asm/groovy/CustomScript", "debug", "()V", false);
        mv.visitEnd();
    }

    /**
     * Здесь я добавляю метод, который регистрирует значение переменной в данный момент времени
     * @param var индекс переменной в LocalVariableTable
     */
    private void visitTraceA(int var) {
        mv.visitCode();
        //на 0 индексе всегда this
        mv.visitVarInsn(ALOAD, 0);

        //TODO: Костыль немного (там есть предел, надо будет потом учесть)
        //Константа, в данном случае нам надо передать индекс переменной
        mv.visitInsn(3 + var);

        //Кастуем к интеджеру
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);

        //Достаем нашу переменную из стора
        mv.visitVarInsn(ALOAD, var);

        //Вызываю свой метод, который зарегает значение переменной
        mv.visitMethodInsn(INVOKEVIRTUAL, "keldkemp/asm/groovy/CustomScript", "regValue", "(Ljava/lang/Integer;Ljava/lang/Object;)V", false);
        mv.visitEnd();

        addMethod();
    }
}
