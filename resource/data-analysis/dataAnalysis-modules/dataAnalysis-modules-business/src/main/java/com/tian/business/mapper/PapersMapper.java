package com.tian.business.mapper;

import java.util.List;
import com.tian.business.domain.Papers;

/**
 * 论文详细信息Mapper接口
 * 
 * @author tian
 * @date 2023-02-12
 */
public interface PapersMapper 
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
     * 删除论文详细信息
     * 
     * @param id 论文详细信息主键
     * @return 结果
     */
    public int deletePapersById(Long id);

    /**
     * 批量删除论文详细信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePapersByIds(Long[] ids);

    /**
     * 根据论文名称查找论文
     *
     * @param name
     * @return
     */
    public Papers selectPapersByName(String name);
}
