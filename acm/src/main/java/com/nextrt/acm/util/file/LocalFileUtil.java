package com.nextrt.acm.util.file;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by XYZM
 * User: xyzm
 * Date: 19-12-31
 * info: 文件操作类
 */
public class LocalFileUtil {
    private static final List<String> ImageType = Arrays.asList("png", "jpg", "jpeg", "bmp", "gif", "svg", "webp");

    public static InputStream getFileInputStream(String filePath){
        Resource resource = new ClassPathResource(filePath);
        try {
            return resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean deleteFile(String fileName){
        File file = new File(fileName);// 读取
        if(file.isFile()){ // 判断是否是文件夹
           return file.delete();// 删除
        }else{
            File[] files = file.listFiles(); // 获取文件
            if(files == null){
               return file.delete();// 删除
            }else{
                for (File value : files) {// 循环
                    deleteFile(value.getAbsolutePath());
                }
                return file.delete();// 删除
            }
        }
    }
    //获取文件MD5
    public static String getMd5Code(MultipartFile file){
        String code = "系统出错";
        try {
            code =  DigestUtils.md5Hex(file.getBytes());
        } catch (IOException ignored) {
        }
        return code;
    }
    //保存文件
    public static int saveFile(MultipartFile file,String fileName){
        File saveFile = new File(fileName);
        if(!saveFile.getParentFile().exists() && !saveFile.getParentFile().mkdirs()) {
            return -2;
        }
        try {
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(file.getBytes());
            out.flush();
            out.close();
            return 1;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static int StringToFile(String data,String fileName){
        File saveFile = new File(fileName);
        if(!saveFile.getParentFile().exists() && !saveFile.getParentFile().mkdirs()) {
            return -2;
        }
        try {
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(data.getBytes());
            out.flush();
            out.close();
            return 1;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String downloadFile(HttpServletResponse response, String path) {
        String fileName = path.substring(path.lastIndexOf(File.separator) + 1);//取文件拓展名
        response.setHeader("content-type", URLConnection.guessContentTypeFromName(fileName));
        response.setContentType(URLConnection.guessContentTypeFromName(fileName));
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(path)));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (FileNotFoundException e1) {
            return "系统找不到指定的文件";
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "success";
    }

}
