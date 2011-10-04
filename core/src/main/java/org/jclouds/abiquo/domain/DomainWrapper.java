package org.jclouds.abiquo.domain;

/**
 * Wrapper interface. Should be implemented by any resource in domain.
 * 
 * @author Francesc Montserrat
 */
public interface DomainWrapper
{
    /**
     * Default save operation in domain.
     */
    public void save();

    /**
     * Default update operation in domain.
     */
    public void update();

    /**
     * Default delete operation in domain.
     */
    public void delete();
}
