package application;

import application.config.WebConfig;
import was.ApplicationBuilder;

public class Main {
    public static void main(String[] args) {
        ApplicationBuilder.builder()
                .setSeverConfig(new WebConfig())
                .addPackageNameForBeanScan("was")
                .build()
                .run();
    }
}
