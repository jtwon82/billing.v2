package com.adgather.service.sdk.gather.batch;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractDayBatch { 
	private static final Logger logger = LoggerFactory.getLogger(AbstractDayBatch.class);
	
	protected IBatch batch = null;
	
	protected void buildBatch(IBuildBatch buildBatch) {
		List<IBatch> batchs = buildBatch.addBatch(new ArrayList<IBatch>());
		for(int i=0; i<batchs.size(); i++) {
			if(i < batchs.size()-1) {
				batchs.get(i).setBatch(batchs.get(i+1));
			}
		}
		batch = batchs.get(0);
	}

	public void addAdid(final SourceBean bean) {
//		System.out.println(bean.getGoogle_aid() + " : " + bean.getIos_ifa());
//		final ADIDMapping mapping = new ADIDMapping();
//		try {
//			mapping.build(null, new IParameter() {
//				@Override
//				public Map execute(Map parameter) {
//					// TODO Auto-generated method stub
////					bean.setOs(getOS(bean.getGoogle_aid()));
//					parameter.put("adid", bean.getAdid());
//					parameter.put("os", bean.getOs());
//					return parameter;
//				}
//			});
//			bean.setAuid(mapping.getAuid()); 
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			logger.error(e.getMessage(), e);
//		}
	}
	
	public void addShopLog(SourceBean bean) {
//		UserAuthIDResult userAuthIDResult = new UserAuthIDResult();
//		userAuthIDResult.setNewAuId(bean.getAuid());
//		userAuthIDResult.setNowAuId(bean.getAuid());
//		ConsumerInclinations ci = new ConsumerInclinations();
//		
//		for(Iterator it = bean.getShopLog().keySet().iterator(); it.hasNext();) {
//			String key = String.valueOf(it.next());
//			logger.debug(Thread.currentThread().getName() + "#" + key + " : " + bean.getShopLog().get(key));
//		}
//		
//		ci.save(res);
	}
	
//	public String getOS(String aid) {
//		return (aid == null || aid.length() == 0) ? "ios" : "android";
//	}
	
	public interface IBuildBatch {
		public List<IBatch> addBatch(List<IBatch> batchs);
	}
	
//	public static HttpServletResponse res = new HttpServletResponse() {
//
//		@Override
//		public void flushBuffer() throws IOException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public int getBufferSize() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public String getCharacterEncoding() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getContentType() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public Locale getLocale() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public ServletOutputStream getOutputStream() throws IOException {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public PrintWriter getWriter() throws IOException {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public boolean isCommitted() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public void reset() {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void resetBuffer() {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void setBufferSize(int arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void setCharacterEncoding(String arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void setContentLength(int arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void setContentLengthLong(long arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void setContentType(String arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void setLocale(Locale arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void addCookie(Cookie arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void addDateHeader(	String arg0,
//									long arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void addHeader(	String arg0,
//								String arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void addIntHeader(	String arg0,
//									int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public boolean containsHeader(String arg0) {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public String encodeRedirectURL(String arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String encodeRedirectUrl(String arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String encodeURL(String arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String encodeUrl(String arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getHeader(String arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public Collection<String> getHeaderNames() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public Collection<String> getHeaders(String arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public int getStatus() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public void sendError(int arg0) throws IOException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void sendError(	int arg0,
//								String arg1) throws IOException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void sendRedirect(String arg0) throws IOException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void setDateHeader(	String arg0,
//									long arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void setHeader(	String arg0,
//								String arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void setIntHeader(	String arg0,
//									int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void setStatus(int arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void setStatus(	int arg0,
//								String arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		
//	};
//
//	public static HttpServletRequest req = new HttpServletRequest() {
//
//		@Override
//		public AsyncContext getAsyncContext() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public Object getAttribute(String arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public Enumeration<String> getAttributeNames() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getCharacterEncoding() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public int getContentLength() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public long getContentLengthLong() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public String getContentType() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public DispatcherType getDispatcherType() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public ServletInputStream getInputStream() throws IOException {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getLocalAddr() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getLocalName() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public int getLocalPort() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public Locale getLocale() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public Enumeration<Locale> getLocales() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getParameter(String arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public Map<String, String[]> getParameterMap() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public Enumeration<String> getParameterNames() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String[] getParameterValues(String arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getProtocol() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public BufferedReader getReader() throws IOException {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getRealPath(String arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getRemoteAddr() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getRemoteHost() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public int getRemotePort() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public RequestDispatcher getRequestDispatcher(String arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getScheme() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getServerName() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public int getServerPort() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public ServletContext getServletContext() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public boolean isAsyncStarted() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public boolean isAsyncSupported() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public boolean isSecure() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public void removeAttribute(String arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void setAttribute(	String arg0,
//									Object arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void setCharacterEncoding(String arg0)
//														throws UnsupportedEncodingException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public AsyncContext startAsync() throws IllegalStateException {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public AsyncContext startAsync(	ServletRequest arg0,
//										ServletResponse arg1)
//																throws IllegalStateException {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public boolean authenticate(HttpServletResponse arg0)
//																throws IOException,
//																ServletException {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public String changeSessionId() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getAuthType() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getContextPath() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public Cookie[] getCookies() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public long getDateHeader(String arg0) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public String getHeader(String arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public Enumeration<String> getHeaderNames() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public Enumeration<String> getHeaders(String arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public int getIntHeader(String arg0) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public String getMethod() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public Part getPart(String arg0) throws IOException, ServletException {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public Collection<Part> getParts() throws IOException, ServletException {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getPathInfo() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getPathTranslated() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getQueryString() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getRemoteUser() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getRequestURI() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public StringBuffer getRequestURL() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getRequestedSessionId() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getServletPath() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public HttpSession getSession() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public HttpSession getSession(boolean arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public Principal getUserPrincipal() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public boolean isRequestedSessionIdFromCookie() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public boolean isRequestedSessionIdFromURL() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public boolean isRequestedSessionIdFromUrl() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public boolean isRequestedSessionIdValid() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public boolean isUserInRole(String arg0) {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public void login(	String arg0,
//							String arg1) throws ServletException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void logout() throws ServletException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public <T extends HttpUpgradeHandler> T upgrade(Class<T> arg0)
//																		throws IOException,
//																		ServletException {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//		
//	};
}
