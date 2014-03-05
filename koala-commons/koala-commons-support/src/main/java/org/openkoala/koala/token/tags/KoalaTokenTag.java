package org.openkoala.koala.token.tags;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.openkoala.koala.token.TokenHelper;

public class KoalaTokenTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3344305771570273660L;

	@Override
	public int doEndTag() throws JspException {
		HttpSession session = this.pageContext.getSession();
		synchronized (session) {
			String token = TokenHelper.generateToken();
			try {
				JspWriter out = this.pageContext.getOut();
				out.print("<input type='Hidden' name='" + TokenHelper.TOKEN_KEY + "' value='" + token + "'>");
				TokenHelper.addToken(session, token);
			} catch (IOException e) {
			}
		}
		return EVAL_PAGE;
	}
}
