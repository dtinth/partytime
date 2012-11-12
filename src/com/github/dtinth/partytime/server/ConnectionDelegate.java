/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.dtinth.partytime.server;

/**
 *
 * @author Thai Pangsakulyanont
 */
public interface ConnectionDelegate {
    
    void closed(Connection connection);
    void statusChanged(Connection connection);
    
}
