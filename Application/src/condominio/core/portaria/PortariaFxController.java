/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.core.portaria;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Dialogs.DialogOptions;
import javafx.scene.control.Dialogs.DialogResponse;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import condominio.Condominio;
import condominio.core.login.Privilegios;
import condominio.server.modelo.ANIMAIS;
import condominio.server.modelo.CADASTRO_MORADOR;
import condominio.server.modelo.TELEFONES;
import condominio.server.modelo.TIPO_MORADOR;
import condominio.server.modelo.VEICULOS;

/**
 *
 * @author Marlon Harnisch
 */
public class PortariaFxController implements Initializable{
    
    @FXML
    TextField identificacao;
    @FXML
    ComboBox<TIPO_MORADOR> tipo;
    @FXML
    Label nomeLabel;
    @FXML
    TextField nome;
    @FXML
    TextField cpf;
    @FXML
    ListView<TELEFONES> telefoneListView;
    @FXML
    TextField email;
    @FXML
    ImageView foto;
    @FXML
    TextField vaga;
    @FXML
    ListView<VEICULOS> veiculosListView;
    @FXML
    ListView<ANIMAIS> animaisListView;    
    @FXML
    Button CadAnimais;
    @FXML
    Button CadVeiculos;
    @FXML
    Button CadTelefones;
    @FXML
    Button ProcFoto;
    @FXML
    Button cadastrar;
    @FXML
    Button limpar;
    @FXML
    Button remover;
    @FXML
    TextArea observacoes;
        
    private PortariaCrud pCrud = new PortariaCrud();
    private Map<Long, ANIMAIS> animaisMap = new HashMap<Long, ANIMAIS>();
    private Map<Long, VEICULOS> veiculosMap = new HashMap<Long, VEICULOS>();
    private Map<Long, TELEFONES> telefonesMap = new HashMap<Long, TELEFONES>();
    private Condominio main;
    
