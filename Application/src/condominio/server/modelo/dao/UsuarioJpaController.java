/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.server.modelo.dao;

import condominio.server.modelo.TIPO_MORADOR;
import condominio.server.modelo.USUARIO;
import condominio.server.modelo.dao.exceptions.NonexistentEntityException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Marlon Harnisch
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController() {
         emf = Persistence.createEntityManagerFactory("CondominioPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public USUARIO create(USUARIO USUARIO) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(USUARIO);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return USUARIO;
    }

    public void edit(USUARIO USUARIO) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            USUARIO = em.merge(USUARIO);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = USUARIO.getId();
                if (findUSUARIO(id) == null) {
                    throw new NonexistentEntityException("The USUARIO with id " + id + " no longer exists.");
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
            USUARIO USUARIO;
            try {
                USUARIO = em.getReference(USUARIO.class, id);
                USUARIO.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The USUARIO with id " + id + " no longer exists.", enfe);
            }
            em.remove(USUARIO);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<USUARIO> findUSUARIOEntities() {
        return findUSUARIOEntities(true, -1, -1);
    }

    public List<USUARIO> findUSUARIOEntities(int maxResults, int firstResult) {
        return findUSUARIOEntities(false, maxResults, firstResult);
    }

    private List<USUARIO> findUSUARIOEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(USUARIO.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList().size() == 0 ? new ArrayList<USUARIO>() : q.getResultList();
        } finally {
            em.close();
        }
    }

    public USUARIO findUSUARIO(String user) {
        EntityManager em = getEntityManager();
        try {
        	 em.getTransaction().begin();
        	 Query query = em.createNativeQuery("SELECT * FROM usuario WHERE USER = ?", USUARIO.class);
        	 query.setParameter(1, user);
             return (USUARIO) query.getSingleResult();
        } catch(Exception e){
        	return new USUARIO();
        }finally {
            em.close();
        }
    }
    
    private Object findUSUARIO(Long id) {
    	EntityManager em = getEntityManager();
        try {
            return em.find(USUARIO.class, id);
        } finally {
            em.close();
        }
	}

    public int getUSUARIOCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<USUARIO> rt = cq.from(USUARIO.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }    
}
