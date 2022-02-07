package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.configures.MethodParameter;
import webserver.context.HttpSession;
import webserver.context.Model;
import webserver.context.ServletRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParameterHandler implements MethodParameter {
    private static final Logger log = LoggerFactory.getLogger(ParameterHandler.class);

    private final Parameter parameter;

    private ParameterHandler(Parameter parameter) {
        this.parameter = parameter;
    }

    public static ParameterHandler of(Parameter parameter) {
        return new ParameterHandler(parameter);
    }

    private Object newInstance(Constructor constructor, List<Object> args) {
        try {
            return constructor.newInstance(args.toArray());
        } catch (InstantiationException e) {
            log.error(e.getMessage());
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
        } catch (InvocationTargetException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public Object parseParameter(ServletRequest servletRequest) {
        if (this.parameter.getType() == HttpSession.class) return servletRequest.getHttpSession();
        if (this.parameter.getType() == Model.class) return servletRequest.getModel();

        Object obj = ArgumentResolverHandler.resolveParameter(this, servletRequest);
        if (obj == null) obj = parseValueParameter(this.parameter, servletRequest);
        if (obj == null) obj = parseReferenceParameter(this.parameter, servletRequest);
        return obj;
    }

    public Object parseValueParameter(Parameter parameter, ServletRequest servletRequest) {
        Class<?> targetType = parameter.getType();
        String parameterName = parameter.getName();
        return castStringParameter(targetType, servletRequest.getParameter(parameterName));
    }

    public Object parseReferenceParameter(Parameter parameter, ServletRequest servletRequest) {
        Class<?> clazz = parameter.getType();
        Constructor[] constructors = clazz.getDeclaredConstructors();
        if (constructors.length == 0) return null;
        Constructor constructor = constructors[0];
        List<Object> args = new ArrayList<>();
        for (Parameter constructorParameter : constructor.getParameters()) {
            String constructorParameterName = constructorParameter.getName();
            Class<?> targetType = constructorParameter.getType();
            args.add(castStringParameter(targetType, servletRequest.getParameter(constructorParameterName)));
        }
        return newInstance(constructor, args);
    }

    private Object castStringParameter(Class<?> targetType, String value){
        if (Arrays.stream(new String[]{"byte", "Byte", "java.lang.Byte"}).anyMatch(s -> s.equals(targetType.getTypeName()))) return Byte.parseByte(value);
        if (Arrays.stream(new String[]{"short", "Short", "java.lang.Short"}).anyMatch(s -> s.equals(targetType.getTypeName()))) return Short.parseShort(value);
        if (Arrays.stream(new String[]{"long", "Long", "java.lang.Long"}).anyMatch(s -> s.equals(targetType.getTypeName()))) return Long.parseLong(value);
        if (Arrays.stream(new String[]{"int", "Integer", "java.lang.Integer"}).anyMatch(s -> s.equals(targetType.getTypeName()))) return Integer.parseInt(value);
        if (Arrays.stream(new String[]{"float", "Float", "java.lang.Float"}).anyMatch(s -> s.equals(targetType.getTypeName()))) return Float.parseFloat(value);
        if (Arrays.stream(new String[]{"double", "Double", "java.lang.Double"}).anyMatch(s -> s.equals(targetType.getTypeName()))) return Double.parseDouble(value);
        if (Arrays.stream(new String[]{"String", "java.lang.String"}).anyMatch(s -> s.equals(targetType.getTypeName()))) return value;
        if (Arrays.stream(new String[]{"char[]", "Character[]", "java.lang.Character[]"}).anyMatch(s -> s.equals(targetType.getTypeName()))) return value.toCharArray();
        if (Arrays.stream(new String[]{"boolean", "Boolean", "java.lang.Boolean"}).anyMatch(s -> s.equals(targetType.getTypeName()))) return Boolean.getBoolean(value);
        return null;
    }

    @Override
    public boolean hasParameterAnnotation(Class<?> parameterClazz) {
        return this.parameter.isAnnotationPresent((Class<Annotation>) parameterClazz);
    }

    @Override
    public Class<?> getParameterType() {
        return this.parameter.getType();
    }
}
