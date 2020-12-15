package com.virtusa.banking;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class ServerTest {


    private static KieBase kbase;
	public static void main(String[] args)
	{
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("ksession-rules");

    	kbase=kSession.getKieBase();
        FactType serverType = kbase.getFactType("rules", "Server");      

        Object debianServer = null;
        try {
            debianServer = serverType.newInstance();
        } catch (InstantiationException e) {
            System.err.println("the class Server on rules package hasn't a constructor");
        } catch (IllegalAccessException e) {
            System.err.println("unable to access the class Server on rules package");
        }
        serverType.set(debianServer, "name", "server001");
        serverType.set(debianServer, "processors", 1);
        serverType.set(debianServer, "memory", 2048); // 2 gigabytes
        serverType.set(debianServer, "diskSpace", 2048); // 2 terabytes
        serverType.set(debianServer, "cpuUsage", 3);

        kSession.insert(debianServer);

        kSession.fireAllRules();

        System.out.println(kSession.getObjects().size());
        getData(kSession);
	}

	private static void getData(KieSession ksession)
	{
		
		FactType serverType = kbase.getFactType("rules", "Server");
        FactType serverStatusType = kbase.getFactType("rules", "ServerStatus");
        FactType virtualizationType = kbase.getFactType("rules", "Virtualization");

        Object debianServer = null;
        try {
            debianServer = serverType.newInstance();
        } catch (InstantiationException e) {
            System.err.println("the class Server on rules package hasn't a constructor");
        } catch (IllegalAccessException e) {
            System.err.println("unable to access the class Server on rules package");
        }
        serverType.set(debianServer, "name", "server002");
        serverType.set(debianServer, "processors", 4);
        serverType.set(debianServer, "memory", 8192); // 8 gigabytes
        serverType.set(debianServer, "diskSpace", 2048); // 2 terabytes
        serverType.set(debianServer, "cpuUsage", 3);

        Object fedoraServer = null;
        try {
            fedoraServer = serverType.newInstance();
        } catch (InstantiationException e) {
            System.err.println("the class Server on rules package hasn't a constructor");
        } catch (IllegalAccessException e) {
            System.err.println("unable to access the class Server on rules package");
        }
        serverType.set(fedoraServer, "name", "server003");
        serverType.set(fedoraServer, "processors", 2);
        serverType.set(fedoraServer, "memory", 2048); // 2 gigabytes
        serverType.set(fedoraServer, "diskSpace", 1048); // 1 terabytes
        serverType.set(fedoraServer, "cpuUsage", 80);

        Object instance001 = null;
        Object instance002 = null;
        Object instance003 = null;
        Object request = null;
        try {
            instance001 = virtualizationType.newInstance();
            instance002 = virtualizationType.newInstance();
            instance003 = virtualizationType.newInstance();
            request = virtualizationType.newInstance();
        } catch (InstantiationException e) {
            System.err.println("the class Virtualization on rules package hasn't a constructor");
        } catch (IllegalAccessException e) {
            System.err.println("unable to access the class Virtualization on rules package");
        }

        virtualizationType.set(instance001, "name", "instance001");
        virtualizationType.set(instance001, "diskSpace", 10);
        virtualizationType.set(instance001, "memory", 2048);

        virtualizationType.set(instance002, "name", "instance002");
        virtualizationType.set(instance002, "diskSpace", 25);
        virtualizationType.set(instance002, "memory", 2048);

        virtualizationType.set(instance003, "name", "instance003");
        virtualizationType.set(instance003, "diskSpace", 25);
        virtualizationType.set(instance003, "memory", 2048);

        virtualizationType.set(request, "name", "instance003");
        virtualizationType.set(request, "diskSpace", 10);
        virtualizationType.set(request, "memory", 3072);

        List<Object> virtualizations = new ArrayList<Object>();
        virtualizations.add(instance001);
        virtualizations.add(instance002);

        serverType.set(debianServer, "virtualizations", virtualizations);

        virtualizations = new ArrayList<Object>();
        virtualizations.add(instance003);

        serverType.set(fedoraServer, "virtualizations", virtualizations);

        ksession.setGlobal("serversAvailability", new ArrayList<Object>());

        ksession.insert(debianServer);
        ksession.insert(fedoraServer);
        ksession.insert(request);

        ksession.fireAllRules();

        @SuppressWarnings("unchecked")
        List<Object> servers = (List<Object>) ksession.getGlobal("serversAvailability");
        //assertEquals(1, servers.size());
        for (Object server : servers) {
            String name = (String) serverStatusType.get(server, "name");
            Integer freeDiskSpace = (Integer) serverStatusType.get(server, "freeDiskSpace");
            System.out.println("Server \"" + name + "\" has " + freeDiskSpace + " MB of free disk space");
        }

	}
}
