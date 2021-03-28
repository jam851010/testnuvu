/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaime.testnuvu.cliente;

import org.joda.time.DateTime;

/**
 *
 * @author jaimeMejia
 */
public class Usuario {

    private Long id;
    private String usuario;
    private String password;
    private String token;
    private DateTime ultimoAcceso;
    private DateTime vigencia;

    public Usuario(Long id, String usuario, String password, DateTime ultimoAcceso, DateTime vigencia) {
        this.id = id;
        this.usuario = usuario;
        this.password = password;
        this.ultimoAcceso = ultimoAcceso;
        this.vigencia = vigencia;
    }

    public Usuario() {
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the ultimoAcceso
     */
    public DateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    /**
     * @param ultimoAcceso the ultimoAcceso to set
     */
    public void setUltimoAcceso(DateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    /**
     * @return the vigencia
     */
    public DateTime getVigencia() {
        return vigencia;
    }

    /**
     * @param vigencia the vigencia to set
     */
    public void setVigencia(DateTime vigencia) {
        this.vigencia = vigencia;
    }

    public String validate() {
        if (isNotEmpty(usuario) && isNotEmpty(password)) {
            return null;
        }
        return "Fields usuario, password cannot be empty";
    }

    static boolean isNotEmpty(String field) {
        return field != null && field.trim().length() > 0;
    }

}
