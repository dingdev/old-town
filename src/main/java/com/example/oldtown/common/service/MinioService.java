package com.example.oldtown.common.service;

import com.example.oldtown.common.api.CommonResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ding.yp
 * @name Minio文件存储服务
 * @info
 * @date 2020/11/09
 */

public interface MinioService {
    /**
     * 上传文件
     */
    CommonResult uploadFile(String folder, MultipartFile file);

    /**
     * 删除文件
     */
    CommonResult deleteFile(String folder,String url);


}
