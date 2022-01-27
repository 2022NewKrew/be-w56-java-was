package bin.jayden.util;

import bin.jayden.http.MyHttpRequest;
import bin.jayden.http.MyHttpSession;

import java.lang.reflect.Parameter;
import java.security.InvalidParameterException;
import java.util.Map;

public class ParameterProcessor {
    public static Object[] getParameterObjects(Parameter[] methodParameters, MyHttpRequest request, MyHttpSession session) {
        Map<String, String> requestParameters = request.getParams();
        Object[] parameters = new Object[methodParameters.length];
        for (int i = 0; i < methodParameters.length; i++) {
            Parameter parameter = methodParameters[i];
            String parameterName = parameter.getName();
            String requestParameterValue = requestParameters.get(parameterName);
            Class<?> parameterType = parameter.getType();
            switch (parameterType.getName()) {
                case "int":
                case "java.lang.Integer":
                    parameters[i] = Integer.parseInt(requestParameterValue);
                    break;
                case "long":
                case "java.lang.Long":
                    parameters[i] = Long.parseLong(requestParameterValue);
                    break;
                case "short":
                case "java.lang.Short":
                    parameters[i] = Short.parseShort(requestParameterValue);
                    break;
                case "byte":
                case "java.lang.Byte":
                    parameters[i] = Byte.parseByte(requestParameterValue);
                    break;
                case "boolean":
                case "java.lang.Boolean":
                    parameters[i] = Boolean.parseBoolean(requestParameterValue);
                    break;
                case "float":
                case "java.lang.Float":
                    parameters[i] = Float.parseFloat(requestParameterValue);
                    break;
                case "double":
                case "java.lang.Double":
                    parameters[i] = Double.parseDouble(requestParameterValue);
                    break;
                case "char":
                case "java.lang.Character":
                    parameters[i] = requestParameterValue.charAt(0);
                    break;
                case "java.lang.String":
                    parameters[i] = requestParameterValue;
                    break;
                case "bin.jayden.http.MyHttpSession":
                    parameters[i] = session;
                    break;
                case "bin.jayden.http.MyHttpRequest":
                    parameters[i] = request;
                    break;
                default: //Todo DTO같은건 추후에 처리 가능하도록
                    throw new InvalidParameterException("잘못된 파라메터!");
            }
        }
        return parameters;
    }
}
