/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.converter;

import java.io.Serializable;

/**
 *
 * @author Owner
 */
public interface JsfEntityAndTableEntityConverter<JE extends Serializable, TE extends Serializable> {
     public TE toTableEntity(JE jsfEntity);
     public JE toJsfEntity(TE tableEntity);
}
