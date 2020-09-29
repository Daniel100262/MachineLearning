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
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Debug.Random;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class PrincipalController {
	
	@FXML private ImageView imageView;

	//Labels
    @FXML private Label lblVerdeCamisaLenny;
    @FXML private Label lblAzulSuspensorioLenny;
    @FXML private Label lblMarromBarbaLenny;
    
    @FXML private Label lblLaranjaCamisaNelson;
    @FXML private Label lblAzulColeteNelson;
    @FXML private Label lblAzulSapatoNelson;
    
    @FXML private Label lblApzHomer;
    @FXML private Label lblApzBart;
    
    @FXML private Label lblMatrizConfusao;


	private double[] caracteristicasImgSel = {0,0,0,0,0,0};
	
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
			caracteristicasImgSel = ExtractFeature.extraiCaracteristicas(f);
			
			//Lenny
			lblVerdeCamisaLenny.setText("Verde Camisa: "+round(caracteristicasImgSel[0], 4));
			lblAzulSuspensorioLenny.setText("Azul Suspensorio: "+round(caracteristicasImgSel[1],4));
			lblMarromBarbaLenny.setText("Marrom Barba: "+round(caracteristicasImgSel[2],4 ));
			
			//Nelson
			lblLaranjaCamisaNelson.setText("Laranja Camisa: "+round(caracteristicasImgSel[3],4));
			lblAzulColeteNelson.setText("Azul Colete: "+round(caracteristicasImgSel[4],4));
			lblAzulSapatoNelson.setText("Azul Sapato: "+round(caracteristicasImgSel[5],4));
			
			for (double d : caracteristicasImgSel) {
				System.out.println(d);
			}
			
		}
	}
	
	public static String gerarMatrizConfusao() throws Exception {
		 DataSource ds = new DataSource("src\\arff\\caracteristicas.arff");
		 Instances instancias = ds.getDataSet();
		 instancias.setClassIndex(instancias.numAttributes()-1);
		 Classifier cls = new J48();
		 Evaluation eval = new Evaluation(instancias);
		 Random rand = new Random(1);  // using seed = 1
		 int folds = 10;
		 eval.crossValidateModel(cls, instancias, folds, rand);
		 return eval.toMatrixString();
	}
	
	DecimalFormat df = new DecimalFormat();
	
	//private double[] caracteristicasImgSel = {1,1,1,1,1,1};
	
	@FXML
	public void classificar() {
		double[] nb = AprendizagemBayesiana.naiveBayes(caracteristicasImgSel);
		System.out.println("LENNY: "+nb[0]);
		System.out.println("NELSON: "+nb[1]);
		lblApzBart.setText("LENNY: "+round(nb[0], 4)+"%");
		lblApzHomer.setText("NELSON: "+round(nb[1], 4)+"%");
	}
	
	@FXML
	public void mostraMatrizDecisao() throws Exception {
		lblMatrizConfusao.setText(gerarMatrizConfusao());
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
