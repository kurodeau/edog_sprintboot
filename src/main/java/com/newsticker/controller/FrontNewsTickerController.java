package com.newsticker.controller;

import javax.validation.Valid;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.annotation.RequestMapping;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.newsticker.model.NewsTickerVO;
import com.util.HttpResult;
import com.newsticker.model.NewsTickerService;

@Controller
@RequestMapping("/front/newsTicker")
public class FrontNewsTickerController {
	
	@Autowired
	NewsTickerService newsTickerSvc;

	/*
	 * 每次刷新頁面觸發, 更新跑馬燈要顯示的訊息內容並回傳
	 * 撈出所有 Display 為true的內容, 並且整併成一串大的 String 給前端
	 */
	// /front/newsTicker/showNewsTicker
	@PostMapping(value = "showNewsTicker", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> showNewsTicker(@RequestBody String jsonData) throws IOException {
		System.out.println("測試訊息:有進入showIsdisplay()");

		// 將接到的 JSON 資料做初步的驗證, 之後轉為 Date 型別
		JSONObject jsonObject = new JSONObject(jsonData);
        System.out.println(jsonData);
        if(jsonData==null || jsonData.equals("{}")){
            HttpResult<String> result = new HttpResult<>(400, "", "時間資訊異常為空");
            return ResponseEntity.badRequest().body(result);
        }
        String currentTime = jsonObject.getString("currentTime");
		
        JSONObject jsonNewsTickers = new JSONObject();
		jsonNewsTickers.put( "data", newsTickerSvc.showNewsTicker(currentTime) );
		System.out.println( "測試資料:印出要回傳的內容=" + jsonNewsTickers );
//        String newsTickersStr = newsTickerSvc.showNewsTicker(currentTime);
//        System.out.println( newsTickersStr );
		// 這個不是正規作法
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("data", newsTickerSvc.showNewsTicker(currentTime));
		
		
		return ResponseEntity.ok(hashMap);
		
	}

}