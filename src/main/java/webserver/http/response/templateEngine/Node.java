package webserver.http.response.templateEngine;

public class Node {
    private NodeType type;
    private String data;

    public Node() {
        type = NodeType.PLAIN;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return type + " : " + data;
    }
}
