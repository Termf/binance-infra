package com.binance.master.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public final class FileUtils {

    /**
     * 判断路径是否为文件
     * 
     * @param filePath
     * @return
     */
    public static boolean isFile(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.isFile();
    }

    /**
     * 判断路径是否为目录
     * 
     * @param path
     * @return
     */
    public static boolean isDirectory(String path) {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        File file = new File(path);
        return file.isDirectory();
    }

    /**
     * 判断文件或目录是否存在
     * 
     * @param path
     * @return
     */
    public static boolean exists(String path) {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        File file = new File(path);
        return file.exists();
    }

}
