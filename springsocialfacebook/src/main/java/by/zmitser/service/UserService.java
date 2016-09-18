package by.zmitser.service;

import by.zmitser.model.User;
import by.zmitser.util.SignupForm;
import by.zmitser.util.UserEditForm;

public interface UserService {
	
	public abstract User signup(SignupForm signupForm);

	public abstract User findOne(long userId);

	public abstract void update(long userId, UserEditForm userEditForm);

}
