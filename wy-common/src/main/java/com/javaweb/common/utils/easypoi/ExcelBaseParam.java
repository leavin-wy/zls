package com.javaweb.common.utils.easypoi;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.servlet.http.HttpServletResponse;

@Data
@Accessors(chain = true)
public class ExcelBaseParam {

    private HttpServletResponse response;

    //文件名
    private String fileName;

    //excel sheet名
    private String sheetName;

    //excel 标题
    private String titleName;

    @Override
    public String toString() {
        return "ExcelBaseParam{" +
                "response=" + response +
                ", fileName='" + fileName + '\'' +
                ", sheetName='" + sheetName + '\'' +
                ", titleName='" + titleName + '\'' +
                '}';
    }
}
