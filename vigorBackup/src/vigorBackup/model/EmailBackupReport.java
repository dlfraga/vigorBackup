package vigorBackup.model;

import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class EmailBackupReport {
private static final String EMAIL_SEPARATOR = ",";

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
		email.setDebug(LoadConfigFile.IS_SMTP_DEBUG_ON);
		email.setSocketConnectionTimeout(10000);
		email.setSocketTimeout(10000);
		email.setHostName(LoadConfigFile.SMTP_HOST);
		email.setSmtpPort(LoadConfigFile.SMTP_PORT);
		email.setSslSmtpPort(String.valueOf(LoadConfigFile.SMTP_PORT));
		email.setAuthenticator(new DefaultAuthenticator(
				LoadConfigFile.SMTP_LOGIN_USERNAME, LoadConfigFile.SMTP_PASSWORD));
		email.setSSL(LoadConfigFile.IS_SMTP_SSL_ENABLED);

		try {
			email.setFrom(LoadConfigFile.SMTP_FROM_EMAIL);
			email.setSubject("Routers backup report");
			email.setHtmlMsg(sb.toString());
			String[] emails = LoadConfigFile.SMTP_TO_EMAIL.split(EMAIL_SEPARATOR);
			for (int i = 0; i < emails.length; i++) {
				email.addTo(emails[i]);
			}
			email.send();
		} catch (EmailException e) {
			if(!LoadConfigFile.IS_SMTP_DEBUG_ON){
				System.out.println("Could not send the e-mail. Active the debug to know why");	
			} else {
				e.printStackTrace();
			}
			
		}

	}
}
