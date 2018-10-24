package com.unisys.br.amsfw.dao.util.service.email;

import javax.ejb.Stateless;

/**
 * Serviço de email do framework amsfw.
 * 
 * @author delfimsm
 * 
 */
@Stateless
/* =========================================================================
 *                 I   M   P   O   R   T   A   N   T   E 
 * =========================================================================
 * (Reflexos nos iTests)
 * É a mesma classe do Email service, mas implementando uma interface Local.
 * =========================================================================
 */
public class EmailServiceImpl extends EmailService implements EmailServiceLocal {

}
