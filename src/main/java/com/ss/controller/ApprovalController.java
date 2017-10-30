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

import com.ss.daoImpl.AccountDaoImpl;
import com.ss.daoImpl.TransactionDaoImpl;
import com.ss.model.ApprovalList;
import com.ss.model.TransactionDetails;


@Controller
public class ApprovalController {
	
	@Autowired
	TransactionDaoImpl transactionDaoImpl;
	
	@Autowired
	AccountDaoImpl accountDaoImpl;
	
	@RequestMapping(value="/userapprovals",method=RequestMethod.GET)
	public String getListOfApprovals(Authentication auth,ModelMap model){
		String username=auth.getName();
		List<ApprovalList> data=transactionDaoImpl.getApprovalList(username);
		if(data==null||data.isEmpty()) {
			model.addAttribute("emptyMsg","Nothing to Approve");
		}
		model.addAttribute("list",data);
		model.addAttribute("column1", "Date");
		model.addAttribute("column2", "Sender");
		model.addAttribute("column3", "Amount");
		model.addAttribute("column4", "id");
		model.addAttribute("title", "Approval List");
		return "approvalpage";

	}
	
	
	@RequestMapping(value="/Merchantuserapprovals",method=RequestMethod.GET)
	public String MerchantgetListOfApprovals(Authentication auth,ModelMap model){
		String username=auth.getName();
		System.out.println(username);
		List<ApprovalList> data=transactionDaoImpl.getApprovalList(username);
		if(data==null||data.isEmpty()) {
			model.addAttribute("emptyMsg","Nothing to Approve");
		}
		model.addAttribute("list",data);
		model.addAttribute("column1", "Date");
		model.addAttribute("column2", "Sender");
		model.addAttribute("column3", "Amount");
		model.addAttribute("column4", "id");
		model.addAttribute("title", "Approval List");
		System.out.println("Hello Before return");
		return "Merchantapprovalpage";
		
		
	}
	
	
	
	@RequestMapping(value="/approveByUser*",method=RequestMethod.POST)
	public @ResponseBody String checkUserName(@RequestParam("transactionId") String transactionId,@RequestParam("status") String status,@RequestParam("amount") String amount){
		//System.out.print("Username to check " +transactionId);
		System.out.print("inside approverBy user");
		int ret = transactionDaoImpl.changestatus(transactionId,status,amount);
		Double a=Double.parseDouble(amount);
		// approve non critical transactions
		if(a<1000){
			
			List<TransactionDetails> transactionlist=transactionDaoImpl.getDetailsforInternalTransfer(transactionId);
			TransactionDetails list=transactionlist.get(0);
			System.out.println(list.getTransferto());
			if(!list.getTransferto().equals("null")){
				// non critical external transfer
				System.out.println("inside external");

				accountDaoImpl.doTransferExternal(list.getTransacterUsername(), a, list.getFromAccountType(), list.getTransferto());
				transactionDaoImpl.setApproverUserName(list.getTransferto(),transactionId);
				}
		}
		return "ret";
	}
	
	
	
	
	@RequestMapping(value="/MerchantapproveByUser*",method=RequestMethod.POST)
	public @ResponseBody String MerchantcheckUserName(@RequestParam("transactionId") String transactionId,@RequestParam("status") String status){
		//System.out.print("Username to check " +transactionId);
		//System.out.print("Username to check " +status);
		//int ret = transactionDaoImpl.changestatus(transactionId,status);
		return "ret";
	}

}
