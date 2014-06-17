package org.openkoala.koalacommons.resourceloader.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class VfsUtils {

	private static final Log logger = LogFactory.getLog(VfsUtils.class);

	private static final String VFS2_PKG = "org.jboss.virtual.";
	private static final String VFS3_PKG = "org.jboss.vfs.";
	private static final String VFS_NAME = "VFS";

	private static enum VFS_VER { V2, V3 }

	private static VFS_VER version;

	private static Method VFS_METHOD_GET_ROOT_URL = null;
	private static Method VFS_METHOD_GET_ROOT_URI = null;

	private static Method VIRTUAL_FILE_METHOD_EXISTS = null;
	private static Method VIRTUAL_FILE_METHOD_GET_INPUT_STREAM;
	private static Method VIRTUAL_FILE_METHOD_GET_SIZE;
	private static Method VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED;
	private static Method VIRTUAL_FILE_METHOD_TO_URL;
	private static Method VIRTUAL_FILE_METHOD_TO_URI;
	private static Method VIRTUAL_FILE_METHOD_GET_NAME;
	private static Method VIRTUAL_FILE_METHOD_GET_PATH_NAME;
	private static Method VIRTUAL_FILE_METHOD_GET_CHILD;

	protected static Class<?> VIRTUAL_FILE_VISITOR_INTERFACE;
	protected static Method VIRTUAL_FILE_METHOD_VISIT;

	private static Method VFS_UTILS_METHOD_IS_NESTED_FILE = null;
	private static Method VFS_UTILS_METHOD_GET_COMPATIBLE_URI = null;
	private static Field VISITOR_ATTRIBUTES_FIELD_RECURSE = null;
	private static Method GET_PHYSICAL_FILE = null;

	static {
		ClassLoader loader = VfsUtils.class.getClassLoader();
		String pkg;
		Class<?> vfsClass;

		// check for JBoss 6
		try {
			vfsClass = loader.loadClass(VFS3_PKG + VFS_NAME);
			version = VFS_VER.V3;
			pkg = VFS3_PKG;

			if (logger.isDebugEnabled()) {
				logger.debug("JBoss VFS packages for JBoss AS 6 found");
			}
		}
		catch (ClassNotFoundException ex) {
			// fallback to JBoss 5
			if (logger.isDebugEnabled())
				logger.debug("JBoss VFS packages for JBoss AS 6 not found; falling back to JBoss AS 5 packages");
			try {
				vfsClass = loader.loadClass(VFS2_PKG + VFS_NAME);

				version = VFS_VER.V2;
				pkg = VFS2_PKG;

				if (logger.isDebugEnabled())
					logger.debug("JBoss VFS packages for JBoss AS 5 found");
			}
			catch (ClassNotFoundException ex2) {
				logger.error("JBoss VFS packages (for both JBoss AS 5 and 6) were not found - JBoss VFS support disabled");
				throw new IllegalStateException("Cannot detect JBoss VFS packages", ex2);
			}
		}

		// cache reflective information
		try {
			String methodName = (VFS_VER.V3.equals(version) ? "getChild" : "getRoot");

			VFS_METHOD_GET_ROOT_URL = ReflectionUtils.findMethod(vfsClass, methodName, URL.class);
			VFS_METHOD_GET_ROOT_URI = ReflectionUtils.findMethod(vfsClass, methodName, URI.class);

			Class<?> virtualFile = loader.loadClass(pkg + "VirtualFile");

			VIRTUAL_FILE_METHOD_EXISTS = ReflectionUtils.findMethod(virtualFile, "exists");
			VIRTUAL_FILE_METHOD_GET_INPUT_STREAM = ReflectionUtils.findMethod(virtualFile, "openStream");
			VIRTUAL_FILE_METHOD_GET_SIZE = ReflectionUtils.findMethod(virtualFile, "getSize");
			VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED = ReflectionUtils.findMethod(virtualFile, "getLastModified");
			VIRTUAL_FILE_METHOD_TO_URI = ReflectionUtils.findMethod(virtualFile, "toURI");
			VIRTUAL_FILE_METHOD_TO_URL = ReflectionUtils.findMethod(virtualFile, "toURL");
			VIRTUAL_FILE_METHOD_GET_NAME = ReflectionUtils.findMethod(virtualFile, "getName");
			VIRTUAL_FILE_METHOD_GET_PATH_NAME = ReflectionUtils.findMethod(virtualFile, "getPathName");
			GET_PHYSICAL_FILE = ReflectionUtils.findMethod(virtualFile, "getPhysicalFile");

			methodName = (VFS_VER.V3.equals(version) ? "getChild" : "findChild");

			VIRTUAL_FILE_METHOD_GET_CHILD = ReflectionUtils.findMethod(virtualFile, methodName, String.class);

			Class<?> utilsClass = loader.loadClass(pkg + "VFSUtils");

			VFS_UTILS_METHOD_GET_COMPATIBLE_URI = ReflectionUtils.findMethod(utilsClass, "getCompatibleURI",
					virtualFile);
			VFS_UTILS_METHOD_IS_NESTED_FILE = ReflectionUtils.findMethod(utilsClass, "isNestedFile", virtualFile);

			VIRTUAL_FILE_VISITOR_INTERFACE = loader.loadClass(pkg + "VirtualFileVisitor");
			VIRTUAL_FILE_METHOD_VISIT = ReflectionUtils.findMethod(virtualFile, "visit", VIRTUAL_FILE_VISITOR_INTERFACE);

			Class<?> visitorAttributesClass = loader.loadClass(pkg + "VisitorAttributes");
			VISITOR_ATTRIBUTES_FIELD_RECURSE = ReflectionUtils.findField(visitorAttributesClass, "RECURSE");
		}
		catch (ClassNotFoundException ex) {
			throw new IllegalStateException("Could not detect the JBoss VFS infrastructure", ex);
		}
	}

	protected static Object invokeVfsMethod(Method method, Object target, Object... args) throws IOException {
		try {
			return method.invoke(target, args);
		}
		catch (InvocationTargetException ex) {
			Throwable targetEx = ex.getTargetException();
			if (targetEx instanceof IOException) {
				throw (IOException) targetEx;
			}
			ReflectionUtils.handleInvocationTargetException(ex);
		}
		catch (Exception ex) {
			ReflectionUtils.handleReflectionException(ex);
		}

		throw new IllegalStateException("Invalid code path reached");
	}

	public static boolean exists(Object vfsResource) {
		try {
			return (Boolean) invokeVfsMethod(VIRTUAL_FILE_METHOD_EXISTS, vfsResource);
		}
		catch (IOException ex) {
			return false;
		}
	}

	public static boolean isReadable(Object vfsResource) {
		try {
			return ((Long) invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_SIZE, vfsResource) > 0);
		}
		catch (IOException ex) {
			return false;
		}
	}

	public static long getSize(Object vfsResource) throws IOException {
		return (Long) invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_SIZE, vfsResource);
	}

	public static long getLastModified(Object vfsResource) throws IOException {
		return (Long) invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED, vfsResource);
	}

	public static InputStream getInputStream(Object vfsResource) throws IOException {
		return (InputStream) invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_INPUT_STREAM, vfsResource);
	}

	public static URL getURL(Object vfsResource) throws IOException {
		return (URL) invokeVfsMethod(VIRTUAL_FILE_METHOD_TO_URL, vfsResource);
	}

	public static URI getURI(Object vfsResource) throws IOException {
		return (URI) invokeVfsMethod(VIRTUAL_FILE_METHOD_TO_URI, vfsResource);
	}

	public static String getName(Object vfsResource) {
		try {
			return (String) invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_NAME, vfsResource);
		}
		catch (IOException ex) {
			throw new IllegalStateException("Cannot get resource name", ex);
		}
	}

	public static Object getRelative(URL url) throws IOException {
		return invokeVfsMethod(VFS_METHOD_GET_ROOT_URL, null, url);
	}

	public static Object getChild(Object vfsResource, String path) throws IOException {
		return invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_CHILD, vfsResource, path);
	}

	public static File getFile(Object vfsResource) throws IOException {
		if (VFS_VER.V2.equals(version)) {
			if ((Boolean) invokeVfsMethod(VFS_UTILS_METHOD_IS_NESTED_FILE, null, vfsResource)) {
				throw new IOException("File resolution not supported for nested resource: " + vfsResource);
			}
			try {
				return new File((URI) invokeVfsMethod(VFS_UTILS_METHOD_GET_COMPATIBLE_URI, null, vfsResource));
			}
			catch (Exception ex) {
				throw new IOException("Failed to obtain File reference for " + vfsResource, ex);
			}
		}
		else {
			return (File) invokeVfsMethod(GET_PHYSICAL_FILE, vfsResource);
		}
	}

	public static Object getRoot(URI url) throws IOException {
		return invokeVfsMethod(VFS_METHOD_GET_ROOT_URI, null, url);
	}

	// protected methods used by the support sub-package

	public static Object getRoot(URL url) throws IOException {
		return invokeVfsMethod(VFS_METHOD_GET_ROOT_URL, null, url);
	}

	protected static Object doGetVisitorAttribute() {
		return ReflectionUtils.getField(VISITOR_ATTRIBUTES_FIELD_RECURSE, null);
	}

	protected static String doGetPath(Object resource) {
		return (String) ReflectionUtils.invokeMethod(VIRTUAL_FILE_METHOD_GET_PATH_NAME, resource);
	}
	
}
