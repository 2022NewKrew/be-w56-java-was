package templateEngine;

import java.util.ArrayList;
import java.util.List;

public class CyEngineNode {

    private final CyEngineNode parent;
    private final List<CyEngineNode> children = new ArrayList<>();

    private final String index;

    private final String contents;

    private final NodeType nodeType;

    public CyEngineNode(NodeType nodeType, CyEngineNode parent, String index, String contents) {
        this.parent = parent;

        this.nodeType = nodeType;

        this.index = index;
        this.contents = contents;
    }

    public void addChild(CyEngineNode cyEngineNode) {
        children.add(cyEngineNode);
    }

    public CyEngineNode getParent() {
        return parent;
    }

    public List<CyEngineNode> getChildren() {
        return children;
    }

    public String getIndex() {
        return index;
    }

    public boolean notEqualIndex(String index) {
        if (this.index == null) {
            return true;
        }

        return !(this.index.equals(index));
    }

    public String getContents() {
        return contents;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

}
