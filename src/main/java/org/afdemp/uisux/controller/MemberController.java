package org.afdemp.uisux.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.afdemp.uisux.domain.security.UserRole;

import org.afdemp.uisux.domain.User;
import org.afdemp.uisux.service.UserRoleService;
import org.afdemp.uisux.service.UserService;

@Controller
public class MemberController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRoleService userRoleService;

	@RequestMapping("/memberInfo")
	public String memberInfo(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
//		UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_MEMBER");
		user.setPassword(""); //password not sent to view
		model.addAttribute("user", user);
		return "memberInfo";
	}
}