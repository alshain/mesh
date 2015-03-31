package com.gentics.cailun.core.repository.action;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;

import com.gentics.cailun.core.data.model.auth.User;

public interface UserPagingActions {

	/**
	 * Returns a Page of users meeting the paging restriction provided in the Pageable object.
	 * 
	 * @param requestUser
	 * @param pageable
	 * @return
	 */
	@Query("MATCH (requestUser:User)--(group:Group)--(role:Role)-[:HAS_PERMISSION {permission-read: true}]-(user:User) where id(requestUser) = {0} return user")
	public Page<User> findAll(User requestUser, Pageable pageable);

}
