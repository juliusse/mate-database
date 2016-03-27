package info.seltenheim.mate;

import info.seltenheim.mate.configuration.SpringConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import play.Application;
import play.Configuration;
import play.GlobalSettings;
import play.Logger;
import play.Play;
import play.mvc.Action;
import play.mvc.Http;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application application) {
        initializeSpring();
        ensureDatabaseIsPresent();
        migrateDatabase();
        
        ensureImagesArePresent();

        super.onStart(application);
    }

    private void initializeSpring() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        SpringConfiguration.initializeContext(context);
    }
    
    private void ensureDatabaseIsPresent() {        
        final Configuration config = Play.application().configuration();
        
        ensureFileIsPresent(config.getString("info.seltenheim.mate.sqlite.location"), "mate.sqlite");
    }
    
    private void ensureImagesArePresent() {
        final Configuration config = Play.application().configuration();
        final String filesDirectory = config.getString("info.seltenheim.mate.files");
        
        ensureFileIsPresent(filesDirectory + "/image1", "img/bottle.png");
        ensureFileIsPresent(filesDirectory + "/image2", "img/box.png");
        ensureFileIsPresent(filesDirectory + "/image3", "img/truck.png");
    }
    
    private void ensureFileIsPresent(String path, String resource) {
        final File srcFile = new File(path);
        
        if (!srcFile.isFile()) {
            final InputStream fileAsStream = Play.application().resourceAsStream(resource);
            
            srcFile.getParentFile().mkdirs();
            try {
                FileUtils.copyInputStreamToFile(fileAsStream, srcFile);
            } catch (IOException e) {
                Logger.error("Cannot create file", e);
            }
        }
    }
    
    private void migrateDatabase() {
        final Configuration config = Play.application().configuration();
        final File databaseFile = new File(config.getString("info.seltenheim.mate.sqlite.location"));
        DatabaseMigrator.migrateDatabase(databaseFile.getAbsolutePath());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Action onRequest(final Http.Request request, Method method) {
        logRequest(request, method);

        return super.onRequest(request, method);
    }

    @Override
    public <A> A getControllerInstance(Class<A> clazz) throws Exception {
        A bean = SpringConfiguration.getBean(clazz);
        if (bean == null) {
            bean = super.getControllerInstance(clazz);
        }
        return bean;
    }

    private void logRequest(Http.Request request, Method method) {
        if (Logger.isDebugEnabled() && !request.path().startsWith("/assets")) {
            StringBuilder sb = new StringBuilder(request.toString());
            sb.append(" ").append(method.getDeclaringClass().getCanonicalName());
            sb.append(".").append(method.getName()).append("(");
            Class<?>[] params = method.getParameterTypes();
            for (int j = 0; j < params.length; j++) {
                sb.append(params[j].getCanonicalName().replace("java.lang.", ""));
                if (j < (params.length - 1))
                    sb.append(',');
            }
            sb.append(")");
            Logger.debug(sb.toString());
        }
    }
}
