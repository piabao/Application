/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.server.modelo.dao;

import condominio.server.modelo.CADASTRO_MORADOR;
import condominio.server.modelo.TELEFONES;
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
public class TelefonesJpaController implements Serializable {

    public TelefonesJpaController() {
        emf = Persistence.createEntityManagerFactory("CondominioPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TELEFONES TELEFONES) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(TELEFONES);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TELEFONES TELEFONES) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TELEFONES = em.merge(TELEFONES);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = TELEFONES.getId();
                if (findTELEFONES(id) == null) {
                    throw new NonexistentEntityException("The tELEFONES with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void remove(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TELEFONES TELEFONES;
            try {
            	TELEFONES = em.getReference(TELEFONES.class, id);
            	TELEFONES.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The TELEFONES with id " + id + " no longer exists.", enfe);
            }
            em.remove(TELEFONES);
            em.getTransaction().commit();
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
            List<TELEFONES> TELEFONES = findTelefonesByIdMorador(id);
            for (TELEFONES telefone : TELEFONES) {
            	em.remove(telefone);				
			}
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TELEFONES> findTELEFONESEntities() {
        return findTELEFONESEntities(true, -1, -1);
    }

    public List<TELEFONES> findTELEFONESEntities(int maxResults, int firstResult) {
        return findTELEFONESEntities(false, maxResults, firstResult);
    }

    private List<TELEFONES> findTELEFONESEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TELEFONES.class));
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

    public TELEFONES findTELEFONES(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TELEFONES.class, id);
        } finally {
            em.close();
        }
    }

    public int getTELEFONESCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TELEFONES> rt = cq.from(TELEFONES.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

	public List<TELEFONES> findTelefonesByIdMorador(Long moradorID) {
		EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createNativeQuery("SELECT * FROM telefones WHERE IDMORADOR = ?", TELEFONES.class);
            query.setParameter(1, moradorID);
            return query.getResultList();
        } finally {
            em.close();
        }
	}
    
}
