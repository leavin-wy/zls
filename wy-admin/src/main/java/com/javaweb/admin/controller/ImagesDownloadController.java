package com.javaweb.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.javaweb.admin.constant.ImageMarkConstant;
import com.javaweb.admin.dto.ImageExportDto;
import com.javaweb.admin.entity.ImageGroups;
import com.javaweb.admin.entity.ImageMark;
import com.javaweb.admin.entity.Images;
import com.javaweb.admin.mapper.ImageGroupsMapper;
import com.javaweb.admin.mapper.ImageMarkMapper;
import com.javaweb.admin.mapper.ImagesMapper;
import com.javaweb.admin.utils.ExcelUtils;
import com.javaweb.common.config.CommonConfig;
import com.javaweb.common.config.UploadFileConfig;
import com.javaweb.common.utils.DateUtils;
import com.javaweb.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * 图片信息下载 控制器
 * </p>
 *
 * @author leavin
 * @since 2021-01-14
 */
@Controller
@RequestMapping("/imagesdownload")
public class ImagesDownloadController {
    @Autowired
    private ImagesMapper imagesMapper;
    @Autowired
    private ImageMarkMapper imageMarkMapper;
    @Autowired
    private ImageGroupsMapper imageGroupsMapper;

    /**
     * 跳转图片列表页面
     * @return
     */
    @GetMapping("/imageListPage")
    public String imageListPage() {
        return "imagesdownload/imageList";
    }

    /**
     * 图片下载
     * @param ids
     * @param request
     * @param response
     */
    @GetMapping("/downloadImages")
    public void getDownload(String ids, HttpServletRequest request, HttpServletResponse response) {
        String[] idarr = ids.split(",");
        List<String> list = new ArrayList<>();
        for(String id : idarr){
            Images image = imagesMapper.selectById(id);
            //文件名称
            /*String fileName = UploadFileConfig.getUploadFolder()+image.getImgUrl();
            list.add(fileName);*/
        }

        String zipFileName = "images_" + new Date().getTime() + ".zip";
        //文件路径
        String path = this.fileToZip(list, zipFileName);
        OutputStream out = null;
        BufferedInputStream br = null;
        try {
            String fileName = URLEncoder.encode(zipFileName, "UTF-8");
            br = new BufferedInputStream(new FileInputStream(path));
            byte[] buf = new byte[1024];
            int len = 0;
            response.reset();
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            out = response.getOutputStream();
            while ((len = br.read(buf)) > 0)
                out.write(buf, 0, len);
            out.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * zip打包工具方法
     * @param list  原文件名list
     * @param zipFileName 压缩包文件名
     * @return
     */
    public String fileToZip(List<String> list,String zipFileName) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        //本地路径
        String zippath = CommonConfig.downloadURL + zipFileName;
        try {
            File zipFile = new File(zippath);
            //获取父目录
            File fileParent = zipFile.getParentFile();
            //判断是否存在
            if (!fileParent.exists()) {
                //创建父目录文件
                fileParent.mkdirs();
            }

            // 创建一个新文件
            zipFile.createNewFile();
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(new BufferedOutputStream(fos));
            byte[] bufs = new byte[1024 * 10];
            for (String fileName : list) {
                File subFile = new File(fileName);
                if (!subFile.exists()) {
                    continue;
                }
                //文件名
                ZipEntry zipEntry = new ZipEntry(subFile.getName());
                zos.putNextEntry(zipEntry);
                fis = new FileInputStream(subFile);
                bis = new BufferedInputStream(fis, 1024 * 10);
                int read = 0;
                while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                    zos.write(bufs, 0, read);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (null != bis) bis.close();
                if (null != zos) zos.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return zippath;
    }

    /**
     * 下载图片标注信息，生成excel
     * @param groupId 图片分组
     * @param response
     * @throws Exception
     */
    @RequestMapping("export")
    public void export(String groupId,HttpServletResponse response) throws Exception{
        QueryWrapper<Images> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("img_mark","1");//默认只下载已标注的图片
        if(StringUtils.isNotEmpty(groupId)){
            queryWrapper.eq("img_group",groupId);
        }
        List<Images> list = imagesMapper.selectList(queryWrapper);
        List<ImageExportDto> excelDtos = new ArrayList<>();
        for (Images image : list) {
            ImageExportDto excelDto = new ImageExportDto();
            //excelDto.setImageUrl(UploadFileConfig.getUploadFolder()+image.getImgUrl());
            ImageGroups imageGroup = imageGroupsMapper.selectById(image.getImgGroup());
            if(imageGroup!=null){
                excelDto.setImageGroup(imageGroup.getGroupName());
            }
            QueryWrapper<ImageMark> queryMarkWrapper = new QueryWrapper<>();
            queryMarkWrapper.eq("img_id",image.getId());
            List<ImageMark> markList = imageMarkMapper.selectList(queryMarkWrapper);

            for(ImageMark imageMark:markList) {
                String defect = imageMark.getImgDefectType()+imageMark.getImgDefect();
                // 拿到该类
                Class<?> clz = excelDto.getClass();
                // 获取实体类的所有属性，返回Field数组
                Field[] fields = clz.getDeclaredFields();

                for (Field field : fields) {
                    Class<?> aClass = excelDto.getClass();
                    Field declaredField = aClass.getDeclaredField(field.getName());
                    //放开类型检查
                    declaredField.setAccessible(true);
                    if (defect.equals(field.getName())){
                        declaredField.set(excelDto, ImageMarkConstant.IMAGEMARK_IMGDEFECTLEVEL_LIST.get(imageMark.getImgDefectLevel()));
                    }
                }
            }

            excelDtos.add(excelDto);
        }
        String fileName = DateUtils.dateTimeNow("yyyyMMddHHmmss");
        // 配置文件下载
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        ExcelUtils.writeExcel(response, excelDtos, fileName, "sheet1", new ImageExportDto());
    }
}
