package org.mobon.billing.consumer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class WaterTest {
	public static void main(String[] args) throws IOException {
		File sourceImageFile = new File("C:\\w\\down\\타겟광고주월별시간통계.jpg");
		File watermarkImageFile = new File("C:\\w\\down\\타겟광고주월별시간통계.jpg");
		File destImageFile = new File("C:\\w\\down\\8218025_out.jpg");
		addImageWatermark(watermarkImageFile, sourceImageFile, destImageFile);
	}

	static void addImageWatermark(File fFile, File sFile, File destImageFile) {
		try {
			BufferedImage fImage = ImageIO.read(fFile);
			BufferedImage sImage = ImageIO.read(sFile);

			BufferedImage buffer = new BufferedImage(fImage.getWidth()+sImage.getWidth(), fImage.getHeight(), BufferedImage.TYPE_USHORT_565_RGB);
			Graphics2D g2d = (Graphics2D) buffer.getGraphics();

			g2d.drawImage(fImage, 0, 0, null);
			g2d.drawImage(sImage, fImage.getWidth(), 0, null);

			ImageIO.write(buffer, "png", destImageFile);

		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}
