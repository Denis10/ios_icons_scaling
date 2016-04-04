package com.denis;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

public class Main {
	public final static String out_dir_48="renamed_images48";
	public final static String out_dir_72="renamed_images72";
	public final static String out_dir_96="renamed_images96";
	public final static String out_dir_144="renamed_images144";
	public final static int size48=48;
	public final static int size72=72;
	public final static int size96=96;
	public final static int size144=144;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File folder = new File("rename_images");
        File[] listOfFiles = folder.listFiles();
        for(File file:listOfFiles) {
        	String name=file.getName();
        	String newName=name.toLowerCase().replace("@2x", "").replace("@3x", "").replace(" ", "_").replace("-", "_");
        	String absolutePath = file.getAbsolutePath();
    		String filePath = absolutePath.
    		    substring(0,absolutePath.lastIndexOf(File.separator));
    		String newPath=filePath+"//"+newName;
    		file.renameTo(new File(newPath));   

    		resizeImage(newPath,size48);
    		resizeImage(newPath,size72);
    		resizeImage(newPath,size96);
    		resizeImage(newPath,size144);
        }        
	}
	
	private static void copyImage(File file,String outDir){
		String name=file.getName();
		String absolutePath = file.getAbsolutePath();
		String filePath = absolutePath.
		    substring(0,absolutePath.lastIndexOf(File.separator));
		File folder = new File(filePath+"//"+outDir);
		folder.mkdir();
		String newPath=folder.getAbsolutePath()+"//"+name;
		try {
			Files.copy(Paths.get(absolutePath),Paths.get(newPath),
					   StandardCopyOption.COPY_ATTRIBUTES,StandardCopyOption.REPLACE_EXISTING);
			File renamedfile=new File(newPath);
			String newName=name.toLowerCase().replace("@2x", "").replace("@3x", "").replace(" ", "_").replace("-", "_");
			String renamedPath=folder.getAbsolutePath()+"//"+newName;
			renamedfile.renameTo(new File(renamedPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private static void resizeImage(String path,int size){
		try {
			String outDir;
			switch(size){
			case size48:
				outDir=out_dir_48;
				break;
			case size72:
				outDir=out_dir_72;
				break;
			case size96:
				outDir=out_dir_96;
				break;
			default:
				outDir=out_dir_144;
				break;
			}
			
			File file=new File(path);
			String name=file.getName();
			String absolutePath = file.getAbsolutePath();
			String filePath = absolutePath.
			    substring(0,absolutePath.lastIndexOf(File.separator));
			File folder = new File(filePath+"//"+outDir);
			folder.mkdir();
			String newPath=folder.getAbsolutePath()+"//"+name;

	        BufferedImage originalImage = ImageIO.read(file);//change path to where file is located
	        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
	        
//	        Dimension dim=getImageDimension(new File(path));
	        int width=originalImage.getWidth();
	        int height=originalImage.getHeight();
	        int resWidth=size,resHeight=size;
	        if (width!=height){
	        	if (width>height){	        		
	        		resHeight=(height*size)/width;
	        		resWidth=size;
	        	}else{
	        		resHeight=(width*size)/height;
	        		height=size;
	        	}
	        }

	        BufferedImage resizeImageJpg = resizeImage(originalImage, type, resWidth, resHeight);
	        ImageIO.write(resizeImageJpg, "png", new File(newPath)); //change path where you want it saved

	    } catch (IOException e) {
	        System.out.println(e.getMessage());
	    }

	}

	private static BufferedImage resizeImage(BufferedImage originalImage, int type, int IMG_WIDTH, int IMG_HEIGHT) {
	    BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
	    Graphics2D g = resizedImage.createGraphics();
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
	    g.dispose();

	    return resizedImage;
	}
	
	public static Dimension getImageDimension(File imgFile) throws IOException {
		  int pos = imgFile.getName().lastIndexOf(".");
		  if (pos == -1)
		    throw new IOException("No extension for file: " + imgFile.getAbsolutePath());
		  String suffix = imgFile.getName().substring(pos + 1);
		  Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
		  if (iter.hasNext()) {
		    ImageReader reader = iter.next();
		    try {
		      ImageInputStream stream = new FileImageInputStream(imgFile);
		      reader.setInput(stream);
		      int width = reader.getWidth(reader.getMinIndex());
		      int height = reader.getHeight(reader.getMinIndex());
		      return new Dimension(width, height);
		    } catch (IOException e) {
		      e.printStackTrace();
		    } finally {
		      reader.dispose();
		    }
		  }

		  throw new IOException("Not a known image file: " + imgFile.getAbsolutePath());
		}
}
