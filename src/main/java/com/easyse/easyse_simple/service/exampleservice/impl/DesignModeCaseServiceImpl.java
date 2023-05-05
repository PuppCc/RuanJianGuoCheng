package com.easyse.easyse_simple.service.exampleservice.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyse.easyse_simple.mapper.exampleservice.DesignModeCaseMapper;
import com.easyse.easyse_simple.pojo.DO.example.DesignModeCase;
import com.easyse.easyse_simple.service.exampleservice.DesignModeCaseService;
import com.easyse.easyse_simple.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

/**
* @author 25405
* @description 针对表【examples_design_mode_case(设计模式案例)】的数据库操作Service实现
* @createDate 2022-12-07 23:06:59
*/
@Service
public class DesignModeCaseServiceImpl extends ServiceImpl<DesignModeCaseMapper, DesignModeCase>
implements DesignModeCaseService {

    @Autowired
    DesignModeCaseMapper designModeCaseMapper;

    @Autowired
    SensitiveFilter sensitiveFilter;

    @Override
    public int addCase(DesignModeCase designModeCase) {
        if (designModeCase == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        // 转义 HTML 标记，防止在 HTML 标签中注入攻击语句
        designModeCase.setTitle(HtmlUtils.htmlEscape(designModeCase.getTitle()));
        designModeCase.setContent(HtmlUtils.htmlEscape(designModeCase.getContent()));

        // 过滤敏感词
        designModeCase.setTitle(sensitiveFilter.filter(designModeCase.getTitle()));
        designModeCase.setContent(sensitiveFilter.filter(designModeCase.getContent()));

        return designModeCaseMapper.insert(designModeCase);
    }
}
