/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.server.modelo.dao;

import condominio.server.modelo.CADASTRO_MORADOR;
import condominio.server.modelo.MODELO;
import condominio.server.modelo.dao.exceptions.NonexistentEntityException;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
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
public class ModeloJpaController implements Serializable {

    public ModeloJpaController() {
         emf = Persistence.createEntityManagerFactory("CondominioPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }   

    public List<MODELO> findMODELOEntities() {
        return findMODELOEntities(true, -1, -1);
    }

    public List<MODELO> findMODELOEntities(int maxResults, int firstResult) {
        return findMODELOEntities(false, maxResults, firstResult);
    }

    private List<MODELO> findMODELOEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MODELO.class));
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

    public MODELO findMODELO(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MODELO.class, id);
        } finally {
            em.close();
        }
    }

    public int getMODELOCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MODELO> rt = cq.from(MODELO.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
   
	public List<MODELO> searchModelo(String value) {
		EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Connection cnt = em.unwrap(Connection.class);
            Query query = em.createNativeQuery("SELECT * FROM modelo WHERE NOME LIKE ?", MODELO.class);
            query.setParameter(1, value +"%");
            List<MODELO> mdl = query.getResultList();
            cnt.close();
            return mdl;
        } catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
            em.close();
        }
	}
    
}
