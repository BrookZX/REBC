package odata.rebc.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import odata.rebc.model.OModel;
import odata.rebc.model.REHouse;
import odata.rebc.model.REResp;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
//import org.apache.olingo.commons.api.edm.EdmBindingTarget;
//import org.apache.olingo.commons.api.edm.EdmEntitySet;
//import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
//import org.apache.olingo.commons.api.http.HttpMethod;
//import org.apache.olingo.commons.api.http.HttpStatusCode;
//import org.apache.olingo.server.api.ODataApplicationException;
//import org.apache.olingo.server.api.uri.UriInfo;
//import org.apache.olingo.server.api.uri.UriResource;
//import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class REBCUtil {
	// public static String convert
	private static final Logger LOG = LoggerFactory.getLogger(REBCUtil.class);
	private static SocketFactoryHttpClientFactory clientFactory = new SocketFactoryHttpClientFactory();
	private static String APIKey = "rdx1dbFRnGKg0h10OD4CV8bJcLQ8qPcavXqUzjOKWgLXGkTTDxmWyAl2jKj0FMbzb7MPt6w7DGIm84uWOQTtirC3FqI1NSezbe0jPTRHHK0Uo4TGgGfYKWWhFxolvynN69WiPYfLh998BdzhMKezOg5t8jLdCf6Fpk1ygaXDhlOXPtF1YKUd8IrZB57FMtGjdn0ZxaxLPhC9CLw7M1xNSgdbjVQIGPlMAsxCvns4OqS607xKdroHwIUEGkWQrxw44dMDtf73WHk8WSJqveq0bD3Y2939dB8Dq16XtdVsJd3EAwJAxDBbThejCZzrZVaBY11F8IKb9GJl1yaiwkz4wLwp8B5zhkVgciF85JBCR0qdCgU78mOuuZfDORpFUtaUdsnMd1OXmOkkDfnbmMvX7XOwa7NXXbyviOJCduxwjgBKx4Dczz2WZi6wUv4g5PxQBg1EeGGlE1BH59I2NQnDynPzJyYpTop2NtXqdHv17zZlVxVMP0WBFz5SwDrhp6aK";
	public static String BCUrl = "https://hyperledger-api.cfapps.sap.hana.ondemand.com/invoke";
	public static String chainCodeID = "0254ec5583f2eff85fcad645c8f093d7";

	public static ArrayList<REHouse> readREHouseWithProject(String project) {
		ArrayList<REHouse> houseList = null;

		String response = httpGetIgnoreSSL(getREUrl(project));

		ObjectMapper mapper = new ObjectMapper();

		REResp resp;
		try {
			resp = mapper.readValue(response, REResp.class);
			houseList = (ArrayList<REHouse>) resp.d.results;
			System.out.println(houseList.size());
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return houseList;
	}

	private static String httpGetIgnoreSSL(String url) {

		HttpGet httpGet = new HttpGet(url);

		URI uri = null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpClient httpClient = clientFactory.create("GET", uri);
		// response
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
		} catch (Exception e) {
		}

		// get response into String
		String temp = "";
		try {
			HttpEntity entity = response.getEntity();
			temp = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
		}

		return temp;
	}

	public static String httpPost(String url, String json) throws IOException {
		URL realUrl = new URL(url);
		// 打开和URL之间的连接
		URLConnection conn = realUrl.openConnection();
		// 设置通用的请求属性
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Content-type",
				"application/json; charset=utf-8");
		conn.setRequestProperty("apikey", APIKey);
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);

		DataOutputStream printout = new DataOutputStream(conn.getOutputStream());
		printout.writeChars(json);
		printout.flush();
		printout.close();

		InputStream instr = conn.getInputStream();
		byte[] bis = IOUtils.toByteArray(instr);
		String responseString = new String(bis, "UTF-8");

		return responseString;
	}

	public static String httpPostIgnoreSSL(String url, String json) {
		HttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);

		httpPost.addHeader("Content-type", "application/json; charset=utf-8");
		httpPost.setHeader("Accept", "application/json");

		httpPost.setHeader("Connection", "Close");
		httpPost.setHeader("apikey", APIKey);
		URI uri = null;
		try {
			uri = new URI(url);

		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// HttpClient httpClient = clientFactory.create(HttpMethod.POST, uri);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			StringEntity requestEntity = new StringEntity(json, "utf-8");
			httpPost.setEntity(requestEntity);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			response = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String temp = "";
		String output = "";
		try {
			//solve chinese charactor encoding
			InputStream is = response.getEntity().getContent();
			InputStreamReader inputReader = new InputStreamReader(is, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputReader);
			
			while ((temp = bufferedReader.readLine()) != null) {
				output = output + temp;
			}
			 
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LOG.info(output);
		
		if (response.getStatusLine().getStatusCode() == 200) {
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			
			return output;
		} else {
			return null;
		}

	}

	private static String getREUrl(String project) {
		StringBuffer sf = new StringBuffer(
				"https://ldcix3u.mo.sap.corp:44311/sap/opu/odata/sap/ZREBC_SRV/EtHouseListSet?$format=json&$filter=Aoid%20eq%20%27");
		String end = "%27";

		String result = null;
		try {
			result = java.net.URLEncoder.encode(project, "utf-8");
			sf.append(result);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sf.append(end);

		return sf.toString();
	}

//	public static EdmEntitySet getNavigationTargetEntitySet(
//			EdmEntitySet startEdmEntitySet,
//			EdmNavigationProperty edmNavigationProperty)
//			throws ODataApplicationException {
//
//		EdmEntitySet navigationTargetEntitySet = null;
//
//		String navPropName = edmNavigationProperty.getName();
//		EdmBindingTarget edmBindingTarget = startEdmEntitySet
//				.getRelatedBindingTarget(navPropName);
//		if (edmBindingTarget == null) {
//			throw new ODataApplicationException("Not supported.",
//					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
//		}
//
//		if (edmBindingTarget instanceof EdmEntitySet) {
//			navigationTargetEntitySet = (EdmEntitySet) edmBindingTarget;
//		} else {
//			throw new ODataApplicationException("Not supported.",
//					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
//		}
//
//		return navigationTargetEntitySet;
//	}
//
//	public static EdmEntitySet getEdmEntitySet(UriInfo uriInfo)
//			throws ODataApplicationException {
//		List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
//		// To get the entity set we have to interpret all URI segments
//		if (!(resourcePaths.get(0) instanceof UriResourceEntitySet)) {
//			throw new ODataApplicationException(
//					"Invalid resource type for first segment.",
//					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
//					Locale.ENGLISH);
//		}
//
//		UriResourceEntitySet uriResource = (UriResourceEntitySet) resourcePaths
//				.get(0);
//
//		return uriResource.getEntitySet();
//	}

}
