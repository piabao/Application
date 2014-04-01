/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.server.modelo.dao;

import condominio.server.modelo.OPERADORA;
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
 * @author Marlon Harnisch
 */
public class OperadoraJpaController implements Serializable {

    public OperadoraJpaController() {
         emf = Persistence.createEntityManagerFactory("CondominioPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(OPERADORA OPERADORA) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(OPERADORA);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(OPERADORA OPERADORA) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OPERADORA = em.merge(OPERADORA);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = OPERADORA.getId();
                if (findOperadora(id) == null) {
                    throw new NonexistentEntityException("The operadora with id " + id + " no longer exists.");
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
            OPERADORA OPERADORA;
            try {
            	OPERADORA = em.getReference(OPERADORA.class, id);
            	OPERADORA.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The operadora with id " + id + " no longer exists.", enfe);
            }
            em.remove(OPERADORA);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<OPERADORA> findOperadoraEntities() {
        return findOperadoraEntities(true, -1, -1);
    }

    public List<OPERADORA> findOperadoraEntities(int maxResults, int firstResult) {
        return findOperadoraEntities(false, maxResults, firstResult);
    }

    private List<OPERADORA> findOperadoraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(OPERADORA.class));
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

    public OPERADORA findOperadora(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OPERADORA.class, id);
        } finally {
            em.close();
        }
    }

    public int getOperadoraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<OPERADORA> rt = cq.from(OPERADORA.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }    
}
