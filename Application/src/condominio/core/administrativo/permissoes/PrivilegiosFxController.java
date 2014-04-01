/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.core.administrativo.permissoes;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Dialogs.DialogOptions;
import javafx.scene.control.Dialogs.DialogResponse;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import condominio.Condominio;
import condominio.core.administrativo.UsuarioCrud;
import condominio.core.login.Privilegios;
import condominio.server.modelo.PRIVILEGIOS;
import condominio.server.modelo.USUARIO;

/**
 *
 * @author Marlon Harnisch
 */
public class PrivilegiosFxController implements Initializable{
	
	@FXML
	CheckBox cadastroVisualiza;
	@FXML
	CheckBox cadastroEdita;
	@FXML
	CheckBox cadastroRemove;
	@FXML
	CheckBox buscaVisualiza;
	@FXML
	CheckBox acessoVisualiza;
	@FXML
	CheckBox acessoEdita;
	@FXML
	CheckBox acessoRemove;
	@FXML
	CheckBox permissaoVisualiza;
	@FXML
	CheckBox permissaoEdita;
	@FXML
	CheckBox permissaoRemove;
	@FXML
	TextField usuario;
    @FXML
    PasswordField senha;
    @FXML
    PasswordField confirmaSenha;
    @FXML
    Button cancelar;
    @FXML
    Button confirmar;
    @FXML
    ListView<USUARIO> listaUsuarios;
    @FXML
    Button remover;
    
    private HashMap<String, Boolean> privilegiosMap = new HashMap<>();
	private Condominio main;
	private UsuarioCrud uCrud = new UsuarioCrud();
   
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
    	listarUsuarios();
    }
    
    private void listarUsuarios() {
		List<USUARIO> usuarios = uCrud.listaUsuarios();
		listaUsuarios.setItems(FXCollections.observableArrayList(usuarios));
	}

	private void populateMap() {
		privilegiosMap.put(Privilegios.VISUALIZA_CADASTRO, cadastroVisualiza.isSelected());
		privilegiosMap.put(Privilegios.REMOVE_CADASTRO, cadastroRemove.isSelected());
		privilegiosMap.put(Privilegios.EDITA_CADASTRO, cadastroEdita.isSelected());
		privilegiosMap.put(Privilegios.VISUALIZA_BUSCA, buscaVisualiza.isSelected());
		privilegiosMap.put(Privilegios.VISUALIZA_ACESSO, acessoVisualiza.isSelected());
		privilegiosMap.put(Privilegios.REMOVE_ACESSO, acessoRemove.isSelected());
		privilegiosMap.put(Privilegios.EDITA_ACESSO, acessoEdita.isSelected());
		privilegiosMap.put(Privilegios.VISUALIZA_PERMISSOES, permissaoVisualiza.isSelected());
		privilegiosMap.put(Privilegios.REMOVE_PERMISSOES, permissaoRemove.isSelected());
		privilegiosMap.put(Privilegios.EDITA_PERMISSOES, permissaoEdita.isSelected());		
	}

	public void cadastrarUsuario(ActionEvent ev){
    	if(!confirmaSenha()){
    		Dialogs.showConfirmDialog(main.getStage(), "Senhas não são idênticas!");
    		return;
    	}
    	populateMap();
    	USUARIO user = new USUARIO();
    	user.setUsuario(usuario.getText());
    	user.setSenha(senha.getText());
    	
    	USUARIO savedUser = uCrud.create(user);
    	
    	List<PRIVILEGIOS> prvList = new ArrayList<PRIVILEGIOS>();
    	for (String permissao : privilegiosMap.keySet()) {
    		PRIVILEGIOS privilegios = new PRIVILEGIOS();    		
    		privilegios.setIdUsuario(savedUser.getId());
    		privilegios.setDescricao(permissao);
    		privilegios.setPermissao(privilegiosMap.get(permissao));
			prvList.add(privilegios);
		}
    	
    	uCrud.create(prvList);
    	
    	listarUsuarios();
    	limpar();    	
    }
	
	public void cancelarCadastro(ActionEvent ev){
		limpar();
	}
	
	public void removerUsuario(){
		USUARIO user = listaUsuarios.getSelectionModel().getSelectedItem();
		DialogResponse response = Dialogs.showConfirmDialog(main.getStage(), "Você tem certeza que deseja remover?",
			      "Remover Usuário", "Permissões", DialogOptions.OK_CANCEL);
		if(response.equals(DialogResponse.OK)){
			uCrud.removerUsuario(user);
			listarUsuarios();			
		}
	}
	
	private void limpar(){
		cadastroEdita.setSelected(false);
		cadastroRemove.setSelected(false);
		cadastroVisualiza.setSelected(false);
		acessoEdita.setSelected(false);
		acessoRemove.setSelected(false);
		acessoVisualiza.setSelected(false);
		buscaVisualiza.setSelected(false);
		permissaoEdita.setSelected(false);
		permissaoRemove.setSelected(false);
		permissaoVisualiza.setSelected(false);
		
		usuario.setText("");
		senha.setText("");
		confirmaSenha.setText("");
	}

    private boolean confirmaSenha() {		
		return senha.getText().equals(confirmaSenha.getText());
	}
    
	public void setApp(Condominio main) {
        this.main = main;       
    }    
}
