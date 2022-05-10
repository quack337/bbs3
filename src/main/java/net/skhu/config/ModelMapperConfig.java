package net.skhu.config;

import java.util.List;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.skhu.entity.User;
import net.skhu.entity.UserRole;
import net.skhu.model.UserDto;
import net.skhu.model.UserEdit;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper createModelMapper() {
    	var modelMapper = new ModelMapper();

    	var typeMap1 = modelMapper.createTypeMap(User.class, UserDto.class);
    	typeMap1.addMappings(mapper ->
    		mapper.using(new UserRolesConvertor())
    			  .map(User::getUserRoles, UserDto::setRoles));

    	var typeMap2 = modelMapper.createTypeMap(User.class, UserEdit.class);
    	typeMap2.addMappings(mapper ->
			mapper.using(new UserRolesConvertor())
				  .map(User::getUserRoles, UserEdit::setRoles));

    	return modelMapper;
    }
}

class UserRolesConvertor extends AbstractConverter<List<UserRole>, String[]> {
	@Override
	protected String[] convert(List<UserRole> userRoles) {
		return userRoles.stream().map(UserRole::getRole).toArray(String[]::new);
	}
};
