/*package com.tekup.miniproject.business.servicesImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tekup.miniproject.business.services.UserService;
import com.tekup.miniproject.dao.entities.User;
import com.tekup.miniproject.dao.repositories.UserRepository;
@Service
public class UserServiceImpl implements UserService,UserDetailsService {
  
    @Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public User saveUser(User user) {
		  // Vérifier si un utilisateur avec la même adresse e-mail existe déjà
		   if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new DataIntegrityViolationException("User with this email '"+user.getEmail()+"' address already exists");
        } 
        // Encoder le mot de passe 
		String passwd = user.getPassword();
		String encodedPasswod = passwordEncoder.encode(passwd);
		user.setPassword(encodedPasswod);
		return  userRepository.save(user);
		
	}
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optUser = userRepository.findUserByEmail(email);

		org.springframework.security.core.userdetails.User springUser = null;

		if (optUser.isEmpty()) {
			throw new UsernameNotFoundException("User with email: " + email + " not found");
		}
		User user = optUser.get();
		List<String> roles = user.getRoles();
		Set<SimpleGrantedAuthority> ga = new HashSet<>();
		for (String role : roles) {
			ga.add(new SimpleGrantedAuthority(role));
		}

		springUser = new org.springframework.security.core.userdetails.User(
				email,
				user.getPassword(),
				ga);
		return springUser;
	}
  

  
}*/
