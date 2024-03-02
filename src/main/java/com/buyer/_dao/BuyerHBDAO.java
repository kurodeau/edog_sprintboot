//package com.buyer._dao;
//	
//import java.io.Serializable;
//import java.sql.Timestamp;
//import java.util.Date;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import javax.persistence.TypedQuery;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//
//import com.buyer.entity.BuyerVO;
//import com.coupon.entity.CouponVO;
//
//import util.HibernateUtil;
//
//public class BuyerHBDAO implements BuyerDAO{
//	
//	private SessionFactory factory;
//
//	public BuyerHBDAO() {
//		factory = HibernateUtil.getSessionFactory();
//	}
//
//	private Session getSession() {
//		return factory.getCurrentSession();
//	}
//
//	@Override
//	public Integer insert(BuyerVO buyer) {
//		return (Integer) getSession().save(buyer);
//	}
//
//	@Override
//	public Integer update(BuyerVO buyer) {
//		try {
//			getSession().update(buyer);
//			return 1;
//		} catch (Exception e) {
//			return -1;
//		}
//		
//	}
//
//	@Override
//	public Integer delete(Integer memberId) {
//		BuyerVO buyer = getSession().get(BuyerVO.class, memberId);
//		try {
//			if (buyer != null) {
//				getSession().delete(memberId);
//				return 1;
//			} else {
//				return 0;
//			}
//		} catch (Exception e) {
//			return -1;
//		}
//	}
//
//	@Override
//	public BuyerVO findByPrimaryKey(Integer memberId) {
//		return getSession().get(BuyerVO.class, memberId);
//	}
//
//	@Override
//	public List<BuyerVO> getAll() {
//		return getSession().createQuery("from BuyerVO", BuyerVO.class).list();
//	}
//
//	@Override
//	public Integer getTotal() {
//		Long total = getSession().createQuery("select count(*) from BuyerVO", Long.class).uniqueResult();
//		return total.intValue();
//	}
//
//	@Override
//	public List<BuyerVO> getByCompositeQuery(Map<String, String> map) {
//		if (map.size() == 0) {
//			return getAll();
//		}
//
//		CriteriaBuilder builder = getSession().getCriteriaBuilder();
//		CriteriaQuery<BuyerVO> criteria = builder.createQuery(BuyerVO.class);
//		Root<BuyerVO> root = criteria.from(BuyerVO.class);
//
//		List<Predicate> predicates = new ArrayList<>();
//
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
//
//		java.util.Date startdate =null;
//		java.util.Date enddate = null;
//		try {
//			startdate = map.containsKey("startdate") ? dateFormat.parse(map.get("startdate")) : null;
//			enddate= map.containsKey("enddate") ? dateFormat.parse(map.get("enddate")) : null;
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//
//		// 注意以下
//		if (map.containsKey("startdate") && map.containsKey("enddate")) {
//			predicates.add(builder.between(root.get("memberRegistrationTime"), startdate, enddate));
//		}
//
//		for (Map.Entry<String, String> row : map.entrySet()) {
//			if ("memberName".equals(row.getKey())) {
//				predicates.add(builder.like(root.get("memberName"), "%" + row.getValue() + "%"));
//			}
//
//			if ("startdate".equals(row.getKey())) {
//				if (!map.containsKey("enddate")) {
//					predicates.add(builder.greaterThanOrEqualTo(root.get("memberRegistrationTime"),startdate));
//				}
//			}
//
//			if ("enddate".equals(row.getKey())) {
//				if (!map.containsKey("startdate")) {
//					predicates.add(
//							builder.lessThanOrEqualTo(root.get("memberRegistrationTime"), enddate));
//				}
//			}
//
//		}
//
//		criteria.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
//		criteria.orderBy(builder.asc(root.get("memberId")));
//		TypedQuery<BuyerVO> query = getSession().createQuery(criteria);
//		System.out.println("===============");
//		System.out.println(query.getResultList());
//		return query.getResultList();
//
//	}
//	
//	
//	//main方法測試
////	public static void main(String[] args) {
////	SessionFactory factory = HibernateUtil.getSessionFactory();
////	Session session = factory.getCurrentSession();
////	session.beginTransaction();
//
//	// ================Insert 測試OK==============
//
////	Query query = session.createQuery("FROM SellerLvVO WHERE sellerLvId = :id");
////	query.setParameter("id", 1);
////	SellerLvVO sellerLv1 = (SellerLvVO) query.uniqueResult();
//	
//	//memberId int AI PK 
//	//memberEmail varchar(200) 
//	//thirdFrom varchar(100) 
//	//memberName varchar(100) 
//	//memberPhone varchar(10) 
//	//memberMobile varchar(20) 
//	//memberBirthday date 
//	//memberPassword varchar(100) 
//	//memberAddress varchar(100) 
//	//isMemberEmail tinyint(1) 
//	//memberRegistrationTime datetime 
//	//petName varchar(100) 
//	//petImg longblob 
//	//petImgUploadTime datetime 
//	//petVaccName1 varchar(100) 
//	//petVaccTime1 datetime 
//	//petVaccName2 varchar(100) 
//	//petVaccTime2 datetime 
//	//isConfirm
//	
////	BuyerVO buyer1 = new BuyerVO();
////	buyer1.setMemberEmail("buyerinsert@example.com");
////	buyer1.setThirdFrom(null);
////	buyer1.setMemberName("buyerinsert");
////	buyer1.setMemberPhone("0212345678");
////	buyer1.setMemberMobile("0999123456");
////	
////	SimpleDateFormat memberBirthday = new SimpleDateFormat("yyyyMMdd");
////	try {
////		buyer1.setMemberBirthday( memberBirthday.parse("20240225") );
////	} catch (ParseException e) {
////		e.printStackTrace();
////	}
////	
////	buyer1.setMemberPassword("pass1234");
////	buyer1.setMemberAddress("buyerinsert地址");
////	buyer1.setIsMemberEmail(false);
////	
////	Timestamp memberRegistrationTime = new Timestamp(System.currentTimeMillis());
////	buyer1.setMemberRegistrationTime( memberRegistrationTime );	
////	buyer1.setPetName("buyerinsert的狗");
////	buyer1.setPetImg(null);
////	buyer1.setPetImgUploadTime(null);
////	buyer1.setPetVaccName1("疫苗的啦");
////	
////	SimpleDateFormat petVaccTime1 = new SimpleDateFormat("yyyyMMdd");
////	try {
////		buyer1.setPetVaccTime1( petVaccTime1.parse("20240101") );
////	} catch (ParseException e) {
////		e.printStackTrace();
////	}
////	
////	buyer1.setPetVaccName2(null);
////	buyer1.setPetVaccTime2(null);
////	// isConfirm 有預設
////	buyer1.setIsConfirm(true);
////
////	Serializable buyerId = session.save(buyer1);
//
//	// ================Update==============
//
////	BuyerVO buyerToUpdate = session.get(BuyerVO.class, (Integer) memberId);
////	buyerToUpdate.setMemberName("New mamber Name");
////	session.update(buyerToUpdate);
////
////	// ================Query==============
////	String sellerLvName = seller1.getSellerLvId().getLvName();
////	System.out.println("sellerLvName: " + sellerLvName);
////
////	// ================Delete==============
////
////	BuyerVO sellerToDelete = session.get(BuyerVO.class, (Integer) sellerId);
////	session.delete(sellerToDelete);
//
//	
//	//任何HQL操作都要包在Transaction中
////	session.getTransaction().commit();	
////	}
//	
//
//}
