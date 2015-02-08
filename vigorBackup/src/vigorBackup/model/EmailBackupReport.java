package vigorBackup.model;

import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import vigorBackup.controller.Main;

public class EmailBackupReport {

	public EmailBackupReport() {
		// TODO Auto-generated constructor stub
	}

	public static void sendBackupReport(
			List<DefaultRouterWebDownloader> routersDownloaders) {
		// TODO: treat auth not needed
		StringBuilder sb = new StringBuilder();
		sb.append("<table border=\"1\"><tr>" + "<th>Cliente</th>"
				+ "<th>Local</th>" + "<th>Backup OK?</th></tr>");
		//Java 8 lambdas
		routersDownloaders
				.forEach(routerDownloader -> {
					sb.append("<tr><td>"
							+ routerDownloader.getRouter().getSiteName()
							+ "</td>");
					sb.append("<td>"
							+ routerDownloader.getRouter().getDescription()
							+ "</td>");
					String backResult = routerDownloader.isBackupOK() ? " <font color=\"green\">OK!</font> "
							: "<font color=\"red\">Error</font>";
					sb.append("<td>" + backResult + "</td>");
					sb.append("</tr>");

				});

		sb.append("</table>");

		HtmlEmail email = new HtmlEmail();
		email.setDebug(Main.IS_SMTP_DEBUG_ON);
		email.setSocketConnectionTimeout(10000);
		email.setSocketTimeout(10000);
		email.setHostName(Main.SMTP_HOST);
		email.setSmtpPort(Main.SMTP_PORT);
		email.setAuthenticator(new DefaultAuthenticator(
				Main.SMTP_LOGIN_USERNAME, Main.SMTP_PASSWORD));
		email.setSSL(Main.IS_SMTP_SSL_ENABLED);

		try {
			email.setFrom(Main.SMTP_FROM_EMAIL);
			email.setSubject("Routers backup report");
			email.setHtmlMsg(sb.toString());
			email.addTo(Main.SMTP_TO_EMAIL);
			email.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
