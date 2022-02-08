package templateEngine;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CyEngine {
    private final Map<String, CyEngineNode> cyEngineNodeContext = new HashMap<>();
    private final CyEngineParser cyEngineParser = new CyEngineParser();
    private final CyEngineCompiler cyEngineCompiler = new CyEngineCompiler();

    public byte[] compile(String path, Map<String, Object> model) {
        if (!cyEngineNodeContext.containsKey(path)) {
            final CyEngineNode cyEngineNode = cyEngineParser.parse(Path.of(path));

            cyEngineNodeContext.put(path, cyEngineNode);
        }

        final CyEngineNode cyEngineNode = cyEngineNodeContext.get(path);

        return cyEngineCompiler.compile(cyEngineNode, model);
    }
}
