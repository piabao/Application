/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.server.modelo.dao;

import condominio.server.modelo.PRIVILEGIOS;
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
public class PrivilegiosJpaController implements Serializable {

    public PrivilegiosJpaController() {
         emf = Persistence.createEntityManagerFactory("CondominioPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(List<PRIVILEGIOS> prvList) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            for (PRIVILEGIOS privilegios : prvList) {
            	em.merge(privilegios);
                em.flush();
                em.clear();
			}
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PRIVILEGIOS PRIVILEGIOS) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PRIVILEGIOS = em.merge(PRIVILEGIOS);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = PRIVILEGIOS.getId();
                if (findPRIVILEGIOS(id) == null) {
                    throw new NonexistentEntityException("The PRIVILEGIOS with id " + id + " no longer exists.");
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
            PRIVILEGIOS PRIVILEGIOS;
            try {
                PRIVILEGIOS = em.getReference(PRIVILEGIOS.class, id);
                PRIVILEGIOS.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The PRIVILEGIOS with id " + id + " no longer exists.", enfe);
            }
            em.remove(PRIVILEGIOS);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PRIVILEGIOS> findPRIVILEGIOSEntities() {
        return findPRIVILEGIOSEntities(true, -1, -1);
    }

    public List<PRIVILEGIOS> findPRIVILEGIOSEntities(int maxResults, int firstResult) {
        return findPRIVILEGIOSEntities(false, maxResults, firstResult);
    }

    private List<PRIVILEGIOS> findPRIVILEGIOSEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PRIVILEGIOS.class));
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

    public PRIVILEGIOS findPRIVILEGIOS(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PRIVILEGIOS.class, id);
        } finally {
            em.close();
        }
    }

    public int getPRIVILEGIOSCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PRIVILEGIOS> rt = cq.from(PRIVILEGIOS.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
   
	public List<PRIVILEGIOS> findPrivilegiosByUser(Long id) {
		EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createNativeQuery("SELECT * FROM privilegios WHERE IDUSUARIO = ?", PRIVILEGIOS.class);
            query.setParameter(1, id);
            return query.getResultList();
        } finally {
            em.close();
        }
	}
    
}
