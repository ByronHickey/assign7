/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxrs;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
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

@Path("product")
@ApplicationScoped
public class ProductREST {
    @PersistenceContext(unitName = "buildit13PU")
    private EntityManager em;

      @Inject
    private UserTransaction transaction;
      
    
    @POST   
    @Consumes({"application/xml", "application/json"})
     public void addOne(Product product) {
        try {
            transaction.begin();
            em.persist(product);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(ProductCodeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void editOne(Product product, @PathParam("id") String id) {
        try {
            Query q = em.createQuery("SELECT p FROM Product p WHERE p.productId = :productId");
            q.setParameter("id", id);
            Product savedP = (Product) q.getSingleResult();
            savedP.setDescription(product.getDescription());            
            transaction.begin();
            em.merge(savedP);
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
            Product found = em.find(Product.class, id);
            em.remove(found);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(ProductCodeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public List<Product> getOne(@PathParam("id") String id) {
        Query q = em.createNamedQuery("Product.findByProductId");
        q.setParameter("productId", id);
        List<Product> product = q.getResultList();
        return product;
    }

    @GET   
    @Produces({"application/xml", "application/json"})
    public List<Product> getAll() {
        List<Product> products = em.createQuery("SELECT p FROM Product p").getResultList();
        return products;
    }

//    @GET
//    @Path("{from}/{to}")
//    @Produces({"application/xml", "application/json"})
//    public List<Product> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
//        return super.findRange(new int[]{from, to});
//    }
//
//    @GET
//    @Path("count")
//    @Produces("text/plain")
//    public String countREST() {
//        return String.valueOf(super.count());
//    }
//
//    @Override
//    protected EntityManager getEntityManager() {
//        return em;
//    }
    
}
