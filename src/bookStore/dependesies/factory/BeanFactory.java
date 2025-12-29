package bookStore.dependesies.factory;

import bookStore.dependesies.annotation.Inject;
import bookStore.dependesies.annotation.Qualifier;
import bookStore.dependesies.config.Configuration;
import bookStore.dependesies.config.JavaConfiguration;
import bookStore.dependesies.configurator.BeanConfigurator;
import bookStore.dependesies.configurator.JavaBeanConfigurator;
import bookStore.dependesies.context.ApplicationContext;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;

public class BeanFactory {

    private final Configuration configuration;
    @Getter
    private final BeanConfigurator beanConfigurator;
    private ApplicationContext applicationContext;

    public BeanFactory(ApplicationContext applicationContext) {
        this.configuration = new JavaConfiguration();
        this.beanConfigurator = new JavaBeanConfigurator(configuration.getPackageToScan(), configuration.getInterfaceToImplementation());
        this.applicationContext = applicationContext;
    }


    @SneakyThrows
    public <T> T getBean(Class<T> clazz) {
        Class<? extends T> implementationClass;

        if (clazz.isInterface()) {
            implementationClass = beanConfigurator.getImplementationClass(clazz);
        } else {
            implementationClass = clazz;
        }

        if (applicationContext.getBeanMap().containsKey(implementationClass)) {
            return (T) applicationContext.getBeanMap().get(implementationClass);
        }

        T bean = implementationClass.getDeclaredConstructor().newInstance();

        applicationContext.getBeanMap().put(implementationClass, bean);

        for (Field field : Arrays.stream(implementationClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Inject.class)).toList()) {
            field.setAccessible(true);

            Qualifier qualifier = field.getAnnotation(Qualifier.class);
            Class<?> dependencyType = field.getType();
            Object dependency;

            if (qualifier != null) {
                Class<?> depImpl = beanConfigurator.getImplementationClass(dependencyType, qualifier.value());
                dependency = applicationContext.getBean(depImpl);
            } else {
                if (dependencyType.isInterface()) {
                    Class<?> depImpl = beanConfigurator.getImplementationClass(dependencyType);
                    dependency = applicationContext.getBean(depImpl);
                } else {
                    dependency = applicationContext.getBean(dependencyType);
                }
            }

            field.set(bean, dependency);
        }

        return bean;
    }



}
