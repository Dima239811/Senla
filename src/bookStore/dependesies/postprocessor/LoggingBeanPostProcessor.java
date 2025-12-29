package bookStore.dependesies.postprocessor;

public class LoggingBeanPostProcessor implements BeanPostProcessor {
    @Override
    public void process(Object bean) {
        System.out.println(String.format("Log: bean has created: %s", bean.getClass()));
    }
}
