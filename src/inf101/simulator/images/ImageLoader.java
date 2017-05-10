package inf101.simulator.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

/**
 * Temporary class for loading images
 * @author Einar Snorrason
 *
 */
public class ImageLoader {
	 

	
	public ImageLoader(String nameIn,String nameOut,int spriteW){
		// Open tileset		
		Image sprites = null;		
		WritableImage img = null;
				try {
					
					sprites = new Image(this.getClass().getResourceAsStream(nameIn));
				} catch (Exception e) {
					System.out.println(e);
					System.exit(0);
				}
				PixelReader reader = sprites.getPixelReader();
				
				
				for (int i=0;i<=2;i++){
					img = new WritableImage(reader,i*spriteW,2*spriteW,spriteW,spriteW);
					File file = new File(nameOut+(i+1)+".png");
					BufferedImage renderedImage = SwingFXUtils.fromFXImage(img, null);
					try {
						ImageIO.write(
						        renderedImage, 
						        "png",
						        file);
						System.out.println("Done");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	}
}
