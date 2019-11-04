/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Owner
 */
@Named
@RequestScoped
public class Info
{
  public String getVersion()
  {
    return FacesContext.class.getPackage().getImplementationVersion();
  }
}
