package com.kpi.labs.timetable.beans;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

public class SpringSecurityInitializingServletContextHandler extends ServletContextHandler implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void startContext() throws Exception {
        // Spring filters must be inject before inject application context.
        FilterHolder securityFilterChain = new FilterHolder(new DelegatingFilterProxy("springSecurityFilterChain"));
        securityFilterChain.setAsyncSupported(true);
        addFilter(securityFilterChain, "/*", EnumSet.allOf(DispatcherType.class));

        // set parent application context
        XmlWebApplicationContext wctx = new XmlWebApplicationContext();
        wctx.setParent(applicationContext);
        wctx.setConfigLocation("");
        wctx.refresh();
        getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, wctx);
        super.startContext();
    }
}
