package com.beeplay.api.dao.impl;

import com.beeplay.api.dao.TestDemoDao;
import org.springframework.stereotype.Service;
import com.beeplay.api.mapper.TestDemoMapper;
import com.beeplay.api.po.TestDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("testDemoDao")
public class TestDemoDaoImpl implements TestDemoDao {
    @Autowired
    private TestDemoMapper testDemoMapper;

    public void saveTestDemoList(List<TestDemo> list){
        for(TestDemo po:list){
            testDemoMapper.saveTestDemo(po);
        }
    }
}
