package view;

public final class TableFrame {

    private TableFrame() {

    }

    public static String create(String tableSource) {
        return tableHeader()+
                tableSource+
                tableFooter();
    }

    private static String tableHeader() {
        return "<div class=\"container\" id=\"main\">\n" +
                "   <div class=\"col-md-10 col-md-offset-1\">\n" +
                "      <div class=\"panel panel-default\">\n" +
                "          <table class=\"table table-hover\">\n";
    }

    private static String tableFooter() {
        return "</table>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n";
    }
}
