/*
 * Copyright (c) 2018 <1253850806@qq.com> All rights reserved.
 */
package com.qf.util;



import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.qf.controller.api.admin.ExportExcelUtils;



/**
 * author   Vareck
 * datetime 2018-10-27 10:20
 */
public class ExcelUtil {
	
    /**
     * 自定义生成excel表格(多表)
     * @param  response       
     * @param  files         文件相关信息（files[0] 文件名 ，files[1] 文件后缀，files[2] 工作区名称（多表用~,~隔开，单表默认传空） ）
     * @param  excelSwitch   样式开关（多表用逗号隔开，0为关1或大于1的为关。excelSwitch[0] 是否有单元格合并 ， excelSwitch[1] 文字水平居*（1居中2居左3居右）， excelSwitch[2] 文字垂直居*（1居中2居左3居右）,excelSwitch[3] 表格背景颜色）
     * @param  map           样式详细信息(addMergedRegion 合并单元格参数(用~分割合并的单元格参数，用~~分割表)，backgroundColor 背景颜色（多表用，号隔开）)
     * @param  titleData     表头数据(第一层为)
     * @param  allData       表数据 
     */ 
	public static void  createExcel( HttpServletResponse response,String[] files ,String[] excelSwitch,Map<String ,String> map ,List<List<String>> titleData,List<List<List<String>>> allData) throws Exception {
        
		//文件名与后缀判断
        if( files[1].equals("xls") ){
        	 response.setContentType("application/ms-excel;charset=UTF-8"); 
        	 response.setHeader("Content-Disposition", "attachment;filename="+ new String(files[0].getBytes("utf-8"),"ISO-8859-1")+"."+files[1]);
        }
        else if( files[1].equals("xlsx") ){
        	response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    		response.addHeader("Content-Disposition", "attachment;filename="+ new String(files[0].getBytes("utf-8"),"ISO-8859-1")+"."+files[1]);
        }else{
        	response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    		response.addHeader("Content-Disposition", "attachment;filename="+ new String(files[0].getBytes("utf-8"),"ISO-8859-1")+"."+files[1]);
        }
		
        //创建工作簿
		XSSFWorkbook wb = new XSSFWorkbook();
		
        //遍历sheet
        String[] sheetNameArray = files[2].split("~,~");
        for( int i=0;i<sheetNameArray.length;i++ ){    

    		/*创建sheet*/
        	XSSFSheet sheet;
        	if( sheetNameArray[i].equals("") || sheetNameArray[i] == null ){
        		sheet = wb.createSheet();
        	}else{
        		sheet = wb.createSheet(sheetNameArray[i]);
        	}

        	/*表格样式设置*/
    		//创建单元格样式
    		XSSFCellStyle style =  wb.createCellStyle();	
    		if( !excelSwitch[1].split(",")[i].equals("0") || !excelSwitch[1].split(",")[i].equals("") ){
    			if(excelSwitch[1].split(",")[i].equals("1")){
        			style.setAlignment(XSSFCellStyle.ALIGN_CENTER);             //文字水平居中
    			}else if(excelSwitch[1].split(",")[i].equals("2")){
    				style.setAlignment(XSSFCellStyle.ALIGN_LEFT);               //文字水平居左
    			}else if(excelSwitch[1].split(",")[i].equals("2")){
    				style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);              //文字水平居右
    			}
    		}
    		if( !excelSwitch[2].split(",")[i].equals("0") || !excelSwitch[2].split(",")[i].equals("") ){
    			if(excelSwitch[2].split(",")[i].equals("1")){
        			style.setVerticalAlignment(XSSFCellStyle.ALIGN_CENTER);      //文字垂直居中
    			}else if(excelSwitch[2].split(",")[i].equals("2")){
    				style.setVerticalAlignment(XSSFCellStyle.ALIGN_LEFT);        //文字垂直居左
    			}else if(excelSwitch[2].split(",")[i].equals("2")){
    				style.setVerticalAlignment(XSSFCellStyle.ALIGN_RIGHT);       //文字垂直居右
    			}
    		}
        	style.setBorderBottom(BorderStyle.THIN);                    	//底边框加黑
        	style.setBorderLeft(BorderStyle.THIN);                      	//左边框加黑
        	style.setBorderRight(BorderStyle.THIN);                     	//右边框加黑
        	style.setBorderTop(BorderStyle.THIN);                       	//上边框加黑
    		if( excelSwitch[3].split(",")[i].equals("1")  ){				//背景颜色
    			String bgc = map.get("backgroundColor");
    			int g = Integer.valueOf(bgc.split(",")[i]);
        		style.setFillForegroundColor((short)g);                     //设置要添加表格背景颜色
        		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);     //solid 填充
    		}
    		//为单元格添加背景样式
    		for (int j = 0; j < allData.get(i).size()+1; j++) { 
    		    Row  row =	sheet.createRow(j); 
    			for (int k = 0; k < titleData.get(i).size(); k++) {
    				row.createCell(k).setCellStyle(style);
    			}
    		}
    		//合并单元格
    		if( excelSwitch[0].split(",")[i].equals("1")  ){
    			String mkey = map.get("addMergedRegion");
    			String[] mkeyArray    = mkey.split("~~"); 
    			String[] mkeyAllArray = mkeyArray[i].split("~");     //第n张表的参数  
    			for( int o=0;o < mkeyAllArray.length; o++ ){
    				String[] nkey = mkeyAllArray[o].split(",");
    				//合并单元格，cellRangAddress四个参数，第一个起始行，第二终止行，第三个起始列，第四个终止列
            		sheet.addMergedRegion(new CellRangeAddress(Integer.valueOf(nkey[0]),Integer.valueOf(nkey[1]),Integer.valueOf(nkey[2]),Integer.valueOf(nkey[3])));     
    			}
    		}

    		/*插入数据*/
    		//标题栏
    		XSSFRow row = sheet.getRow(0);                      
    		for( int t=0;t<titleData.get(i).size();t++ ){
        		row.getCell(t).setCellValue(titleData.get(i).get(t));  
    		}
    		//数据
    		for( int s=1;s<=allData.get(i).size();s++ ){
    			XSSFRow rowl = sheet.getRow(s);
        		for( int r=0;r<allData.get(i).get(s-1).size();r++ ){
            		rowl.getCell(r).setCellValue(allData.get(i).get(s-1).get(r)); 
        		} 
    		}
        }
        /*excel文件导出*/
        wb.write(response.getOutputStream());
        wb.close();
        response.getOutputStream().flush();
	}
    
	
	
}