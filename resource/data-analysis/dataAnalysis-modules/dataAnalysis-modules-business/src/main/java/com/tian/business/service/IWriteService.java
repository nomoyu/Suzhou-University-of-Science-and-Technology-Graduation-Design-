package com.tian.business.service;

import com.tian.business.domain.Write;

import java.util.List;

public interface IWriteService {
    public int add(Write write);

    public float queryVersion();

    public List<Write> queryList(Write write);
}
