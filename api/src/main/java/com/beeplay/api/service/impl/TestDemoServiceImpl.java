package com.beeplay.api.service.impl;


import com.beeplay.api.dao.TestDemoDao;
import com.beeplay.api.mapper.TestDemoMapper;
import com.beeplay.api.po.TestDemo;
import com.beeplay.api.service.TestDemoService;
import com.beeplay.core.db.ds.DataSource;
import com.beeplay.core.db.ds.DataSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("testDemoService")
public class TestDemoServiceImpl implements TestDemoService {
    @Autowired
    private TestDemoMapper testDemoMapper;
    @Autowired
    private TestDemoDao testDemoDao;

    @Transactional(readOnly = true)
    @DataSource(name= DataSourceContext.DATA_SOURCE_READ)
    public  List<TestDemo> listTestDemo(){
        return testDemoMapper.listTestDemo();
    }

    @Transactional(rollbackFor = Exception.class)
    @DataSource(name= DataSourceContext.DATA_SOURCE_WRITE)
    public  void saveTestDemo(TestDemo logPo){
        testDemoMapper.saveTestDemo(logPo);
    }
    @Transactional(rollbackFor = Exception.class)
    @DataSource(name= DataSourceContext.DATA_SOURCE_WRITE)
    public void saveTestDemoList(List<TestDemo> list){
        testDemoDao.saveTestDemoList(list);
    }
}
