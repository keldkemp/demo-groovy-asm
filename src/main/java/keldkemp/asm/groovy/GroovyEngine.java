package keldkemp.asm.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import keldkemp.asm.services.Debuger;
import org.codehaus.groovy.control.CompilerConfiguration;

public class GroovyEngine {

    private final CompilerConfiguration config;
    private final Debuger debuger = new Debuger();

    public GroovyEngine() {
        this.config = new CompilerConfiguration();
        this.config.setScriptBaseClass(CustomScript.class.getName());
        this.config.setBytecodePostprocessor(new CustomBytecodeProcessor(debuger));
        this.config.setDebug(true);
    }

    public void start() {
        String code = "def te = 5\n" +
                "te += 55\n" +
                "def et = 10\n" +
                "et += 25\n" +
                "def result = te + et\n";

        Binding binding = new Binding();
        binding.setProperty("debuger", debuger);

        GroovyShell shell = new GroovyShell(binding, config);
        Script script = shell.parse(code);

        Object result = script.run();
    }
}
