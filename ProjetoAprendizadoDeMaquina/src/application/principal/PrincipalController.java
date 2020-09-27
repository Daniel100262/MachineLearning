package application.principal;

import java.io.File;
import java.text.DecimalFormat;

import algoritmos.AprendizagemBayesiana;
import application.extractor_feature.ExtractFeature;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class PrincipalController {
	
	@FXML private ImageView imageView;

	//Labels
    @FXML private Label lblLaranjaCamisaBart;
    @FXML private Label lblAzulCalcaoBart;
    @FXML private Label lblAzulSapatoBart;
    @FXML private Label lblAzulCalcaHomer;
    @FXML private Label lblMarromBocaHomer;
    @FXML private Label lblPretoSapatoHomer;
    @FXML private Label lblApzHomer;
    @FXML private Label lblApzBart;
	
	@FXML
	public void enviarCaracteristicasParaARFF() {
		ExtractFeature.criarARFF();
	}
	
	@FXML
	public void selecionaImagem() {
		File f = buscaImg();
		if(f != null) {
			Image img = new Image(f.toURI().toString());
			imageView.setImage(img);
			imageView.setFitWidth(img.getWidth());
			imageView.setFitHeight(img.getHeight());
			double[] caracteristicas = ExtractFeature.extraiCaracteristicas(f);
			
			//Bart
			lblLaranjaCamisaBart.setText("Laranja camisa: "+round(caracteristicas[0], 4));
			lblAzulCalcaoBart.setText("Azul Calção: "+round(caracteristicas[1], 4));
			lblAzulSapatoBart.setText("Azul Sapato: "+round(caracteristicas[2], 4));
			
			//Homer
			lblAzulCalcaHomer.setText("Azul Calça: "+round(caracteristicas[3], 4));
			lblMarromBocaHomer.setText("Marrom Boca: "+round(caracteristicas[4], 4));
			lblPretoSapatoHomer.setText("Preto Sapato: "+round(caracteristicas[5], 4));
			
		}
	}
	
	DecimalFormat df = new DecimalFormat();
	
	private double[] caracteristicasImgSel = {0,0,0,0,0,0};
	
	@FXML
	public void classificar() {
		double[] nb = AprendizagemBayesiana.naiveBayes(caracteristicasImgSel);
		System.out.println("Bart: "+nb[0]);
		System.out.println("Homer: "+nb[1]);
		lblApzBart.setText("BART: "+round(nb[0], 4)+"%");
		lblApzHomer.setText("HOMER: "+round(nb[1], 4)+"%");
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	private File buscaImg() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new 
				   FileChooser.ExtensionFilter(
						   "Imagens", "*.jpg", "*.JPG", 
						   "*.png", "*.PNG", "*.gif", "*.GIF", 
						   "*.bmp", "*.BMP")); 	
		 fileChooser.setInitialDirectory(new File("src/imagens"));
		 File imgSelec = fileChooser.showOpenDialog(null);
		 try {
			 if (imgSelec != null) {
			    return imgSelec;
			 }
		 } catch (Exception e) {
			e.printStackTrace();
		 }
		 return null;
	}
	
}
