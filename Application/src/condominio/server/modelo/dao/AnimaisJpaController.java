/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.server.modelo.dao;

import condominio.server.modelo.ANIMAIS;
import condominio.server.modelo.CADASTRO_MORADOR;
import condominio.server.modelo.dao.exceptions.NonexistentEntityException;

import java.io.Serializable;
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
 * @author Longa
 */
public class AnimaisJpaController implements Serializable {

    public AnimaisJpaController() {
         emf = Persistence.createEntityManagerFactory("CondominioPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ANIMAIS ANIMAIS) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(ANIMAIS);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ANIMAIS ANIMAIS) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ANIMAIS = em.merge(ANIMAIS);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = ANIMAIS.getId();
                if (findANIMAIS(id) == null) {
                    throw new NonexistentEntityException("The aNIMAIS with id " + id + " no longer exists.");
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
            ANIMAIS ANIMAIS;
            try {
            	ANIMAIS = em.getReference(ANIMAIS.class, id);
                ANIMAIS.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ANIMAIS with id " + id + " no longer exists.", enfe);
            }
            em.remove(ANIMAIS);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void destroy(String moradorId){
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ANIMAIS> ANIMAIS = findAnimaisByMorador(moradorId);
            for (ANIMAIS animais2 : ANIMAIS) {
            	em.remove(animais2);
			}
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ANIMAIS> findANIMAISEntities() {
        return findANIMAISEntities(true, -1, -1);
    }

    public List<ANIMAIS> findANIMAISEntities(int maxResults, int firstResult) {
        return findANIMAISEntities(false, maxResults, firstResult);
    }

    private List<ANIMAIS> findANIMAISEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ANIMAIS.class));
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

    public ANIMAIS findANIMAIS(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ANIMAIS.class, id);
        } finally {
            em.close();
        }
    }

    public int getANIMAISCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ANIMAIS> rt = cq.from(ANIMAIS.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
   
	public List<ANIMAIS> findAnimaisByMorador(String morador) {
		EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createNativeQuery("SELECT * FROM animais WHERE IDMORADOR = ?", ANIMAIS.class);
            query.setParameter(1, morador);
            return query.getResultList();
        } finally {
            em.close();
        }
	}
    
}
