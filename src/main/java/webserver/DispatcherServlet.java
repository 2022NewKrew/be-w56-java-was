package webserver;

import controller.IndexController;
import controller.UserController;
import request.HttpRequest;
import response.HttpResponse;

/**
 * HttpRequest 의 method 와 url 을 확인해 올바른 Controller 메서드를 찾아 실행시키고 HttpResponse 를 반환한다.
 */
public class DispatcherServlet {

    private static final UrlMapper URL_MAPPER = UrlMapper.getInstance();

    static {
        UserController.register();
        IndexController.register();
    }

    private DispatcherServlet() {}

    public static HttpResponse dispatch(HttpRequest httpRequest) {
        if (!httpRequest.validate()) {
            return new HttpResponse();
        }

        String method = httpRequest.getMethod();
        String url = httpRequest.getUrl();

        return URL_MAPPER.get(url, method).apply(httpRequest);

//        BufferedReader br = httpRequest.getBufferedReader();
//
//        try {
//            String line = br.readLine();
//            if (line != null) {
//                String url = line.split(" ")[1];
//                url = URLDecoder.decode(url, "UTF-8");
//                LOG.debug("Request > {}", line);
//                LOG.debug("Url     > {}", url);
//
//                if (url.startsWith("/user/create")) {
//                    List<String> arr = Arrays.stream(
//                        url.substring(1).split("&")).map(item -> item.split("=")[1]).collect(
//                        Collectors.toList());
//                    User user = new User(arr.get(0), arr.get(1), arr.get(2), arr.get(3));
//                    LOG.info("User    > {}", user);
//                }
//
//                String[] urlItems = url.split("/");
//                if (urlItems.length > 1 && urlItems[urlItems.length - 1].matches("\\w+\\.\\w+")) {
//                    body = Files.readAllBytes(new File("./webapp" + url).toPath());
//                }
//
//                while (!line.equals("")) {
//                    line = br.readLine();
//                    LOG.debug("        > {}", line);
//                }
//            }
//        } catch (IOException e) {
//            LOG.error(e.getMessage());
//        }
//        return new HttpResponse(body);
    }
}
