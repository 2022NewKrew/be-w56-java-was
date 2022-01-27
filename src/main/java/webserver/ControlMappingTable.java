package webserver;

import controller.UserController;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ControlMappingTable {
    private final Map<String, Map<String, Function<Request, String>>> typeMappingTable = new HashMap<>();

    public ControlMappingTable() {
        Map<String, Function<Request, String>> getMappingTable = new HashMap<>();
        getMappingTable.put("/user/create", UserController::signUp);
        typeMappingTable.put("GET", getMappingTable);
    }

    public Function<Request, String> findMethod(String type, String uri) {
        if (!typeMappingTable.containsKey(type)) {
            return DefaultRequestHandler::defaultHandler;
        }

        Map<String, Function<Request, String>> controlMappingTable = typeMappingTable.get(type);
        if (!controlMappingTable.containsKey(uri)) {
            return DefaultRequestHandler::defaultHandler;
        }

        return controlMappingTable.get(uri);
    }
}
