package com.ss.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ss.daoImpl.TransactionDaoImpl;
import com.ss.model.ApprovalList;


@Controller
public class ApprovalController {
	
	@Autowired
	TransactionDaoImpl transactionDaoImpl;
	
	@RequestMapping(value="/userapprovals",method=RequestMethod.GET)
	public String getListOfApprovals(Authentication auth,ModelMap model){
		String username=auth.getName();
		List<ApprovalList> data=transactionDaoImpl.getApprovalList(username);
		model.addAttribute("list",data);
		model.addAttribute("column1", "Date");
		model.addAttribute("column2", "Sender");
		model.addAttribute("column3", "Amount");
		model.addAttribute("column4", "id");
		model.addAttribute("title", "Approval List");
		return "approvalpage";

	}
	
	
	@RequestMapping(value="/approveByUser*",method=RequestMethod.POST)
	public @ResponseBody String checkUserName(@RequestParam("transactionId") String transactionId,@RequestParam("status") String status){
		//System.out.print("Username to check " +transactionId);
		//System.out.print("Username to check " +status);
		int ret = transactionDaoImpl.changestatus(transactionId,status);
		return "ret";
	}

}
