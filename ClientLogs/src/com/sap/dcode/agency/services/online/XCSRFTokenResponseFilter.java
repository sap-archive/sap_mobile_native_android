package com.sap.dcode.agency.services.online;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.sap.dcode.agency.services.logs.AgencyLogManager;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.httpc.filters.IResponseFilter;
import com.sap.smp.client.httpc.filters.IResponseFilterChain;

public class XCSRFTokenResponseFilter implements IResponseFilter {
	private static XCSRFTokenResponseFilter instance;

	private Context context;
	private XCSRFTokenRequestFilter requestFilter;

	
	private XCSRFTokenResponseFilter(Context context, XCSRFTokenRequestFilter requestFilter) {
		this.context = context;
		this.requestFilter = requestFilter;
	}

	/**
	 * @return XCSRFTokenResponseFilter
	 */
	public static XCSRFTokenResponseFilter getInstance(Context context, XCSRFTokenRequestFilter requestFilter) {
		if (instance == null) {
			instance = new XCSRFTokenResponseFilter(context, requestFilter);
		}
		return instance;
	}


	@Override
	public Object filter(IReceiveEvent event, IResponseFilterChain chain)
			throws IOException {

		List<String> xcsrfTokens = event.getResponseHeaders().get("X-CSRF-Token");
		Log.i("XCSRFTokenResponseFilter", "xcsrfTokens: " + xcsrfTokens);
		if (xcsrfTokens != null) {
			String xcsrfToken = xcsrfTokens.get(0);
			if (xcsrfToken != null)
				requestFilter.setLastXCSRFToken(xcsrfToken);
		}

		//TODO 5-9 WRITE event information in logs
		//BEGIN
		//END

		return chain.filter();
	}

	@Override
	public Object getDescriptor() {
		return "XCSRFTokenResponseFilter";
	}

}
