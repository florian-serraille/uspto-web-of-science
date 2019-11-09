package labs.com.usptodatabasegenerator.uspto.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext appContext;

    public SpringContextUtil() {}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        appContext = applicationContext;
    }

    public static Object getBean(String beanName) {
        return appContext.getBean(beanName);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName, Class<T> type) {
        return (T) appContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> type) {
        return appContext.getBean(type);
    }

    public static Resource[] getResources(String location) throws IOException {
        return appContext.getResources(location);
    }

}