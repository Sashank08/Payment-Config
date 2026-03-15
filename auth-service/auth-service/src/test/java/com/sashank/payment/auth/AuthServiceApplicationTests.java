package com.sashank.payment.auth;

import com.sashank.payment.auth.dto.LoginRequest;
import com.sashank.payment.auth.dto.RegisterRequest;
import com.sashank.payment.auth.entity.User;
import com.sashank.payment.auth.exception.EmailAlreadyExistsException;
import com.sashank.payment.auth.exception.InvalidCredentialsException;
import com.sashank.payment.auth.repo.UserRepo;
import com.sashank.payment.auth.service.JwtService;
import com.sashank.payment.auth.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepo userRepo;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtService jwtService;

	@InjectMocks
	private UserService userService;

	//Test register success
	@Test
	void testRegisterUserSuccess() {

		RegisterRequest request = new RegisterRequest();
		request.setEmail("user@gmail.com");
		request.setPassword("password");

		when(userRepo.findByEmail("user@gmail.com")).thenReturn(Optional.empty());
		when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

		User savedUser = User.builder()
				.email("user@gmail.com")
				.password("encodedPassword")
				.role("USER")
				.build();

		when(userRepo.save(any(User.class))).thenReturn(savedUser);

		User result = userService.registerUser(request);

		assertEquals("user@gmail.com", result.getEmail());
		assertEquals("USER", result.getRole());

		verify(userRepo).save(any(User.class));
	}

	//Test register when email already exists
	@Test
	void testRegisterUserEmailExists() {

		RegisterRequest request = new RegisterRequest();
		request.setEmail("user@gmail.com");
		request.setPassword("password");

		when(userRepo.findByEmail("user@gmail.com"))
				.thenReturn(Optional.of(new User()));

		assertThrows(
				EmailAlreadyExistsException.class,
				() -> userService.registerUser(request)
		);
	}

	// Test login success
	@Test
	void testLoginSuccess() {

		LoginRequest request = new LoginRequest();
		request.setEmail("user@gmail.com");
		request.setPassword("password");

		User user = User.builder()
				.email("user@gmail.com")
				.password("encodedPassword")
				.role("USER")
				.build();

		when(userRepo.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));
		when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
		when(jwtService.generateToken(user)).thenReturn("mockedToken");

		String token = userService.loginUser(request);

		assertEquals("mockedToken", token);
	}

	//Test login invalid password
	@Test
	void testLoginInvalidPassword() {

		LoginRequest request = new LoginRequest();
		request.setEmail("user@gmail.com");
		request.setPassword("wrong");

		User user = User.builder()
				.email("user@gmail.com")
				.password("encodedPassword")
				.role("USER")
				.build();

		when(userRepo.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));
		when(passwordEncoder.matches("wrong", "encodedPassword")).thenReturn(false);

		assertThrows(
				InvalidCredentialsException.class,
				() -> userService.loginUser(request)
		);
	}

	//Test login user not found
	@Test
	void testLoginUserNotFound() {

		LoginRequest request = new LoginRequest();
		request.setEmail("user@gmail.com");
		request.setPassword("password");

		when(userRepo.findByEmail("user@gmail.com")).thenReturn(Optional.empty());

		assertThrows(
				InvalidCredentialsException.class,
				() -> userService.loginUser(request)
		);
	}

	//Test get all users
	@Test
	void testGetAllUsers() {

		List<User> users = List.of(new User(), new User());

		when(userRepo.findAll()).thenReturn(users);

		List<User> result = userService.getAllUsers();

		assertEquals(2, result.size());
	}

	//Test delete user success
	@Test
	void testDeleteUserSuccess() {

		User user = User.builder()
				.email("user@gmail.com")
				.build();

		when(userRepo.findByEmail("user@gmail.com"))
				.thenReturn(Optional.of(user));

		userService.deleteUserByEmail("user@gmail.com");

		verify(userRepo).delete(user);
	}

	//Test delete user not found
	@Test
	void testDeleteUserNotFound() {

		when(userRepo.findByEmail("user@gmail.com"))
				.thenReturn(Optional.empty());

		assertThrows(
				RuntimeException.class,
				() -> userService.deleteUserByEmail("user@gmail.com")
		);
	}
}