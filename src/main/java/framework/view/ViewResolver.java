package framework.view;

/**
 * ModelView를 받아 알맞은 view를 만들어주는 클래스
 */
public class ViewResolver {
    /**
     * ModelView를 받아 알맞은 view를 만들어주는 메소드
     * @param modelView Front Controller로부터 전달받은 ModelView 객체, 요청에 필요한 정보를 담음
     * @throws Exception Static file을 찾지 못했을 때 발생
     */
    public static void resolve(ModelView modelView) {
        // Redirect & Static Files
        if (modelView.isStatic()) {
            modelView.readStaticFile();
        }

        // TODO: Forward
    }
}
