package com.tian.business.service.impl;

import java.util.List;

import com.tian.common.core.exception.ServiceException;
import com.tian.common.core.utils.StringUtils;
import com.tian.common.core.utils.bean.BeanValidators;
import com.tian.common.security.utils.SecurityUtils;
import com.tian.system.api.domain.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tian.business.mapper.PapersMapper;
import com.tian.business.domain.Papers;
import com.tian.business.service.IPapersService;

import javax.validation.Validator;

/**
 * 论文详细信息Service业务层处理
 * 
 * @author tian
 * @date 2023-02-12
 */
@Service
public class PapersServiceImpl implements IPapersService 
{
    private static final Logger log = LoggerFactory.getLogger(PapersServiceImpl.class);
    @Autowired
    private PapersMapper papersMapper;

    @Autowired
    protected Validator validator;

    /**
     * 查询论文详细信息
     * 
     * @param id 论文详细信息主键
     * @return 论文详细信息
     */
    @Override
    public Papers selectPapersById(Long id)
    {
        return papersMapper.selectPapersById(id);
    }

    /**
     * 查询论文详细信息列表
     * 
     * @param papers 论文详细信息
     * @return 论文详细信息
     */
    @Override
    public List<Papers> selectPapersList(Papers papers)
    {
        return papersMapper.selectPapersList(papers);
    }

    /**
     * 新增论文详细信息
     * 
     * @param papers 论文详细信息
     * @return 结果
     */
    @Override
    public int insertPapers(Papers papers)
    {
        return papersMapper.insertPapers(papers);
    }

    /**
     * 修改论文详细信息
     * 
     * @param papers 论文详细信息
     * @return 结果
     */
    @Override
    public int updatePapers(Papers papers)
    {
        return papersMapper.updatePapers(papers);
    }

    /**
     * 批量删除论文详细信息
     * 
     * @param ids 需要删除的论文详细信息主键
     * @return 结果
     */
    @Override
    public int deletePapersByIds(Long[] ids)
    {
        return papersMapper.deletePapersByIds(ids);
    }

    /**
     * 删除论文详细信息信息
     * 
     * @param id 论文详细信息主键
     * @return 结果
     */
    @Override
    public int deletePapersById(Long id)
    {
        return papersMapper.deletePapersById(id);
    }

    /**
     * 导入论文数据
     * @param papersList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return
     */
    @Override
    public String importUser(List<Papers> papersList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(papersList) || papersList.size() == 0)
        {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
//        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (Papers paper : papersList)
        {
            try
            {
                // 验证是否存在这个论文
                Papers u = papersMapper.selectPapersByName(paper.getName());
                if (StringUtils.isNull(u))
                {
                    BeanValidators.validateWithException(validator, paper);
                    //paper.setPassword(SecurityUtils.encryptPassword(password));
                    //paper.setCreateBy(operName);
                    this.insertPapers(paper);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、论文 " + paper.getName() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, paper);
                    //paper.setUpdateBy(operName);
                    paper.setId(u.getId());
                    papersMapper.updatePapers(paper);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、论文 " + paper.getName() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、论文 " + paper.getName() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、论文 " + paper.getName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
