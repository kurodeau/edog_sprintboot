package com.ad.model;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/ad/AdPicReader")
public class AdPicReader  extends HttpServlet{

	Connection con;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		res.setContentType("image/gif");
		ServletOutputStream out = res.getOutputStream();
		
		try {
			Statement stmt = con.createStatement();
			String id = req.getParameter("adId").trim();
			int picId = Integer.parseInt(id);
			
			ResultSet rs = stmt.executeQuery(
			"SELECT adimg FROM edog.ad WHERE adid ="+id
			);
			
			
			
			if (rs.next()) {
				BufferedInputStream in = new BufferedInputStream(rs.getBinaryStream("adimg"));
				
				byte[] buf = new byte[4*1024];
				int len;
				while((len = in.read(buf))!=-1) {
					out.write(buf, 0 ,len);
				}
				
				
				in.close();
			}else {
				InputStream in = getServletContext().getResourceAsStream("/nodata/none02.jpg");
				byte[] b = new byte[in.available()];
				in.read(b);
				out.write(b);
				in.close();
			}
			
			
			
			rs.close();
			stmt.close();

		}catch (Exception e) {
//			URL url = getServletContext().getResource("/nodata/null01.jpg");
//			System.out.println(url.toString());
			InputStream in = getServletContext().getResourceAsStream("/nodata/null01.jpg");
			byte[] b = new byte[in.available()];
			in.read(b);
			out.write(b);
			in.close();
		}
	}
	

	
	public void destroy() {
		try {
			if (con != null) con.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public void init() throws ServletException {
		try {
			Context ctx = new javax.naming.InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/TestDB2");
			con = ds.getConnection();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
