package webserver.http.response;

public abstract class View implements ResponseBody<String> {

    public static View staticFile(String path) {
        return new StaticView(path);
    }

    public static View body(String body) {
        return new BodyView(body);
    }


    static class StaticView extends View {
        private String filePath;

        StaticView(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public String getResponseValue() {
            return filePath;
        }
    }

    static class BodyView extends View {
        private String body;

        BodyView(String body) {
            this.body = body;
        }

        @Override
        public String getResponseValue() {
            return body;
        }
    }
}
