/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.core;

import condominio.Condominio;
import condominio.core.login.Privilegios;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Marlon Harnisch
 */
public class CondominioFxController implements Initializable{
    
    @FXML
    MenuItem portariaBusca;
    @FXML
    MenuItem portariaCadastro;
    @FXML
    MenuItem permisoes;
    @FXML
    Label bemVindo;
    @FXML
    Label sair;
    
    
    private AnchorPane busca;
    private AnchorPane cadastro;
    private AnchorPane permissoes;
    private AnchorPane relatorios;
    
    private Condominio main;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configuraPermissoes();
    } 
    
    private void configuraPermissoes() {
		Map<String, Boolean> prv = Privilegios.getPermissoes();
		if(!prv.get(Privilegios.VISUALIZA_CADASTRO)){
			portariaCadastro.setDisable(true);
		}
		if(!prv.get(Privilegios.VISUALIZA_BUSCA)){
			portariaBusca.setDisable(true);
		}
		if(!prv.get(Privilegios.VISUALIZA_PERMISSOES)){
			permisoes.setDisable(true);
		}
	}

	public void AbrirPermissoes(ActionEvent e){
    	setVisible(permissoes);      
    }
    
    public void AbrirBusca(ActionEvent e){
    	setVisible(busca);
    }
    
    public void AbrirCadastro(ActionEvent e){
    	setVisible(cadastro);
    }
    
    public void AbrirRelatorios(ActionEvent e){
    	setVisible(relatorios);
    }
    
    public void sair(MouseEvent ev){
    	main.carregaLogin();
    }
    
    public void setVisible(AnchorPane pane){
    	cadastro.setVisible(false);
    	busca.setVisible(false);
    	permissoes.setVisible(false);
    	relatorios.setVisible(false);
    	
    	pane.setVisible(true);
    }

    public void setApp(Condominio condominio) {
        this.main = condominio;
        sair.setCursor(Cursor.HAND);
        bemVindo.setText("Bem Vindo "+ Privilegios.usuario);
        initComponents();
    }

	private void initComponents() {
		this.busca = main.busca;
		this.cadastro = main.cadastro.keySet().iterator().next();//TODO ...
		this.permissoes = main.permissoes;
		this.relatorios = main.relatorios.keySet().iterator().next();
		if(Privilegios.getPermissoes().get(Privilegios.VISUALIZA_BUSCA)){
			init(busca, true);			
		}
		init(relatorios, false);
		init(cadastro, false);
		init(permissoes, false);
	}
    
	private void init(AnchorPane page, boolean visible){
		page.setLayoutX(10);
		page.setLayoutY(40);
        main.getPane().getChildren().add(page);
        page.setVisible(visible);
	}
}
