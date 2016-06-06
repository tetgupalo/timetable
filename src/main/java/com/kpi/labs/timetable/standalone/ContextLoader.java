package com.kpi.labs.timetable.standalone;


import static org.springframework.core.io.ResourceLoader.CLASSPATH_URL_PREFIX;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public abstract class ContextLoader {
    private static final Logger LOG = LoggerFactory.getLogger(ContextLoader.class);

    public static void loadContext(String path, String[] args) {
        try {
            Properties properties = getProperties(path, args);
            LOG.info("INIT PHASE START");
            Set<String> activeProfiles = getProfiles(properties);
            GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
            if (!activeProfiles.isEmpty()) {
                ctx.getEnvironment().setActiveProfiles(activeProfiles.toArray(new String[activeProfiles.size()]));
            } else {
                LOG.warn("No profiles are activated - using default");
            }
            ctx.addBeanFactoryPostProcessor(getPropertyConfigurer(properties));
            ctx.getEnvironment().getPropertySources().addLast(new PropertiesPropertySource("rit-properties", properties));
            ctx.load(new ClassPathResource("META-INF/spring/bootstrapper.xml"));
            ctx.refresh();
            if (ctx.isActive() && ctx.isRunning()) {
                LOG.info("Spring context has successfully started {}", new Date(ctx.getStartupDate()));
            } else {
                LOG.error("Context is not active or running");
            }
        } catch (Exception e) {
            LOG.error("Context creation failure", e);
        } finally {
            LOG.info("INIT PHASE END");
        }

    }

    private static Properties getProperties(String path, String[] args) throws IOException {
        if (path == null || args.length == 0) {
            throw new RuntimeException("Patch or args can not be empty");
        }
        Properties prop = new Properties();
        if (path.startsWith(CLASSPATH_URL_PREFIX)) {
            prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource(args[0]));
            return prop;
        } else {
            String logger = path + "log4j2.json";
            Configurator.initialize(null, logger);
            FileInputStream fileInputStream = null;
            try {
                for (String s : args) {
                    fileInputStream = new FileInputStream(new File(path + s));
                    prop.load(fileInputStream);
                    fileInputStream.close();
                    prop.putAll(prop);
                }
            } finally {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }
        }

        return prop;

    }

    private static PropertyPlaceholderConfigurer getPropertyConfigurer(Properties properties) {
        PropertyPlaceholderConfigurer placeholderConfigurer = new PropertyPlaceholderConfigurer();
        placeholderConfigurer.setProperties(properties);
        return placeholderConfigurer;
    }

    private static Set<String> getProfiles(Properties properties) {
        String prop = properties.getProperty("profile");
        Set<String> activeProfiles = new HashSet<>();
        for (String profile : prop.split(",")) {
            profile = profile.trim().toUpperCase();
            if (profile.isEmpty()) {
                continue;
            }
            String root = profile.split("\\.")[0];
            if (!activeProfiles.contains(profile)) {
                LOG.info("Activating profile {}:[{}]", root, profile);
                activeProfiles.add(profile);
                if (!activeProfiles.contains(root)) {
                    LOG.debug("Activating root profile [{}]", root);
                    activeProfiles.add(root);
                }
            }
        }
        return activeProfiles;
    }
}
