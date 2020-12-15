package com.virtusa.ecommerce;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.virtusa.ecommerce.eshop.Item;
import com.virtusa.ecommerce.service.EShopConfigService;

public class RuleAttributesTest {
   
    public static void main(String[] args) {
    	System.out.println( "Bootstrapping the Rule Engine ..." );
	    //1) Bootstrapping a Rule Engine Session
        KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("ksession-ruleattribute-rules");
        
        EShopConfigService eShopConfigService = new EShopConfigService() {

			@Override
			public boolean isMidHighCategoryEnabled() {
				// TODO Auto-generated method stub
				return true;
			}
        	
        };
        
        kSession.setGlobal("configService", eShopConfigService);
        Item item = new Item("item 1", 350.00, 500.00);
        kSession.insert(item);
        int fired = kSession.fireAllRules();
        System.out.println("Fired All Rules"+fired);
        System.out.println("Category"+item.getCategory());
        //disabled
        eShopConfigService = new EShopConfigService() {

			@Override
			public boolean isMidHighCategoryEnabled() {
				// TODO Auto-generated method stub
				return false;
			}
        	
        };
        
        kSession.setGlobal("configService", eShopConfigService);
        item = new Item("item 1", 350.00, 500.00);
        kSession.insert(item);
        fired = kSession.fireAllRules();
        System.out.println("Fired All Rules"+fired);
        System.out.println("Category"+item.getCategory());        
        
        
    }
    
}
