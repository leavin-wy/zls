package com.javaweb.admin.config;

import com.javaweb.admin.constant.*;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Thymeleaf模板配置
 */
@Configuration
public class ThymeleafAConfig {

    @Resource
    private void configureThymeleafStaticVars(ThymeleafViewResolver viewResolver) {
        if (viewResolver != null) {
            Map<String, Object> vars = new HashMap<>();

            /**
             * 图片是否标注
             */
            vars.put("IMAGES_IMGMARK_LIST", ImagesConstant.IMAGES_IMGMARK_LIST);
            /**
             * 图片缺陷等级
             */
            vars.put("IMAGEMARK_IMGDEFECTLEVEL_LIST", ImageMarkConstant.IMAGEMARK_IMGDEFECTLEVEL_LIST);
            /**
             * 表面缺欠--缺陷
             */
            vars.put("IMAGEMARK_IMGDEFECT_BMQQ_LIST", ImageMarkConstant.IMAGEMARK_IMGDEFECT_BMQQ_LIST);
            /**
             * 几何形状缺欠--缺陷
             */
            vars.put("IMAGEMARK_IMGDEFECT_JHXZQQ_LIST", ImageMarkConstant.IMAGEMARK_IMGDEFECT_JHXZQQ_LIST);

            viewResolver.setStaticVariables(vars);
        }
    }
}
