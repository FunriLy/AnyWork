package com.qg.AnyWork.config;

import com.qg.AnyWork.AnyWorkApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by FunriLy on 2017/7/12.
 * From small beginnings comes great things.
 */
public class ServletInitiallizer extends SpringBootServletInitializer {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        return application.sources(AnyWorkApplication.class);
    }
}

