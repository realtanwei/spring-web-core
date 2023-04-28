package project.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础控制器
 *
 * @author tanwei
 * @date 2023-02-06 17:02
 **/
public abstract class BaseController<S> {

    S service;

    @Autowired
    public void setService(S service) {
        this.service = service;
    }
}
