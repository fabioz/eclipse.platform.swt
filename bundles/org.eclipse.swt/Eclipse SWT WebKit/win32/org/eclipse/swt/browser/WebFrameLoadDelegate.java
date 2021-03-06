/*******************************************************************************
 * Copyright (c) 2010, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;


import java.io.*;
import java.net.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.webkit.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class WebFrameLoadDelegate {
	COMObject iWebFrameLoadDelegate;
	int refCount = 0;

	Browser browser;
	String html;
	String url;

	static final String OBJECTNAME_EXTERNAL = "external"; //$NON-NLS-1$

WebFrameLoadDelegate (Browser browser) {
	createCOMInterfaces ();
	this.browser = browser;
}

void addEventHandlers (boolean top) {
	if (top) {
		StringBuffer buffer = new StringBuffer ("window.SWTkeyhandler = function SWTkeyhandler(e) {"); //$NON-NLS-1$
		buffer.append ("try {e.returnValue = HandleWebKitEvent(e.type, e.keyCode, e.charCode, e.altKey, e.ctrlKey, e.shiftKey, e.metaKey);} catch (e) {}};"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('keydown', SWTkeyhandler, true);"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('keypress', SWTkeyhandler, true);"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('keyup', SWTkeyhandler, true);"); //$NON-NLS-1$
		browser.execute (buffer.toString ());

		buffer = new StringBuffer ("window.SWTmousehandler = function SWTmousehandler(e) {"); //$NON-NLS-1$
		buffer.append ("try {e.returnValue = HandleWebKitEvent(e.type, e.screenX, e.screenY, e.detail, e.button + 1, e.altKey, e.ctrlKey, e.shiftKey, e.metaKey, e.relatedTarget != null);} catch (e) {}};"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('mousedown', SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('mouseup', SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('mousemove', SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('mousewheel', SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('dragstart', SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('mouseover', SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('mouseout', SWTmousehandler, true);"); //$NON-NLS-1$

		browser.execute (buffer.toString ());
	} else {
		StringBuffer buffer = new StringBuffer ("for (var i = 0; i < frames.length; i++) {"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('keydown', window.SWTkeyhandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('keypress', window.SWTkeyhandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('keyup', window.SWTkeyhandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('mousedown', window.SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('mouseup', window.SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('mousemove', window.SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('mouseover', window.SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('mouseout', window.SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('mousewheel', window.SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('dragstart', window.SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ('}');
		browser.execute (buffer.toString ());
	}
}

int AddRef () {
	refCount++;
	return refCount;
}

void createCOMInterfaces () {
	iWebFrameLoadDelegate = new COMObject (new int[] {2, 0, 0, 2, 2, 3, 2, 3, 3, 2, 3, 2, 5, 2, 2, 3, 4}) {
		@Override
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		@Override
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		@Override
		public long /*int*/ method3 (long /*int*/[] args) {return didStartProvisionalLoadForFrame (args[0], args[1]);}
		@Override
		public long /*int*/ method4 (long /*int*/[] args) {return COM.E_NOTIMPL;}
		@Override
		public long /*int*/ method5 (long /*int*/[] args) {return didFailProvisionalLoadWithError (args[0], args[1], args[2]);}
		@Override
		public long /*int*/ method6 (long /*int*/[] args) {return didCommitLoadForFrame (args[0], args[1]);}
		@Override
		public long /*int*/ method7 (long /*int*/[] args) {return didReceiveTitle (args[0], args[1], args[2]);}
		@Override
		public long /*int*/ method8 (long /*int*/[] args) {return COM.E_NOTIMPL;}
		@Override
		public long /*int*/ method9 (long /*int*/[] args) {return didFinishLoadForFrame (args[0], args[1]);}
		@Override
		public long /*int*/ method10 (long /*int*/[] args){return COM.E_NOTIMPL;}
		@Override
		public long /*int*/ method11 (long /*int*/[] args){return didChangeLocationWithinPageForFrame (args[0], args[1]);}
		@Override
		public long /*int*/ method12 (long /*int*/[] args){return COM.S_OK;}
		@Override
		public long /*int*/ method13 (long /*int*/[] args){return COM.E_NOTIMPL;}
		@Override
		public long /*int*/ method14 (long /*int*/[] args){return COM.S_OK;}
		@Override
		public long /*int*/ method15 (long /*int*/[] args){return COM.S_OK;}
		@Override
		public long /*int*/ method16 (long /*int*/[] args){return didClearWindowObject (args[0], args[1], args[2], args[3]);}
	};

	/* Callbacks that take double parameters require custom callbacks that instead pass pointers to the doubles. */
	long /*int*/ ppVtable = iWebFrameLoadDelegate.ppVtable;
	long /*int*/[] pVtable = new long /*int*/[1];
	COM.MoveMemory (pVtable, ppVtable, OS.PTR_SIZEOF);
	long /*int*/[] funcs = new long /*int*/[17];
	COM.MoveMemory (funcs, pVtable[0], OS.PTR_SIZEOF * funcs.length);
	funcs[12] = WebKit_win32.willPerformClientRedirectToURL_CALLBACK (funcs[12]);
	COM.MoveMemory (pVtable[0], funcs, OS.PTR_SIZEOF * funcs.length);
}

