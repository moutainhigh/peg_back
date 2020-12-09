/*
 *    Copyright (c) 2018-2025, kbopark All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: kbopark
 */
package cn.qdzhhl.kbopark.admin.service.impl;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qdzhhl.kbopark.admin.api.entity.SysFile;
import cn.qdzhhl.kbopark.admin.mapper.SysFileMapper;
import cn.qdzhhl.kbopark.admin.service.SysFileService;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.oss.OssProperties;
import cn.qdzhhl.kbopark.common.oss.service.OssTemplate;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件管理
 *
 * @author Luckly
 * @date 2019-06-18 17:18:42
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {

	private final OssProperties ossProperties;

	private final OssTemplate minioTemplate;

	/**
	 * 上传文件
	 * @param file
	 * @return
	 */
	@Override
	public R uploadFile(MultipartFile file) {
		String uuid = IdUtil.simpleUUID();
		String fileName =  uuid + StrUtil.DOT + FileUtil.extName(file.getOriginalFilename());
		String fileThumbName = uuid + "_thumb_" + StrUtil.DOT + FileUtil.extName(file.getOriginalFilename());
		Map<String, String> resultMap = new HashMap<>(4);
		resultMap.put("bucketName", ossProperties.getBucketName());
		resultMap.put("fileName", fileThumbName);
		resultMap.put("fileSourceName", fileName);
		resultMap.put("name", file.getOriginalFilename());
		resultMap.put("url", String.format("/admin/sys-file/%s/%s", ossProperties.getBucketName(), fileThumbName));

		try {
			InputStream inputStream = file.getInputStream();
			BufferedImage read = ImageIO.read(inputStream);
			//直接保存原图
			minioTemplate.putObject(ossProperties.getBucketName(), fileName, inputStream);
			//生成缩略图并保存
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImgUtil.scale(read, outputStream, 0.5f);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
			minioTemplate.putObject(ossProperties.getBucketName(), fileThumbName, byteArrayInputStream);
			// 文件管理数据记录,收集管理追踪文件
			fileLog(file, fileName);
			fileLog(file, fileThumbName);
		}catch (Exception e) {
			log.error("上传失败", e);
			return R.failed(e.getLocalizedMessage());
		}
		return R.ok(resultMap);
	}

	/**
	 * 读取文件
	 * @param bucket
	 * @param fileName
	 * @param response
	 */
	@Override
	public void getFile(String bucket, String fileName, HttpServletResponse response) {
		try (InputStream inputStream = minioTemplate.getObject(bucket, fileName)) {
			response.setContentType("application/octet-stream; charset=UTF-8");
			IoUtil.copy(inputStream, response.getOutputStream());
		}
		catch (Exception e) {
			log.error("文件读取异常: {}", e.getLocalizedMessage());
		}
	}

	/**
	 * 删除文件
	 * @param id
	 * @return
	 */
	@Override
	@SneakyThrows
	@Transactional(rollbackFor = Exception.class)
	public Boolean deleteFile(Long id) {
		SysFile file = this.getById(id);
		minioTemplate.removeObject(ossProperties.getBucketName(), file.getFileName());
		return this.removeById(id);
	}

	/**
	 * 文件管理数据记录,收集管理追踪文件
	 * @param file 上传文件格式
	 * @param fileName 文件名
	 */
	private void fileLog(MultipartFile file, String fileName) {
		SysFile sysFile = new SysFile();
		// 原文件名
		String original = file.getOriginalFilename();
		sysFile.setFileName(fileName);
		sysFile.setOriginal(original);
		sysFile.setFileSize(file.getSize());
		sysFile.setType(FileUtil.extName(original));
		sysFile.setBucketName(ossProperties.getBucketName());
		sysFile.setCreateUser(SecurityUtils.getUser().getUsername());
		this.save(sysFile);
	}

}
