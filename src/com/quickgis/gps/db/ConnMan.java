package com.quickgis.gps.db;


/**
 * <p>Title: WebGPS</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: SEG Navigation Co. Ltd.</p>
 *
 * @author not attributable
 * @version 1.0
 */

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

 
import javax.sql.DataSource; 
import com.mchange.v2.c3p0.DataSources;


public class ConnMan {
	static private ConnMan instance;
 
	 
	private DataSource dataSource;

	static synchronized public ConnMan getInstance() { 
		if (instance == null) {
			instance = new ConnMan();
		}
		return instance;
	}

	private ConnMan() {
		this.init();
	}

	public void freeConnection( Connection con) {
		try {
			if (con != null && !con.isClosed()) {

				con.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void CloseCstam(ResultSet rs, CallableStatement cstam,
			 Connection conn) {
		if (rs != null) {
			try {
				rs.close();
				rs = null;
			} catch (SQLException e) {
			}
		}
		if (cstam != null) {
			try {
				cstam.close();
				cstam = null;
			} catch (SQLException e) {
			}
		}
		if(conn != null){
			try {
				freeConnection( conn); // 在freeConnection中判断了是否为null的情况
				conn = null;
			} catch (Exception e) {
			}
		}

	}

	public void CloseStam(ResultSet rs, Statement stam,
			Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
		if (stam != null) {
			try {
				stam.close();
			} catch (SQLException e) {
			}
		}
		try {
			freeConnection( conn); // 在freeConnection中判断了是否为null的情况
		} catch (Exception e) {
		}
	}

	public void ClosePstam(ResultSet rs, PreparedStatement pstam,
			Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
		if (pstam != null) {
			try {
				pstam.close();
			} catch (SQLException e) {
			}
		}
		try {
			freeConnection(conn); // 在freeConnection中判断了是否为null的情况
		} catch (Exception e) {
		}

	}
	
	public void ClosePstam(ResultSet rs, PreparedStatement[] pstam,
			Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
		for( int i=0;i<pstam.length;i++){
			if (pstam[i] != null) {
				try {
					pstam[i].close();
				} catch (SQLException e) {
				}
			}
		}
		try {
			freeConnection(conn); // 在freeConnection中判断了是否为null的情况
		} catch (Exception e) {
		}

	}

	public Connection getConnection(String name) {
		return this.getConnection();
	}

	public Connection getConnection(String name, long time) {
		return this.getConnection();
	}

	public Connection getConnection() {
		Connection con = null;
		try {
			con = this.dataSource.getConnection();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return con;
	}

	

	public void CloseForanePstam(ResultSet rs, PreparedStatement pstam,
			Connection con) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstam != null) { 
				pstam.close();
			}
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void release() {
		
	}

	private void init() { 
		InputStream is=null;
		try {
			 is = getClass().getResourceAsStream(
					"/Connection.properties");
			Properties dbProps = new Properties();
			dbProps.load(is);
			Class.forName(dbProps
					.getProperty("driverClassName")); 
			DataSource updataSource  = DataSources.unpooledDataSource(dbProps
					.getProperty("datasourcename") ,dbProps
					.getProperty("datasourceusername"),dbProps
					.getProperty("datasourcepass"));  //"jdbc:postgresql://localhost/test",
				     //  "swaldman",
				  //     "test");
			
		 
			Map overrides = new HashMap();
		 
			overrides.put("maxPoolSize", dbProps
					.getProperty("maxPoolSize")); //"boxed primitives" also work
			this.dataSource=DataSources.pooledDataSource(updataSource,overrides); 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(is!=null){
				try{
				is.close();
				}catch(Exception ex){}
			}
		} 
	}
}
