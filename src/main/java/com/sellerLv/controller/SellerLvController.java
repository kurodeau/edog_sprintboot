package com.sellerLv.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sellerLv.entity.SellerLvVO;
import com.sellerLv.service.SellerLvService;

@WebServlet("/sellerLv/sellerLv.do")
public class SellerLvController extends HttpServlet {

	private List<String> errorMsgs = null;
	private SellerLvService sellerLvSvc;

	@Override
	public void init() throws ServletException {
		sellerLvSvc = new SellerLvService();
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		errorMsgs = new LinkedList<String>();

		req.setCharacterEncoding("UTF-8");
		req.setAttribute("errorMsgs", errorMsgs);

		String action = req.getParameter("action");
		String forwardPath = "";

		switch (action) {

		case "getAll":
			forwardPath = getAll(req, res);
			break;
		case "update":
			forwardPath = update(req, res);
			break;

		case "getOne_For_Update":
			forwardPath = getOne_For_Update(req, res);
			break;

		case "insert":
			forwardPath = insert(req, res);
			break;
			
		case "getOne_For_Display":
			forwardPath = getOne_For_Display(req, res);
			break;

		default:
			forwardPath = "/index.jsp";
		}
		res.setContentType("text/html; charset=UTF-8");
		RequestDispatcher dispatcher = req.getRequestDispatcher(forwardPath);
		dispatcher.forward(req, res);
	}

	// ***************************
	// Method Area
	// ***************************
	private String getOne_For_Display(HttpServletRequest req, HttpServletResponse res) {
		Integer sellerLvId =Integer.parseInt(req.getParameter("sellerLvId")) ;
		SellerLvVO sellerLvVO= sellerLvSvc.getOneSellerLv(sellerLvId);
		req.setAttribute("sellerLvVO", sellerLvVO);

		return "/sellerLv/listOneSellerLv.jsp";
		
	}

	private String getAll(HttpServletRequest req, HttpServletResponse res) {
		String page = req.getParameter("page");
		int currentPage = (page == null) ? 1 : Integer.parseInt(page);

		List<SellerLvVO> list = sellerLvSvc.getAll();

		if (req.getSession().getAttribute("sellerPageQty") == null) {
			int sellerPageQty = sellerLvSvc.getTotal();
			req.getSession().setAttribute("sellerPageQty", sellerPageQty);
		}

		req.setAttribute("list", list);
		req.setAttribute("currentPage", currentPage);

		return "/sellerLv/listAllSellerLv.jsp";
	}



	private String getOne_For_Update(HttpServletRequest req, HttpServletResponse res) {
		Integer sellerLvId = Integer.valueOf(req.getParameter("sellerLvId"));
		SellerLvVO sellerLvVO = sellerLvSvc.getOneSellerLv(sellerLvId);

		req.setAttribute("sellerLvVO", sellerLvVO);
		return "/sellerLv/update_sellerLv_input.jsp";
	}

