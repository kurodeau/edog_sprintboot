package com.productorder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cart.model.CartVO;
import com.product.model.ProductService;
import com.product.model.ProductVO;
import com.redis.JedisUtil;
import com.seller.entity.SellerVO;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;





@Service("productOrderService")
public class ProductOrderService {

	@Autowired
	ProductOrderRepository repository;
	
	@Autowired
	ProductService productSvc;
	
	
	

	public void addProductOrder(ProductOrderVO productOrderVO) {
		repository.save(productOrderVO);
	}

	public void updateProductOrder(ProductOrderVO productOrderVO) {
		repository.save(productOrderVO);
	}

	public void deleteProductOrder(Integer orderId) {
		if (repository.existsById(orderId))
			repository.deleteByOrderId(orderId);
	}
	


	public ProductOrderVO getOneProductOrder(Integer orderId) {
		Optional<ProductOrderVO> optional = repository.findById(orderId);
//		return optional.get();
		return optional.orElse(null); // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}
	public List<ProductOrderVO> findBySellerId(Integer sellerId) {
		return repository.findBySellerId(sellerId);
	}
	
	

	public List<ProductOrderVO> getAll() {
		return repository.findAll();
	}

/////查詢賣家訂單狀態//////////////////////////////////////////////////////

	//顯示未處理訂單
	public List<ProductOrderVO> getSellerProductOrderPendingConfirm(Integer sellerId) {
		
		List<ProductOrderVO> sellerProductOrders = repository.findBySellerId(sellerId);		
		
		 List<ProductOrderVO> list = sellerProductOrders.stream()
                 .filter(productOrder -> "1".equals(productOrder.getOrderStatus().toString()))
                 .collect(Collectors.toList());									 
				return list;		
	}
	
	//顯示處理中訂單
		public List<ProductOrderVO> getSellerProductOrderSellerProcessing(Integer sellerId) {
			
			List<ProductOrderVO> sellerProductOrders = repository.findBySellerId(sellerId);		
			
			List<ProductOrderVO> list = sellerProductOrders.stream()
		             .filter(productOrder -> "5".equals(productOrder.getOrderStatus().toString())
		            		 				|| "6".equals(productOrder.getOrderStatus().toString()))
		             .collect(Collectors.toList());									 
					return list;	
		}
	
		//顯示已完成訂單
		public List<ProductOrderVO> getSellerProductOrderCompleted(Integer sellerId) {
			
			List<ProductOrderVO> sellerProductOrders = repository.findBySellerId(sellerId);			
			
			 List<ProductOrderVO> list = sellerProductOrders.stream()
		             .filter(productOrder -> "7".equals(productOrder.getOrderStatus().toString()))
		             .collect(Collectors.toList());									 
					return list;		
		}
		//顯示已取消訂單
		public List<ProductOrderVO> getSellerProductOrderCanceled(Integer sellerId) {
			
			List<ProductOrderVO> sellerProductOrders = repository.findBySellerId(sellerId);		
			
			 List<ProductOrderVO> list = sellerProductOrders.stream()
		             .filter(productOrder -> "2".equals(productOrder.getOrderStatus().toString())
		            		 			  || "3".equals(productOrder.getOrderStatus().toString()))
		             .collect(Collectors.toList());									 
					return list;		
		}
	
	
/////平台查詢所有訂單//////////////////////////////////////////////////////
	public List<ProductOrderVO> getProductOrderPendingConfirm() {
		
		List<ProductOrderVO> allProductOrders = repository.findAll();		
		
		 List<ProductOrderVO> list = allProductOrders.stream()
                 .filter(productOrder -> "1".equals(productOrder.getOrderStatus().toString()))
                 .collect(Collectors.toList());									 
				return list;		

	}

	public List<ProductOrderVO> getProductOrderNotAcceptedByBuyer() {

		List<ProductOrderVO> allProductOrders = repository.findAll();

		List<ProductOrderVO> list = allProductOrders.stream()
				.filter(productOrder -> "2".equals(productOrder.getOrderStatus().toString()))
				.collect(Collectors.toList());
		return list;
	}

	public List<ProductOrderVO> getProductOrderNotAcceptedBySeller() {

		List<ProductOrderVO> allProductOrders = repository.findAll();

		List<ProductOrderVO> list = allProductOrders.stream()
				.filter(productOrder -> "3".equals(productOrder.getOrderStatus().toString()))
				.collect(Collectors.toList());
		return list;
	}

	public List<ProductOrderVO> getProductOrderAccepted() {

		List<ProductOrderVO> allProductOrders = repository.findAll();

		List<ProductOrderVO> list = allProductOrders.stream()
				.filter(productOrder -> "4".equals(productOrder.getOrderStatus().toString()))
				.collect(Collectors.toList());
		return list;
	}

	public List<ProductOrderVO> getProductOrderSellerProcessing() {

		List<ProductOrderVO> allProductOrders = repository.findAll();

		List<ProductOrderVO> list = allProductOrders.stream()
				.filter(productOrder -> "5".equals(productOrder.getOrderStatus().toString())
						|| "6".equals(productOrder.getOrderStatus().toString()))
				.collect(Collectors.toList());
		return list;
	}

	public List<ProductOrderVO> getProductOrderItemShipped() {

		List<ProductOrderVO> allProductOrders = repository.findAll();

		List<ProductOrderVO> list = allProductOrders.stream()
				.filter(productOrder -> "6".equals(productOrder.getOrderStatus().toString()))
				.collect(Collectors.toList());
		return list;
	}

	public List<ProductOrderVO> getProductOrderCompleted() {

		List<ProductOrderVO> allProductOrders = repository.findAll();

		List<ProductOrderVO> list = allProductOrders.stream()
				.filter(productOrder -> "7".equals(productOrder.getOrderStatus().toString()))
				.collect(Collectors.toList());
		return list;
	}

	public List<ProductOrderVO> getProductOrderCanceled() {

		List<ProductOrderVO> allProductOrders = repository.findAll();

		List<ProductOrderVO> list = allProductOrders.stream()
				.filter(productOrder -> "2".equals(productOrder.getOrderStatus().toString())
						|| "3".equals(productOrder.getOrderStatus().toString()))
				.collect(Collectors.toList());
		return list;
	}

/////買家查詢所有訂單 下面方法Sky新增//////////////////////////////////////////////////////	
	public List<ProductOrderVO> findByBuyerId(Integer buyerId) {
		return repository.findByMemberId(buyerId);
	}
	
	
	
	//買家查詢待處理訂單
	public List<ProductOrderVO> getBuyerProductOrderPendingConfirm(Integer buyerId) {
		
		List<ProductOrderVO> buyerProductOrders = repository.findByMemberId(buyerId);		
		
		 List<ProductOrderVO> list = buyerProductOrders.stream()
                 .filter(productOrder -> "1".equals(productOrder.getOrderStatus().toString()))
                 .collect(Collectors.toList());									 
				return list;		
	}
	
	
	//買家查詢處理中訂單
	public List<ProductOrderVO> getProductOrderBuyerProcessing(Integer buyerId) {

		List<ProductOrderVO> buyerProductOrders = repository.findByMemberId(buyerId);

		List<ProductOrderVO> list = buyerProductOrders.stream()
				.filter(productOrder -> "5".equals(productOrder.getOrderStatus().toString())
						|| "6".equals(productOrder.getOrderStatus().toString()))
				.collect(Collectors.toList());
		return list;
	}
	
	//買家查詢完成訂單
	public List<ProductOrderVO> getProductOrderBuyerCompleted(Integer buyerId) {

		List<ProductOrderVO> buyerProductOrders = repository.findByMemberId(buyerId);

		List<ProductOrderVO> list = buyerProductOrders.stream()
				.filter(productOrder -> "7".equals(productOrder.getOrderStatus().toString()))
				.collect(Collectors.toList());
		return list;
	}
	
	
	//買家查詢取消訂單
	public List<ProductOrderVO> getProductOrderBuyerCanceled(Integer buyerId) {

		List<ProductOrderVO> buyerProductOrders = repository.findByMemberId(buyerId);

		List<ProductOrderVO> list = buyerProductOrders.stream()
				.filter(productOrder -> "2".equals(productOrder.getOrderStatus().toString())
						|| "3".equals(productOrder.getOrderStatus().toString()))
				.collect(Collectors.toList());
		return list;
	}
	
	
////////跳轉結帳頁面用/////////////////////////////////////////////////
	// Redis 存Key List+ DTO 作法, 獲取member 所有購物車資訊
		public Map<String, Set<CartVO>> getAllByMemberId(String memberId) {

			memberId = "8";
			
			// 設定redisKey, 取得連線
			String redisKey = "order:"+ memberId;
			System.out.println("redisKey= " + redisKey); //測試訊息
			JedisPool jedisPool = JedisUtil.getJedisPool();

			// 本方法會用到的跨區域變數
			Map<String, Set<CartVO>> cartClassfi = new HashMap<>(); // 最後要傳回前端的資料包
			Set<CartVO> cartSet = new HashSet<CartVO>(); // 要被裝入 cartClassfi 的Value
			List<String> redisData = new ArrayList<String>(); // Redis 中的 ProductID 撈出裝成List來遍歷或檢查

			// 索取redis連線, 用try 整個包起來
			try (
				// 從 Redis 中讀取資料 並且指定為db10
				Jedis jedis = jedisPool.getResource()) {
				jedis.select(10);

				// 獲得該筆Redis 內容Keys
				Set<String> allProductId = jedis.hkeys(redisKey);
				System.out.println("allProuductId= " + allProductId);

				// 宣告要裝入回傳List<CartVO> 的參數
				String sellerCompany;
				SellerVO sellerVO;
				String productNum;
							
//				//===== 先計算List中每個商品編號有幾個
//				Map<String, Integer> productAndNum = new HashMap<>();
//				for (String p : redisData) {
//					productAndNum.put(p, productAndNum.getOrDefault(p, 0) + 1);
//				}
	            //===== 把Keys Map 中的值 實體化為要回傳的CartVO
			    for (String productId : allProductId) 
			    {
		            System.out.println("service測試訊息,productId: " + productId );
		            System.out.println("service測試訊息,productId 數量value: " + jedis.hget(redisKey, productId) );
		            CartVO cartVO = new CartVO();    
		            ProductVO productVO = productSvc.getOneProduct( Integer.valueOf( productId ) );
		            cartVO.setProductVO(productVO);
		            sellerCompany = productVO.getSellerVO().getSellerCompany();
		            cartVO.setSellerCompany( sellerCompany );
		            cartVO.setSellerVO( productVO.getSellerVO() );
		            cartVO.setProductNum( jedis.hget(redisKey, productId).toString() );
//		            System.out.println( "測試cartVO=" + cartVO.toString() );

					// 將每個 CartVO 透過 "sellerCompany" 鍵關聯起来
//		            System.out.println("將每個 CartVO 透過 \"sellerCompany\" 鍵關聯起来");
		            cartSet = cartClassfi.getOrDefault(cartVO.getSellerCompany(), new HashSet<CartVO>());
		            cartSet.add(cartVO);
					cartClassfi.put(sellerCompany, cartSet);
					
					
		        }
				
			} catch (Exception e) {
				System.out.println("service從redis讀出資料有問題");
				e.printStackTrace();
			}
//			System.out.println("回傳 cartClassfi"); // 測試資料
			return cartClassfi;
		}
	
////////////////////////////////////////////////////////////////	
}
