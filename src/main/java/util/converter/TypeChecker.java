package util.converter;

import java.util.ArrayList;
import java.util.List;

public class TypeChecker {
    public static boolean equals(Class<?> type1, Class<?> type2) {
        return getAncestor(type1).equals(type2);
    }

    private static Class<?> getAncestor(Class<?> type){
        List<Class<?>> hierarchy = new ArrayList<>(){{ add(type); }};
        addImplementedInterfaces(type, hierarchy);

        Class<?> parent = type;
        Class<?> grandParent = parent.getSuperclass();
        while (grandParent != Object.class && grandParent != null){
            parent = grandParent;
            addImplementedInterfaces(parent, hierarchy);
            grandParent = parent.getSuperclass();
        }

        return hierarchy.get(hierarchy.size()-1);
    }

    private static void addImplementedInterfaces(Class<?> type, List<Class<?>> container){
        container.addAll(List.of(type.getInterfaces()));
    }
}
