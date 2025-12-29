package bookStore.dependesies.context;

import bookStore.dependesies.factory.BeanFactory;
import bookStore.dependesies.postprocessor.BeanPostProcessor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {

    @Setter
    private BeanFactory beanFactory;

    @Getter
    private Map<Class, Object> beanMap = new ConcurrentHashMap<>();

    public  ApplicationContext() {
        //is.beanFactory = beanFactory;
    }

    public <T> T getBean(Class<T> clazz) {

        if (beanMap.containsKey(clazz)) {
            return (T) beanMap.get(clazz);
        }

        T bean = beanFactory.getBean(clazz);
        callPostProcessors(bean);
        beanMap.put(clazz, bean);

        return bean;
    }

    @SneakyThrows
    private void callPostProcessors(Object object) {
        for (Class processor : beanFactory.getBeanConfigurator().getScanner()
                .getSubTypesOf(BeanPostProcessor.class)) {
            BeanPostProcessor postProcessor = (BeanPostProcessor) processor.getDeclaredConstructor().newInstance();
            postProcessor.process(object);
        }
    }
}
