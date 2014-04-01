/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio;

import condominio.core.CondominioFxController;
import condominio.core.administrativo.permissoes.PrivilegiosFxController;
import condominio.core.administrativo.relatorios.RelatorioCadastroFxController;
import condominio.core.login.LoginController;
import condominio.core.portaria.BuscaFxController;
import condominio.core.portaria.PortariaFxController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Marlon Harnish
 */
public class Condominio extends Application {
    
    private Stage stage;
    private FXMLLoader loader;
    private AnchorPane page;
    public Map<AnchorPane, PortariaFxController> cadastro = new HashMap<AnchorPane, PortariaFxController>();
    public Map<AnchorPane, RelatorioCadastroFxController> relatorios = new HashMap<AnchorPane, RelatorioCadastroFxController>();
	public AnchorPane busca;
	public AnchorPane permissoes;
    
	//293441
	
	
    @Override
    public void start(Stage primaryStage) {
    	stage = primaryStage;
            carregarPermissoes();            
            carregarBusca();
            carregarCadastro();
            carregarRelatorios();
            //carregarPrimeiraPagina();
            carregaLogin();
            primaryStage.show();
    }
	public void carregaLogin() {
		try {
            LoginController login = (LoginController) replaceSceneContent("core/login/Login.fxml");            
            login.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Condominio.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

	/**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void carregarPrimeiraPagina() {
        try {
            CondominioFxController primeiraPg = (CondominioFxController) replaceSceneContent("core/Condominio.fxml");
            primeiraPg.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Condominio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void carregarPermissoes() {
    	FXMLLoader fxmlLoader = loadFX("core/administrativo/permissoes/Permissoes.fxml");
    	permissoes = fxmlLoader.getRoot();
		((PrivilegiosFxController) fxmlLoader.getController()).setApp(this);
	}
    private void carregarBusca() {
    	FXMLLoader fxmlLoader = loadFX("core/portaria/PortariaBusca.fxml");
    	busca = fxmlLoader.getRoot();
		((BuscaFxController) fxmlLoader.getController()).setApp(this);    	
	}
    
    private void carregarCadastro() {
    	FXMLLoader fxmlLoader = loadFX("core/portaria/PortariaCadastro.fxml");
    	AnchorPane cad = fxmlLoader.getRoot();
    	cadastro.put(cad, (PortariaFxController)fxmlLoader.getController());
		((PortariaFxController) fxmlLoader.getController()).setApp(this);        
    }
    
    private void carregarRelatorios() {
    	FXMLLoader fxmlLoader = loadFX("core/administrativo/relatorios/RelatorioCadastro.fxml");
    	AnchorPane relat = fxmlLoader.getRoot();
    	relatorios.put(relat, (RelatorioCadastroFxController)fxmlLoader.getController());
		((RelatorioCadastroFxController) fxmlLoader.getController()).setApp(this);        
    }
    
    private FXMLLoader loadFX(String fxml){
    	try {
    	 URL location = getClass().getResource(fxml);
    	 FXMLLoader fxmlLoader = new FXMLLoader();  
         fxmlLoader.setLocation(getClass().getResource(fxml));
         Parent root = (Parent)fxmlLoader.load(location.openStream());
         return fxmlLoader;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    private Initializable replaceSceneContent(String fxml) throws Exception {
        loader = new FXMLLoader();
        InputStream in = Condominio.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Condominio.class.getResource(fxml));        
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        
        return (Initializable) loader.getController();
    }  

    public AnchorPane getPane() {
       return page; 
    }
	public Stage getStage() {
		return stage;
	}
 }
