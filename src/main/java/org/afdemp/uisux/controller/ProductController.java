package org.afdemp.uisux.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.afdemp.uisux.domain.MemberCartItem;
import org.afdemp.uisux.domain.Product;
import org.afdemp.uisux.domain.ShoppingCart;
import org.afdemp.uisux.domain.User;
import org.afdemp.uisux.domain.security.UserRole;
import org.afdemp.uisux.service.MemberCartItemService;
import org.afdemp.uisux.service.AccountService;
import org.afdemp.uisux.service.CategoryService;
import org.afdemp.uisux.service.ProductService;
import org.afdemp.uisux.service.UserRoleService;
import org.afdemp.uisux.service.UserService;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private  MemberCartItemService memberCartItemService;
	
	@Autowired
	private AccountService accountService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRoleService userRoleService;
	
	@RequestMapping("/productList")
	public String productList(Model model) {
		List<Product> productList = productService.findAllActive();
		model.addAttribute("productList", productList);		
		return "productList";
	}
	
	@RequestMapping("/productInfo")
	public String productInfo(@RequestParam("id") Long id, Model model) {
		Product product = productService.findOne(id);
		model.addAttribute("product", product);
		
		return "productInfo";
	}
	
	@RequestMapping(value = "/offer", method = RequestMethod.POST)
	public String offer(@ModelAttribute("product") Product product,
			@ModelAttribute("qty") Integer qty,
			Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_MEMBER");
		ShoppingCart shoppingCart = userRole.getShoppingCart();
		
		if (qty > 0 && memberCartItemService.putUpForSale(product, qty, shoppingCart)) {
			model.addAttribute("offerSuccess", true);
		}else {
			model.addAttribute("offerFailure", true);
		}
		
		model.addAttribute("memberCartItemList", memberCartItemService.findByShoppingCart(shoppingCart));
		
		return "offerList";
	}
	
	@RequestMapping("/offerList")
	public String productInfo(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_MEMBER");
		ShoppingCart shoppingCart = userRole.getShoppingCart();
		model.addAttribute("memberCartItemList", memberCartItemService.findByShoppingCart(shoppingCart));
		return "offerList";
	}
	
	
	@RequestMapping(value="/removeOffer", method=RequestMethod.POST)
	public String remove(@ModelAttribute("id") Long memberCartItemId, Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_MEMBER");
		ShoppingCart shoppingCart = userRole.getShoppingCart();
		
		if (memberCartItemService.removeMemberCartItem(memberCartItemId, shoppingCart.getId())) {
			model.addAttribute("offerRemoveSuccess", true);
		}else {
			model.addAttribute("offerRemoveFailure", true);
		}
		model.addAttribute("memberCartItemList", memberCartItemService.findByShoppingCart(shoppingCart));
		return "offerList";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

//	@RequestMapping(value = "/add", method = RequestMethod.GET)
//	public String addProduct(Model model) {
//		Product product = new Product();
//		Category category = new Category();
//		List<Category> categoryList = categoryService.findAll();
//		model.addAttribute("product", product);
//		model.addAttribute("category", category);
//		model.addAttribute("categoryList", categoryList);
//		return "addProduct";
//	}
//
//	@RequestMapping(value = "/add", method = RequestMethod.POST)
//	public String addProductPost(@ModelAttribute("product") Product product, BindingResult productResult,
//				@ModelAttribute("type") String type, BindingResult typeResult, Model model) {
//
//		if (productResult.hasErrors() || typeResult.hasErrors()) {
//			model.addAttribute("insertSuccess",false);
//			return "addProduct";
//		}
//
//		boolean success;
//		if ((productService.createProduct(product, type)!=null) & ImageUtility.trySaveImage(product)) {
//				success = true;
//		}else{
//			success = false;
//		}
//		
//		product = new Product();
//		List<Category> categoryList = categoryService.findAll();
//		model.addAttribute("categoryList",categoryList);
//		model.addAttribute("insertSuccess",success);
//		return "addProduct";
//	}
//	
//
//	
//	@RequestMapping(value="/remove", method=RequestMethod.POST)
//	public String remove(
//			@ModelAttribute("id") String id, Model model
//			) {
//		productService.removeOne(Long.parseLong(id.substring(8)));
//		List<Product> productList = productService.findAll();
//		model.addAttribute("productList", productList);
//		return "redirect:/product/productList";
//	}
//	
//	
//
//	
//	@RequestMapping("/updateProduct")
//	public String updateProduct(@RequestParam("id") Long id, Model model) {
//		Product product = productService.findOne(id);
//		List<Category> categoryList = categoryService.findAll();
//		model.addAttribute("product", product);
//		model.addAttribute("categoryList", categoryList);
//		return "updateProduct";
//	}
//	
//	@RequestMapping(value="/updateProduct", method=RequestMethod.POST)
//	public String updateProductPost(@ModelAttribute("product") Product product, BindingResult productResult,
//			@ModelAttribute("type") String type, BindingResult typeResult, Model model) {
//		
//		if (productResult.hasErrors() || typeResult.hasErrors()) {
//			return "redirect:/product/productInfo?id="+product.getId();
//		}
//
//		boolean success;
//		if ((productService.createProduct(product, type)!=null) & ImageUtility.trySaveImage(product)) {
//				success = true;
//		}else{
//			success = false;
//		}
//		model.addAttribute("updateSuccess",success);
//		
//		return "redirect:/product/productInfo?id="+product.getId();
//	}
//	
//	
//	@RequestMapping("/toggleProductActive")
//	public String toggleProductActive(@RequestParam("id") String id, Model model) {
//		String productId =id.substring(8);
//		Product product = productService.findOne(Long.parseLong(productId));
//		productService.toggleActive(product);
//		model.addAttribute("product", product);
//		return "productList";
//	}
//	
//	@RequestMapping(value = "/stockUp", method = RequestMethod.GET)
//	public String stockUp(@RequestParam("id") Long id, Model model) {
//		Product product = productService.findOne(id);
//		List<MemberCartItem> memberCartItemList = memberCartItemService.findAllAvailableItems(id);
//		
//		List<MemberCartItemWrapper> wrapperList = new ArrayList<>();
//		for (MemberCartItem memberCartItem : memberCartItemList) {
//			wrapperList.add(new MemberCartItemWrapper(memberCartItem.getShoppingCart().getUserRole().getUser(), memberCartItem)); 
//		}
//		
//		model.addAttribute("product", product);
//		model.addAttribute("wrapperList", wrapperList);
//		return "stockUp";
//	}
//	
//	@RequestMapping(value = "/stockUpConfirm", method = RequestMethod.GET)
//	public String stockUpConfirm(@RequestParam("memberCartItemId") Long memberCartItemId, Model model) {
//		
//		MemberCartItem memberCartItem = memberCartItemService.findById(memberCartItemId);
//		
//		MemberCartItemWrapper memberCartItemWrapper = new MemberCartItemWrapper(memberCartItem.getShoppingCart().getUserRole().getUser(), memberCartItem);
//		
//		model.addAttribute("memberCartItemWrapper", memberCartItemWrapper);
//		return "stockUpConfirm";
//	}


}