package com.kpi.labs.timetable.beans;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;

public class ChildApplicationContextInitializer<C extends ConfigurableApplicationContext>
        implements ApplicationContextInitializer<C>, ApplicationContextAware {
    private ApplicationContext parent;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.parent = applicationContext;
    }

    @Override
    public void initialize(C applicationContext) {
        if (parent instanceof ConfigurableApplicationContext) {
            final PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
            final MutablePropertySources propertySources =
                    ((ConfigurableApplicationContext) parent).getEnvironment().getPropertySources();
            Map<String, PropertyPlaceholderConfigurer> ppc = parent.getBeansOfType(PropertyPlaceholderConfigurer.class);
            for (PropertyPlaceholderConfigurer pc : ppc.values()) {
                applicationContext.addBeanFactoryPostProcessor(pc);
            }
            configurer.setPropertySources(propertySources);
            configurer.setLocalOverride(true);
            applicationContext.addBeanFactoryPostProcessor(configurer);
        }
        applicationContext.setParent(parent);
    }
}
