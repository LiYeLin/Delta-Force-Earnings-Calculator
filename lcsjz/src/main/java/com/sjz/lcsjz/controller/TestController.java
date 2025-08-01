package com.sjz.lcsjz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 测试
 * </p>
 *
 * @author enhe
 * @since 2025-07-29
 */
@Controller
@RequestMapping("/sjz-lc")
public class TestController {
    @GetMapping
    public String verify() {
        return "success";
    }

}
