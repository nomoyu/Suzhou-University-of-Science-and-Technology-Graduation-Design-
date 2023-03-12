package com.tian.business.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.tian.common.security.utils.SecurityUtils;
import com.tian.system.api.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tian.common.log.annotation.Log;
import com.tian.common.log.enums.BusinessType;
import com.tian.common.security.annotation.RequiresPermissions;
import com.tian.business.domain.Papers;
import com.tian.business.service.IPapersService;
import com.tian.common.core.web.controller.BaseController;
import com.tian.common.core.web.domain.AjaxResult;
import com.tian.common.core.utils.poi.ExcelUtil;
import com.tian.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 论文详细信息Controller
 * 
 * @author tian
 * @date 2023-02-12
 */
@RestController
@RequestMapping("/papers")
public class PapersController extends BaseController
{
    @Autowired
    private IPapersService papersService;

    /**
     * 查询论文详细信息列表
     */
    @RequiresPermissions("business:papers:list")
    @GetMapping("/list")
    public TableDataInfo list(Papers papers)
    {
        startPage();
        List<Papers> list = papersService.selectPapersList(papers);
        return getDataTable(list);
    }

    /**
     * 导出论文详细信息列表
     */
    @RequiresPermissions("business:papers:export")
    @Log(title = "论文详细信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Papers papers)
    {
        List<Papers> list = papersService.selectPapersList(papers);
        ExcelUtil<Papers> util = new ExcelUtil<Papers>(Papers.class);
        util.exportExcel(response, list, "论文详细信息数据");
    }

    /**
     * 获取论文详细信息详细信息
     */
    @RequiresPermissions("business:papers:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(papersService.selectPapersById(id));
    }

    /**
     * 新增论文详细信息
     */
    @RequiresPermissions("business:papers:add")
    @Log(title = "论文详细信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Papers papers)
    {
        return toAjax(papersService.insertPapers(papers));
    }

    /**
     * 修改论文详细信息
     */
    @RequiresPermissions("business:papers:edit")
    @Log(title = "论文详细信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Papers papers)
    {
        return toAjax(papersService.updatePapers(papers));
    }

    /**
     * 删除论文详细信息
     */
    @RequiresPermissions("business:papers:remove")
    @Log(title = "论文详细信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(papersService.deletePapersByIds(ids));
    }

    /**
     * 导入论文模板信息
     * @param file
     * @param updateSupport
     * @return
     * @throws Exception
     */
    @Log(title = "论文管理", businessType = BusinessType.IMPORT)
//    @RequiresPermissions("business:paper:import")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<Papers> util = new ExcelUtil<Papers>(Papers.class);
        List<Papers> papersList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = papersService.importUser(papersList, updateSupport, operName);
        return success(message);
    }

    /**
     * 论文模板
     * @param response
     * @throws IOException
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException
    {
        ExcelUtil<Papers> util = new ExcelUtil<Papers>(Papers.class);
        util.importTemplateExcel(response, "论文数据");
    }
}
