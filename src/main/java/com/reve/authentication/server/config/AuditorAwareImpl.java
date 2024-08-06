package com.reve.authentication.server.config;

import java.util.Optional;

import com.reve.authentication.server.securityImpl.security.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {
	@Autowired
	private SecurityService securityService;

	@Override
	public Optional<Long> getCurrentAuditor() {
		return Optional.of(securityService.getCurrentLoggedInUserId());
	}
}
