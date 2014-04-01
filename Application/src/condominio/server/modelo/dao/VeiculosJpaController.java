/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.server.modelo.dao;

import condominio.server.modelo.CADASTRO_MORADOR;
import condominio.server.modelo.TELEFONES;
import condominio.server.modelo.VEICULOS;
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
public class VeiculosJpaController implements Serializable {

    public VeiculosJpaController() {
         emf = Persistence.createEntityManagerFactory("CondominioPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VEICULOS VEICULOS) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(VEICULOS);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VEICULOS VEICULOS) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VEICULOS = em.merge(VEICULOS);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = VEICULOS.getId();
                if (findVEICULOS(id) == null) {
                    throw new NonexistentEntityException("The vEICULOS with id " + id + " no longer exists.");
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
            VEICULOS VEICULOS;
            try {
            	VEICULOS = em.getReference(VEICULOS.class, id);
            	VEICULOS.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The VEICULOS with id " + id + " no longer exists.", enfe);
            }
            em.remove(VEICULOS);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    public void destroy(String moradorID){
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<VEICULOS> VEICULOS = findVeiculosByMorador(moradorID);
            for (VEICULOS veiculo : VEICULOS) {
            	em.remove(veiculo);				
			}
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VEICULOS> findVEICULOSEntities() {
        return findVEICULOSEntities(true, -1, -1);
    }

    public List<VEICULOS> findVEICULOSEntities(int maxResults, int firstResult) {
        return findVEICULOSEntities(false, maxResults, firstResult);
    }

    private List<VEICULOS> findVEICULOSEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VEICULOS.class));
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

    public VEICULOS findVEICULOS(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VEICULOS.class, id);
        } finally {
            em.close();
        }
    }

    public int getVEICULOSCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VEICULOS> rt = cq.from(VEICULOS.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

	public List<VEICULOS> findVeiculosByMorador(String moradorID) {
		EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createNativeQuery("SELECT * FROM veiculos WHERE IDMORADOR = ?", VEICULOS.class);
            query.setParameter(1, moradorID);
            return query.getResultList();
        } finally {
            em.close();
        }
	}    
}
