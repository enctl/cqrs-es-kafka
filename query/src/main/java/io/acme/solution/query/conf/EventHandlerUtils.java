package io.acme.solution.query.conf;

import io.acme.solution.query.messaging.EventHandler;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Event handler utilities
 */
public class EventHandlerUtils {

    private EventHandlerUtils() {

    }

    public static Map<String, EventHandler> buildEventHandlersRegistry(final String basePackage,
                                                                       final ApplicationContext context) {

        final Map<String, EventHandler> registry = new HashMap<>();
        final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        final AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory();
        scanner.addIncludeFilter(new AssignableTypeFilter(EventHandler.class));

        EventHandler currentHandler = null;

        for (BeanDefinition bean : scanner.findCandidateComponents(basePackage)) {
            currentHandler = (EventHandler) beanFactory.createBean(ClassUtils.resolveClassName(bean.getBeanClassName(), context.getClassLoader()),
                    AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
            registry.put(currentHandler.getInterest(), currentHandler);
        }

        return registry;
    }
}
