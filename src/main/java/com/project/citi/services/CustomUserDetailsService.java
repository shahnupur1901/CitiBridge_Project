//package com.project.citi.services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.project.citi.dto.User;
//import com.project.citi.repository.UserRepository;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//	@Autowired
//	private UserRepository userRepository;
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		// TODO Auto-generated method stub
//		
//		User user = userRepository.findByUserName(username);
//		if(user == null) {
//			throw new UsernameNotFoundException("User not found");
//		}
//		return null;
//	}
//
//	
//}
