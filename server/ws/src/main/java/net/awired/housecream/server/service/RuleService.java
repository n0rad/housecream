package net.awired.housecream.server.service;

import net.awired.housecream.server.api.resource.RuleResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
public class RuleService implements RuleResource {

}
