package com.jim.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liqing on 2016/5/20.
 */
public interface BaseService {
    static final Logger logger = LoggerFactory.getLogger(BaseService.class);
	void createDestination();
}
