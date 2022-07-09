package com.example.demo;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Theme(value = "myapp")
@PWA(name = "My App", shortName = "My App", offlineResources = {})
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class Demo4Application extends SpringBootServletInitializer implements AppShellConfigurator {

public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
        SpringApplication.run(Demo4Application.class, args);
        }
}
