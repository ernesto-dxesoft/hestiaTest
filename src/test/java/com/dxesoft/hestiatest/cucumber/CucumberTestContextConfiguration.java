package com.dxesoft.hestiatest.cucumber;

import com.dxesoft.hestiatest.HestiaTestApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = HestiaTestApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
