package com.gentics.mesh.core.data.service;

import com.gentics.mesh.core.Page;
import com.gentics.mesh.core.Result;
import com.gentics.mesh.core.data.model.root.SchemaRoot;
import com.gentics.mesh.core.data.model.schema.propertytype.BasicPropertyType;
import com.gentics.mesh.core.data.model.schema.propertytype.MicroPropertyType;
import com.gentics.mesh.core.data.model.schema.propertytype.PropertyType;
import com.gentics.mesh.core.data.model.tinkerpop.Schema;
import com.gentics.mesh.core.data.model.tinkerpop.User;
import com.gentics.mesh.core.rest.schema.response.SchemaResponse;
import com.gentics.mesh.paging.PagingInfo;

public interface SchemaService {

	SchemaResponse transformToRest(Schema schema);

	Schema findByUUID(String uuid);

	void deleteByUUID(String uuid);

	public Result<Schema> findAll();

	Page<Schema> findAllVisible(User requestUser, PagingInfo pagingInfo);

	Schema findByName(String name);

	Schema create(String name);

	SchemaRoot createRoot();

	SchemaRoot findRoot();

	BasicPropertyType create(String nameKeyword, PropertyType i18nString);

	MicroPropertyType createMicroPropertyTypeSchema(String key);

	BasicPropertyType createBasicPropertyTypeSchema(String key, PropertyType type);

	BasicPropertyType createListPropertyTypeSchema(String key);

	Schema findOne(Long id);

	Schema findByName(String projectName, String name);

	void delete(Schema schema);


}
