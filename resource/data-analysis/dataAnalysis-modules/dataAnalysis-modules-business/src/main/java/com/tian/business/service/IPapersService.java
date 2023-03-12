package com.tian.business.service;

import java.util.List;
import com.tian.business.domain.Papers;
import com.tian.system.api.domain.SysUser;

/**
 * 论文详细信息Service接口
 * 
 * @author tian
 * @date 2023-02-12
 */
public interface IPapersService 
{
    /**
     * 查询论文详细信息
     * 
     * @param id 论文详细信息主键
     * @return 论文详细信息
     */
    public Papers selectPapersById(Long id);

    /**
     * 查询论文详细信息列表
     * 
     * @param papers 论文详细信息
     * @return 论文详细信息集合
     */
    public List<Papers> selectPapersList(Papers papers);

    /**
     * 新增论文详细信息
     * 
     * @param papers 论文详细信息
     * @return 结果
     */
    public int insertPapers(Papers papers);

    /**
     * 修改论文详细信息
     * 
     * @param papers 论文详细信息
     * @return 结果
     */
    public int updatePapers(Papers papers);

    /**
     * 批量删除论文详细信息
     * 
     * @param ids 需要删除的论文详细信息主键集合
     * @return 结果
     */
    public int deletePapersByIds(Long[] ids);

    /**
     * 删除论文详细信息信息
     * 
     * @param id 论文详细信息主键
     * @return 结果
     */
    public int deletePapersById(Long id);

    /**
     * 导入论文数据
     *
     * @param papersList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importUser(List<Papers> papersList, Boolean isUpdateSupport, String operName);
}
