package de.htwsaar;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;

import java.util.Arrays;

/**
 * Abstrakte Oberklasse der Applikation welche die JavaFX Application Klasse um die Initialisierung
 * von SpringBoot erweitert.
 *
 * @author cedosw
 */
@Lazy
@SpringBootApplication
public abstract class AbstractJavaFxApp extends Application {

    /** Die Kommandozeilenparameter des eigentlichen Programmaufrufs. */
    private static String[] savedArgs;

    /** Der Spring ApplicationContext. */
    private ConfigurableApplicationContext applicationContext;

    public static Application instance;

    /**
     * Startet SpringBoot und Autowired die JavaFX-Application im Spring Kontext.
     */
    @Override
    public void init() throws Exception {
        AbstractJavaFxApp.instance = this;
        SpringApplicationBuilder builder = new SpringApplicationBuilder(getClass());
        applicationContext = builder.headless(false).run(savedArgs);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);


    }

    @Override
    public void stop() throws Exception {
        super.stop();
        SpringApplication.exit(applicationContext);
        System.exit(0);
    }

    /**
     * Startet die JavaFX Applikation.
     *
     * @param args Die Kommandozeilenparameter.
     */
    protected static void launchApp(Class<? extends AbstractJavaFxApp> appClass, String[] args) {
        AbstractJavaFxApp.savedArgs = args;
        Arrays.asList(args).forEach((s)->System.out.println(s));
        Application.launch(appClass, args);
    }
}