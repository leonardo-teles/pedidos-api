package com.nelioalves.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nelioalves.service.exception.FileException;

@Service
public class ImageService {

	public BufferedImage getJpgImage(MultipartFile uploadedFile) {
		String extensao = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
		if(!"png".equals(extensao) && !"jpg".equals(extensao)) {
			throw new FileException("Somente imagens PNG e JPG são permitidas.");
		}
		
		try {
			BufferedImage imagem = ImageIO.read(uploadedFile.getInputStream());
			
			if("png".equals(extensao)) {
				imagem = converterDePngAJpg(imagem);
			}
			
			return imagem;
			
		} catch (IOException e) {
			throw new FileException("Erro ao ler o arquivo.");
		}
	}

	public BufferedImage converterDePngAJpg(BufferedImage imagem) {
		BufferedImage imagemJpg = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_INT_RGB);
		imagemJpg.createGraphics().drawImage(imagem, 0, 0, Color.WHITE, null);
		
		return imagemJpg;
	}
	
	public InputStream getInputStream(BufferedImage imagem, String extensao) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(imagem, extensao, os);
			
			return new ByteArrayInputStream(os.toByteArray());
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}
	
}
