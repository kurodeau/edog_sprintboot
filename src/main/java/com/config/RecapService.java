package com.config;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class RecapService {
	private static final String API_KEY = "AIzaSyCx3ZXibbCCS7nDowrx9_JlCL-Bf9GygBk";
	private static final String PROJECT_ID = "polished-logic-416206";
	private static final String SITE_KEY = "6LeS6J8pAAAAABV5hvfIwhFZSQI_QPRiZwoOOH-H";

	private final RestTemplate restTemplate = new RestTemplate();

//	public static void main(String[] args) throws IOException {
//		String token = "03AFcWeA40WcRjiyw3E99eLBMF-3uDYwexHtTBnCtdjsvucC9XWYRJ-L2Wqn6dAPcVMmLvL9x2FIfT3zuLREwNNaO60zdfKd4GdE02Et8bLGOrcoZxBirAaH5aiQZ4or4etSGsDLimBzZikwFzCWvGrHu1H8MhV3RJzIt7CSogRCQe8g3FcQbrGCz8oYKY1_OQtMeKpwLjL9tQ9VEp-ylgcqiKtaRfzoqDxEp8jOReQAMwulDKd2kfAwDqV4J-mC4jnpy--hJPCgp6L6_mTrk0OO5eV2uir8ER9-CHwop36e_2XQN-KM_U2P-Xvtcip0UfUXGK-2y_IMl3dFF4Kkd8Db0pXoPbD1SaBbO6Nc5vEh4J5cl14PCliolfOsLPv0T2or6xYtDrJoLChw9mQbabt4XOiEKXfH-893Yq8pGxtTDTzAPyxMyznxoISLFaZjzJ0G14EzHneH3rhUn4eIskNnowxskwRmaVO9afQc1X3POOhjTa8WptTVGFbt_BTOaobiwnJtIa1LMXt3t3h0h8JYYqh7wZeCukOlQ4MeEWoYypyVsA7IZJrOGzVrVI2bMIvCt9SSUHXdTaUKx1v_JUu79p6C6VvC-RausWGrG9a33_sfm-FJV1MAd7WJ4KRaJRpEeLeUhgmPvcrqV5e0vGXA8GWWogK1oSYN1QGPJwe7kB07FGBJ61k1cyC7CtAGA_2NQlDwIPbC3n3MTwxuv_48hQH4QfIN9aaQEvfVdmDtCnAL5kec59NP0_UCgeC-phzo0NxKqX4xEYZgzW_yirAQeHQnbJn_AbGNlTNCWpMxtv0ZakoDbAhm7TSZMQyWbYUTjMAXRfrd9gmNjqfSXvmJ7nyiYtC0ADIzcxyxS1uXfrTf1_b4ZcTHU0jB2hKkVh4kbTH_rSQZEvAiyfob7lQK20DKOzKXSJykZgcIILImnqR7uqewMWlOuQpzEYXin4G_fyJ9vVUGfZ8U36MuQMqkji0VAsD9g79tbL4b59O4KUWDnke4MoSAxPdmkMk3XG_uNd7NdA8px40URIHlLrFs9tAJV_du4pBgWvhXrUfBXc8Q5BMh_7dV7m83BBOn9W7SDIr_Twk9w4lFsQcWFy_9ZA71HfqMbJxq5qQey78cLnMLM5nVeD2fjkPcXpBzRZ3QH42UQFfmckTwFm-WGA58T7nz5cvNrF2vVZKU_u4Mqsifc0tvt5GtvqMWQGZsRLlUTW161is-_QjfONtt2x1LIk6op_fGIR7YdQjGMpALi1yoOUT-58iUFYOm2oAs_6rZGZtcjMSttNXgqr6l4-iIaJKYdtjB-_2qb9DqK1qENF17xyTuzRMTC67tYmG7Icf1o0lJkXaH2q4_UC1nPmwuJJJwWTLYrSa_iXEO3w0QlQqhL6D8RS5_3TyJE91ccibf_8-kJR39OPgUeDGHAf65UbHjB6Mrah74ARX_3u3rCfdBz3kGciN1F_tUedHC76KrM6mwg_nxSJhvyUfpXdDrtEU5RVwVHkL_Nx6YSJVcnp6xVFaVfFPMkdc-0Ju9GWeIuJ13QcgHA7lSYwvoVr61SsqaBlzODNutDA5PIQlVOyxaIuoKMTCYo5KRbdpBTEnntpmxPwx6BbIpuPYTW1TsbtiMxh14OPw1hrKAO25FPk8XJMsJB7SEM2lmtlc7waRrx8OB4k7dZHh9FUoKw4fBctiCKU1XYjdg";
//		Event e = buildJsonRequestBody(token, "login");
//		sendPostRequest(e);
//
//	}

	public  Double parseScore(String responseBody) {

		JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
		JsonObject riskAnalysis = jsonObject.getAsJsonObject("riskAnalysis");
		Double score = riskAnalysis.get("score").getAsDouble();

		return score;
	}

	public  Event buildJsonRequestBody(String token, String userAction) {
		// Build the JSON request body
		Event event = new Event(token, userAction, SITE_KEY);
		return event;
	}

	public  String sendPostRequest(Event event) {
		RestTemplate restTemplate = new RestTemplate();

		
		System.out.println("sendPostRequest" + event.token);
		// 使用Gson将Event对象转换为JSON字符串
		Gson gson = new Gson();
		JsonObject wrappedJson = new JsonObject();
		wrappedJson.add("event", JsonParser.parseString(gson.toJson(event)));

		// 设置请求头，指定Content-Type为application/json
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// 封装JSON数据和请求头
		HttpEntity<String> requestEntity = new HttpEntity<>(gson.toJson(wrappedJson), headers);

		// 构造请求URL
		String url = "https://recaptchaenterprise.googleapis.com/v1/projects/" + PROJECT_ID + "/assessments?key="
				+ API_KEY;

		// 发送POST请求并获取响应
		ResponseEntity<String> response = restTemplate.exchange(url, // 请求URL
				HttpMethod.POST, // 请求方法
				requestEntity, // 请求实体（JSON数据和请求头）
				String.class // 响应类型
		);

		// 打印响应内容
		System.out.println("Response status code: " + response.getStatusCode());
		System.out.println("Response body: " + response.getBody());

		return response.getBody();
	}

	static class Event {
		String token;
		String expectedAction;
		String siteKey;

		public Event(String token, String expectedAction, String siteKey) {
			this.token = token;
			this.expectedAction = expectedAction;
			this.siteKey = siteKey;
		}
	}

}
