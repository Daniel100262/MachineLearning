package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MessageUtils {
	
	
	public static void exibeMensagem(String titulo, String cabecalho, String msg, AlertType tipoAlerta) {
		Alert alerta = new Alert(tipoAlerta);
		alerta.setTitle(titulo);
		alerta.setHeaderText(cabecalho);
		alerta.setContentText(msg);
		alerta.showAndWait();
	}
	
	public static void exibeMensagem(String titulo, String cabecalho, String msg) {
		Alert alerta = new Alert(AlertType.INFORMATION);
		alerta.setTitle(titulo);
		alerta.setHeaderText(cabecalho);
		alerta.setContentText(msg);
		alerta.showAndWait();
	}
	
	public static void exibeMensagem(String titulo, String cabecalho) {
		Alert alerta = new Alert(AlertType.INFORMATION);
		alerta.setTitle(titulo);
		alerta.setHeaderText(cabecalho);
		alerta.showAndWait();
	}
	
	
}