int didChangeLocationWithinPageForFrame (long /*int*/ webView, long /*int*/ frame) {
	IWebFrame iwebframe = new IWebFrame (frame);
	long /*int*/[] result = new long /*int*/[1];
	int hr = iwebframe.dataSource (result);
	if (hr != COM.S_OK || result[0] == 0) {
		return COM.S_OK;
	}
	IWebDataSource dataSource = new IWebDataSource (result[0]);
	result[0] = 0;
	hr = dataSource.request (result);
	dataSource.Release ();
	if (hr != COM.S_OK || result[0] == 0) {
		return COM.S_OK;
	}
	IWebURLRequest request = new IWebURLRequest (result[0]);
	result[0] = 0;
	hr = request.URL (result);
	request.Release ();
	if (hr != COM.S_OK || result[0] == 0) {
		return COM.S_OK;
	}
	String url2 = WebKit.extractBSTR (result[0]);
	COM.SysFreeString (result[0]);
	if (url2.length() == 0) return COM.S_OK;
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (url2.equals (WebKit.URI_FILEROOT)) {
		url2 = WebKit.ABOUT_BLANK;
	} else {
		int length = WebKit.URI_FILEROOT.length ();
		if (url2.startsWith (WebKit.URI_FILEROOT) && url2.charAt (length) == '#') {
			url2 = WebKit.ABOUT_BLANK + url2.substring (length);
		}
	}
	final Display display = browser.getDisplay ();
	result[0] = 0;
	IWebView iWebView = new IWebView (webView);
	hr = iWebView.mainFrame (result);
	boolean top = false;
	if (hr == COM.S_OK && result[0] != 0) {
		top = frame == result[0];
		new IWebFrame (result[0]).Release ();
	}
	if (top) {
		StatusTextEvent statusText = new StatusTextEvent (browser);
		statusText.display = display;
		statusText.widget = browser;
		statusText.text = url2;
		StatusTextListener[] statusTextListeners = browser.webBrowser.statusTextListeners;
		for (int i = 0; i < statusTextListeners.length; i++) {
			statusTextListeners[i].changed (statusText);
		}
	}

	LocationEvent location = new LocationEvent (browser);
	location.display = display;
	location.widget = browser;
	location.location = url2;
	location.top = top;
	LocationListener[] locationListeners = browser.webBrowser.locationListeners;
	for (int i = 0; i < locationListeners.length; i++) {
		locationListeners[i].changed (location);
	}
	return COM.S_OK;
}

int didClearWindowObject (long /*int*/ webView, long /*int*/ context, long /*int*/ windowScriptObject, long /*int*/ frame) {
	WebKit_win32.JSGlobalContextRetain (context);
	long /*int*/ globalObject = WebKit_win32.JSContextGetGlobalObject (context);
	long /*int*/ privateData = ((WebKit)browser.webBrowser).webViewData;
	long /*int*/ externalObject = WebKit_win32.JSObjectMake (context, WebKit.ExternalClass, privateData);
	byte[] bytes = null;
	try {
		bytes = (OBJECTNAME_EXTERNAL + '\0').getBytes (WebKit.CHARSET_UTF8);
	} catch (UnsupportedEncodingException e) {
		bytes = (OBJECTNAME_EXTERNAL + '\0').getBytes ();
	}
	long /*int*/ name = WebKit_win32.JSStringCreateWithUTF8CString (bytes);
	WebKit_win32.JSObjectSetProperty (context, globalObject, name, externalObject, 0, null);
	WebKit_win32.JSStringRelease (name);

	for (BrowserFunction current : browser.webBrowser.functions.values()) {
		browser.execute (current.functionString);
	}

	IWebView iwebView = new IWebView (webView);
	long /*int*/[] mainFrame = new long /*int*/[1];
	iwebView.mainFrame (mainFrame);
	boolean top = mainFrame[0] == frame;
	new IWebFrame (mainFrame[0]).Release ();
	addEventHandlers (top);
	return COM.S_OK;
}

