package com.sjz.lcsjz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 * </p>
 *
 * @author enhe
 * @since 2025-07-29
 */
@RestController
@RequestMapping("/sjz-lc")
public class TestController {

    @GetMapping
    @ResponseBody
    public String verify() {
        return "success";
    }

}
