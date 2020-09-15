package app;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import auth.Blocklist;
import service.ServiceContainer;

@WebListener
public class Initializer implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		String root = arg0.getServletContext().getRealPath("") + File.separator + "WEB-INF" + File.separator + "data";
		ServiceContainer service = new ServiceContainer(root);
		arg0.getServletContext().setAttribute("service", service);
		arg0.getServletContext().setAttribute("blocklist", new Blocklist(service.getUserService()));
	}

}
