package webserver.util;

public class ClassAndInstance {
    private Class aClass;
    private Object instance;

    public ClassAndInstance(Class aClass, Object instance) {
        this.aClass = aClass;
        this.instance = instance;
    }

    public Class getaClass() {
        return aClass;
    }

    public Object getInstance() {
        return instance;
    }
}
