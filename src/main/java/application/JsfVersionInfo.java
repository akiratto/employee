/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Owner
 */
@Named
@RequestScoped
public class JsfVersionInfo
{
  public String getVersion()
  {
    return FacesContext.class.getPackage().getImplementationVersion();
  }
}