	private String update(HttpServletRequest req, HttpServletResponse res) {
		
		SellerLvVO sellerlvVO = new SellerLvVO();

		Integer sellerLvId = null;
		try {
			String sellerLvIdstr = req.getParameter("sellerLvId");
			if (sellerLvIdstr == null || sellerLvIdstr.trim().length() > 10) {
				errorMsgs.add("請填寫等級(不超過10字)");
			}
			sellerLvId = Integer.valueOf(sellerLvIdstr);

		} catch (NumberFormatException e) {
			errorMsgs.add("等級請填寫數字");
		}
		
		
		String lvName = req.getParameter("lvName");
		
		System.out.println("lvNamelvNamelvName"+lvName);
		if (lvName == null || lvName.trim().length() == 0) {
			errorMsgs.add("等級名稱請勿空白");
		}
		
		
		 BigDecimal platformCommission = BigDecimal.ZERO; // Default value
		    try {
		        String platformCommissionStr = req.getParameter("platformCommission");
		        if (platformCommissionStr != null && !platformCommissionStr.trim().isEmpty()) {
		            platformCommission = new BigDecimal(platformCommissionStr);
		        }
		        
		        if (platformCommission.scale() != 2) {
		            errorMsgs.add("平台佣金請輸入兩位小數");
		        }
		    } catch (NumberFormatException e) {
		        errorMsgs.add("平台佣金請輸入小數");
		    }

		    
		    Integer adAllowType=null;
			try {
				String adAllowTypeStr = req.getParameter("adAllowType");
				if (adAllowTypeStr != null && !adAllowTypeStr.trim().isEmpty()) {
					adAllowType = Integer.valueOf(adAllowTypeStr);
				}
			} catch (NumberFormatException e) {
				errorMsgs.add("廣告種類請填入數字");
			}

			// Boolean parsing
			Boolean isExportGoldflow = null;
			try {
				isExportGoldflow = req.getParameter("isExportGoldflow") == null ? false : true;
			} catch (IllegalArgumentException e) {
				System.out.println("金流有誤");
			}
			
			
	
			
			
			Integer freightSub = null;
			try {
			    String freightSubStr = req.getParameter("freightSub");
			    if (freightSubStr != null && !freightSubStr.trim().isEmpty()) {
			        freightSub = Integer.valueOf(freightSubStr);
			    }
			} catch (NumberFormatException e) {
			    errorMsgs.add("運費減免次數請填入數字");
			}

			Integer returnSubPerMonth = null;
			try {
			    String returnSubPerMonthStr = req.getParameter("returnSubPerMonth");
			    if (returnSubPerMonthStr != null && !returnSubPerMonthStr.trim().isEmpty()) {
			        returnSubPerMonth = Integer.valueOf(returnSubPerMonthStr);
			    }
			} catch (NumberFormatException e) {
			    errorMsgs.add("每月可退訂次數請填入數字");
			}

			// Boolean parsing
			Boolean isShowPriority = null;
			try {
				isShowPriority = req.getParameter("isShowPriority") == null ? false : true;
			} catch (IllegalArgumentException e) {
				System.out.println("優先有誤");
			}			
			
			Integer shelvesNumber = null;
			try {
			    String shelvesNumberStr = req.getParameter("shelvesNumber");
			    if (shelvesNumberStr != null && !shelvesNumberStr.trim().isEmpty()) {
			        shelvesNumber = Integer.valueOf(shelvesNumberStr);
			    }
			} catch (NumberFormatException e) {
			    errorMsgs.add("上架商品數量請填入數字");
			}
			
			SellerLvVO sellerLvVO = new SellerLvVO();
			
			sellerLvVO.setSellerLvId(sellerLvId);
			sellerLvVO.setLvName(lvName);
			sellerLvVO.setPlatformCommission(platformCommission);
			sellerLvVO.setAdAllowType(adAllowType);
			sellerLvVO.setIsExportGoldflow(isExportGoldflow);
			sellerLvVO.setFreightSub(freightSub);
			sellerLvVO.setReturnSubPerMonth(returnSubPerMonth);
			sellerLvVO.setIsShowPriority(isShowPriority);
			sellerLvVO.setShelvesNumber(shelvesNumber);

			req.setAttribute("sellerLvVO", sellerLvVO);

			if (!errorMsgs.isEmpty()) {
				return ("/sellerLv/update_sellerLv_input.jsp");
			}
			sellerLvVO = sellerLvSvc.updateSellerLv(sellerLvId, lvName, platformCommission, adAllowType, isExportGoldflow, freightSub, returnSubPerMonth, isShowPriority, shelvesNumber);

		return "/sellerLv/listOneSellerLv.jsp";
	}
	

