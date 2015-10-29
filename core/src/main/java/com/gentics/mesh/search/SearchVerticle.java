package com.gentics.mesh.search;

import static com.gentics.mesh.core.HttpConstants.APPLICATION_JSON;
import static io.vertx.core.http.HttpMethod.GET;
import static io.vertx.core.http.HttpMethod.POST;

import org.jacpfx.vertx.spring.SpringVerticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gentics.mesh.core.AbstractCoreApiVerticle;
import com.gentics.mesh.core.data.GenericVertex;
import com.gentics.mesh.core.data.root.RootVertex;
import com.gentics.mesh.core.rest.common.AbstractListResponse;
import com.gentics.mesh.core.rest.common.RestModel;
import com.gentics.mesh.core.rest.group.GroupListResponse;
import com.gentics.mesh.core.rest.node.NodeListResponse;
import com.gentics.mesh.core.rest.project.ProjectListResponse;
import com.gentics.mesh.core.rest.role.RoleListResponse;
import com.gentics.mesh.core.rest.schema.MicroschemaListResponse;
import com.gentics.mesh.core.rest.schema.SchemaListResponse;
import com.gentics.mesh.core.rest.tag.TagFamilyListResponse;
import com.gentics.mesh.core.rest.tag.TagListResponse;
import com.gentics.mesh.core.rest.user.UserListResponse;
import com.gentics.mesh.handler.InternalActionContext;

import io.vertx.ext.web.Route;

@Component
@Scope("singleton")
@SpringVerticle
public class SearchVerticle extends AbstractCoreApiVerticle {

	@Autowired
	private SearchRestHandler searchHandler;

	public SearchVerticle() {
		super("search");
	}

	@Override
	public void registerEndPoints() throws Exception {
		route("/*").handler(springConfiguration.authHandler());
		addSearchEndpoints();
	}

	/**
	 * Add various search endpoints using the aggregation nodes.
	 */
	private void addSearchEndpoints() {
		db.noTrx(noTx -> {
			registerSearchHandler("users", boot.meshRoot().getUserRoot(), UserListResponse.class);
			registerSearchHandler("groups", boot.meshRoot().getGroupRoot(), GroupListResponse.class);
			registerSearchHandler("roles", boot.meshRoot().getRoleRoot(), RoleListResponse.class);
			registerSearchHandler("nodes", boot.meshRoot().getNodeRoot(), NodeListResponse.class);
			registerSearchHandler("tags", boot.meshRoot().getTagRoot(), TagListResponse.class);
			registerSearchHandler("tagFamilies", boot.meshRoot().getTagFamilyRoot(), TagFamilyListResponse.class);
			registerSearchHandler("projects", boot.meshRoot().getProjectRoot(), ProjectListResponse.class);
			registerSearchHandler("schemas", boot.meshRoot().getSchemaContainerRoot(), SchemaListResponse.class);
			registerSearchHandler("microschemas", boot.meshRoot().getMicroschemaContainerRoot(), MicroschemaListResponse.class);
			addSearchStatusHandler();
		});
	}

	private void addSearchStatusHandler() {
		Route getRoute = route("/status").method(GET).produces(APPLICATION_JSON);
		getRoute.handler(rc -> {
			searchHandler.handleStatus(InternalActionContext.create(rc));
		});
	}

	/**
	 * Register the selected search handler.
	 * 
	 * @param typeName
	 *            Name of the search endpoint
	 * @param root
	 *            Aggregation node that should be used to load the objects that were found within the search index
	 * @param classOfRL
	 *            Class of matching list response
	 */
	private <T extends GenericVertex<TR>, TR extends RestModel, RL extends AbstractListResponse<TR>> void registerSearchHandler(String typeName,
			RootVertex<T> root, Class<RL> classOfRL) {
		Route postRoute = route("/" + typeName).method(POST).consumes(APPLICATION_JSON).produces(APPLICATION_JSON);
		postRoute.handler(rc -> {
			try {
				searchHandler.handleSearch(InternalActionContext.create(rc), root, classOfRL);
			} catch (Exception e) {
				// fail(rc, "search_error_query");
				rc.fail(e);
			}
		});
	}

}
