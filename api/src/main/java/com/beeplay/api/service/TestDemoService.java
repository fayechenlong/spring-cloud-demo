package com.beeplay.api.service;


import com.beeplay.api.po.TestDemo;

import java.util.List;

public interface TestDemoService {
    List<TestDemo> listTestDemo();

    void saveTestDemo(TestDemo logPo);

    void saveTestDemoList(List<TestDemo> list);
}
