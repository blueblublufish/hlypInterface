package com.hlyp.api.utils.order;

import com.hlyp.api.bean.HesoAccount;
import com.hlyp.api.bean.HesoOrderConsume;
import com.hlyp.api.bean.HesoOrderConsumeDetail;
import com.hlyp.api.dao.HesoAccountMapper;
import com.hlyp.api.param.order.AmaniOrderdetail;
import com.hlyp.api.param.order.CustomerInformation;
import com.hlyp.api.param.order.Orderinformation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class AMANIUtil {

    public  String packToXmlByOrder(HesoOrderConsume orderConsume, List<HesoOrderConsumeDetail> details,HesoAccount account){
        Orderinformation orderinformation = new Orderinformation();
        orderinformation.setClothingid(orderConsume.getPinlei());
        orderinformation.setSizecategoryid(orderConsume.getChicunfenlei());
        orderinformation.setAreaid(orderConsume.getChimaquyu());
        orderinformation.setFabirc(orderConsume.getClothType());
        orderinformation.setCustormerbody(orderConsume.getTetiCode());
        if("10053".equals(orderConsume.getChicunfenlei())){
            orderinformation.setCustormerbody(orderConsume.getChengyiteti());
        }
        orderinformation.setClothingstyle(orderConsume.getChangdu());
        orderinformation.setRemark("");
        orderinformation.setOrderno(orderConsume.getOrderId());
        orderinformation.setAmount("1");
        //身材数据
        CustomerInformation customerInformation = new CustomerInformation();

        customerInformation.setHeight("111");
        customerInformation.setGender(account.getSex());
        customerInformation.setWeight("111");
        customerInformation.setHeightunited("10266");
        customerInformation.setWeightunited("10261");
        customerInformation.setName(account.getUserId());
        customerInformation.setMemos("");
        customerInformation.setEmail("");
        customerInformation.setAddress("");
        customerInformation.setTel("13600059771");
        //订单详情
        List<AmaniOrderdetail> detaillist = new ArrayList<AmaniOrderdetail>();
        for(HesoOrderConsumeDetail detail : details){
            AmaniOrderdetail amaniOrderdetail = new AmaniOrderdetail();
            amaniOrderdetail.setBodystyle(detail.getWeidu());
            amaniOrderdetail.setCategoryid(detail.getClothType());
            amaniOrderdetail.setPartsize(detail.getBodySize());
            if("10053".equals(orderConsume.getChicunfenlei())){
                amaniOrderdetail.setPartsize(detail.getChengyiliangti());
            }
            amaniOrderdetail.setSizespecheight("");
            amaniOrderdetail.setOrdersprocess(detail.getSystemCode());
            if("10054".equals(orderConsume.getChicunfenlei())){
                amaniOrderdetail.setSizespecheight(detail.getBiaozhunsize());
                amaniOrderdetail.setPartsize(detail.getBiaozhunCode());
            }
            detaillist.add(amaniOrderdetail);
        }
        return  packXml(orderinformation,customerInformation,detaillist);
    }


    String packXml(Orderinformation orderinformation,CustomerInformation customerInformation,List<AmaniOrderdetail> detaillist){

        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version='1.0' encoding='UTF-8' standalone='yes'?>");
        sb.append("<orderinformation id=\"\" includesuit=\"0A01\" appendclothingid=\"\">");
        sb.append("<clothingid>"+orderinformation.getClothingid()+"</clothingid>");
        sb.append("<sizecategoryid>"+orderinformation.getSizecategoryid()+"</sizecategoryid>");
        sb.append("<areaid>"+orderinformation.getAreaid()+"</areaid>");
        sb.append("<fabric>"+orderinformation.getFabirc()+"</fabric>");
        sb.append("<amount>"+orderinformation.getAmount()+"</amount>");
        sb.append("<clothingstyle>"+orderinformation.getClothingstyle()+"</clothingstyle>");
        sb.append("<custormerbody>"+orderinformation.getCustormerbody()+"</custormerbody>");
        sb.append("<orderno>"+orderinformation.getOrderno()+"</orderno>");
        sb.append("<semifinished>2</semifinished>");
        sb.append("<remark>"+orderinformation.getRemark()+"</remark>");


        //顾客信息开始
        sb.append("<customerInformation genderid=\""+customerInformation.getGender()+"\">");
        sb.append("<name>"+customerInformation.getName()+"</name>");
        sb.append("<height>"+customerInformation.getHeight()+"</height>");
        sb.append("<heightunitid>"+customerInformation.getHeightunited()+"</heightunitid>");
        sb.append("<weight>"+customerInformation.getWeight()+"</weight>");
        sb.append("<weightunitid>"+customerInformation.getWeightunited()+"</weightunitid>");
        sb.append("<email>"+customerInformation.getEmail()+"</email>");
        sb.append("<address>"+customerInformation.getAddress()+"</address>");
        sb.append("<tel>"+customerInformation.getTel()+"</tel>");
        sb.append("<memos>"+customerInformation.getMemos()+"</memos>");
        sb.append("</customerInformation>");
        //顾客信息结束
        //订单明细开始
        sb.append("<orderdetails>");
        for(AmaniOrderdetail detail:detaillist){
            sb.append("<orderdetail>");
            sb.append("<sizespecheight>"+detail.getSizespecheight()+"</sizespecheight>");
            sb.append("<categoryid>"+detail.getCategoryid()+"</categoryid>");
            sb.append("<bodystyle>"+detail.getBodystyle()+"</bodystyle>");
            sb.append("<partsize>"+detail.getPartsize()+"</partsize>");
            sb.append("<ordersprocess>"+detail.getOrdersprocess()+"</ordersprocess>");
            sb.append("</orderdetail>");
        }
        sb.append("</orderdetails>");
        //订单明细结束
        sb.append("</orderinformation>");
        System.out.println("==返回xml数据:" + sb.toString());

        return sb.toString();
    }
}
