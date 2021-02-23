package com.example.oldtown.common.service.impl;

import com.example.oldtown.common.api.CommonResult;
import com.example.oldtown.common.service.MinioService;
import com.example.oldtown.common.domain.MinioResponse;
import io.minio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ding.yp
 * @name Minio文件存储服务实现
 * @info
 * @date 2020/11/09
 */

@Service
public class MinioServiceImpl implements MinioService {

    private final Logger LOGGER = LoggerFactory.getLogger(MinioServiceImpl.class);
    @Value("${minio.endpoint}")
    private String ENDPOINT;
    @Value("${minio.bucketName}")
    private String BUCKET_NAME;
    @Value("${minio.bucketSide}")
    private String BUCKET_SIDE;
    @Value("${minio.accessKey}")
    private String ACCESS_KEY;
    @Value("${minio.secretKey}")
    private String SECRET_KEY;

    /**
     * 桶占位符
     */
    private static final String BUCKET_PARAM = "${bucket}";
    /**
     * bucket权限-只读
     */
    private static final String READ_ONLY = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "/*\"]}]}";
    /**
     * bucket权限-只读
     */
    private static final String WRITE_ONLY = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "/*\"]}]}";
    /**
     * bucket权限-读写
     */
    private static final String READ_WRITE = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:DeleteObject\",\"s3:GetObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\",\"s3:AbortMultipartUpload\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "/*\"]}]}";

    /**
     * 文件上传
     * @param folder
     * @param file
     * @return
     */
    @Override
    public CommonResult uploadFile(String folder, MultipartFile file) {
        try {
            //创建一个MinIO的Java客户端
            MinioClient minioClient = MinioClient.builder().endpoint(ENDPOINT).credentials(ACCESS_KEY, SECRET_KEY).build();
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
            if (!isExist) {
                //创建存储桶并设置只读权限
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(BUCKET_NAME).config(READ_ONLY.replace(BUCKET_PARAM,BUCKET_NAME)).build());
            }
            String filename = file.getOriginalFilename();

            // 设置存储对象名称
            String objectName = folder + filename;
            // 使用putObject上传一个文件到存储桶中
            minioClient.putObject(PutObjectArgs.builder().bucket(BUCKET_NAME).object(objectName).stream(file.getInputStream(),
                    file.getInputStream().available(),-1).contentType(file.getContentType()).build());
            LOGGER.info("文件上传成功!");
            MinioResponse minioResponse = new MinioResponse();
            minioResponse.setName(filename);
            minioResponse.setUrl(BUCKET_SIDE + objectName);
            return CommonResult.success(minioResponse);
        } catch (Exception e) {
            LOGGER.error("上传文件发生错误: {}！", e);
            return CommonResult.failed("上传文件发生错误:"+e.getMessage());
        }
    }

    /**
     * 文件删除
     * @param url
     * @return
     */
    @Override
    public CommonResult deleteFile(String folder,String url) {
        try {

            if (!url.startsWith(BUCKET_SIDE+folder)) {
                return CommonResult.failed("参数url格式应为:"+BUCKET_SIDE+folder+"xxx");
            }

            String objectName = url.replace(BUCKET_SIDE, "");
            MinioClient minioClient = MinioClient.builder().endpoint(ENDPOINT).credentials(ACCESS_KEY, SECRET_KEY).build();
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(BUCKET_NAME).object(objectName).build());
            return CommonResult.success("成功删除文件:"+BUCKET_SIDE+objectName);
        } catch (Exception e) {
            LOGGER.error("删除文件失败:", e);
            return CommonResult.failed("删除文件失败:"+e.getMessage());
        }
    }





}
