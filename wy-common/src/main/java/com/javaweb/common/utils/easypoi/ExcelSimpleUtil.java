package com.javaweb.common.utils.easypoi;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.javaweb.common.exception.CustomException;
import com.javaweb.common.utils.ExcelUtil;
import com.javaweb.common.utils.ReflectUtils;
import com.javaweb.common.utils.StringUtils;
import com.javaweb.common.utils.Tuple2;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description Excel简单导出工具类   使用注解的导出  时使用此类
 * @author: wjw
 * @Date: 2021/2/9
 * @Time: 11:08
 */
public class ExcelSimpleUtil {
    public static final Logger logger = LoggerFactory.getLogger(ExcelSimpleUtil.class);

    /**
     * 导出Excel
     *
     * @param title
     * @param sheetName
     * @param datas
     * @param clazz
     * @return
     */
    public static <T> Workbook exportExcel(String title, String sheetName, List<T> datas, Class<T> clazz,boolean ifUseStyle) {
        return exportExcelExecusions(title, sheetName, datas, clazz, null,ifUseStyle);
    }

    /**
     * 导出下载模板
     *
     * @param datas
     * @param clazz
     * @param <T>
     * @param importDesc 上传说明
     * @return
     */
    public static <T> Workbook exportTemplate(List<T> datas, Class<T> clazz, String importDesc,boolean ifUseStyle) {
        if (StringUtils.isNotBlank(importDesc)) {
            importDesc = "填表说明：修改数据，主键不可修改，新增不需添加主键;" + importDesc;
        } else {
            importDesc = "填表说明：修改数据，主键不可修改，新增不需添加主键";
        }
        return exportExcelExecusions(importDesc, "sheet", datas, clazz, null,ifUseStyle);
    }


    /**
     * 携带排除信息导出数据
     *
     * @param title
     * @param sheetName
     * @param datas
     * @param clazz
     * @param execusions 注意这个数组中应该传递导出的实体对象的@Excel注解的name的名字
     * @return
     */
    public static Workbook exportExcelExecusions(String title, String sheetName, List<?> datas, Class clazz, String[] execusions,boolean ifUseStyle) {
        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        if(ifUseStyle){
            exportParams.setIndexName("序号");
            exportParams.setStyle(ExportStyle.class);
        }else {
            exportParams.setStyle(DefaultExportStyle.class);
        }
        if (execusions != null) {
            exportParams.setExclusions(execusions);
        }
        if (!CollectionUtils.isEmpty(datas)) {
            if (datas.size() > 20000) {
                return  ExcelExportUtil.exportBigExcel(exportParams, clazz, datas);
            }
        }
        return ExcelExportUtil.exportExcel(exportParams, clazz, datas);
    }


    /**
     * easypoi
     * 导入Excel（少量数据）
     *
     * @param is
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> importExcel(InputStream is, Class<T> clazz) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        return ExcelImportUtil.importExcel(is,
                clazz, params);
    }

    /**
     * 大量数据，基于ExcelUtil.readMoreThan1000RowBySheet与easypoi注解写的解析
     *
     * @param filePath
     * @param clazz
     * @param hasTitle 是否含有标题
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> importBigExcel(String filePath, Class<T> clazz, boolean hasTitle) throws Exception {

        //获取字段跟注解的映射
        List<Field> allFields = ReflectUtils.getAllFields(clazz);
        Map<String, Field> fieldMap = allFields.stream()
                .map(field -> Tuple2.of(field.getAnnotation(Excel.class), field))
                .filter(t->null!=t._1)
                .collect(Collectors.toMap(t -> t._1.name(), t -> t._2, (o, n) -> n));

        List<Object> resultExcel = ExcelUtil.readMoreThan1000Row(filePath);
        int size = resultExcel.size();
        int headRow;
        if(hasTitle && size>=3){
            headRow = 1;
        }else if(!hasTitle && size>=2){
            headRow = 0;
        }else {
            throw new CustomException("导入Excel无数据或者格式不正确！");
        }

        List<String> headRowList = (ArrayList<String>)resultExcel.get(headRow);
        Map<Integer,Field> headIndexMap = new HashMap<>();
        for (int i = 0; i < headRowList.size(); i++) {
            headIndexMap.put(i,fieldMap.get(headRowList.get(i)));
        }

        List<T> resultList = resultExcel.subList(headRow + 1, size).stream().map(row -> {
            T t = null;
            try {
                t = clazz.newInstance();
                List<String> dataList = (ArrayList<String>) row;
                for (int i = 0; i < dataList.size(); i++) {
                    Field field = headIndexMap.get(i);
                    field.setAccessible(true);
                    field.set(t, ConvertUtils.convert(dataList.get(i), field.getType()));
                }
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("发生异常");
            }
            return t;
        }).collect(Collectors.toList());

        return resultList;
    }


    /**
     * 导出MAP类型EXCEL
     *
     * @param tempLateMap 模板Map           key:字段值  value:列名
     * @param list        数据集合      key：字段    value:字段属性值
     * @return
     */
    public static Workbook templateDonwload(Map<String, Object> tempLateMap, List<Map<String, Object>> list, String title, String sheetName) {

        List<ExcelExportEntity> beanList = new ArrayList<ExcelExportEntity>();
        for (Map.Entry entry : tempLateMap.entrySet()) {
            beanList.add(new ExcelExportEntity(String.valueOf(entry.getValue()), entry.getKey()));
        }
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(title, sheetName, ExcelType.XSSF), beanList,
                list);
        return workbook;
    }

}
