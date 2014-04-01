/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.server.modelo.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import condominio.server.modelo.CADASTRO_MORADOR;
import condominio.server.modelo.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author Marlon Harnisch
 */
public class CadastroMoradorJpaController implements Serializable {
	
    public CadastroMoradorJpaController() {
        
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
    	emf = Persistence.createEntityManagerFactory("CondominioPU");
        return emf.createEntityManager();
    }

    public CADASTRO_MORADOR create(CADASTRO_MORADOR CADASTRO_MORADOR) {
        EntityManager em = null;
        CADASTRO_MORADOR obj;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            obj = em.merge(CADASTRO_MORADOR);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
		return obj;
    }

    public void edit(CADASTRO_MORADOR CADASTRO_MORADOR) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CADASTRO_MORADOR = em.merge(CADASTRO_MORADOR);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = CADASTRO_MORADOR.getId();
                if (findCADASTRO_MORADOR(id) == null) {
                    throw new NonexistentEntityException("The cADASTRO_MORADOR with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CADASTRO_MORADOR CADASTRO_MORADOR;
            try {
                CADASTRO_MORADOR = em.getReference(CADASTRO_MORADOR.class, id);
                CADASTRO_MORADOR.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The CADASTRO_MORADOR with id " + id + " no longer exists.", enfe);
            }
            em.remove(CADASTRO_MORADOR);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CADASTRO_MORADOR> findCADASTRO_MORADOREntities() {
        return findCADASTRO_MORADOREntities(true, -1, -1);
    }

    public List<CADASTRO_MORADOR> findCADASTRO_MORADOREntities(int maxResults, int firstResult) {
        return findCADASTRO_MORADOREntities(false, maxResults, firstResult);
    }

    private List<CADASTRO_MORADOR> findCADASTRO_MORADOREntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CADASTRO_MORADOR.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public CADASTRO_MORADOR findCADASTRO_MORADOR(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CADASTRO_MORADOR.class, id);
        } finally {
            em.close();
        }
    }

    public int getCADASTRO_MORADORCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CADASTRO_MORADOR> rt = cq.from(CADASTRO_MORADOR.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<CADASTRO_MORADOR> searchMorador(String value) {    	
    	EntityManager em = getEntityManager();
        try {
        		em.getTransaction().begin(); 
        		Connection cnt = em.unwrap(Connection.class);
        		
            Query query = em.createNativeQuery("SELECT * FROM cadastro_morador WHERE((NOME LIKE ?) OR (IDENTIFICADOR LIKE ?) OR (CPF LIKE ?))", CADASTRO_MORADOR.class);
            query.setParameter(1, value +"%");
            query.setParameter(2, value +"%");
            query.setParameter(3, value +"%");
            List<CADASTRO_MORADOR> mor =query.getResultList();
            cnt.close();
            return mor;          
        } catch (SQLException e) {
        	return new ArrayList<CADASTRO_MORADOR>();
        	//e.printStackTrace();
		} finally {
        	em.close();
        	emf.close();
        }
		
    }
    
}
