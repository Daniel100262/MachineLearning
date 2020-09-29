package application.extractor_feature;

import java.io.File;
import java.io.FileOutputStream;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class ExtractFeature {
	
	public static double[] extraiCaracteristicas(File imagem) {
		
		double[] caracteristicas = new double[7];
		
		double VerdeCamisaLenny = 0;
		double AzulSuspensorioLenny = 0;
		double MarromBarbaLenny = 0;
		
		double LaranjaCamisaNelson = 0;
		double AzulColeteNelson = 0;
		double AzulSapatoNelson = 0;
		
		Image img = new Image(imagem.toURI().toString());
		PixelReader pr = img.getPixelReader();
		
		Mat imagemOriginal = Imgcodecs.imread(imagem.getPath());
		Mat imagemProcessada = imagemOriginal.clone();

		int largura = (int)img.getWidth();
		int altura = (int)img.getHeight();
		
		for(int i=0; i<altura; i++) {
			for(int j=0; j<largura; j++) {
				
				Color cor = pr.getColor(j,i);
				
				double r = cor.getRed()*255; 
				double g = cor.getGreen()*255;
				double b = cor.getBlue()*255;
				
				if(i> (altura/4) && i < 3*(altura/4) &&isVerdeCamisaLenny(r, g, b)) {
					VerdeCamisaLenny++;
					imagemProcessada.put(i, j, new double[]{0,255,128});
				}
				
				if(i< (altura/1.25) && i>(altura/1.5) && isAzulSuspensorioLenny(r, g, b)) {
					AzulSuspensorioLenny++;
					imagemProcessada.put(i, j, new double[]{0,255,128});
				}
				
				if(i < (altura/2 + altura/3) && isMarromBarbaLenny(r, g, b)) {
					MarromBarbaLenny++;
					imagemProcessada.put(i, j, new double[]{0,255,128});
				}
				
				if(i> (altura/4) && isLaranjaCamisaNelson(r, g, b)) {
					LaranjaCamisaNelson++;
					imagemProcessada.put(i, j, new double[]{25, 0, 255 });
				}
				
				if(i< (altura/2) && isAzulColeteNelson(r, g, b)) {
					AzulColeteNelson++;
					imagemProcessada.put(i, j, new double[]{25, 0, 255 });
				}
				
			
				if (i > (altura/2) && isAzulSapatoNelson(r, g, b)) {
					AzulSapatoNelson++;
					imagemProcessada.put(i, j, new double[]{25, 0, 255 });
				}
			}
		}
	
		
		// Normaliza as características pelo número de pixels totais da imagem para %
		VerdeCamisaLenny = (VerdeCamisaLenny / (largura * altura)) * 100;
		AzulSuspensorioLenny = (AzulSuspensorioLenny / (largura * altura)) * 100;
        MarromBarbaLenny = (MarromBarbaLenny / (largura * altura)) * 100;
        LaranjaCamisaNelson = (LaranjaCamisaNelson / (largura * altura)) * 100;
        AzulColeteNelson = (AzulColeteNelson / (largura * altura)) * 100;
        AzulSapatoNelson = (AzulSapatoNelson / (largura * altura)) * 100;
        
        caracteristicas[0] = VerdeCamisaLenny;
        caracteristicas[1] = AzulSuspensorioLenny;
        caracteristicas[2] = MarromBarbaLenny;
        
        caracteristicas[3] = LaranjaCamisaNelson;
        caracteristicas[4] = AzulColeteNelson;
        caracteristicas[5] = AzulSapatoNelson;
        //APRENDIZADO SUPERVISIONADO - JÁ SABE QUAL A CLASSE NAS IMAGENS DE TREINAMENTO
        caracteristicas[6] = imagem.getName().charAt(0)=='l'?0:1;
		
		HighGui.imshow("Imagem original", imagemOriginal);
        HighGui.imshow("Imagem processada", imagemProcessada);
        
        HighGui.waitKey(50);
		
		return caracteristicas;
	}
	

	
	/*
	 * Caracteristicas Lenny
	 * 
	 * */
	
	public static boolean isVerdeCamisaLenny(double r, double g, double b) {
		if (r >= 14 && r <= 70 && g>=71 && g<= 160 && b>=10 && b <= 35) {
			return true;
		}
		return false;
	}
	
	public static boolean isAzulSuspensorioLenny(double r, double g, double b) {
		if (r >= 30 && r <= 60 && g>=10 && g<= 35 && b>=85 && b<=135) {
			return true;
		}
		return false;
	}
	
	public static boolean isMarromBarbaLenny(double r, double g, double b) {
		if (r >= 175 && r<= 220 && g >= 125 && g <= 200 && b>=100 && b<= 160) {
			return true;
		}
		return false;
	}
	
	
	
	
	/*
	 * Caracteristicas Nelson
	 * 
	 * */
	
	public static boolean isLaranjaCamisaNelson(double r, double g, double b) {
		if (r >= 185 && r <= 235 && g >= 95 && g <= 140 && b >= 85 && b <= 110) {
			return true;
		}
		return false;
	}
	
	
	public static boolean isAzulColeteNelson(double r, double g, double b) {
		if (r >= 90 && r <= 125 && g >= 129 && g <= 149 && b >= 136 && b <= 178) {
			return true;
		}
		return false;
	}
	
	
	public static boolean isAzulSapatoNelson(double r, double g, double b) {
		if (r >= 39 && r <= 45 && g >= 99 && g <= 106 && b >= 30 && b <= 200) {
			return true;
		}
		return false;
	}

	
	//Extraindo caracteristicas do diretorio fixo
	public static void criarARFF() {
				
	    // Cabeçalho do arquivo Weka
		String exportacao = "@relation caracteristicas\n\n";
		
		//Lenny
		exportacao += "@attribute verde_camisa_lenny real\n";
		exportacao += "@attribute azul_suspensorio_lenny real\n";
		exportacao += "@attribute marrom_barba_lenny real\n";
		
		//Nelson
		exportacao += "@attribute laranja_camisa_nelson real\n";
		exportacao += "@attribute azul_colete_nelson real\n";
		exportacao += "@attribute azul_sapato_nelson real\n";
		
		exportacao += "@attribute classe {Lenny, Nelson}\n\n";
		exportacao += "@data\n";
	        
	    // Diretório onde estão armazenadas as imagens
	    File diretorio = new File("src\\application\\imagens");
	    File[] arquivos = diretorio.listFiles();
	    
        // Definição do vetor de características
        double[][] caracteristicas = new double[668][7];
        
        // Percorre todas as imagens do diretório
        int cont = -1;
        for (File imagem : arquivos) {
        	cont++;
        	
        	
        	caracteristicas[cont] = extraiCaracteristicas(imagem);
        	
        	String classe = caracteristicas[cont][6] == 0 ?"Lenny":"Nelson";
        	
        	exportacao += caracteristicas[cont][0] + "," 
                    + caracteristicas[cont][1] + "," 
        		    + caracteristicas[cont][2] + "," 
                    + caracteristicas[cont][3] + "," 
        		    + caracteristicas[cont][4] + "," 
                    + caracteristicas[cont][5] + "," 
                    + classe + "\n";
        }
        
     // Grava o arquivo ARFF no disco
        try {
        	File arquivo = new File("src\\arff\\caracteristicas.arff");
        	FileOutputStream f = new FileOutputStream(arquivo);
        	f.write(exportacao.getBytes());
        	f.close();
        	utils.MessageUtils.exibeMensagem("Sucesso!", "Dados gravados com sucesso no ARFF!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

}
