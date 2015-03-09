package vigorBackup.model;

import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * Responsible for the email reporting. This class builds a simple html table
 * with the downloaders results.
 */
public final class EmailBackupReport {
	/**
	 * This class can't be instantiated as there's no need for it.
	 */
	private EmailBackupReport() {
		// not used
	}

	/**
	 * The separator that is used to split the "TO" addresses.
	 */
	private static final String EMAIL_SEPARATOR = ",";
	/**
	 * Maximum time to wait for a server response, in milliseconds.
	 */
	private static final int SMTP_TIMEOUT = 10000;

	/**
	 * Sends the backups report.
	 * 
	 * @param routersDownloaders
	 *            The list of downloaders to build the report.
	 */
	public static void sendBackupReport(
			final List<BaseDownloader> routersDownloaders) {
		// TODO: treat auth not needed
		StringBuilder sb = new StringBuilder();
		sb.append("<table border=\"1\"><tr>" + "<th>Cliente</th>"
				+ "<th>Local</th>" + "<th>Backup OK?</th></tr>");

		routersDownloaders.forEach(downloader -> {
			sb.append("<tr><td>" + downloader.getRouter().getSiteName()
					+ "</td>");
			sb.append("<td>" + downloader.getRouter().getDescription()
					+ "</td>");

			String result = downloader.isBackupOK() ? ""
					+ "<font color=\"green\">OK!</font>"
					: "<font color=\"red\">Error</font>";

			sb.append("<td>" + result + "</td>");
			sb.append("</tr>");

		});

		sb.append("</table>");

		HtmlEmail email = new HtmlEmail();
		email.setDebug((boolean) Configs.getConfig(EConfigs.IS_DEBUG_ON));
		email.setSocketConnectionTimeout(SMTP_TIMEOUT);
		email.setSocketTimeout(SMTP_TIMEOUT);
		email.setHostName((String) Configs.getConfig(EConfigs.SMTP_HOST));
		email.setSmtpPort((int) Configs.getConfig(EConfigs.SMTP_PORT));
		email.setSslSmtpPort(String.valueOf(Configs
				.getConfig(EConfigs.SMTP_PORT)));

		email.setAuthenticator(new DefaultAuthenticator((String) Configs
				.getConfig(EConfigs.SMTP_LOGIN_USERNAME), (String) Configs
				.getConfig(EConfigs.SMTP_PASSWORD)));
		email.setSSL((boolean) Configs.getConfig(EConfigs.IS_SMTP_SSL_ENABLED));

		try {
			email.setFrom((String) Configs.getConfig(EConfigs.SMTP_FROM_EMAIL));
			email.setSubject("Routers backup report");
			email.setHtmlMsg(sb.toString());
			String emailsTo = (String) Configs
					.getConfig(EConfigs.SMTP_TO_EMAIL);

			String[] emails = emailsTo.split(EMAIL_SEPARATOR);

			for (int i = 0; i < emails.length; i++) {
				email.addTo(emails[i]);
			}
			email.send();
		} catch (EmailException e) {
			if (!(boolean) Configs.getConfig(EConfigs.IS_DEBUG_ON)) {
				System.out.println("Could not send the e-mail. "
						+ "Active the debug  mode to know why");
			} else {
				e.printStackTrace();
			}

		}

	}
}