int didCommitLoadForFrame (long /*int*/ webview, long /*int*/ frame) {
	IWebFrame iWebFrame = new IWebFrame (frame);
	long /*int*/[] result = new long /*int*/[1];
	int hr = iWebFrame.dataSource (result);
	if (hr != COM.S_OK || result[0] == 0) {
		return COM.S_OK;
    }
	IWebDataSource dataSource = new IWebDataSource (result[0]);
	result[0] = 0;
	hr = dataSource.request (result);
	dataSource.Release ();
	if (hr != COM.S_OK || result[0] == 0) {
		return COM.S_OK;
	}
	IWebMutableURLRequest request = new IWebMutableURLRequest (result[0]);
	result[0] = 0;
	hr = request.URL (result);
	request.Release ();
	if (hr != COM.S_OK || result[0] == 0) {
		return COM.S_OK;
	}
	String url2 = WebKit.extractBSTR (result[0]);
	COM.SysFreeString (result[0]);
	if (url2.length () == 0) return COM.S_OK;
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (url2.equals (WebKit.URI_FILEROOT)) {
		url2 = WebKit.ABOUT_BLANK;
	} else {
		int length = WebKit.URI_FILEROOT.length ();
		if (url2.startsWith (WebKit.URI_FILEROOT) && url2.charAt (length) == '#') {
			url2 = WebKit.ABOUT_BLANK + url2.substring (length);
		}
	}
	Display display = browser.getDisplay ();
	result[0] = 0;
	IWebView iwebView = new IWebView (webview);
	hr = iwebView.mainFrame (result);
	boolean top = false;
	if (hr == COM.S_OK && result[0] != 0) {
		top = frame == result[0];
		new IWebFrame (result[0]).Release ();
	}
	if (top) {
		/* reset resource status variables */
		this.url = url2;

		/*
		* Each invocation of setText() causes webView_didCommitLoadForFrame to be invoked
		* twice, once for the initial navigate to about:blank, and once for the auto-navigate
		* to about:blank that WebKit does when loadHTMLString is invoked.  If this is the
		* first webView_didCommitLoadForFrame callback received for a setText() invocation
		* then do not send any events or re-install registered BrowserFunctions.
		*/
		if (url2.startsWith (WebKit.ABOUT_BLANK) && html != null) return COM.S_OK;

		/* re-install registered functions */
		for (BrowserFunction function : browser.webBrowser.functions.values()) {
			browser.webBrowser.execute (function.functionString);
		}

		ProgressEvent progress = new ProgressEvent (browser);
		progress.display = display;
		progress.widget = browser;
		progress.current = 1;
		progress.total = WebKit.MAX_PROGRESS;
		ProgressListener[] progressListeners = browser.webBrowser.progressListeners;
		for (int i = 0; i < progressListeners.length; i++) {
			progressListeners[i].changed (progress);
		}
		if (browser.isDisposed ()) return COM.S_OK;

		StatusTextEvent statusText = new StatusTextEvent (browser);
		statusText.display = display;
		statusText.widget = browser;
		statusText.text = url2;
		StatusTextListener[] statusTextListeners = browser.webBrowser.statusTextListeners;
		for (int i = 0; i < statusTextListeners.length; i++) {
			statusTextListeners[i].changed (statusText);
		}
		if (browser.isDisposed ()) return COM.S_OK;
	}
	LocationEvent location = new LocationEvent (browser);
	location.display = display;
	location.widget = browser;
	location.location = url2;
	location.top = top;
	LocationListener[] locationListeners = browser.webBrowser.locationListeners;
	for (int i = 0; i < locationListeners.length; i++) {
		locationListeners[i].changed (location);
	}
	return COM.S_OK;
}