	private String insert(HttpServletRequest req, HttpServletResponse res) {
		SellerLvVO sellerlvVO = new SellerLvVO();

		
		
		String lvName = req.getParameter("lvName");
		
		System.out.println("lvNamelvNamelvName"+lvName);
		if (lvName == null || lvName.trim().length() == 0) {
			errorMsgs.add("等級名稱請勿空白");
		}
		
		
		 BigDecimal platformCommission = BigDecimal.ZERO; // Default value
		    try {
		        String platformCommissionStr = req.getParameter("platformCommission");
		        if (platformCommissionStr != null && !platformCommissionStr.trim().isEmpty()) {
		            platformCommission = new BigDecimal(platformCommissionStr);
		        }
		        
		        if (platformCommission.scale() != 2) {
		            errorMsgs.add("平台佣金請輸入兩位小數");
		        }
		    } catch (NumberFormatException e) {
		        errorMsgs.add("平台佣金請輸入小數");
		    }

		    
		    Integer adAllowType=null;
			try {
				String adAllowTypeStr = req.getParameter("adAllowType");
				if (adAllowTypeStr != null && !adAllowTypeStr.trim().isEmpty()) {
					adAllowType = Integer.valueOf(adAllowTypeStr);
				}
			} catch (NumberFormatException e) {
				errorMsgs.add("廣告種類請填入數字");
			}

			// Boolean parsing
			Boolean isExportGoldflow = null;
			try {
				isExportGoldflow = req.getParameter("isExportGoldflow") == null ? false : true;
			} catch (IllegalArgumentException e) {
				System.out.println("金流有誤");
			}
			
			
	
			
			
			Integer freightSub = null;
			try {
			    String freightSubStr = req.getParameter("freightSub");
			    if (freightSubStr != null && !freightSubStr.trim().isEmpty()) {
			        freightSub = Integer.valueOf(freightSubStr);
			    }
			} catch (NumberFormatException e) {
			    errorMsgs.add("運費減免次數請填入數字");
			}

			Integer returnSubPerMonth = null;
			try {
			    String returnSubPerMonthStr = req.getParameter("returnSubPerMonth");
			    if (returnSubPerMonthStr != null && !returnSubPerMonthStr.trim().isEmpty()) {
			        returnSubPerMonth = Integer.valueOf(returnSubPerMonthStr);
			    }
			} catch (NumberFormatException e) {
			    errorMsgs.add("每月可退訂次數請填入數字");
			}

			// Boolean parsing
			Boolean isShowPriority = null;
			try {
				isShowPriority = req.getParameter("isShowPriority") == null ? false : true;
			} catch (IllegalArgumentException e) {
				System.out.println("優先有誤");
			}			
			
			Integer shelvesNumber = null;
			try {
			    String shelvesNumberStr = req.getParameter("shelvesNumber");
			    if (shelvesNumberStr != null && !shelvesNumberStr.trim().isEmpty()) {
			        shelvesNumber = Integer.valueOf(shelvesNumberStr);
			    }
			} catch (NumberFormatException e) {
			    errorMsgs.add("上架商品數量請填入數字");
			}
			
			
		
			

			SellerLvVO sellerLvVO = new SellerLvVO();
			
			sellerLvVO.setLvName(lvName);
			sellerLvVO.setPlatformCommission(platformCommission);
			sellerLvVO.setAdAllowType(adAllowType);
			sellerLvVO.setIsExportGoldflow(isExportGoldflow);
			sellerLvVO.setFreightSub(freightSub);
			sellerLvVO.setReturnSubPerMonth(returnSubPerMonth);
			sellerLvVO.setIsShowPriority(isShowPriority);
			sellerLvVO.setShelvesNumber(shelvesNumber);

			req.setAttribute("sellerLvVO", sellerLvVO);

			if (!errorMsgs.isEmpty()) {
				return ("/sellerLv/addSellerLv.jsp");
			}

		sellerLvVO = sellerLvSvc.addSellerLv(lvName, platformCommission, adAllowType, isExportGoldflow, freightSub, returnSubPerMonth, isShowPriority, shelvesNumber);
		req.setAttribute("sellerLvVO", sellerLvVO);

		return "/sellerLv/listOneSellerLv.jsp";
	}

}
