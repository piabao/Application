/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.core.portaria;

import java.util.ArrayList;
import java.util.List;

import condominio.server.modelo.ANIMAIS;
import condominio.server.modelo.CADASTRO_MORADOR;
import condominio.server.modelo.MODELO;
import condominio.server.modelo.OPERADORA;
import condominio.server.modelo.TELEFONES;
import condominio.server.modelo.TIPO_MORADOR;
import condominio.server.modelo.VEICULOS;
import condominio.server.modelo.dao.AnimaisJpaController;
import condominio.server.modelo.dao.CadastroMoradorJpaController;
import condominio.server.modelo.dao.ModeloJpaController;
import condominio.server.modelo.dao.OperadoraJpaController;
import condominio.server.modelo.dao.TelefonesJpaController;
import condominio.server.modelo.dao.TipoMoradorJpaController;
import condominio.server.modelo.dao.VeiculosJpaController;
import condominio.server.modelo.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author Marlon Harnisch
 */
public class PortariaCrud {
    
    private CadastroMoradorJpaController cadastro;
    private AnimaisJpaController animais;
    private VeiculosJpaController veiculos;
    private TelefonesJpaController telefones;
    private TipoMoradorJpaController tipoMorador;
    private OperadoraJpaController operadora;
    private ModeloJpaController modelo;
    
    public PortariaCrud(){
        cadastro = new CadastroMoradorJpaController();
        animais = new AnimaisJpaController();
        veiculos = new VeiculosJpaController();
        telefones = new TelefonesJpaController();
        tipoMorador = new TipoMoradorJpaController();
        operadora = new OperadoraJpaController();
        modelo = new ModeloJpaController();
    }
    
    public CADASTRO_MORADOR cadastrarMorador(CADASTRO_MORADOR valores){
    	CADASTRO_MORADOR morador = cadastro.create(valores);
        return morador == null ? new CADASTRO_MORADOR() : morador;
    }
    
    public void cadastrarTelefones(TELEFONES valores){
        telefones.create(valores);
    }
    
    public void cadastrarVeiculos(VEICULOS valores){
        veiculos.create(valores);
    }
    
    public void cadastrarAnimais(ANIMAIS valores){
        animais.create(valores);
    }
    
    public void cadastrarTipoMorador(TIPO_MORADOR valores){
        tipoMorador.create(valores);
    }

	public List<TIPO_MORADOR> getAllTipoMorador() {
		List<TIPO_MORADOR> moradores = tipoMorador.findTIPO_MORADOREntities();
		return moradores == null ? new ArrayList<TIPO_MORADOR>() : moradores;
	}

	public List<VEICULOS> getVeiculosByMorador(String morador) {
		List<VEICULOS> veiculo = veiculos.findVeiculosByMorador(morador);
		return veiculo == null ? new ArrayList<VEICULOS>() : veiculo;
	}

	public List<ANIMAIS> getAnimaisByMorador(String morador) {
		List<ANIMAIS> animal = animais.findAnimaisByMorador(morador);
		return animal == null ? new ArrayList<ANIMAIS>() : animal;
	}

	public List<TELEFONES> getTelefonesByIdMorador(Long moradorID) {
		List<TELEFONES> telefone= telefones.findTelefonesByIdMorador(moradorID);                
		return telefone == null ? new ArrayList<TELEFONES>() : telefone;
	}

	public List<OPERADORA> getOperadora() {
		List<OPERADORA> opera = operadora.findOperadoraEntities();
		return opera == null ? new ArrayList<OPERADORA>() : opera;
	}
    
    public List<CADASTRO_MORADOR> searchMorador(String value){
    	List<CADASTRO_MORADOR> moradores=  cadastro.searchMorador(value);
    	return moradores == null ? new ArrayList<CADASTRO_MORADOR>() : moradores;
    }

	public TIPO_MORADOR getTipoMorador(Long tipo_morador) {	
		TIPO_MORADOR moradores = tipoMorador.findTIPO_MORADOR(tipo_morador);
		return moradores == null ? new TIPO_MORADOR() : moradores;
	}

	public List<MODELO> searchModelo(String newValue) {		
		return modelo.searchModelo(newValue);
	}

	public boolean removerMorador(CADASTRO_MORADOR moradorEmEdicao) {
		try {
			cadastro.destroy(moradorEmEdicao.getId());
			telefones.destroy(moradorEmEdicao.getId());
			if(cadastro.searchMorador(moradorEmEdicao.getIdentificador()).size() == 0){
				veiculos.destroy(moradorEmEdicao.getIdentificador());	
				animais.destroy(moradorEmEdicao.getIdentificador());
			}
		} catch (NonexistentEntityException e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void removeAnimal(Long id) {
		try {
			animais.destroy(id);
		} catch (NonexistentEntityException e) {
			e.printStackTrace();
		}
		
	}

	public void removeTelefone(Long id) {
		try {
			telefones.remove(id);
		} catch (NonexistentEntityException e) {
			e.printStackTrace();
		}
	}

	public void removeVeiculo(Long id) {
		try {
			veiculos.destroy(id);
		} catch (NonexistentEntityException e) {
			e.printStackTrace();
		}
	}
    
    
    
    
}