int didFailProvisionalLoadWithError (long /*int*/ webView, long /*int*/ error, long /*int*/ frame) {
	IWebError iweberror = new IWebError (error);
	int[] errorCode = new int[1];
	int hr = iweberror.code (errorCode);
	if (WebKit_win32.WebURLErrorBadURL < errorCode[0]) return COM.S_OK;

	String failingURLString = null;
	long /*int*/[] failingURL = new long /*int*/[1];
	hr = iweberror.failingURL (failingURL);
	if (hr == COM.S_OK && failingURL[0] != 0) {
		failingURLString = WebKit.extractBSTR (failingURL[0]);
		COM.SysFreeString (failingURL[0]);
	}
	if (failingURLString != null && WebKit_win32.WebURLErrorServerCertificateNotYetValid <= errorCode[0] && errorCode[0] <= WebKit_win32.WebURLErrorSecureConnectionFailed) {
		/* handle invalid certificate error */
		long /*int*/[] result = new long /*int*/[1];
		hr = iweberror.localizedDescription (result);
		if (hr != COM.S_OK || result[0] == 0) {
			return COM.S_OK;
		}
		String description = WebKit.extractBSTR (result[0]);
		COM.SysFreeString (result[0]);

		result[0] = 0;
		hr = iweberror.QueryInterface (WebKit_win32.IID_IWebErrorPrivate, result);
		if (hr != COM.S_OK || result[0] == 0) {
			return COM.S_OK;
		}

		IWebErrorPrivate webErrorPrivate = new IWebErrorPrivate (result[0]);
		result[0] = 0;
		long /*int*/[] certificate = new long /*int*/[1];
		hr = webErrorPrivate.sslPeerCertificate (certificate);
		webErrorPrivate.Release ();
		if (hr != COM.S_OK || certificate[0] == 0) {
			return COM.S_OK;
		}
		if (showCertificateDialog (webView, failingURLString, description, certificate[0])) {
			IWebFrame iWebFrame = new IWebFrame (frame);
			hr = WebKit_win32.WebKitCreateInstance (WebKit_win32.CLSID_WebMutableURLRequest, 0, WebKit_win32.IID_IWebMutableURLRequest, result);
			if (hr != COM.S_OK || result[0] == 0) {
				certificate[0] = 0;
				return COM.S_OK;
			}
			IWebMutableURLRequest request = new IWebMutableURLRequest (result[0]);
			request.setURL (failingURL[0]);
			request.setAllowsAnyHTTPSCertificate ();
			iWebFrame.loadRequest (request.getAddress ());
			request.Release ();
		}
		certificate[0] = 0;
		return COM.S_OK;
	}

	/* handle other types of errors */
	long /*int*/[] result = new long /*int*/[1];
	hr = iweberror.localizedDescription (result);
	if (hr != COM.S_OK || result[0] == 0) {
		return COM.S_OK;
	}
	String description = WebKit.extractBSTR (result[0]);
	COM.SysFreeString (result[0]);
	if (!browser.isDisposed ()) {
		String message = failingURLString != null ? failingURLString + "\n\n" : ""; //$NON-NLS-1$ //$NON-NLS-2$
		message += Compatibility.getMessage ("SWT_Page_Load_Failed", new Object[] {description}); //$NON-NLS-1$
		MessageBox messageBox = new MessageBox (browser.getShell (), SWT.OK | SWT.ICON_ERROR);
		messageBox.setMessage (message);
		messageBox.open ();
	}
	return COM.S_OK;
}

