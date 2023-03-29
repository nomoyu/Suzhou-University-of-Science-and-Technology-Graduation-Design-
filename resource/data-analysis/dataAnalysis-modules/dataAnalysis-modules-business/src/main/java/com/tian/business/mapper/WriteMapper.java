package com.tian.business.mapper;

import com.tian.business.domain.Write;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WriteMapper {
    public int add(Write write);

    public float queryVersion();

    public List<Write> queryList(Write write);
}
