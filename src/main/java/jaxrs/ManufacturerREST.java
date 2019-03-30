/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxrs;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


/**
 *
 * @author c0006557
 */
@Path("manufacturer")
@ApplicationScoped
public class ManufacturerREST {
    @PersistenceContext(unitName = "buildit13PU")
    private EntityManager em;
    
    @Inject
    private UserTransaction transaction;
    
    @POST
    @Consumes({"application/xml", "application/json"})
    public void addOne(Manufacturer entity) {
        try {
            transaction.begin();
            em.persist(entity);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(ProductCodeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void editOne(Manufacturer manufacturer, @PathParam("id") String id) {
        try {
            Query q = em.createQuery("SELECT m FROM Manufacturer m WHERE m.manufacturerId = :manufacturerId");
            q.setParameter("id", id);
            Manufacturer man = (Manufacturer) q.getSingleResult();
            man.setName(manufacturer.getName());
            man.setManufacturerId(manufacturer.getManufacturerId());
            transaction.begin();
            em.merge(man);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(ProductCodeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @DELETE
    @Path("{id}")
    public void deleteOne(@PathParam("id") String id) {
        try {
            transaction.begin();
            Manufacturer found = em.find(Manufacturer.class, id);
            em.remove(found);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(ProductCodeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public List<Manufacturer> getOne(@PathParam("id") String id) {
        Query q = em.createNamedQuery("Manufacturer.findByManufacturerId");
        q.setParameter("manufacturerId", id);
        List<Manufacturer> manufacturers = q.getResultList();
        return manufacturers;
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<Manufacturer> getAll() {
        List<Manufacturer> manufacturers = em.createQuery("SELECT m FROM Manufacturer m").getResultList();
        return manufacturers;
    }

    
}
