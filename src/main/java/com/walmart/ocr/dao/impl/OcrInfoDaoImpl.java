/**
 * 
 */
package com.walmart.ocr.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.ocr.dao.OcrInfoDao;
import com.walmart.ocr.model.SmartOCRDataModel;

/**
 * @author U46591
 *
 */

@Repository
public class OcrInfoDaoImpl implements OcrInfoDao {

	Logger LOGGER = Logger.getLogger(OcrInfoDaoImpl.class);
	@Autowired
	SessionFactory template;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.walmart.ocr.dao.OcrInfoDao#save(com.walmart.ocr.model.
	 * SmartOCRDataModel)
	 */
	@Override
	@Transactional(readOnly=false)
	public Serializable createOcrData(SmartOCRDataModel ocrDataModel) {
		
		LOGGER.info("persist ");
		Serializable id = null;
		Session session= template.openSession();
		id = session.save(ocrDataModel);
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.walmart.ocr.dao.OcrInfoDao#get(java.io.Serializable)
	 */
	@Override
	public SmartOCRDataModel findOcrDataById(Serializable id) {
		// TODO Auto-generated method stub
		LOGGER.info("retrieve ocr data");
		SmartOCRDataModel dataModel=null;
		Criteria criteria=template.openSession().createCriteria(SmartOCRDataModel.class);
		criteria.add(Restrictions.eq("ocrRequestId",id));
		
		dataModel=(SmartOCRDataModel) criteria.uniqueResult();
		return dataModel;
	}

	@SuppressWarnings({ "unchecked"})
	@Override
	public List<SmartOCRDataModel> findAllOcrData() {
		Session session=template.openSession();
		String hql="From SmartOCRDataModel";
		List<SmartOCRDataModel>  users=null;
		try{
			Query query=session.createQuery(hql);
			users=query.list();
		}catch(Exception e){
			System.out.println(e);
		}
		return users;
	}

}
