/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.core.portaria;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import condominio.Condominio;
import condominio.server.modelo.ANIMAIS;
import condominio.server.modelo.CADASTRO_MORADOR;
import condominio.server.modelo.TELEFONES;
import condominio.server.modelo.VEICULOS;

/**
 *
 * @author Marlon Harnisch
 */
public class BuscaFxController implements Initializable{
    
	@FXML
	TextField busca;
	@FXML
	ListView<CADASTRO_MORADOR> resultado;
    @FXML
    Button exibirDetalhes;
    @FXML
    TextArea detalhes;
    @FXML
    Button editar;
    @FXML
    Button acesso;
    @FXML
    Button novaBusca;
    
	private Condominio main;
	private PortariaCrud pCrud = new PortariaCrud();
	private boolean canChange = false;
	private CADASTRO_MORADOR detalhe;
	private boolean navegando = false;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
    	detalhes.setStyle("-fx-font-weight: bold;"+
    						"-fx-font-size: 16;");
    	busca.requestFocus();
    	acesso.setDisable(true);
    	resultado.setVisible(false);
    	resultado.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CADASTRO_MORADOR>() {

			@Override
    	    public void changed(ObservableValue<? extends CADASTRO_MORADOR> observable, CADASTRO_MORADOR oldValue, CADASTRO_MORADOR newValue) {
    	    	if(newValue == null){
    	    		return;
    	    	}
    	    	if(navegando){
    	    		return;
    	    	}
    	    	busca.setText(newValue.toString());
    	    	canChange = true;
    	    	preencherDetalhes(newValue);
    	    	detalhe = newValue;
    	    	resultado.setVisible(false);
    	    }
    	});
    	busca.textProperty().addListener(new ChangeListener<String>() {
    	    @Override
    	    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
    	    	if(newValue.isEmpty()){
    	    		resultado.setVisible(false);
    	    		return;
    	    	}
    	    	if(canChange){
    	    		canChange = false;
    	    		return;
    	    	}
    	    	ObservableList<CADASTRO_MORADOR> resultadoList = FXCollections.observableList(pCrud.searchMorador(newValue));
    	    	if(resultadoList.isEmpty()){
    	    		resultado.setVisible(false);
    	    		return;
    	    	}
    	    	resultado.setVisible(true);
    	        resultado.setItems(resultadoList);
    	    }
    	});
    	
    	busca.setOnKeyPressed(new EventHandler<KeyEvent>()
    		    {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.DOWN)){
                	resultado.requestFocus();
                }
            }
        });
    	
    	resultado.setOnKeyPressed(new EventHandler<KeyEvent>()
    		    {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.DOWN)){
                	navegando = true;
                }
                if (ke.getCode().equals(KeyCode.ENTER)){
                	navegando = false;
                	busca.setText(resultado.getSelectionModel().getSelectedItem().toString());
                	preencherDetalhes(resultado.getSelectionModel().getSelectedItem());
                	detalhe = resultado.getSelectionModel().getSelectedItem();
                	resultado.setVisible(false);
                }
            }
        });
    }  
    
    private void preencherDetalhes(CADASTRO_MORADOR value) {
    	String tipoMorador = pCrud.getTipoMorador(value.getTipo_morador()).getDescricao();
    	List<TELEFONES> telefones = pCrud.getTelefonesByIdMorador(value.getId());
    	List<VEICULOS> veiculos = pCrud.getVeiculosByMorador(value.getIdentificador());
    	List<ANIMAIS> animais = pCrud.getAnimaisByMorador(value.getIdentificador());
    	
    	StringBuilder content = new StringBuilder();
    	content.append("Nome: "+value.getNome()+ "\n");
    	content.append("Casa: "+ value.getIdentificador()+"\n");
    	content.append("Residente: "+ tipoMorador+"\n");
    	content.append("E-mail: "+ value.getEmail()+"\n");
    	for (TELEFONES tel : telefones) {
    		content.append("Telefone: "+ tel.getNumero()+"\n");			
		}
    	content.append("\n");
    	for (VEICULOS vei : veiculos) {
    		content.append("Veiculo: "+ vei.getModelo()+" Placa: "+vei.getPlaca()+"\n");			
		}
    	content.append("\n");
    	for (ANIMAIS anim : animais) {
    		content.append("Animal: "+ anim+"\n");			
		}
    	content.append("\n OBSERVAÇÕES: "+ value.getObservacao()+"\n");
    	
		detalhes.setText(content.toString());
	}
    
    public void limpar(ActionEvent ev){
    	busca.setText("");
    	detalhes.setText("");
    }
    
    public void acesso(ActionEvent ev){
    	//acessar
    }
    
    public void editar(ActionEvent ev){
    	main.cadastro.values().iterator().next().editar(detalhe);
    	main.busca.setVisible(false);
    	main.cadastro.keySet().iterator().next().setVisible(true);
    	limpar(ev);
    }    

    public void setApp(Condominio main) {
        this.main = main;       
    }    
}
