package org.afdemp.uisux.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.afdemp.uisux.domain.Transaction;
import org.afdemp.uisux.domain.User;
import org.afdemp.uisux.domain.security.UserRole;
import org.afdemp.uisux.domain.Account;
import org.afdemp.uisux.service.AccountService;
import org.afdemp.uisux.service.ClientOrderService;
import org.afdemp.uisux.service.TransactionService;
import org.afdemp.uisux.service.UserRoleService;
import org.afdemp.uisux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;

	@Autowired
	private  UserService userService;

	@Autowired
	private UserRoleService userRoleService;
	
	
	@RequestMapping(value = "/accountInfo", method = RequestMethod.GET)
	public String accountInfo(@RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate,
			Model model, Principal principal) {
		
		User user = userService.findByUsername(principal.getName());
		UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_MEMBER");
		Account account = userRole.getAccount();
		
		List<Transaction> withdrawList = transactionService.fetchAccountWithdrawsByPeriod(account, Timestamp.valueOf(
				LocalDate.parse(fromDate).atStartOfDay()), Timestamp.valueOf(LocalDate.parse(toDate).atTime(23, 59, 59)));
		
		List<Transaction> depositList = transactionService.fetchAccountDepositsByPeriod(account, Timestamp.valueOf(
				LocalDate.parse(fromDate).atStartOfDay()), Timestamp.valueOf(LocalDate.parse(toDate).atTime(23, 59, 59)));
		
		model.addAttribute("withdrawList", withdrawList);
		model.addAttribute("depositList", depositList);
		model.addAttribute("user", user);
		model.addAttribute("account", account);
		return "accountInfo";
	}
	
	@RequestMapping(value = "/accountInfo", method = RequestMethod.POST)
	public String searchAccountInfoByPeriod(@ModelAttribute("fromDate") String fromDate,
			@ModelAttribute("toDate") String toDate,
			RedirectAttributes redirect) {
		
		redirect.addAttribute("fromDate", fromDate);
		redirect.addAttribute("toDate", toDate);
		return "redirect:/accountInfo";
	}

}