int didFinishLoadForFrame (long /*int*/ webview, long /*int*/ frame) {
	IWebView iWebView = new IWebView (webview);
	long /*int*/[] iWebFrame = new long /*int*/[1];
	int hr = iWebView.mainFrame (iWebFrame);
	if (hr != COM.S_OK || iWebFrame[0] == 0) {
		return COM.S_OK;
	}
	boolean top = frame == iWebFrame[0];
	new IWebFrame (iWebFrame[0]).Release();
	if (!top) return COM.S_OK;

	/*
	 * If html is not null then there is html from a previous setText() call
	 * waiting to be set into the about:blank page once it has completed loading.
	 */
	if (html != null) {
		if (getUrl ().startsWith (WebKit.ABOUT_BLANK)) {
			((WebKit)browser.webBrowser).loadingText = true;
			long /*int*/ string = WebKit.createBSTR (html);
			long /*int*/ URLString;
			if (((WebKit)browser.webBrowser).untrustedText) {
				URLString = WebKit.createBSTR (WebKit.ABOUT_BLANK);
			} else {
				URLString = WebKit.createBSTR (WebKit.URI_FILEROOT);
			}
			IWebFrame mainFrame = new IWebFrame (frame);
			mainFrame.loadHTMLString (string, URLString);
			html = null;
		}
	}

	/*
	* The loadHTMLString() invocation above will trigger a second didFinishLoadForFrame
	* callback when it is completed.  If text was just set into the browser then wait for this
	* second callback to come before sending the title or completed events.
	*/
	if (!((WebKit)browser.webBrowser).loadingText) {
		if (browser.isDisposed ()) return COM.S_OK;
		/*
		* To be consistent with other platforms a title event should be fired when a
		* page has completed loading.  A page with a <title> tag will do this
		* automatically when the didReceiveTitle callback is received.  However a page
		* without a <title> tag will not do this by default, so fire the event
		* here with the page's url as the title.
		*/
		Display display = browser.getDisplay ();
		IWebFrame mainFrame = new IWebFrame (frame);
		long /*int*/[] result = new long /*int*/[1];
		hr = mainFrame.dataSource (result);
		if (hr != COM.S_OK || result[0] == 0) {
			return COM.S_OK;
		}
		IWebDataSource dataSource = new IWebDataSource (result[0]);
		result[0] = 0;
		hr = dataSource.pageTitle (result);
		dataSource.Release ();
		if (hr != COM.S_OK) {
			return COM.S_OK;
		}
		String title = null;
		if (result[0] != 0) {
			title = WebKit.extractBSTR (result[0]);
			COM.SysFreeString (result[0]);
		}
		if (title == null || title.length () == 0) {	/* page has no title */
			TitleEvent newEvent = new TitleEvent (browser);
			newEvent.display = display;
			newEvent.widget = browser;
			newEvent.title = getUrl ();
			TitleListener[] titleListeners = browser.webBrowser.titleListeners;
			for (int i = 0; i < titleListeners.length; i++) {
				titleListeners[i].changed (newEvent);
			}
			if (browser.isDisposed ()) return COM.S_OK;
		}

		ProgressEvent progress = new ProgressEvent (browser);
		progress.display = display;
		progress.widget = browser;
		progress.current = WebKit.MAX_PROGRESS;
		progress.total = WebKit.MAX_PROGRESS;
		ProgressListener[] progressListeners = browser.webBrowser.progressListeners;
		for (int i = 0; i < progressListeners.length; i++) {
			progressListeners[i].completed (progress);
		}
		if (browser.isDisposed ()) return COM.S_OK;
	}
	((WebKit)browser.webBrowser).loadingText = false;
	return COM.S_OK;
}

int didReceiveTitle (long /*int*/ webView, long /*int*/ title, long /*int*/ frame) {
	long /*int*/[] mainFrame = new long /*int*/[1];
	IWebView iWebView = new IWebView (webView);
	int hr = iWebView.mainFrame (mainFrame);
	if (hr != COM.S_OK || frame == 0) {
		return COM.S_OK;
	}
	if (frame == mainFrame[0]) {
		String newTitle = WebKit.extractBSTR (title);
		TitleEvent newEvent = new TitleEvent (browser);
		newEvent.display = browser.getDisplay ();
		newEvent.widget = browser;
		newEvent.title = newTitle;
		TitleListener[] titleListeners = browser.webBrowser.titleListeners;
		for (int i = 0; i < titleListeners.length; i++) {
			titleListeners[i].changed (newEvent);
		}
	}
	new IWebFrame (mainFrame[0]).Release ();
	return COM.S_OK;
}

int didStartProvisionalLoadForFrame (long /*int*/ webView, long /*int*/ frame) {
	return COM.S_OK;
}

void disposeCOMInterfaces () {
	if (iWebFrameLoadDelegate != null) {
		iWebFrameLoadDelegate.dispose ();
		iWebFrameLoadDelegate = null;
	}
}

long /*int*/ getAddress () {
	return iWebFrameLoadDelegate.getAddress ();
}

String getUrl () {
	/* WebKit auto-navigates to about:blank at startup */
	if (url == null || url.length () == 0) return WebKit.ABOUT_BLANK;
	return url;
}

