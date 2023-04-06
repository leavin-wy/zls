package com.javaweb.common.utils.easypoi;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @description
 * @author: wjw
 * @Date: 2021/2/9
 * @Time: 11:08
 */
public class ExcelDownloadUtil {
    public static final Logger logger = LoggerFactory.getLogger(ExcelDownloadUtil.class);

    /**
     * 下载Excel
     *
     * @param workbook
     * @param filename
     * @param response
     */
    public static void downloadExcel(Workbook workbook, String filename, HttpServletResponse response) throws IOException {
        try {
            filename = new String(filename.getBytes(), "ISO8859-1");
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

    }
}