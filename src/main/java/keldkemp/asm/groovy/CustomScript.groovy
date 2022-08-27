package keldkemp.asm.groovy

import keldkemp.asm.services.Debuger

abstract class CustomScript extends Script {

    private Debuger debuger

    Debuger getDebug() {
        if (debuger != null) {
            return debuger
        }
        return binding.getProperty("debuger") as Debuger
    }

    void debug() {
        getDebug().debug()
    }

    void regValue(Integer index, Object value) {
        getDebug().regValue(index, value);
    }
}
