package mapper;

public interface GenericMapper<L, R> {

    R toRightObject(L l);

    L toLeftObject(R r);

}
