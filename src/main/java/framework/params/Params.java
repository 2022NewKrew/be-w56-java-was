package framework.params;

import java.util.Map;

/**
 * Controller의 Parameter instance 및 class -> instance mapper를 가지고 있는 클래스
 * HandlerMapping 클래스에서 Method를 invoke할 때 적절한 매개변수들을 매핑해주는 역할 수행
 * 요청마다 새로운 model과 session 객체들이 생성되어야하기 때문에 Beans에서 static으로 생성하는 것이 아니라 FrontController에서 동적으로 객체 생성
 */
public class Params {
    public Map<Class<?>, Object> paramMapper;
    public Model model;
    public Session session;

    public Params() {
        model = new Model();
        session = new Session();
        paramMapper = Map.of(Model.class, model, Session.class, session);
    }
}
