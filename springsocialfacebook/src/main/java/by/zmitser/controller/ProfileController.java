package by.zmitser.controller;

import by.zmitser.model.User;
import by.zmitser.service.UserService;
import by.zmitser.util.MyControllerAdvice;
import by.zmitser.util.MyUtil;
import by.zmitser.util.UserEditForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
@SessionAttributes("redirectAfterConnecting")
public class ProfileController {
	
	private Facebook facebook;

	@Autowired
	public void setFacebook(Facebook facebook) {
		this.facebook = facebook;
	}
	
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}	
	
    @RequestMapping(value = "/{userId}")
    public String getById(@PathVariable("userId") long userId, Model model, SessionStatus status) {
    	
    	status.setComplete();
    	
    	model.addAttribute("facebookAuthorized", MyUtil.isAuthorized(facebook));
    	model.addAttribute(userService.findOne(userId));
    	
	  	return "user";
    }
    
    @RequestMapping(value = "/{userId}/edit")
    public String edit(@PathVariable("userId") long userId, Model model) {
    	
		User user = userService.findOne(userId);
		UserEditForm form = new UserEditForm();
		form.setName(user.getName());
    	model.addAttribute(form);
    	
		return "user-edit";

    }
    
	@RequestMapping(value = "/{userId}/edit", method = RequestMethod.POST)
	public String edit(@PathVariable("userId") long userId,
                       @ModelAttribute("userEditForm") @Valid UserEditForm userEditForm,
                       BindingResult result, RedirectAttributes redirectAttributes,
                       HttpServletRequest request) throws ServletException {

		if (result.hasErrors())
			return "user-edit";

		userService.update(userId, userEditForm);
		MyUtil.flash(redirectAttributes, "success", "editSuccessful");
		request.logout();

		return "redirect:/";
	}
	
    @RequestMapping(value = "/{userId}/connect/facebook", method = RequestMethod.POST)
    public String connectFacebook(@PathVariable("userId") long userId, Model model) {
    	
    	model.addAttribute("redirectAfterConnecting", "/users/" + userId);
    	
		return "forward:/connect/facebook?scope=" + MyControllerAdvice.FACEBOOK_SCOPE;

    }

	
}
