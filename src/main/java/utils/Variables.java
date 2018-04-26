/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Albert
 */
public class Variables {
    //Map amb els usuaris que es troben a la sessió de connexió al servidor
    private static Map<String, String> usuarisSessio = new HashMap<>();
    
    public void addUserSession(String login, String token){
        usuarisSessio.put(login,token);
    }
    
    public void removeUserSession(String login, String token){
        usuarisSessio.remove(login,token);
    }
    
    public boolean checkToken (String token){
        return usuarisSessio.containsValue(token); //nombreMap.get(K clave);
    }
}
