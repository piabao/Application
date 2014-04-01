/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.server.modelo.dao;

import condominio.server.modelo.TIPO_MORADOR;
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
public class TipoMoradorJpaController implements Serializable {

    public TipoMoradorJpaController() {
         emf = Persistence.createEntityManagerFactory("CondominioPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TIPO_MORADOR TIPO_MORADOR) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(TIPO_MORADOR);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TIPO_MORADOR TIPO_MORADOR) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TIPO_MORADOR = em.merge(TIPO_MORADOR);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = TIPO_MORADOR.getId();
                if (findTIPO_MORADOR(id) == null) {
                    throw new NonexistentEntityException("The tIPO_MORADOR with id " + id + " no longer exists.");
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
            TIPO_MORADOR TIPO_MORADOR;
            try {
                TIPO_MORADOR = em.getReference(TIPO_MORADOR.class, id);
                TIPO_MORADOR.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The TIPO_MORADOR with id " + id + " no longer exists.", enfe);
            }
            em.remove(TIPO_MORADOR);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TIPO_MORADOR> findTIPO_MORADOREntities() {
        return findTIPO_MORADOREntities(true, -1, -1);
    }

    public List<TIPO_MORADOR> findTIPO_MORADOREntities(int maxResults, int firstResult) {
        return findTIPO_MORADOREntities(false, maxResults, firstResult);
    }

    private List<TIPO_MORADOR> findTIPO_MORADOREntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TIPO_MORADOR.class));
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

    public TIPO_MORADOR findTIPO_MORADOR(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TIPO_MORADOR.class, id);
        } finally {
            em.close();
        }
    }

    public int getTIPO_MORADORCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TIPO_MORADOR> rt = cq.from(TIPO_MORADOR.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
