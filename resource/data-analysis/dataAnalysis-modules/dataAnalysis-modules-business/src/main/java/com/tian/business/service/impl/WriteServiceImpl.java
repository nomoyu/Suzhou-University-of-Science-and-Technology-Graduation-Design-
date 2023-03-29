package com.tian.business.service.impl;

import com.tian.business.domain.Write;
import com.tian.business.mapper.WriteMapper;
import com.tian.business.service.IWriteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tian
 * Date: 2023/3/29
 * Time: 8:03
 */
@Service
public class WriteServiceImpl implements IWriteService {

    @Resource
    private WriteMapper writeMapper;

    /**
     * 添加记录每次版本加一
     * @param write
     * @return
     */
    @Override
    public int add(Write write) {
        write.setVersion((float) (writeMapper.queryVersion()+0.1));
        return writeMapper.add(write);
    }

    @Override
    public float queryVersion() {
        return writeMapper.queryVersion();
    }

    @Override
    public List<Write> queryList(Write write) {
        return writeMapper.queryList(write);
    }
}
