package org.openkoala.koala.token.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openkoala.koala.token.TokenHelper;

public class KoalaTokenGenerateServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2260406951592429225L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String token = TokenHelper.generateAndAddToken(req.getSession());
		resp.getWriter().write(token);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
