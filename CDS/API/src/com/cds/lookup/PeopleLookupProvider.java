/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cds.lookup;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Comarch
 */
public class PeopleLookupProvider implements Lookup.Provider{
    private InstanceContent content;
    private final Lookup dynamicLookup;
    private final Lookup.Result<Integer> results;
    private static PeopleLookupProvider instance;
    
    private PeopleLookupProvider() {
        content = new InstanceContent();
        dynamicLookup = new AbstractLookup(content);
        results = dynamicLookup.lookupResult(Integer.class);
    }
    
    public static synchronized PeopleLookupProvider getInstance()
    {
        if(instance == null)
            instance = new PeopleLookupProvider();
        
        return instance;
    }

    public InstanceContent getContent() {
        return content;
    }

    public Lookup getLookup() {
        return dynamicLookup;
    }
}