int QueryInterface (long /*int*/ riid, long /*int*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return COM.E_INVALIDARG;
	GUID guid = new GUID ();
	COM.MoveMemory (guid, riid, GUID.sizeof);

	if (COM.IsEqualGUID (guid, COM.IIDIUnknown)) {
		COM.MoveMemory (ppvObject, new long /*int*/[] {iWebFrameLoadDelegate.getAddress ()}, OS.PTR_SIZEOF);
		new IUnknown (iWebFrameLoadDelegate.getAddress ()).AddRef ();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID (guid, WebKit_win32.IID_IWebFrameLoadDelegate)) {
		COM.MoveMemory (ppvObject, new long /*int*/[] {iWebFrameLoadDelegate.getAddress ()}, OS.PTR_SIZEOF);
		new IUnknown (iWebFrameLoadDelegate.getAddress ()).AddRef ();
		return COM.S_OK;
	}

	COM.MoveMemory (ppvObject, new long /*int*/[] {0}, OS.PTR_SIZEOF);
	return COM.E_NOINTERFACE;
}

int Release () {
	refCount--;
	if (refCount == 0) {
		disposeCOMInterfaces ();
	}
	return refCount;
}

boolean showCertificateDialog (long /*int*/ webView, final String failingUrlString, final String description, final long /*int*/ certificate) {
	Shell parent = browser.getShell ();
	final Shell shell = new Shell (parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
	shell.setText (Compatibility.getMessage ("SWT_InvalidCert_Title")); //$NON-NLS-1$
	shell.setLayout (new GridLayout ());
	Label label = new Label (shell, SWT.WRAP);
	String host = null;
	try {
		host = new URL (failingUrlString).getHost ();
	} catch (MalformedURLException e) {
		/* show the url instead */
		host = failingUrlString;
	}
	StringBuffer message = new StringBuffer ("\n"); //$NON-NLS-1$
	message.append (Compatibility.getMessage ("SWT_InvalidCert_Message", new String[] {host})); //$NON-NLS-1$
	message.append ("\n\n"); //$NON-NLS-1$
	message.append (Compatibility.getMessage (description));
	message.append ("\n"); //$NON-NLS-1$
	message.append (Compatibility.getMessage ("SWT_InvalidCert_Connect")); //$NON-NLS-1$
	message.append ("\n"); //$NON-NLS-1$
	label.setText(message.toString ());

	GridData data = new GridData ();
	Monitor monitor = browser.getMonitor ();
	int maxWidth = monitor.getBounds ().width * 2 / 3;
	int width = label.computeSize (SWT.DEFAULT, SWT.DEFAULT).x;
	data.widthHint = Math.min (width, maxWidth);
	data.horizontalAlignment = GridData.FILL;
	data.grabExcessHorizontalSpace = true;
	label.setLayoutData (data);

	final boolean[] result = new boolean[1];
	final Button[] buttons = new Button[3];
	Listener listener = new Listener() {
		@Override
		public void handleEvent (Event event) {
			if (event.widget == buttons[2]) {
				showCertificate (shell, certificate);
			} else {
				result[0] = event.widget == buttons[0];
				shell.close();
			}
		}
	};

	Composite composite = new Composite (shell, SWT.NONE);
	data = new GridData ();
	data.horizontalAlignment = GridData.END;
	composite.setLayoutData (data);
	composite.setLayout (new GridLayout (3, true));
	buttons[0] = new Button (composite, SWT.PUSH);
	buttons[0].setText (SWT.getMessage("SWT_Continue")); //$NON-NLS-1$
	buttons[0].setLayoutData (new GridData (GridData.FILL_HORIZONTAL));
	buttons[0].addListener (SWT.Selection, listener);
	buttons[1] = new Button (composite, SWT.PUSH);
	buttons[1].setText (SWT.getMessage("SWT_Cancel")); //$NON-NLS-1$
	buttons[1].setLayoutData (new GridData (GridData.FILL_HORIZONTAL));
	buttons[1].addListener (SWT.Selection, listener);
	buttons[2] = new Button (composite, SWT.PUSH);
	buttons[2].setText (SWT.getMessage("SWT_ViewCertificate")); //$NON-NLS-1$
	buttons[2].setLayoutData (new GridData (GridData.FILL_HORIZONTAL));
	buttons[2].addListener (SWT.Selection, listener);

	shell.setDefaultButton (buttons[0]);
	shell.pack ();

	Rectangle parentSize = parent.getBounds ();
	Rectangle shellSize = shell.getBounds ();
	int x = parent.getLocation ().x + (parentSize.width - shellSize.width) / 2;
	int y = parent.getLocation ().y + (parentSize.height - shellSize.height) / 2;
	shell.setLocation (x, y);
	shell.open ();
	Display display = browser.getDisplay ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	return result[0];
}

void showCertificate (Shell parent, long /*int*/ certificate) {
	CERT_CONTEXT context = new CERT_CONTEXT ();
	OS.MoveMemory (context, certificate, CERT_CONTEXT.sizeof);
	CERT_INFO info = new CERT_INFO ();
	OS.MoveMemory (info, context.pCertInfo, CERT_INFO.sizeof);

	int length = OS.CertNameToStr (OS.X509_ASN_ENCODING, info.Issuer, OS.CERT_SIMPLE_NAME_STR, null, 0);
	TCHAR tchar = new TCHAR (0, length);
	OS.CertNameToStr (OS.X509_ASN_ENCODING, info.Issuer, OS.CERT_SIMPLE_NAME_STR, tchar, length);
	String issuer = tchar.toString (0, tchar.strlen ());

	length = OS.CertNameToStr (OS.X509_ASN_ENCODING, info.Subject, OS.CERT_SIMPLE_NAME_STR, null, 0);
	tchar = new TCHAR (0, length);
	OS.CertNameToStr (OS.X509_ASN_ENCODING, info.Subject, OS.CERT_SIMPLE_NAME_STR, tchar, length);
	String subject = tchar.toString (0, tchar.strlen ());

	final String SEPARATOR_DATE = "/"; //$NON-NLS-1$
	final String SEPARATOR_TIME = ":"; //$NON-NLS-1$
	SYSTEMTIME systemTime = new SYSTEMTIME ();
	OS.FileTimeToSystemTime (info.NotBefore, systemTime);
	String validFrom = systemTime.wDay + SEPARATOR_DATE + systemTime.wMonth + SEPARATOR_DATE + systemTime.wYear;
	String validFromTime = systemTime.wHour + SEPARATOR_TIME + systemTime.wMinute + SEPARATOR_TIME + systemTime.wSecond;

	systemTime = new SYSTEMTIME ();
	OS.FileTimeToSystemTime (info.NotAfter, systemTime);
	String validTo = systemTime.wDay + SEPARATOR_DATE + systemTime.wMonth + SEPARATOR_DATE + systemTime.wYear;
	String validToTime = systemTime.wHour + SEPARATOR_TIME + systemTime.wMinute + SEPARATOR_TIME + systemTime.wSecond;

	length = info.SerialNumber.cbData;
	byte[] serialNumber = new byte[length];
	OS.MoveMemory (serialNumber, info.SerialNumber.pbData, length);
	String hexSerialNumber = new String ();
	for (int i = length - 1; i >= 0; i--) {
		int number = 0xFF & serialNumber[i];
		String hex = Integer.toHexString (number);
		if (hex.length () == 1) hexSerialNumber += "0"; //$NON-NLS-1$
		hexSerialNumber += hex + " "; //$NON-NLS-1$
	}

	final Shell dialog = new Shell (parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
	dialog.setText (SWT.getMessage ("SWT_Certificate")); //$NON-NLS-1$
	dialog.setLayout (new GridLayout (1, false));

	TabFolder tabFolder = new TabFolder (dialog, SWT.NONE);
	tabFolder.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true, 1, 1));
	tabFolder.setLayout (new FillLayout ());

	TabItem general = new TabItem (tabFolder, SWT.NONE);
	general.setText (SWT.getMessage ("SWT_General")); //$NON-NLS-1$
	Composite composite = new Composite (tabFolder, SWT.BORDER);
	composite.setLayout (new GridLayout (1, false));
	Label issuedTo = new Label (composite, SWT.NONE);
	issuedTo.setLayoutData (new GridData (SWT.BEGINNING, SWT.CENTER, false, false));
	issuedTo.setText (Compatibility.getMessage ("SWT_IssuedTo", new Object[] {subject})); //$NON-NLS-1$
	Label issuedBy = new Label (composite, SWT.NONE);
	issuedBy.setLayoutData (new GridData (SWT.BEGINNING, SWT.CENTER, false, false));
	issuedBy.setText (Compatibility.getMessage ("SWT_IssuedFrom", new Object[] {issuer}));  //$NON-NLS-1$
	Label valid = new Label (composite, SWT.NONE);
	valid.setLayoutData (new GridData (SWT.BEGINNING, SWT.CENTER, false, false));
	valid.setText (Compatibility.getMessage ("SWT_ValidFromTo", new Object[] {validFrom, validTo})); //$NON-NLS-1$
	general.setControl (composite);

	TabItem details = new TabItem (tabFolder, SWT.NONE);
	details.setText (SWT.getMessage ("SWT_Details")); //$NON-NLS-1$
	Table table = new Table (tabFolder, SWT.SINGLE | SWT.BORDER| SWT.FULL_SELECTION);
	table.setHeaderVisible (true);
	TableColumn tableColumn = new TableColumn (table, SWT.LEAD);
	tableColumn.setText (SWT.getMessage ("SWT_Field")); //$NON-NLS-1$
	tableColumn = new TableColumn (table, SWT.NONE);
	tableColumn.setText (SWT.getMessage ("SWT_Value")); //$NON-NLS-1$
	TableItem tableItem = new TableItem(table, SWT.NONE);
	String version = "V" + String.valueOf (info.dwVersion + 1); //$NON-NLS-1$
	tableItem.setText (new String[]{SWT.getMessage ("SWT_Version"), version}); //$NON-NLS-1$
	tableItem = new TableItem (table, SWT.NONE);
	tableItem.setText (new String[] {SWT.getMessage ("SWT_SerialNumber"), hexSerialNumber}); //$NON-NLS-1$
	tableItem = new TableItem (table, SWT.NONE);
	tableItem.setText (new String[] {SWT.getMessage ("SWT_Issuer"), issuer}); //$NON-NLS-1$

	tableItem = new TableItem (table, SWT.NONE);
	StringBuffer stringBuffer2 = new StringBuffer ();
	stringBuffer2.append (validFrom);
	stringBuffer2.append (", "); //$NON-NLS-1$
	stringBuffer2.append (validFromTime);
	stringBuffer2.append (" GMT"); //$NON-NLS-1$
	tableItem.setText (new String[] {SWT.getMessage ("SWT_ValidFrom"), stringBuffer2.toString ()}); //$NON-NLS-1$

	tableItem = new TableItem (table, SWT.NONE);
	StringBuffer stringBuffer = new StringBuffer ();
	stringBuffer.append (validTo);
	stringBuffer.append (", "); //$NON-NLS-1$
	stringBuffer.append (validToTime);
	stringBuffer.append (" GMT"); //$NON-NLS-1$
	tableItem.setText (new String[] {SWT.getMessage ("SWT_ValidTo"), stringBuffer.toString ()}); //$NON-NLS-1$

	tableItem = new TableItem (table, SWT.NONE);
	tableItem.setText (new String[] {SWT.getMessage ("SWT_Subject"), subject}); //$NON-NLS-1$
	for (int i = 0; i < table.getColumnCount (); i++) {
		table.getColumn (i).pack ();
	}
	details.setControl (table);

	Button ok = new Button (dialog, SWT.PUSH);
	GridData layoutData = new GridData (SWT.END, SWT.CENTER, false, false);
	layoutData.widthHint = 75;
	ok.setLayoutData (layoutData);
	ok.setText (SWT.getMessage ("SWT_OK")); //$NON-NLS-1$
	ok.addSelectionListener (new SelectionAdapter() {
		@Override
		public void widgetSelected (SelectionEvent e) {
			dialog.dispose ();
		}
	});

	dialog.setDefaultButton (ok);
	dialog.pack ();
	Rectangle parentSize = parent.getBounds ();
	Rectangle dialogSize = dialog.getBounds ();
	int x = parent.getLocation ().x + (parentSize.width - dialogSize.width) / 2;
	int y = parent.getLocation ().y + (parentSize.height - dialogSize.height) / 2;
	dialog.setLocation (x, y);
	dialog.open ();
	Display display = browser.getDisplay ();
	while (!dialog.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}

}

}
