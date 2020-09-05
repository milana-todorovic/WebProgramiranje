package app;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import repository.file_repos.FileRepositoryContainer;

@WebListener
public class Initializer implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		String root = arg0.getServletContext().getRealPath("") + File.separator + "WEB-INF" + File.separator + "data";
		File file = new File(root);
		if (!file.exists()) {
			try {
				file.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		arg0.getServletContext().setAttribute("repo", new FileRepositoryContainer(root));
	}

}
