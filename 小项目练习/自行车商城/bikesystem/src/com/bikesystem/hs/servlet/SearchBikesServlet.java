package com.bikesystem.hs.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bikesystem.entity.BikeKind;
import com.bikesystem.entity.BikeRent;
import com.bikesystem.hs.dao.BikeKindDaoImpl;
import com.bikesystem.hs.dao.BikeRentDaoImpl;
import com.bikesystem.hs.dao.IBikeKindDao;
import com.bikesystem.hs.dao.IBikeRentDao;
import com.bikesystem.hs.dao.IShopDao;
import com.bikesystem.hs.dao.ShopDaoImpl;

/**
 * 搜索自行车的结果
 */
@WebServlet("/hs/search")
public class SearchBikesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private	IBikeRentDao rentDao;   
    private IBikeKindDao kindDao;
    public SearchBikesServlet() {
        super();
    }
    @Override
    public void init(ServletConfig config) throws ServletException {
    	rentDao = new BikeRentDaoImpl();
    	kindDao = new BikeKindDaoImpl();
    }
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bikeName=request.getParameter("bikename");
		String shopName = request.getParameter("shopname");
		String color = request.getParameter("color");
		String isRented = request.getParameter("isrented");
		String dayPrice = request.getParameter("dayprice");
		String kindName = request.getParameter("kindname");
		
		//动态添加传入的参数
		Map<String,Object> parMaps = new HashMap<String, Object>();
		addParameters(parMaps,"bikename",bikeName);
		addParameters(parMaps,"shopname",shopName);
		addParameters(parMaps,"color",color);
		addParameters(parMaps,"isrented",isRented);
		addParameters(parMaps,"dayprice",dayPrice);
		addParameters(parMaps,"kindname",kindName);
		System.out.println(parMaps);
		
		
		//默认搜索结果为最热租的12个车
		List<BikeRent> bikeRentList = rentDao.queryHotBikeRent();
		System.out.println("bikeName:"+bikeName);
		if(parMaps.size() != 0){
			bikeRentList = rentDao.queryAllBikeRentByParameter(parMaps);
		}
		
		if(bikeRentList!=null){
			request.setAttribute("bikeRentList", bikeRentList);
		}
		request.setAttribute("bikename",bikeName);
		System.out.println(bikeRentList);
		
		//查询赛车种类
		List<BikeKind> bikeKindsList = kindDao.queryAllBikeKind();
		if(bikeKindsList != null){
			request.setAttribute("bikeKindsList", bikeKindsList);
		}
		
		request.getRequestDispatcher("/WEB-INF/jsp/hs/searchBikes.jsp").forward(request, response);
		
	}
	private void addParameters(Map<String,Object> map,String key , String value){
		if(value!=null){
			map.put(key, value);
		}
	}

}
