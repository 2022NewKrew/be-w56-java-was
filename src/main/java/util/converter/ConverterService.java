package util.converter;

import lombok.RequiredArgsConstructor;
import webserver.domain.entity.User;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConverterService {
    private static final Map<TypePair, Converter<?, ?>> typePairToConverter = new HashMap<>(){{
        put(new TypePair(Map.class, User.class), new BodyParamsToUserConverter());
    }};

    public static <T, R> R convert(T from, Class<R> toType) {
        try {
            Converter<T, R> converter = (Converter<T, R>) typePairToConverter.get(new TypePair(from.getClass(), toType));
            return converter.convert(from);
        }catch (Exception e){
            throw new IllegalStateException("cannot convert");
        }
    }

    @RequiredArgsConstructor
    private static class TypePair{
        private final Class<?> fromType;
        private final Class<?> toType;


        @Override
        public int hashCode() {
            return 31 * getAncestor(fromType).hashCode()
                    + getAncestor(toType).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof TypePair)){
                return false;
            }

            TypePair other = (TypePair) obj;
            return getAncestor(fromType).equals(getAncestor(other.fromType))
                    && getAncestor(toType).equals(getAncestor(other.toType));
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
}
