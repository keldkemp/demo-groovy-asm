package keldkemp.asm.services;

import java.util.HashMap;
import java.util.Map;

public class Debuger {

    private Map<String, Integer> map = new HashMap<>();
    private final Map<Integer, Object> mapValue = new HashMap<>();

    int iter = 0;

    public void debug() {
        System.out.println("Итерация: " + iter);
        iter++;
        map.forEach((k, v) -> {
            if (mapValue.containsKey(v)) {
                Object value = mapValue.get(v);
                System.out.println(k + " = " + value);
            }
        });
    }

    public void regValue(Integer index, Object value) {
        mapValue.put(index, value);
    }

    public void regMap(Map<String, Integer> map) {
        this.map = map;
    }
}
