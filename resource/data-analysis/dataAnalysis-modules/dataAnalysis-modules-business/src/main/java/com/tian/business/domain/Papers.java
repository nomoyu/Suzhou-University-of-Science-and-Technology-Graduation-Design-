package com.tian.business.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tian.common.core.annotation.Excel;
import com.tian.common.core.web.domain.BaseEntity;

/**
 * 论文详细信息对象 bus_papers
 * 
 * @author tian
 * @date 2023-02-12
 */
public class Papers extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 论文id */
    private Long id;


    /** 论文名称 */
    @Excel(name = "论文名称")
    private String name;

    /** 论文内容 */
    @Excel(name = "论文内容")
    private String content;

    /** 论文年限 */
//    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "论文年限", width = 30, dateFormat = "yyyy-MM-dd")
    private Date year;

    /** 比赛类型 */
    @Excel(name = "比赛类型(0-国赛1-美赛)")
    private String type;

    /** 论文分类 */
    @Excel(name = "论文分类(0-5:A-E)")
    private String level;
    @Excel(name = "文件路径")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setYear(Date year) 
    {
        this.year = year;
    }

    public Date getYear() 
    {
        return year;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setLevel(String level) 
    {
        this.level = level;
    }

    public String getLevel() 
    {
        return level;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("content", getContent())
            .append("year", getYear())
            .append("type", getType())
            .append("level", getLevel())
            .toString();
    }
}