    private VEICULOS veiculoEmEdicao;
    private TELEFONES telefoneEmEdicao;
    private ANIMAIS animalEmEdicao;
	private CADASTRO_MORADOR moradorEmEdicao;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
    	remover.setDisable(true);
    	buildTipoComboBox();    	
    	identificacao.textProperty().addListener(new ChangeListener<String>() {
    	    @Override
    	    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
    	    	buildAnimaisList(newValue);
    	    	buildVeiculosList(newValue);
    	    }
    	});
    }    
    
    public void configuraPermissoes() {
		Map<String, Boolean> permissoes = Privilegios.getPermissoes();
    	if(!permissoes.get(Privilegios.EDITA_CADASTRO)){
    		cadastrar.setDisable(true);
    	}
	}

	private void buildVeiculosList(String value) {
		veiculosMap.clear();
    	for(VEICULOS v : pCrud.getVeiculosByMorador(value)){
    		veiculosMap.put(v.getId(), v);
    	}
    	ObservableList<VEICULOS> veiculos = FXCollections.observableList(new ArrayList<VEICULOS>(veiculosMap.values()));
		veiculosListView.setItems(veiculos);
	}

	private void buildAnimaisList(String value) {
		animaisMap.clear();
		for (ANIMAIS anm : pCrud.getAnimaisByMorador(value)) {
			animaisMap.put(anm.getId(), anm);			
		}
		ObservableList<ANIMAIS> animais = FXCollections.observableList(new ArrayList<ANIMAIS>(animaisMap.values()));
		animaisListView.setItems(animais);
	}

	private void buildTelefoneList(Long moradorID) {
		ObservableList<TELEFONES> telefones = FXCollections.observableList(
				pCrud.getTelefonesByIdMorador(moradorID));	
		telefoneListView.setItems(telefones);
	}

	private void buildTipoComboBox() {
		ObservableList<TIPO_MORADOR> tipoMorador = FXCollections.observableList(pCrud.getAllTipoMorador());	
		tipo.setItems(tipoMorador);
    }

	public void cadastrar(ActionEvent e){
        CADASTRO_MORADOR morador = new CADASTRO_MORADOR();
        if(identificacao.getText().isEmpty() || nome.getText().isEmpty()){
        	Dialogs.showErrorDialog(getStage(), "Preencha o campo IdentificaÁ„o e Nome");
        	return;
        }
        if(moradorEmEdicao!= null){
        	morador.setId(moradorEmEdicao.getId());
        }
        morador.setIdentificador(identificacao.getText());
        morador.setCpf(cpf.getText());
        morador.setEmail(email.getText());
        morador.setNome(nome.getText());
        morador.setVaga(vaga.getText());
        Long tipoMorador = tipo.getSelectionModel().getSelectedItem() == null ? 0 : tipo.getSelectionModel().getSelectedItem().getId();
        morador.setTipo_morador(tipoMorador);
        morador.setObservacao(observacoes.getText());

        try {       
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(SwingFXUtils.fromFXImage(foto.getImage(), null), "jpg", baos );
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			
			morador.setImage(imageInByte);
			baos.close();
        } catch (IOException e1) {
        	e1.printStackTrace();
        }
        
        CADASTRO_MORADOR idMorador = pCrud.cadastrarMorador(morador);
        
        mergeAnimais(idMorador);
        mergeVeiculos(idMorador);
        mergeTelefones(idMorador);
        
        if(idMorador!= null){
        	limpar();
        }        
        
        if(idMorador!=null){
        	Dialogs.showInformationDialog(getStage(), "Cadastro realizado com sucesso!");
        }
    }

	private void mergeVeiculos(CADASTRO_MORADOR idMorador) {
		for(VEICULOS vei : pCrud.getVeiculosByMorador(idMorador.getIdentificador())){
			boolean remove = true;
			for (VEICULOS veiculo : veiculosMap.values()) {
				//Se n„o encontrar na lista algum item ja salvo, remove
	        	if(veiculo.getId() != null && vei.getId().compareTo(veiculo.getId()) == 0){
	        		 remove = false;
	        		 continue;
	        	}
			}
	        if(remove){
	        	pCrud.removeVeiculo(vei.getId());
	        	
	        }
		}
		for (VEICULOS veiculo : veiculosMap.values()) {
			veiculo.setIdMorador(idMorador.getIdentificador());
			pCrud.cadastrarVeiculos(veiculo);
		}
	}

	private void mergeTelefones(CADASTRO_MORADOR idMorador) {
		for(TELEFONES tel : pCrud.getTelefonesByIdMorador(idMorador.getId())){
			boolean remove = true;
			for (TELEFONES telefone : telefonesMap.values()) {
				//Se n„o encontrar na lista algum item ja salvo, remove
	        	if(telefone.getId() != null && tel.getId().compareTo(telefone.getId()) == 0){
	        		 remove = false;
	        		 continue;
	        	}
			}
	        if(remove){
	        	pCrud.removeTelefone(tel.getId());
	        	
	        }
		}
		for (TELEFONES telefone : telefonesMap.values()) {
        	telefone.setIdMorador(idMorador.getId());
			pCrud.cadastrarTelefones(telefone);
		}
	}

	private void mergeAnimais(CADASTRO_MORADOR idMorador) {
		for(ANIMAIS anm : pCrud.getAnimaisByMorador(idMorador.getIdentificador())){
        	boolean remove = true;
        	for (ANIMAIS animal : animaisMap.values()) {
        		//Se n„o encontrar na lista algum item ja salvo, remove
	        	if(animal.getId() != null && anm.getId().compareTo(animal.getId()) == 0){
	        		 remove = false;
	        		 continue;
	        	}
			}
        	if(remove){
        		pCrud.removeAnimal(anm.getId());
        	}
        }
        for (ANIMAIS animal : animaisMap.values()) {
        	animal.setIdMorador(idMorador.getIdentificador());
        	pCrud.cadastrarAnimais(animal);        	
        }
	}
    
    public void abrirVeiculo(ActionEvent event){
        ((VeiculosFxController)abrirFXML(event, "CadastroVeiculos.fxml", "Cadastro de Ve√≠culos")).setApp(this);
    }
    
    public void editarVeiculo(ActionEvent event){
    	VEICULOS veic = veiculosListView.getSelectionModel().getSelectedItem();
    	veiculoEmEdicao = veic;
        ((VeiculosFxController)abrirFXML(event, "CadastroVeiculos.fxml", "Cadastro de Ve√≠culos")).editar(this, veic);
    }
    
    public void editarTelefone(ActionEvent event){
    	TELEFONES tel = telefoneListView.getSelectionModel().getSelectedItem();
    	telefoneEmEdicao = tel;
        ((TelefoneFxController)abrirFXML(event, "CadastroTelefones.fxml", "Adicionar Telefone")).editar(this,tel);
    }
    
    public void editarAnimal(ActionEvent event){
    	ANIMAIS anm = animaisListView.getSelectionModel().getSelectedItem();
    	animalEmEdicao = anm;
        ((AnimaisFxController)abrirFXML(event, "CadastroAnimais.fxml", "Cadastro de Animais")).editar(this,anm);
    }
    
    public void excluirVeiculo(ActionEvent event){
    	veiculosMap.remove(veiculosListView.getSelectionModel().getSelectedItem().getId());
    	veiculosListView.setItems(FXCollections.observableList(new ArrayList<VEICULOS>(veiculosMap.values())));
    }
    public void excluirTelefone(ActionEvent event){
    	telefonesMap.remove(telefoneListView.getSelectionModel().getSelectedItem().getId());
    	telefoneListView.setItems(FXCollections.observableList(new ArrayList<TELEFONES>(telefonesMap.values())));
    }
    public void excluirAnimal(ActionEvent event){
    	animaisMap.remove(animaisListView.getSelectionModel().getSelectedItem().getId());
    	animaisListView.setItems(FXCollections.observableList(new ArrayList<ANIMAIS>(animaisMap.values())));
    }
    
    public void abrirTelefone(ActionEvent event){
        ((TelefoneFxController)abrirFXML(event, "CadastroTelefones.fxml", "Adicionar Telefone")).setApp(this);
    }
    
    public void abrirAnimal(ActionEvent event){
        ((AnimaisFxController)abrirFXML(event, "CadastroAnimais.fxml", "Cadastro de Animais")).setApp(this);
    }
    
    public Initializable abrirFXML(ActionEvent event, String fxml, String titulo){
        try {  
            URL location = getClass().getResource(fxml);
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = (Parent) fxmlLoader.load(location.openStream());
            
            Stage animalStage = new Stage();       
            animalStage.setScene(new Scene(root));
            animalStage.setTitle(titulo);
            animalStage.initModality(Modality.WINDOW_MODAL);
            animalStage.initOwner(((Node)event.getSource()).getScene().getWindow());  
            animalStage.show();
            return (Initializable) fxmlLoader.getController();
            
        } catch (IOException ex) {
            Logger.getLogger(PortariaFxController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Initializable getLoader(FXMLLoader loader){
        return (Initializable) loader.getController();
    }
    
     public void concluirVeiculo(VEICULOS veic){
         veiculosMap.put(veic.getId(), veic);
         ObservableList<VEICULOS> model = FXCollections.observableList(new ArrayList<VEICULOS>(veiculosMap.values()));
         veiculosListView.setItems(FXCollections.observableArrayList(new ArrayList<VEICULOS>()));
         veiculosListView.setItems(model);
     }
     public void cancelarVeiculo(ActionEvent event){
         hide(event);
     }
     
     public void concluirTelefone(TELEFONES tel){
         telefonesMap.put(tel.getId(), tel);
         ObservableList<TELEFONES> model = FXCollections.observableList(new ArrayList<>(telefonesMap.values()));
         telefoneListView.setItems(FXCollections.observableArrayList(new ArrayList<TELEFONES>()));
         telefoneListView.setItems(model);
     }
     public void cancelarTelefone(ActionEvent event){        
          hide(event);
     }
     public void concluirAnimal(ANIMAIS anm){         
         animaisMap.put(anm.getId(), anm);
         ObservableList<ANIMAIS> anmModel = FXCollections.observableList(new ArrayList<>(animaisMap.values()));
         animaisListView.setItems(FXCollections.observableArrayList(new ArrayList<ANIMAIS>()));
         animaisListView.setItems(anmModel);
     }
     public void cancelarAnimal(ActionEvent event){
          hide(event);
     }

    private void hide(ActionEvent event) {
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
    public void cancelar(ActionEvent event) {
        limpar();
    }
    
    public void onIdentificationWrite(ActionEvent ev){
    	
    }
    
    public void procurarImagem(ActionEvent ev){
    	FileChooser fileChooser = new FileChooser();
        
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        FileChooser.ExtensionFilter extFilterBMP = new FileChooser.ExtensionFilter("BMP files (*.bmp)", "*.BMP");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG, extFilterBMP);
          
        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        String path = file.getAbsolutePath();
        
        try {
        	InputStream inputStream = new FileInputStream(path);
            Image image = new Image(inputStream, 
            		foto.getFitWidth(),    //requestedWidth
            		foto.getFitHeight(),    //requestedHeigh
                                true,   //preserveRatio
                                true);  //smooth
            foto.setImage(image);
        } catch (IOException ex) {
            Logger.getLogger(PortariaFxController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void limpar(){
    	identificacao.setText("");
        tipo.getSelectionModel().select(-1);
        nome.setText("");
        cpf.setText("");
        email.setText("");
        foto.setImage(new Image("condominio/core/images/unknown_user.jpg"));
        vaga.setText("");
        veiculosListView.setItems(FXCollections.observableList(new ArrayList<VEICULOS>()));
        animaisListView.setItems(FXCollections.observableList(new ArrayList<ANIMAIS>()));
        telefoneListView.setItems(FXCollections.observableList(new ArrayList<TELEFONES>()));
        telefonesMap.clear();
        veiculosMap.clear();
        animaisMap.clear();
        observacoes.setText("");
        moradorEmEdicao = null;
    }
    

    public void remover(ActionEvent ev){
    	DialogResponse response = Dialogs.showConfirmDialog(getStage(), "Tem certeza que deseja remover este morador?", "Remover morador", "Cadastro", DialogOptions.YES_NO);
    	if(response.equals(DialogResponse.NO)){
    		return;
    	}
    	if(!pCrud.removerMorador(moradorEmEdicao)){
    		Dialogs.showErrorDialog(getStage(), "Erro ao excluir!");
    	}
    	Dialogs.showInformationDialog(getStage(),  moradorEmEdicao.getNome()+" Excluido com sucesso!");
    	remover.setDisable(true);
    	limpar();
    }
    
    public void editar(CADASTRO_MORADOR detalhe) {
    	remover.setDisable(false);
    	moradorEmEdicao = detalhe;
    	identificacao.setText(detalhe.getIdentificador());
    	tipo.getSelectionModel().clearSelection();	
        tipo.getSelectionModel().select(pCrud.getTipoMorador(detalhe.getTipo_morador()));
        nome.setText(detalhe.getNome());
        cpf.setText(detalhe.getCpf());
        telefoneListView.setItems(FXCollections.observableList(pCrud.getTelefonesByIdMorador(detalhe.getId())));
        email.setText(detalhe.getEmail());
        foto.setImage(new Image("condominio/core/images/unknown_user.jpg"));
        vaga.setText(detalhe.getVaga());
        List<ANIMAIS> anm = pCrud.getAnimaisByMorador(detalhe.getIdentificador());
        List<TELEFONES> tel = pCrud.getTelefonesByIdMorador(detalhe.getId());
        List<VEICULOS> vei = pCrud.getVeiculosByMorador(detalhe.getIdentificador());
        for(ANIMAIS a : anm){
        	animaisMap.put(a.getId(), a);
        }
        for(VEICULOS v : vei){
        	veiculosMap.put(v.getId(), v);
        }
        for(TELEFONES t : tel){
        	telefonesMap.put(t.getId(), t);
        }
        
        telefoneListView.setItems(FXCollections.observableList(tel));
        veiculosListView.setItems(FXCollections.observableList(vei));
        animaisListView.setItems(FXCollections.observableList(anm));
        
        observacoes.setText(detalhe.getObservacao());
        try {
        	InputStream in = new ByteArrayInputStream(detalhe.getImage());
        	BufferedImage bImageFromConvert;
			bImageFromConvert = ImageIO.read(in);
			foto.setImage(SwingFXUtils.toFXImage(bImageFromConvert, null));
        } catch (IOException e) {
        	e.printStackTrace();
        }        
    }
    
    public void webCam(ActionEvent ev){ 
    	Webcam webcam = Webcam.getDefault();
    	WebcamPanel wp = new WebcamPanel(webcam);    	
    	webcam.open();
		Image image = SwingFXUtils.toFXImage(webcam.getImage(), null);
    	foto.setImage(image);
		webcam.close();
    }
    
    public Stage getStage(){
    	return main.getStage();
    }

    public void setApp(Condominio main) {
        this.main = main; 
    }
    
}
