package com.javaweb.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class CommonConfig {

    /**
     * 图片域名
     */
    public static String imageURL;

    /**
     * 图片域名赋值
     *
     * @param url 域名地址
     */
    @Value("${server.IMAGE_URL}")
    public void setImageURL(String url) {
        imageURL = url;
    }

    /**
     * 文件下载保存本地地址
     */
    public static String downloadURL;

    /**
     * 文件下载保存本地地址赋值
     *
     * @param url 地址
     */
    @Value("${server.DOWNLOAD_URL}")
    public void setDownloadURL(String url) {
        downloadURL = url;
    }
}
