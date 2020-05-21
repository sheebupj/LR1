package com.hpe.utilities;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.hpe.data.ExemptCertDTO;
import com.hpe.lr1.ReadMessageAndUpdateOnsourceDb;

public class JdbcUtilities {
	final static Logger logger = Logger.getLogger(JdbcUtilities.class);

	public static Connection getConnection() {
		ResourceBundle rb = utility.getResourceBundle();

		String server = rb.getString("DbUrl");
		String url = rb.getString(server);
		String user = rb.getString("GteDbUser");
		String pass = rb.getString("GteDbPassword");
		Connection con = null;
		// Creating the connection
		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			con = DriverManager.getConnection(url, user, pass);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;

	}

	public static int insertExemptDetailsToOnesourceDb(ExemptCertDTO certDTO, boolean flag) {

		int update = 0;
		if (flag) {
			Connection con = getConnection();

			java.sql.Date createdDate = (java.sql.Date) getDateFromString(certDTO.getCertCreated(), flag);
			java.sql.Date expDate = (java.sql.Date) getDateFromString(certDTO.getExpiration_date(), flag);
			Long custId = parseNo(certDTO.getCustomerId());
			if (custId != -1) {
				boolean existCustomer = checkCustomerWithThisId(custId, flag);
				if (!existCustomer) {
					logger.info("@dxc-info:" + "Saving Customer");
					if (saveCustomer(certDTO, flag) == 1) {
						logger.info("@dxc-info:" + "Customer created successfully");
					} else {
						logger.error("@dxc-error:" + "Customer Not created");
					}
					// certDTO.setCustomerId(Integer.toString(custId));
				}
			} else {
				return -1;
			}

			// int customer_id = 0;// getCustomerId(certDTO, con);
			if (certDTO.isCertificateStatus()) {

				Long exCertId = parseNo(certDTO.getExemptCertificateId());
				if (exCertId == -1) {
					return -1;
				}
				if (!checkCertificateWithThisId(exCertId, flag)) {
					logger.info("@dxc-info:" + "Inserting certificate details started");
					int reasonId = getReasonId(certDTO.getExemptReasonCode(), flag);
					// CONTACT_NAME=?,"
					// + "CONTACT_EMAIL=?
					String content_type=getContentType(certDTO);
					String sql = "insert into tb_exempt_certs "
							+ "(EXEMPT_CERT_ID, CUSTOMER_ID, CERTIFICATE_NUMBER,EXEMPT_REASON_ID,"
							+ "CERT_USE,STATUS,CONTENT_TYPE,CREATED_BY,FROM_DATE,CREATION_DATE,"
							+ "ACTIVE,LOCKED,TO_DATE,FULLY_EXEMPT,PURCH_REG_TAX_ID)"
							+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					try {

						PreparedStatement pSt = con.prepareStatement(sql);

						pSt.setLong(1, exCertId);
						pSt.setLong(2, custId);
						pSt.setString(3, certDTO.getCertificateNO());
						pSt.setInt(4, reasonId);
						pSt.setString(5, "B");
						pSt.setString(6, "A");
						pSt.setString(7, content_type);
						pSt.setInt(8, 1);
						pSt.setDate(9, createdDate);
						pSt.setDate(10, createdDate);
						pSt.setString(11, "Y");
						pSt.setString(12, "N");
						pSt.setDate(13, expDate);
						pSt.setString(14, certDTO.getIsFullyExempt());
						pSt.setString(15, certDTO.getTax_id());
						// pSt.setString(13, certDTO.getContact_name());
						// pSt.setString(14, certDTO.getContact_email_address());
						if (pSt.executeUpdate() == 1) {
							update = 1;
							logger.info("@dxc-info:" + "Exempt cert details are updated successfully");
						}

						con.close();
						
						insertCertZone(certDTO);
						

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				

			}

		}
		return update;
	}
	public static String getContentType(ExemptCertDTO certDTO) {
		String contentType="US";
		if (!certDTO.getCertCountry().equals("United States")) {
			contentType="INTL";
		}
		return contentType;
	}

	public static void insertCertZone(ExemptCertDTO certDTO) {
		logger.info("@dxc-info:inserting excempt zone started" );
		int id=getCertZoneId();
		int zoneId=getZoneId(certDTO);
		long certid=parseNo(certDTO.getExemptCertificateId());
		java.sql.Date createdDate = (java.sql.Date) getDateFromString(certDTO.getCertCreated(), true);
		String sql = "insert into tb_exempt_cert_zones " + "(EXEMPT_CERT_ZONE_ID, EXEMPT_CERT_ID, ZONE_ID,CREATED_BY,"
				+ "CREATION_DATE,EXEMPT_TYPE)" + " values(?,?,?,?,?,?)";
		PreparedStatement pst=null;
		try {
		
		Connection con= getConnection();
		pst= con.prepareStatement(sql);
		//String zoneName=certDTO.getExposureZoneName().toUpperCase();
		pst.setInt(1, id);
		pst.setLong(2, certid);
		pst.setInt(3, zoneId);
		pst.setInt(4, 1);
		pst.setDate(5, createdDate);
		pst.setString(6, "Y");
		int result=pst.executeUpdate();
			
			if(pst!=null) {
				pst.close();
			}
			if(con!=null) {
				con.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static int getCertZoneId() {
		int id=-1;
		Statement statement=null;
		try {
		String sql="select  MAX(EXEMPT_CERT_ZONE_ID) from tb_exempt_cert_zones";
		Connection con= getConnection();
		statement= con.createStatement();
		ResultSet resultSet=statement.executeQuery(sql);
			if(resultSet.next()) {
				id=resultSet.getInt(1);
			}
			if(statement!=null) {
				statement.close();
			}
			if(con!=null) {
				con.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return id +1;
	}
	
	public static int getZoneId(ExemptCertDTO certDTO) {
		int id=-1;
		PreparedStatement pst=null;
		try {
		String sql="select ZONE_ID from tb_zones where  name=?";
		Connection con= getConnection();
		pst= con.prepareStatement(sql);
		String zoneName=certDTO.getExposureZoneName().toUpperCase();
		pst.setString(1, zoneName);
		ResultSet resultSet=pst.executeQuery();
			if(resultSet.next()) {
				id=resultSet.getInt(1);
			}
			if(pst!=null) {
				pst.close();
			}
			if(con!=null) {
				con.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return id;
	}

	public static void insertCustomer(ExemptCertDTO certDTO, boolean flag) {
		// Connection con = getConnection();
		if (saveCustomer(certDTO, flag) == 1) {
			logger.info("@dxc-info:" + "Customer created successfully");
		} else {
			logger.error("@dxc-error:" + "Customer Not created");
		}

	}

	public static int updateCustomer(ExemptCertDTO certDTO, boolean flag) {
		Connection con = null;
		int result = -1;
		// +++++++++++++++++++++++
		Long custId = parseNo(certDTO.getCustomerId());
		if (custId == -1) {

			return result;
		}

		// ==========================

		if (flag) {
			boolean exist = checkCustomerWithThisId(custId, flag);
			if (exist) {
				con = getConnection();
				String sql = "select   max(customer_id) from tb_customers ";
				java.sql.Date createdDate = getCurrentDate();
				if (certDTO.getCustCreated() != null) {
					createdDate = (java.sql.Date) getDateFromString(certDTO.getCustCreated(), flag);
				}
				/*
				 * String insertSql1 =
				 * "insert into  tb_customers(CUSTOMER_ID,CUSTOMER_GROUP_ID," +
				 * "CUST_NUMBER ,CUST_NAME,CREATED_BY,CREATION_DATE,ADDRESS_1," +
				 * "CITY,ZIP_CODE,STATE,PROVINCE) values(?,?,?,?,?,?,?,?,?,?,?)"; CONTACT_NAME
				 * VARCHAR2(60) CONTACT_EMAIL VARCHAR2(60) CONTACT_PHONE VARCHAR2(20)
				 */
				String insertSql1 = "update  tb_customers set CUST_NAME=?,CREATION_DATE=?,"
						+ "ADDRESS_1=?,CITY=?,ZIP_CODE=?,STATE=?,PROVINCE=?,CONTACT_NAME=?,"
						+ "CONTACT_EMAIL=?,COUNTRY=? where CUSTOMER_ID=? ";// ,CONTACT_PHONE=?

				Statement st;
				PreparedStatement pst;
				try {
					if (!validateStringForCustomerNoAndName(certDTO.getCustomerName())) {
						logger.info("@dxc-info customer Name is " + certDTO.getCustomerName()
								+ " Customer no is using as name");
						certDTO.setCustomerName(certDTO.getCustomerNo());
					}

					logger.info("@dxc-info:" + "customer with id" + certDTO.getCustomerId() + " start updating");
					// Long custId = Long.valueOf(certDTO.getCustomerId());
					pst = con.prepareStatement(insertSql1);
					// pst.setLong(6, custId);
					// pst.setInt(2, getCustomerGroupId(certDTO,flag));
					// pst.setString(3, certDTO.getCustomerNo());
					pst.setString(1, certDTO.getCustomerName());
					pst.setDate(2, createdDate);
					pst.setString(3, certDTO.getCustAddress());
					pst.setString(4, certDTO.getCustCity());
					pst.setString(5, certDTO.getCustPostCode());
					pst.setString(6, certDTO.getCustState());
					pst.setString(7, certDTO.getCustProvince());
					pst.setString(8, certDTO.getContact_name());
					// System.out.println(certDTO.getContat_phone_number().length());
					// pst.setString(9, certDTO.getPhone_number().substring(2));
					pst.setString(9, certDTO.getContact_email_address());
					pst.setString(10, certDTO.getCustCountry());
					pst.setLong(11, custId);

					if (pst.executeUpdate() == 1) {
						logger.info("@dxc-info:" + "customer with id" + certDTO.getCustomerId() + " updated");
						result = 1;
					}

					pst.close();
					con.close();

				} catch (SQLException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
					logger.error("@dxc-error: exception happend during sql ooeration");
				}
			} else {
				logger.info("@dxc-info:" + "No customer with this custmer id in One source db:" + custId
						+ "  No updation possible");
			}

		}
		return result;

	}

	public static int saveCustomer(ExemptCertDTO certDTO, boolean flag) {
		Connection con = getConnection();
		int maxCustomerid = -1;
		PreparedStatement pst = null;
		if (flag) {
			String sql = "select   max(customer_id) from tb_customers ";
			java.sql.Date createdDate = getCurrentDate();
			if (certDTO.getCustCreated() != null) {
				createdDate = (java.sql.Date) getDateFromString(certDTO.getCustCreated(), flag);
			}
			String insertSql1 = "insert into  tb_customers(CUSTOMER_ID,CUSTOMER_GROUP_ID,"
					+ "CUST_NUMBER ,CUST_NAME,CREATED_BY,CREATION_DATE,ADDRESS_1,"
					+ "CITY,ZIP_CODE,STATE,PROVINCE,CONTACT_NAME,CONTACT_EMAIL,COUNTRY)"
					+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			//

			Long custId;
			try {

				if (certDTO.getCustomerId() == null) {
					logger.info("@dxc-info:" + " this customer id :" + certDTO.getCustomerId() + " not valid");
					return -1;
				}

				if (!validateStringForCustomerNoAndName(certDTO.getCustomerNo())) {
					logger.error("@dxc-error customer No is " + certDTO.getCustomerNo()
							+ " Saving customer details aborted");
					return -1;
				}
				if (!validateStringForCustomerNoAndName(certDTO.getCustomerName())) {
					logger.info("@dxc-info customer Name is " + certDTO.getCustomerName()
							+ " Customer no is using as name");
					certDTO.setCustomerName(certDTO.getCustomerNo());
				}

				custId = parseNo(certDTO.getCustomerId());
				if (custId == -1) {
					return -1;
				}
				pst = con.prepareStatement(insertSql1);

				pst.setLong(1, custId);
				pst.setInt(2, getCustomerGroupId(certDTO, flag));
				pst.setString(3, certDTO.getCustomerNo());
				pst.setString(4, certDTO.getCustomerName());
				pst.setInt(5, 10);
				pst.setDate(6, createdDate);
				pst.setString(7, certDTO.getCustAddress());
				pst.setString(8, certDTO.getCustCity());
				pst.setString(9, certDTO.getCustPostCode());
				pst.setString(10, certDTO.getCustState());
				pst.setString(11, certDTO.getCustProvince());
				pst.setString(12, certDTO.getContact_name());
				pst.setString(13, certDTO.getContact_email_address());
				pst.setString(14, certDTO.getCustCountry());
				maxCustomerid = pst.executeUpdate();
//				if (pst.executeUpdate() != 1) {
//					maxCustomerid = -1;
//				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				maxCustomerid = -1;
				logger.error("@dxc-error:" + "exception while inserting the customer details");
			} finally {

				try {
					if (pst != null) {
						pst.close();
						con.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		return maxCustomerid;

	}

	public static boolean checkCustomerWithThisId(Long custId, boolean flag) {
		boolean result = false;

		if (custId != null) {
			Connection con = getConnection();
			if (flag) {

				String sql = "select customer_id from tb_customers where customer_id = ? ";
				try {
					PreparedStatement preparedStatement = con.prepareStatement(sql);
					preparedStatement.setLong(1, custId);
					ResultSet resultSet = preparedStatement.executeQuery();

					if (resultSet.next()) {
						logger.info("@dxc-info:" + "Customer exist with Customer_id in one source DB:" + custId);
						result = true;
					} else {
						logger.info("@dxc-info:" + "Customer Not exist with Customer_id in one source DB:" + custId);
					}
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error("@dxc-error:" + "exception occured while databse operation:cheking this customer id"
							+ custId);
				}
			}
		} else {
			logger.error("@dxc-error:" + "customer id is not valid and is:" + custId);
		}

		return result;
	}

	public static boolean checkCertificateWithThisId(Long certId, boolean flag) {
		boolean result = false;
		Connection con = getConnection();
		if (flag) {
			String sql = "select * from tb_exempt_certs where exempt_cert_id=?";
			try {
				PreparedStatement preparedStatement = con.prepareStatement(sql);
				preparedStatement.setLong(1, certId);
				ResultSet resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {
					logger.info("@dxc-info:" + "certificate exist with Certificate id:" + certId + " in one source DB");
					result = true;
				} else {
					logger.info("No certifiacate exist with Certificate_id:" + certId + " in one source DB");
				}
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("@dxc-error: exception happend duirng sql operation");
			}
		}
		return result;
	}

	public static int deleteCertificat(Long certId, boolean flag) {
		int result = -1;
		if (flag) {
			Connection con = getConnection();

			String sql = "delete * from tb_exempt_certs where exempt_cert_id=?";
			try {
				PreparedStatement preparedStatement = con.prepareStatement(sql);
				preparedStatement.setLong(1, certId);
				result = preparedStatement.executeUpdate();
				logger.info("@dxc-info:" + "Certificate with id " + certId + " deleted");
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	public static void deleteCustomer(Long cust_Id, boolean flag) {
		int result = -1;
		if (flag) {
			Connection con = getConnection();
			boolean exist = checkCustomerWithThisId(cust_Id, flag);
			if (exist) {
				String sql = "delete * from tb_customers wwhere CUSTOMER_ID=?";
				try {
					PreparedStatement preparedStatement = con.prepareStatement(sql);
					preparedStatement.setLong(1, cust_Id);
					result = preparedStatement.executeUpdate();
					logger.info("@dxc-info:" + "Customer with id " + cust_Id + " deleted");
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				logger.error("@dxc-error:" + "Customer Not exist with Customer_id " + cust_Id
						+ " in One Source DB, No deletion needed");
			}

		}

	}

	public static int getReasonId(String reasonCode, boolean flag) {
		Connection con = getConnection();
		int exemptReasonId = -1;
		if (flag) {
			String sql = "select   EXEMPT_REASON_ID  from  tb_exempt_reasons where upper(DESCRIPTION)=?";

			PreparedStatement pst;
			try {
				pst = con.prepareStatement(sql);
				pst.setString(1, reasonCode);
				ResultSet rs = pst.executeQuery();
				if (rs.next()) {
					exemptReasonId = rs.getInt(1);
				}
				rs.close();
				pst.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return exemptReasonId;

	}

	public static int getExemptCertId(boolean flag) {
		Connection con = getConnection();
		int maxCertid = -1;
		if (flag) {
			String sql = "select   max(EXEMPT_CERT_ID) from  tb_exempt_certs";

			Statement st;
			try {
				st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				if (rs.next()) {
					maxCertid = rs.getInt(1);
				}
				rs.close();
				st.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return maxCertid + 1;

	}

	public static Date getDateFromString(String date, boolean flag) {
		// 2013-03-20 19:20:11.993364
		// "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
		if (flag) {
			logger.info("@dxc-info:" + date + " " + date.length() + " " + "yyyy-MM-dd HH:mm:ss.SSSSSS".length());
			SimpleDateFormat dformat = getDateFormat(date.length());
			try {
				Date d = dformat.parse(date);
				return new java.sql.Date(d.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return getCurrentDate();

	}

	private static java.sql.Date getCurrentDate() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Date(today.getTime());
	}

	private static Integer getCustomerGroupId(ExemptCertDTO certDTO, boolean flag) {
		int result = -1;
		if (flag) {
			String key = getCustomerGroupIdKey(certDTO);
			if (key == null) {
				key = certDTO.getCustCountry();
				if (key == null) {
					key = "Worldwide";
				}
			}
			return utility.getCustomerGroup().get(key);
		}
		return result;
	}

	public static int updateCertificate(ExemptCertDTO certDTO, boolean flag) {
		int result = -1;
		Long certId = null;
		if (certDTO.getExemptCertificateId() == null) {
			logger.info("@dxc-info:" + " this certificate id :" + certDTO.getExemptCertificateId() + " not valid");
			return 1;
		}

		certId = parseNo(certDTO.getExemptCertificateId());
		if (certId == -1) {
			return -1;
		}

		if (flag) {
			// boolean exist=checkCertificateWithThisId(con, certId, flag)
			Connection con = getConnection();
			boolean exist = checkCertificateWithThisId(certId, flag);
			if (exist) {
				int reasonId = getReasonId(certDTO.getExemptReasonCode(), flag);
				java.sql.Date createdDate = (java.sql.Date) getDateFromString(certDTO.getCertCreated(), flag);
				java.sql.Date expDate = (java.sql.Date) getDateFromString(certDTO.getExpiration_date(), flag);
				// EXEMPT_CERT_ID, CERTIFICATE_NUMBER,EXEMPT_REASON_ID,"
				// +
				// "CEreasonIdRT_USE,STATUS,CONTENT_TYPE,CREATED_BY,FROM_DATE,CREATION_DATE,ACTIVE,LOCKED)
				String sql = "UPDATE tb_exempt_certs  " + "SET CERTIFICATE_NUMBER = ? ," + " EXEMPT_REASON_ID = ?"
						+ ",CREATION_DATE = ? , " + "TO_DATE=?, " + "FULLY_EXEMPT=? ," + "PURCH_REG_TAX_ID=?"
						+ " WHERE EXEMPT_CERT_ID = ?  ";
				try {
					PreparedStatement preparedStatement = con.prepareStatement(sql);
					preparedStatement.setString(1, certDTO.getCertificateNO());
					preparedStatement.setInt(2, reasonId);
					preparedStatement.setDate(3, createdDate);
					preparedStatement.setDate(4, expDate);
					preparedStatement.setString(5, certDTO.getIsFullyExempt());
					preparedStatement.setString(6, certDTO.getTax_id());
					preparedStatement.setLong(7, certId);
					result = preparedStatement.executeUpdate();
					logger.info("@dxc-info:" + "Certificate with id " + certId + " updated");
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				logger.info(
						"@dxc-info:" + "no certificate data with this certificate id :" + certId + " in one source DB");
			}
//		PreparedStatement pSt = con.prepareStatement(sql);
//		pSt.setLong(1, exCertId);
//		pSt.setLong(2, Long.valueOf(certDTO.getCustomerId()));
//		pSt.setString(3, certDTO.getCertificateNO());
//		pSt.setInt(4, reasonId);
//		pSt.setString(5, "B");
//		pSt.setString(6, "A");
//		pSt.setString(7, "US");
//		pSt.setInt(8, 1);
//		pSt.setDate(9, createdDate);
//		pSt.setDate(10, createdDate);
//		pSt.setString(11, "Y");
//		pSt.setString(12, "N");
//		if (pSt.executeUpdate() == 1) {
//			update = 1;
//			System.out.println("Exempt cert details are updated successfully");
//		}
//
//		con.close();
		}
		return result;
	}

	public static SimpleDateFormat getDateFormat(int len) {
		if (len == 19) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else if (len > 22) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		} else if (len == 10) {
			return new SimpleDateFormat("yyyy-MM-dd");
		}

		return null;
	}

	public static Long parseNo(String number) {
		Long no = new Long(-1);
		if (number == null) {
			logger.info("@dxc-info:" + " this  id :" + number + " not valid");
			return no;
		}
		try {
			no = Long.valueOf(number);
		} catch (NumberFormatException e) {
			// It's OK to ignore "e" here because returning a default value is the
			// documented behaviour on invalid input.
			// return def;
			logger.info("@dxc-info:" + " this  id :" + number + " not valid");
			return no;
		}
		return no;
	}

	private static String getCustomerGroupIdKey(ExemptCertDTO certDTO) {
		String key = null;
		if (certDTO.getCertCountry() != null) {
			return certDTO.getCertCountry();
		} else if (certDTO.getCustCountry() != null) {
			return certDTO.getCustCountry();

		}
		return key;
	}

	private static boolean validateStringForCustomerNoAndName(String no) {

		boolean valid = true;
		if (no == null || no.length() < 1) {
			valid = false;
		}
		return valid;

	}

}
