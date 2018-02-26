package odata.rebc.util;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import odata.rebc.model.BRequestObj;
import odata.rebc.model.REHouse;

public class testClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// ArrayList<REHouse> houses =
		// REBCUtil.readREHouseWithProject(REBCUtil.targetURL, "Z/F000-400");

		// String json = REBCUtil.get("Z/F000-400");
		// System.out.println(json);

		// String response =
		// REBCUtil.httpGetIgnoreSSL("https://ldcix3u.mo.sap.corp:44311/sap/opu/odata/sap/ZREBC_SRV/EtHouseListSet?$filter=Aoid%20eq%20%27Z%2F000-400%27&$format=json");
		// //System.out.println(response);
		//
		// ObjectMapper mapper = new ObjectMapper();
		//
		// try {
		// REResp resp = mapper.readValue(response, REResp.class);
		// System.out.println(resp.d.results.size());
		// } catch (JsonParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (JsonMappingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// ArrayList<REHouse> houses =
		// REBCUtil.readREHouseWithProject("Z/000-400");
		// System.out.println(houses.size());

		BRequestObj req = new BRequestObj();
		req.chaincodeId = REBCUtil.chainCodeID;
		req.fcn =  "read";
		
		 req.args.add("12");
		 req.args.add("equal");
//		 req.args.add("长泰广场");
//		 req.args.add("金科路2557号");
//		 req.args.add("长甲地产");
//		 req.args.add("长甲地产");
		ObjectMapper mapper = new ObjectMapper(); 
		String json = null;
		try {
			json = mapper.writeValueAsString(req);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println(json);
		
		
		String response;
		response = REBCUtil.httpPostIgnoreSSL(REBCUtil.BCUrl, json);
		System.out.println(response);
	}

}
