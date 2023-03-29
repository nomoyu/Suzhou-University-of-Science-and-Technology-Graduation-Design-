package com.tian.business.controller;

import com.tian.business.domain.Papers;
import com.tian.business.domain.Write;
import com.tian.business.mapper.WriteMapper;
import com.tian.business.service.IWriteService;
import com.tian.common.core.web.controller.BaseController;
import com.tian.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: tian
 * Date: 2023/3/28
 * Time: 22:22
 */
@RestController
@RequestMapping("/write")
public class WriteController extends BaseController {
    @Autowired
    private IWriteService writeService;

    /**
     * 增加笔记记录
     * @param write
     * @return
     */
    @PostMapping
    public AjaxResult add(@RequestBody Write write)
    {
        return toAjax(writeService.add(write));
    }

    @GetMapping("/list")
    public AjaxResult list(Write write){
        return success(writeService.queryList(write));
    }
}
