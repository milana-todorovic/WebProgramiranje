package app;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

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
		arg0.getServletContext().setAttribute("service", new ServiceContainer(root));
	}

}
