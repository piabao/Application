/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.core.portaria.modelo;

import javafx.scene.control.Control;

/**
 *
 * @author Longa
 */
public class Animal extends Control{
    private String idMorador;
    private String nome;
    private String raca;
    private String cor;
    private String porte;

    public String getIdMorador() {
        return idMorador;
    }

    public void setIdMorador(String idMorador) {
        this.idMorador = idMorador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }
    
    
}
