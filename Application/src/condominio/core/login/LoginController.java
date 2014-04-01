/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 */
package condominio.core.login;

import java.net.URL;
import java.util.ResourceBundle;

import condominio.Condominio;
import condominio.core.administrativo.UsuarioCrud;
import condominio.server.modelo.USUARIO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * Login Controller.
 */
public class LoginController extends AnchorPane implements Initializable {

    @FXML
    TextField userId;
    @FXML
    PasswordField password;
    @FXML
    Button login;
    @FXML
    Label errorMessage;

    private UsuarioCrud userCrud;
	private Condominio application;
    
    
    public void setApp(Condominio application){
    	userCrud = new UsuarioCrud();
    	this.application = application;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorMessage.setText("");
    }

    public void processLogin(ActionEvent event) {
    	USUARIO usuario = userCrud.processaLogin(userId.getText());
    	if(usuario.getUsuario() == null || usuario.getUsuario().isEmpty()){
    		errorMessage.setText("Usuario inixistente " + userId.getText());
    		return;
    	}
    	if(!validaLogin(usuario)){
    		errorMessage.setText("Senha incorreta " + userId.getText());
    		return;
    	}
    	errorMessage.setText("Ol� " + userId.getText());
    	preencherPrivilegios(usuario);
    	application.carregarPrimeiraPagina();
    }
	
	private void preencherPrivilegios(USUARIO usuario) {
		Privilegios.usuario = usuario.getUsuario();
		Privilegios.setPermissoes(userCrud.getPrivilegios(usuario.getId()));
	}

	private boolean validaLogin(USUARIO usuario) {
		if(usuario.getSenha().equals(password.getText())){
			return true;
		}
		return false;
	}
}
