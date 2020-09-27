package application.extractor_feature;

import java.io.File;
import java.io.FileOutputStream;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class ExtractFeature {
	
	public static double[] extraiCaracteristicas(File imagem) {
		
		double[] caracteristicas = new double[7];

		double laranjaCamisaBart = 0;
		double azulCalcaoBart = 0;
		double azulSapatoBart = 0;
		double azulCalcaHomer = 0;
		double marromBocaHomer = 0;
		double cinzaSapatoHomer = 0; 
		
		
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
				
				if(isCamisaLaranjaBart(r, g, b)) {
					laranjaCamisaBart++;
					imagemProcessada.put(i, j, new double[]{0, 255, 128});
				}
				if(i>(altura/2) && isCalcaoAzulBart(r, g, b)) {
					azulCalcaoBart++;
					imagemProcessada.put(i, j, new double[]{0, 255, 128});
				}
				if (i > (altura/2 + altura/3) && isSapatoBart(r, g, b)) {
					azulSapatoBart++;
					imagemProcessada.put(i, j, new double[]{0, 255, 128});
				}
				if(isCalcaAzulHomer(r, g, b)) {
					azulCalcaHomer++;
					imagemProcessada.put(i, j, new double[]{0, 255, 255});
				}
				if(i < (altura/2 + altura/3) && isBocaHomer(r, g, b)) {
					marromBocaHomer++;
					imagemProcessada.put(i, j, new double[]{0, 255, 255});
				}
				if (i > (altura/2 + altura/3) && isSapatoHomer(r, g, b)) {
					cinzaSapatoHomer++;
					imagemProcessada.put(i, j, new double[]{0, 255, 255});
				}
				
			}
		}
	
		
		// Normaliza as características pelo número de pixels totais da imagem para %
        laranjaCamisaBart = (laranjaCamisaBart / (largura * altura)) * 100;
        azulCalcaoBart = (azulCalcaoBart / (largura * altura)) * 100;
        azulSapatoBart = (azulSapatoBart / (largura * altura)) * 100;
        azulCalcaHomer = (azulCalcaHomer / (largura * altura)) * 100;
        marromBocaHomer = (marromBocaHomer / (largura * altura)) * 100;
        cinzaSapatoHomer = (cinzaSapatoHomer / (largura * altura)) * 100;
        
        caracteristicas[0] = laranjaCamisaBart;
        caracteristicas[1] = azulCalcaoBart;
        caracteristicas[2] = azulSapatoBart;
        
        caracteristicas[3] = azulCalcaHomer;
        caracteristicas[4] = marromBocaHomer;
        caracteristicas[5] = cinzaSapatoHomer;
        //APRENDIZADO SUPERVISIONADO - JÁ SABE QUAL A CLASSE NAS IMAGENS DE TREINAMENTO
        caracteristicas[6] = imagem.getName().charAt(0)=='b'?0:1;
		
		//HighGui.imshow("Imagem original", imagemOriginal);
        //HighGui.imshow("Imagem processada", imagemProcessada);
        
        //HighGui.imshow("Teste sem fundo", removeFundoImagem(imagemProcessada, imagemOriginal));
        
       // HighGui.waitKey(0);
		
		return caracteristicas;
	}

	
	
	
	private static Mat removeFundoImagem(Mat currFrame, Mat oldFrame) {
		
		Mat greyImage = new Mat();
		Mat foregroundImage = new Mat();
		
		Core.absdiff(currFrame, oldFrame, foregroundImage);
		Imgproc.cvtColor(foregroundImage, greyImage, Imgproc.COLOR_BGR2GRAY);
		
		int thresh_type = Imgproc.THRESH_BINARY_INV;
		
		Imgproc.threshold(greyImage, greyImage, 10, 255, thresh_type);
		currFrame.copyTo(foregroundImage, greyImage);
		
		oldFrame = currFrame;
		return foregroundImage;
		
	}
	
	
	
	
	
	/*
	 * Retorna true se encontrar padrão de cores referente ao que se pede
	 * */
	
	public static boolean isCamisaLaranjaBart(double r, double g, double b) {
		 if (b >= 7 && b <= 90 &&  g >= 70 && g <= 105 &&  r >= 200 && r <= 255) {                       
         	return true;
         }
		 return false;
	}
	public static boolean isCalcaoAzulBart(double r, double g, double b) {
		if (b >= 125 && b <= 170 &&  g >= 5 && g <= 125 &&  r >= 0 && r <= 20) {                       
			return true;
		}
		return false;
	}
	public static boolean isSapatoBart(double r, double g, double b) {
		if (b >= 125 && b <= 140 &&  g >= 3 && g <= 12 &&  r >= 5 && r <= 20) {                       
			return true;
		}
		return false;
	}
	public static boolean isCalcaAzulHomer(double r, double g, double b) {
		if (b >= 150 && b <= 180 &&  g >= 98 && g <= 120 &&  r >= 0 && r <= 90) {                       
			return true;
		}
		return false;
	}
	public static boolean isBocaHomer(double r, double g, double b) {
		if (b >= 95 && b <= 140 &&  g >= 160 && g <= 185 &&  r >= 175 && r <= 200) {                       
			return true;
		}
		return false;
	}
	public static boolean isSapatoHomer(double r, double g, double b) {
		if (b >= 25 && b <= 45 &&  g >= 25 && g <= 45 &&  r >= 25 && r <= 45) {                       
			return true;
		}
		return false;
	}


	
	
	
	
	
	
	
	//Extraindo caracteristicas do diretorio fixo
	public static void criarARFF() {
				
	    // Cabeçalho do arquivo Weka
		String exportacao = "@relation caracteristicas\n\n";
		exportacao += "@attribute laranja_camisa_bart real\n";
		exportacao += "@attribute azul_calcao_bart real\n";
		exportacao += "@attribute azul_sapato_bart real\n";
		exportacao += "@attribute marrom_boca_homer real\n";
		exportacao += "@attribute azul_calca_homer real\n";
		exportacao += "@attribute cinza_sapato_homer real\n";
		exportacao += "@attribute classe {Bart, Homer}\n\n";
		exportacao += "@data\n";
	        
	    // Diretório onde estão armazenadas as imagens
	    File diretorio = new File("src\\application\\imagens");
	    File[] arquivos = diretorio.listFiles();
	    
        // Definição do vetor de características
        double[][] caracteristicas = new double[293][7];
        
        // Percorre todas as imagens do diretório
        int cont = -1;
        for (File imagem : arquivos) {
        	cont++;
        	caracteristicas[cont] = extraiCaracteristicas(imagem);
        	
        	String classe = caracteristicas[cont][6] == 0 ?"Bart":"Homer";
        	
//        	System.out.println("Laranja camisa Bart: " + caracteristicas[cont][0] 
//            		+ " - Azul calção Bart: " + caracteristicas[cont][1] 
//            		+ " - Azul sapato Bart: " + caracteristicas[cont][2] 
//            		+ " - Azul calça Homer: " + caracteristicas[cont][3] 
//            		+ " - Marrom boca Homer: " + caracteristicas[cont][4] 
//            		+ " - Preto sapato Homer: " + caracteristicas[cont][5] 
//            		+ " - Classe: " + classe);
        	
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
