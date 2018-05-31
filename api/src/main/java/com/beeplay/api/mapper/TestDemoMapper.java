package com.beeplay.api.mapper;

import com.beeplay.api.po.TestDemo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TestDemoMapper {
    @Select("SELECT * FROM test_demo")
    List<TestDemo> listTestDemo();
    @Insert("INSERT INTO test_demo (id,name) VALUES (#{id}, #{name})")
    void saveTestDemo(TestDemo logPo);
}
