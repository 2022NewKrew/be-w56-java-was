package domain;

public class Name {
    private final String name;

    public Name(String name) {
        valid(name);
        this.name = name;
    }

    private void valid(String name) {
        if (name == null || name.equals("")) {
            throw new NullPointerException("이름을 입력하세요.");
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
