
package org.meruvian.yama.cs.webapi.service.commons;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.meruvian.yama.core.commons.FileInfo;
import org.meruvian.yama.core.commons.FileInfoRepository;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FileInfoService implements EnvironmentAware {
	@Inject
	private FileInfoRepository fileInfoRepository;
	private String uploadBasePath;
	
	@Transactional
	public FileInfo saveFileInfo(String path, FileInfo fileInfo) throws IOException {
		FileInfo info = new FileInfo();
		info.setPath(StringUtils.join(uploadBasePath, path));
		info.setOriginalName(StringUtils.defaultString(fileInfo.getOriginalName()));
		
		File file = new File(info.getPath());
		if (!file.exists()) {
			file.mkdirs();
		}
		
		info.setContentType(fileInfo.getContentType());
		info = fileInfoRepository.save(info);
		info.setPath(StringUtils.join(info.getPath(), "/", info.getId()));
		if (StringUtils.isBlank(fileInfo.getOriginalName())) {
			info.setOriginalName(info.getId());
		}
		
		InputStream inputStream = fileInfo.getDataBlob();
		OutputStream outputStream = new FileOutputStream(info.getPath());
		info.setSize(IOUtils.copy(inputStream, outputStream));
		IOUtils.closeQuietly(inputStream);
		IOUtils.closeQuietly(outputStream);
		
		return info;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.uploadBasePath = environment.getProperty("upload.path.base");
	}
}
